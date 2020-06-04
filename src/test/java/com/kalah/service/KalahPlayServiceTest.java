package com.kalah.service;

import com.kalah.domain.Game;
import com.kalah.domain.GameDTO;
import com.kalah.domain.NewGameDTO;
import com.kalah.domain.GameMapper;
import com.kalah.domain.GameRepository;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 */

@RunWith(MockitoJUnitRunner.class)
public class KalahPlayServiceTest {

    private static final String URL = "http://localhost:8080/games/1";

    @Mock
    private GameRepository repository;

    @Mock
    private GameActionsFacade facade;

    @Mock
    private GameMapper mapper;

    private KalahPlayService service;

    @Before
    public void init() {
        service = new KalahPlayServiceImpl(repository, mapper, facade);
    }

    @Test
    public void testCreateGame() {
        Game created = new Game();
        created.setId(1);
        when(repository.save(any(Game.class))).thenReturn(created);
        when(mapper.toNewDTO(created)).thenReturn(new NewGameDTO(created.getId(), URL));

        NewGameDTO newGame = service.createNewGame();
        assertEquals(1, newGame.getId());
        assertEquals(URL, newGame.getUri());
    }

    @Test
    public void testChoosePit() {
        Game game = new Game();
        game.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(game));
        when(repository.save(game)).thenReturn(game);
        when(mapper.toDTO(game)).thenReturn(new GameDTO(game.getId(), URL, game.getBoard()));

        GameDTO gameDTO = service.choosePit(1, 3);

        verify(facade).choosePit(game, 3);
        assertEquals(1, gameDTO.getId());
        assertEquals(URL, gameDTO.getUrl());
        MatcherAssert.assertThat(gameDTO.getStatus(), CoreMatchers.is(game.getBoard()));
    }
}
