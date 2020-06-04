package com.kalah.controller;

import com.kalah.domain.ExceptionDTO;
import org.springframework.http.HttpStatus;
import com.kalah.exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Converts exceptions to Http statuses.
 * {@link HttpStatus}
 * {@link ExceptionDTO} represents error.
 *
 */

@RestControllerAdvice
public class GameExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO handleNotFound(Exception ex) {
        return new ExceptionDTO(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPitNumberException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handleBadRequest(Exception ex) {
        return new ExceptionDTO(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameCompletedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionDTO handleConflict(GameCompletedException ex) {
        return new ExceptionDTO(ex.getMessage(), HttpStatus.CONFLICT, ex.getStatus());
    }
}
