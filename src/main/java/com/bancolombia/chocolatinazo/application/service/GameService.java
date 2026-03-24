package com.bancolombia.chocolatinazo.application.service;

import com.bancolombia.chocolatinazo.application.dto.response.GameRecordResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameResponse;
import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.enums.RuleType;
import com.bancolombia.chocolatinazo.domain.model.Game;
import com.bancolombia.chocolatinazo.domain.model.GameRecord;
import com.bancolombia.chocolatinazo.domain.model.User;
import com.bancolombia.chocolatinazo.domain.port.IGameRecordRepository;
import com.bancolombia.chocolatinazo.domain.port.IGameRepository;
import com.bancolombia.chocolatinazo.domain.port.IUserRepository;
import com.bancolombia.chocolatinazo.infrastructure.exception.InvalidInputException;
import com.bancolombia.chocolatinazo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

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
    private final IUserRepository userRepository;
    private final Random random;

    public GameService(IGameRepository gameRepository,
                       IGameRecordRepository gameRecordRepository,
                       IUserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.gameRecordRepository = gameRecordRepository;
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

    /**
     * Get the current ACTIVE game information.
     *
     * @return GameResponse with the active game details
     * @throws ResourceNotFoundException if no active game exists
     */
    public GameResponse getCurrentGame() {
        Game activeGame = gameRepository.findByStatus(GameStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active game found"));

        return toGameResponse(activeGame);
    }

    /**
     * Get all game records for the current ACTIVE game.
     *
     * @return List of GameRecordResponse with all picks in the current game
     * @throws ResourceNotFoundException if no active game exists
     */
    public List<GameRecordResponse> getCurrentGameRecords() {
        Game activeGame = gameRepository.findByStatus(GameStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active game found"));

        return gameRecordRepository.findByGame_Id(activeGame.getId()).stream()
                .map(this::toGameRecordResponse)
                .collect(Collectors.toList());
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
}

