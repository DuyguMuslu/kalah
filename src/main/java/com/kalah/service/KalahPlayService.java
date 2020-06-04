package com.kalah.service;

import com.kalah.domain.GameDTO;
import com.kalah.domain.NewGameDTO;
import com.kalah.exception.*;

/**
 * Service layer, represents business logic
 * for API endpoints.
 *
 */
public interface KalahPlayService {

    /**
     * Creates new Kalah game.
     *
     * @return new game
     */
    NewGameDTO createNewGame();

    /**
     * Operates the steps of the chosen pit.
     *
     * @param gameId unique identifier of a game
     * @param pitId  id of the pit selected to play
     * @return modified game
     * @throws GameCompletedException
     *         if the game has been already completed.
     *
     * @throws GameNotFoundException
     *         if provided game identifier is not in database.
     *
     */
    GameDTO choosePit(int gameId, int pitId);
}
