package com.kalah.exception;

/**
 * Error for the play requests which are related not yet created game
 *
 */
public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(String message) {
        super(message);
    }
}
