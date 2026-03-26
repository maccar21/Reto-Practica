package com.bancolombia.chocolatinazo.application.service;

import com.bancolombia.chocolatinazo.application.dto.response.FinishedGameResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameRecordResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameResponse;
import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.enums.RuleType;
import com.bancolombia.chocolatinazo.domain.model.FinishedGame;
import com.bancolombia.chocolatinazo.domain.model.Game;
import com.bancolombia.chocolatinazo.domain.model.GameRecord;
import com.bancolombia.chocolatinazo.domain.model.ChocolatinaConfig;
import com.bancolombia.chocolatinazo.domain.model.User;
import com.bancolombia.chocolatinazo.domain.port.IChocolatinaConfigRepository;
import com.bancolombia.chocolatinazo.domain.port.IFinishedGameRepository;
import com.bancolombia.chocolatinazo.domain.port.IGameRecordRepository;
import com.bancolombia.chocolatinazo.domain.port.IGameRepository;
import com.bancolombia.chocolatinazo.domain.port.IUserRepository;
import com.bancolombia.chocolatinazo.infrastructure.exception.InvalidInputException;
import com.bancolombia.chocolatinazo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;


/**
 * Service responsible for game management operations.
 * Handles creating games, picking chocolatinas, and retrieving game state.
 */
@Service
public class GameService {

    private final IGameRepository gameRepository;
    private final IGameRecordRepository gameRecordRepository;
    private final IFinishedGameRepository finishedGameRepository;
    private final IChocolatinaConfigRepository chocolatinaConfigRepository;
    private final IUserRepository userRepository;
    private final Random random;

    public GameService(IGameRepository gameRepository,
                       IGameRecordRepository gameRecordRepository,
                       IFinishedGameRepository finishedGameRepository,
                       IChocolatinaConfigRepository chocolatinaConfigRepository,
                       IUserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.gameRecordRepository = gameRecordRepository;
        this.finishedGameRepository = finishedGameRepository;
        this.chocolatinaConfigRepository = chocolatinaConfigRepository;
        this.userRepository = userRepository;
        this.random = new Random();
    }

    /**
     * Create a new game with the specified rule.
     * Only one ACTIVE game can exist at a time.
     *
     * @param ruleType The rule for the game (MIN or MAX)
     * @return GameResponse with the created game information
     * @throws InvalidInputException if an ACTIVE game already exists
     */
    public GameResponse createGame(RuleType ruleType) {
        // Validate that no ACTIVE game already exists — only one game can be active at a time
        if (gameRepository.findByStatus(GameStatus.ACTIVE).isPresent()) {
            throw new InvalidInputException("Cannot create a new game: an ACTIVE game already exists");
        }

        // Create a new game with the given rule type (MIN or MAX) and ACTIVE status
        Game newGame = new Game();
        newGame.setStatus(GameStatus.ACTIVE);
        newGame.setRuleType(ruleType);

        // Persist the new game and return the response DTO
        Game savedGame = gameRepository.save(newGame);
        return toGameResponse(savedGame);
    }

    /**
     * Pick a chocolatina for the authenticated user in the current ACTIVE game.
     * Generates a random number between 1 and 320 and registers it.
     *
     * @param userId The UUID of the authenticated user (extracted from JWT)
     * @return GameRecordResponse with the picked chocolatina information
     * @throws ResourceNotFoundException if no active game or user not found
     */
    public GameRecordResponse pickChocolatina(UUID userId) {
        // Step 1: Find the currently ACTIVE game — throws 404 if no game is in progress
        Game activeGame = gameRepository.findByStatus(GameStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active game found. An admin must create a game first"));

        // Step 2: Find the authenticated user by ID — throws 404 if user does not exist
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Step 3: Validate that the player has not already picked a chocolatina in this game (one pick per player per game)
        if (gameRecordRepository.existsByGame_IdAndUser_Id(activeGame.getId(), userId)) {
            throw new InvalidInputException("This user have already picked a chocolatina in this game");
        }

        // Step 4: Generate a unique random chocolatina number (1-320) ensuring no duplicates within the same game
        int chocolatinaNumber;
        do {
            chocolatinaNumber = random.nextInt(320) + 1;
        } while (gameRecordRepository.existsByGame_IdAndChocolatinaNumber(activeGame.getId(), chocolatinaNumber));

        // Step 5: Create and save the game record associating the player with the chocolatina number
        GameRecord gameRecord = new GameRecord();
        gameRecord.setGame(activeGame);
        gameRecord.setUser(user);
        gameRecord.setChocolatinaNumber(chocolatinaNumber);

        // Step 6: Persist the record and return the response DTO
        GameRecord savedRecord = gameRecordRepository.save(gameRecord);
        return toGameRecordResponse(savedRecord);
    }


    /**
     * Calculate the loser of the current ACTIVE game.
     *
     * @param rule "MIN" or "MAX"
     * @return FinishedGameResponse with loser details and payment info
     */
    public FinishedGameResponse calculateLoser(String rule) {
        // Step 1: Find the currently ACTIVE game — throws 404 if no game is in progress
        Game activeGame = gameRepository.findByStatus(GameStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active game found"));

        // Step 2: Get all game records for this game — throws 400 if no players have picked
        List<GameRecord> records = gameRecordRepository.findByGame_Id(activeGame.getId());
        if (records.isEmpty()) {
            throw new InvalidInputException("No game records found. Players must pick chocolatinas first");
        }

        // Step 3: Validate that at least 2 players participated — cannot determine a loser with only one player
        if (records.size() < 2) {
            throw new InvalidInputException("Cannot determine a loser with only one player. At least 2 players are required");
        }

        // Step 4: Determine the loser based on the rule (MIN = lowest number loses, MAX = highest number loses)
        GameRecord loserRecord;
        if ("MIN".equals(rule)) {
            loserRecord = records.stream()
                    .min(Comparator.comparingInt(GameRecord::getChocolatinaNumber))
                    .orElseThrow(() -> new InvalidInputException("Could not determine loser"));
        } else {
            loserRecord = records.stream()
                    .max(Comparator.comparingInt(GameRecord::getChocolatinaNumber))
                    .orElseThrow(() -> new InvalidInputException("Could not determine loser"));
        }

        // Step 5: Get the current chocolatina price — snapshot at the moment of calculation
        BigDecimal chocolatinaPrice = chocolatinaConfigRepository.findLatest()
                .map(ChocolatinaConfig::getPrice)
                .orElseThrow(() -> new ResourceNotFoundException("Chocolatina price not configured. An admin must set the price first"));

        // Step 6: Calculate the total amount the loser must pay (number of chocolatinas × unit price)
        int totalChocolatinas = records.size();
        BigDecimal totalPaid = chocolatinaPrice.multiply(BigDecimal.valueOf(totalChocolatinas));

        // Step 7: Build and save the FinishedGame record with all loser details and payment info
        FinishedGame finishedGame = new FinishedGame();
        finishedGame.setGame(activeGame);
        finishedGame.setLoser(loserRecord.getUser());
        finishedGame.setLosingNumber(loserRecord.getChocolatinaNumber());
        finishedGame.setTotalChocolatinas(totalChocolatinas);
        finishedGame.setChocolatinaPrice(chocolatinaPrice);
        finishedGame.setTotalPaid(totalPaid);
        finishedGame.setRuleType(RuleType.valueOf(rule));

        FinishedGame savedFinished = finishedGameRepository.save(finishedGame);

        // Step 8: Delete all game records — clean the table for the next game round
        gameRecordRepository.deleteAll();

        // Step 9: Mark the current game as FINISHED so a new game can be created
        activeGame.setStatus(GameStatus.FINISHED);
        gameRepository.save(activeGame);

        return toFinishedGameResponse(savedFinished);
    }


    // ========== Private helper methods ==========

    /**
     * Maps a Game entity to a GameResponse DTO.
     */
    private GameResponse toGameResponse(Game game) {
        return new GameResponse(
                game.getId(),
                game.getStatus(),
                game.getRuleType(),
                game.getCreatedAt()
        );
    }

    /**
     * Maps a GameRecord entity to a GameRecordResponse DTO.
     * Includes the player's username from the User relationship.
     */
    private GameRecordResponse toGameRecordResponse(GameRecord recordPick) {
        return new GameRecordResponse(
                recordPick.getId(),
                recordPick.getGame().getId(),
                recordPick.getUser().getId(),
                recordPick.getUser().getUsername(),
                recordPick.getChocolatinaNumber(),
                recordPick.getPickedAt()
        );
    }

    /**
     * Maps a FinishedGame entity to a FinishedGameResponse DTO.
     * Includes loser username and rule type from the entity relationships.
     */
    private FinishedGameResponse toFinishedGameResponse(FinishedGame finishedGame) {
        return new FinishedGameResponse(
                finishedGame.getId(),
                finishedGame.getGame().getId(),
                finishedGame.getLoser().getId(),
                finishedGame.getLoser().getUsername(),
                finishedGame.getLosingNumber(),
                finishedGame.getTotalChocolatinas(),
                finishedGame.getChocolatinaPrice(),
                finishedGame.getTotalPaid(),
                finishedGame.getRuleType().name(),
                finishedGame.getFinishedAt()
        );
    }
}
