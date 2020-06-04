package com.kalah.controller;

import com.kalah.domain.GameDTO;
import com.kalah.domain.NewGameDTO;
import com.kalah.service.KalahPlayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Rest endpoints for Kalah.
 *
 */

@RestController
@RequestMapping("/games")
@Api(value="Kalah",  tags=("kalah"))
public class GameController {

    private final KalahPlayService service;

    public GameController(KalahPlayService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value="Create the game", notes="Creates new Kalah game")
    public NewGameDTO createNewGame() {
        return service.createNewGame();
    }

    @PutMapping("/{gameId}/pits/{pitId}")
    @ApiOperation(value="Make a move")
    public GameDTO choosePit(@PathVariable int gameId,  @PathVariable int pitId) { return service.choosePit(gameId, pitId); }
}
