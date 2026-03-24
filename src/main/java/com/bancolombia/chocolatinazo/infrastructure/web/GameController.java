package com.bancolombia.chocolatinazo.infrastructure.web;

import com.bancolombia.chocolatinazo.application.dto.request.CalculateLoserRequest;
import com.bancolombia.chocolatinazo.application.dto.request.CreateGameRequest;
import com.bancolombia.chocolatinazo.application.dto.response.FinishedGameResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameRecordResponse;
import com.bancolombia.chocolatinazo.application.dto.response.GameResponse;
import com.bancolombia.chocolatinazo.application.service.GameService;
import com.bancolombia.chocolatinazo.domain.enums.RuleType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller for game management endpoints.
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Create a new game with MIN or MAX rule.
     * Only ADMIN users can create games.
     *
     * @param request The request containing the rule (MIN or MAX)
     * @return GameResponse with the created game information
     */
    @PostMapping("/create")
    public ResponseEntity<GameResponse> createGame(@Valid @RequestBody CreateGameRequest request) {
        RuleType ruleType = RuleType.valueOf(request.getRule().toUpperCase());
        GameResponse createdGame = gameService.createGame(ruleType);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGame);
    }

    /**
     * Pick a chocolate bar for the authenticated user.
     * Only PLAYER users can pick. The userId is extracted from the JWT token.
     *
     * @param authentication The authentication object containing the user ID
     * @return GameRecordResponse with the picked chocolatina information
     */
    @PostMapping("/pick")
    public ResponseEntity<GameRecordResponse> pickChocolatina(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getPrincipal().toString());
        GameRecordResponse picked = gameService.pickChocolatina(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(picked);
    }


    /**
     * Calculate the loser of the current game and finish it.
     * Only ADMIN users can invoke this.
     * Determines the loser based on rule (MIN or MAX), calculates payment,
     * saves result in finished_games, deletes game records, and marks game as FINISHED.
     *
     * @param request The request containing the rule (MIN or MAX)
     * @return FinishedGameResponse with loser information and payment details
     */
    @PostMapping("/calculate-loser")
    public ResponseEntity<FinishedGameResponse> calculateLoser(
            @Valid @RequestBody CalculateLoserRequest request) {
        FinishedGameResponse result = gameService.calculateLoser(request.getRule());
        return ResponseEntity.ok(result);
    }
}

