package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.FinishedGame;

import java.util.List;

/**
 * Port interface for FinishedGame persistence operations.
 * Part of the domain layer — decouples business logic from the JPA infrastructure.
 * Implemented by {@link com.bancolombia.chocolatinazo.infrastructure.repository.FinishedGameRepository}.
 */
public interface IFinishedGameRepository {

    /** Persist a finished game record with loser details and payment info. */
    FinishedGame save(FinishedGame finishedGame);

    /** Retrieve all finished games for audit purposes. */
    List<FinishedGame> findAll();
}
