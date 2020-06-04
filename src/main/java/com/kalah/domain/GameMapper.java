package com.kalah.domain;

import com.kalah.domain.Game;
import com.kalah.domain.GameDTO;
import com.kalah.domain.NewGameDTO;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriTemplate;
import java.util.Map;

/**
 * Converts Entity to data transfer object.
 *
 */

@Component
public class GameMapper {

    private String gameUrl;

    public GameMapper(Environment environment) {
        this.gameUrl =  environment.getProperty("game.url");
    }

    public GameDTO toDTO(Game game) {
        int id = game.getId();
        Map<Integer, Integer> status = game.getBoard();
        String url = generateGameUrl(id);
        return new GameDTO(id, url, status);
    }

    public NewGameDTO toNewDTO(Game game) {
        int id = game.getId();
        String url = generateGameUrl(id);
        return new NewGameDTO(id, url);
    }

    private String generateGameUrl(int gameId) {
        return new UriTemplate(gameUrl).expand(gameId).toString();
    }
}
