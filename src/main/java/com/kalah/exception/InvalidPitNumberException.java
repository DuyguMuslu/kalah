package com.kalah.exception;

/**
 * Error for the play requests which are invalid to play
 *
 */
public class InvalidPitNumberException extends RuntimeException {

    public InvalidPitNumberException(String message) {
        super(message);
    }
}
