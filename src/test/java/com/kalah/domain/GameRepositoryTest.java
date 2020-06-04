package com.kalah.domain;

import com.kalah.domain.enums.Player;
import com.kalah.domain.enums.Status;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class GameRepositoryTest {

    @Autowired
    private GameRepository repository;

    @Test
    public void testSaveGame() {
        Game game = new Game();
        Game created = repository.save(game);

        assertNotNull(created);
        assertEquals(1, created.getId());
        Assert.assertEquals(Status.IN_PROGRESS, created.getStatus());
        Assert.assertEquals(Player.FIRST_PLAYER, created.getPlayer());
    }
}
