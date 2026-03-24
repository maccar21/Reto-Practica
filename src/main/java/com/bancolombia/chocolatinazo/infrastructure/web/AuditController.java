package com.bancolombia.chocolatinazo.infrastructure.web;

import com.bancolombia.chocolatinazo.application.dto.response.FinishedGameResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameRecordResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameResponse;
import com.bancolombia.chocolatinazo.application.service.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for audit endpoints.
 * Provides read-only access to current and finished game data for AUDITOR and ADMIN roles.
 */
@RestController
@RequestMapping("/api/audit")
public class AuditController {

    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    /**
     * Get the current ACTIVE game information.
     * Accessible by AUDITOR and ADMIN roles.
     *
     * @return GameResponse with the active game details
     */
    @GetMapping("/current-game")
    public ResponseEntity<GameResponse> getCurrentGame() {
        GameResponse currentGame = auditService.getCurrentGame();
        return ResponseEntity.ok(currentGame);
    }

    /**
     * Get all game records (player picks) for the current ACTIVE game.
     * Lists all movements: player, chocolatina number, timestamp.
     * Accessible by AUDITOR and ADMIN roles.
     *
     * @return List of GameRecordResponse objects
     */
    @GetMapping("/current-game/records")
    public ResponseEntity<List<GameRecordResponse>> getCurrentGameRecords() {
        List<GameRecordResponse> records = auditService.getCurrentGameRecords();
        return ResponseEntity.ok(records);
    }

    /**
     * Get all finished games with loser details and payment information.
     * Accessible by AUDITOR and ADMIN roles.
     *
     * @return List of FinishedGameResponse with all completed game records
     */
    @GetMapping("/finished-games")
    public ResponseEntity<List<FinishedGameResponse>> getFinishedGames() {
        List<FinishedGameResponse> finishedGames = auditService.getFinishedGames();
        return ResponseEntity.ok(finishedGames);
    }
}

