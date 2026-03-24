package com.bancolombia.chocolatinazo.application.service;

import com.bancolombia.chocolatinazo.application.dto.response.FinishedGameResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameRecordResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameResponse;
import com.bancolombia.chocolatinazo.domain.enums.GameStatus;
import com.bancolombia.chocolatinazo.domain.model.FinishedGame;
import com.bancolombia.chocolatinazo.domain.model.Game;
import com.bancolombia.chocolatinazo.domain.model.GameRecord;
import com.bancolombia.chocolatinazo.domain.port.IFinishedGameRepository;
import com.bancolombia.chocolatinazo.domain.port.IGameRecordRepository;
import com.bancolombia.chocolatinazo.domain.port.IGameRepository;
import com.bancolombia.chocolatinazo.infrastructure.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for audit operations.
 * Provides read-only access to current and finished games data for auditors and admins.
 */
@Service
public class AuditService {

    private final IFinishedGameRepository finishedGameRepository;
    private final IGameRepository gameRepository;
    private final IGameRecordRepository gameRecordRepository;

    public AuditService(IFinishedGameRepository finishedGameRepository,
                        IGameRepository gameRepository,
                        IGameRecordRepository gameRecordRepository) {
        this.finishedGameRepository = finishedGameRepository;
        this.gameRepository = gameRepository;
        this.gameRecordRepository = gameRecordRepository;
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

        return new GameResponse(
                activeGame.getId(),
                activeGame.getStatus(),
                activeGame.getRuleType(),
                activeGame.getCreatedAt()
        );
    }

    /**
     * Get all game records for the current ACTIVE game.
     * Lists all player picks: player, chocolatina number, timestamp.
     *
     * @return List of GameRecordResponse with all picks in the current game
     * @throws ResourceNotFoundException if no active game exists
     */
    public List<GameRecordResponse> getCurrentGameRecords() {
        Game activeGame = gameRepository.findByStatus(GameStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("No active game found"));

        return gameRecordRepository.findByGame_Id(activeGame.getId()).stream()
                .map(this::toGameRecordResponse)
                .toList();
    }

    /**
     * Retrieve all finished games with loser details and payment information.
     * Used by AUDITOR and ADMIN roles to review game history.
     *
     * @return List of FinishedGameResponse with all completed game records
     */
    public List<FinishedGameResponse> getFinishedGames() {
        return finishedGameRepository.findAll().stream()
                .map(this::toFinishedGameResponse)
                .toList();
    }

    /**
     * Maps a FinishedGame entity to a FinishedGameResponse DTO.
     * The loser username is obtained from the User relationship (normalized DB design).
     *
     * @param finishedGame The entity to convert
     * @return FinishedGameResponse DTO
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

