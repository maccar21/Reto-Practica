package com.bancolombia.chocolatinazo.domain.port;

import com.bancolombia.chocolatinazo.domain.model.FinishedGame;

import java.util.List;

/**
 * Port interface for FinishedGame repository operations.
 */
public interface IFinishedGameRepository {
    FinishedGame save(FinishedGame finishedGame);
    List<FinishedGame> findAll();
}
