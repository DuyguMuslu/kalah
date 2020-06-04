package com.kalah.exception;

import com.kalah.domain.enums.Status;

/**
 * Error for the play requests which are related in the completed game.
 *
 */
public class GameCompletedException extends RuntimeException {

    private final Status status;

    public GameCompletedException(String message, Status status) {
        super(message);
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
