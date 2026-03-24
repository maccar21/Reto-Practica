package com.bancolombia.chocolatinazo.application.service;

import com.bancolombia.chocolatinazo.application.dto.response.FinishedGameResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameRecordResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameResponse;
import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.enums.RuleType;
import com.bancolombia.chocolatinazo.domain.model.FinishedGame;
import com.bancolombia.chocolatinazo.domain.model.Game;
import com.bancolombia.chocolatinazo.domain.model.GameRecord;
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
import java.util.stream.Collectors;

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
        if (gameRepository.findByStatus(GameStatus.ACTIVE).isPresent()) {
            throw new InvalidInputException("Cannot create a new game: an ACTIVE game already exists");
        }

        Game newGame = new Game();
        newGame.setStatus(GameStatus.ACTIVE);
        newGame.setRuleType(ruleType);

        Game savedGame = gameRepository.save(newGame);
        return toGameResponse(savedGame);
    }

    /**
     * Pick a chocolate bar for the authenticated user in the current ACTIVE game.
     * Generates a random number between 1 and 320 and registers it.
     *
     * @param userId The UUID of the authenticated user (extracted from JWT)
     * @return GameRecordResponse with the picked chocolatina information
     * @throws ResourceNotFoundException if no active game or user not found
     */
    public GameRecordResponse pickChocolatina(UUID userId) {
        Game activeGame = gameRepository.findByStatus(GameStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active game found. An admin must create a game first"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        int chocolatinaNumber = random.nextInt(320) + 1;

        GameRecord gameRecord = new GameRecord();
        gameRecord.setGame(activeGame);
        gameRecord.setUser(user);
        gameRecord.setChocolatinaNumber(chocolatinaNumber);

        GameRecord savedRecord = gameRecordRepository.save(gameRecord);
        return toGameRecordResponse(savedRecord);
    }


    // ========== Private helper methods ==========

    private GameResponse toGameResponse(Game game) {
        return new GameResponse(
                game.getId(),
                game.getStatus(),
                game.getRuleType(),
                game.getCreatedAt()
        );
    }

    private GameRecordResponse toGameRecordResponse(GameRecord record) {
        return new GameRecordResponse(
                record.getId(),
                record.getGame().getId(),
                record.getUser().getId(),
                record.getUser().getUsername(),
                record.getChocolatinaNumber(),
                record.getPickedAt()
        );
    }

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

    /**
     * Calculate the loser of the current ACTIVE game.
     *
     * Steps:
     * 1. Find the ACTIVE game
     * 2. Get all game records
     * 3. Determine loser based on rule (MIN = lowest number loses, MAX = highest number loses)
     * 4. Get current chocolatina price
     * 5. Calculate total to pay (quantity × price)
     * 6. Save FinishedGame with loser info
     * 7. Delete all game records (clean table for next game)
     * 8. Mark game as FINISHED
     *
     * @param rule "MIN" or "MAX"
     * @return FinishedGameResponse with loser details and payment info
     */
    public FinishedGameResponse calculateLoser(String rule) {
        // 1. Find the ACTIVE game
        Game activeGame = gameRepository.findByStatus(GameStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active game found"));

        // 2. Get all game records for this game
        List<GameRecord> records = gameRecordRepository.findByGame_Id(activeGame.getId());
        if (records.isEmpty()) {
            throw new InvalidInputException("No game records found. Players must pick chocolatinas first");
        }

        // 3. Determine loser based on rule
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

        // 4. Get current chocolatina price
        BigDecimal chocolatinaPrice = chocolatinaConfigRepository.findLatest()
                .map(config -> config.getPrice())
                .orElseThrow(() -> new ResourceNotFoundException("Chocolatina price not configured. An admin must set the price first"));

        // 5. Calculate total to pay
        int totalChocolatinas = records.size();
        BigDecimal totalPaid = chocolatinaPrice.multiply(BigDecimal.valueOf(totalChocolatinas));

        // 6. Save FinishedGame
        FinishedGame finishedGame = new FinishedGame();
        finishedGame.setGame(activeGame);
        finishedGame.setLoser(loserRecord.getUser());
        finishedGame.setLosingNumber(loserRecord.getChocolatinaNumber());
        finishedGame.setTotalChocolatinas(totalChocolatinas);
        finishedGame.setChocolatinaPrice(chocolatinaPrice);
        finishedGame.setTotalPaid(totalPaid);
        finishedGame.setRuleType(RuleType.valueOf(rule));

        FinishedGame savedFinished = finishedGameRepository.save(finishedGame);

        // 7. Delete all game records (clean for next game)
        gameRecordRepository.deleteAll();

        // 8. Mark game as FINISHED
        activeGame.setStatus(GameStatus.FINISHED);
        gameRepository.save(activeGame);

        return toFinishedGameResponse(savedFinished);
    }
}

