package com.kalah.service;

import com.kalah.domain.Game;
import com.kalah.domain.enums.Status;
import com.kalah.domain.GameDTO;
import com.kalah.domain.NewGameDTO;
import com.kalah.exception.*;
import com.kalah.domain.GameMapper;
import com.kalah.domain.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic {@link KalahPlayService} implementation.
 *
 */

@Service
public class KalahPlayServiceImpl implements KalahPlayService {

    private final GameRepository repository;
    private final GameMapper mapper;
    private final GameActionsFacade facade;

    public KalahPlayServiceImpl(GameRepository repository, GameMapper mapper, GameActionsFacade facade) {
        this.repository = repository;
        this.mapper = mapper;
        this.facade = facade;
    }

    @Override
    @Transactional
    public NewGameDTO createNewGame() {
        Game created = repository.save(new Game());
        return mapper.toNewDTO(created);
    }

    @Override
    @Transactional
    public GameDTO choosePit(int gameId, int pitId) {
        Game game = repository.findById(gameId).orElseThrow(
                () -> new GameNotFoundException("Game with id: " + gameId + " not found."));
        checkGameStatus(game);
        facade.choosePit(game, pitId);
        Game afterMove = repository.save(game);
        return mapper.toDTO(afterMove);
    }

    private void checkGameStatus(Game game) {
        Status status = game.getStatus();
        if (status != Status.IN_PROGRESS) {
            throw new GameCompletedException("Game has been already completed with status:" + status, status);
        }
    }
}
