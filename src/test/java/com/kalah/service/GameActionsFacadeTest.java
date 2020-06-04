package com.kalah.service;

import com.kalah.domain.Game;
import com.kalah.domain.enums.Player;
import com.kalah.domain.enums.Status;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static com.kalah.TestUtility.fillBoard;
import static com.kalah.TestUtility.prepareBoardForResult;
import static org.junit.Assert.*;

/**
 */
public class GameActionsFacadeTest {

    private final GameActionsFacade facade = new GameActionsFacade();
    private Class<? extends GameActionsFacade> clazz = facade.getClass();

    private Game game;
    private Map<Integer, Integer> board;

    @Before
    public void init() {
        game = new Game();
        board = game.getBoard();
    }

    @Test
    public void testResetPit() throws Exception {
        Method resetPit = clazz.getDeclaredMethod("resetPit", int.class, Map.class);
        openMethodForTest(resetPit);
        resetPit.invoke(facade, 3, board);
        assertEquals(0, board.get(3).intValue());

    }

    @Test
    public void testIncreaseStonesOfPit() throws Exception {
        Method increaseStonesOfPit = clazz.getDeclaredMethod("increaseStonesOfPit", int.class, Map.class, int.class);
        openMethodForTest(increaseStonesOfPit);
        increaseStonesOfPit.invoke(facade, 1, board, 8);
        assertEquals(14, board.get(1).intValue());
    }

    @Test
    public void testPlayOneMoreTurn() throws Exception {
        Method playOneMoreTurn = clazz.getDeclaredMethod("playOneMoreTurn", int.class, Player.class);
        openMethodForTest(playOneMoreTurn);
        boolean first = (boolean) playOneMoreTurn.invoke(facade, 7, Player.FIRST_PLAYER);
        boolean second = (boolean) playOneMoreTurn.invoke(facade, 14, Player.SECOND_PLAYER);
        boolean invalid = (boolean) playOneMoreTurn.invoke(facade, 12, Player.SECOND_PLAYER);
        assertTrue(first);
        assertTrue(second);
        assertFalse(invalid);
    }

    @Test
    public void testGetOppositePit() throws Exception {
        Method getOppositePit = clazz.getDeclaredMethod("getOppositePit", int.class);
        openMethodForTest(getOppositePit);
        int opposite = (int) getOppositePit.invoke(facade, 8);
        assertEquals(6, opposite);
    }

    @Test
    public void testIsUserPit() throws Exception {
        Method isUserPit = clazz.getDeclaredMethod("isUserPit", int.class, Player.class);
        openMethodForTest(isUserPit);
        for (Integer pit : Player.FIRST_PLAYER.getPits()) {
            boolean result = (boolean) isUserPit.invoke(facade, pit, Player.FIRST_PLAYER);
            assertTrue(result);
        }

        for (Integer pit : Player.SECOND_PLAYER.getPits()) {
            boolean result = (boolean) isUserPit.invoke(facade, pit, Player.SECOND_PLAYER);
            assertTrue(result);
        }
        boolean result = (boolean) isUserPit.invoke(facade, 3, Player.SECOND_PLAYER);
        assertFalse(result);
    }

    @Test
    public void testLastPitWasOwnEmptyPit() throws Exception {
        Method lastPitWasOwnEmptyPit = clazz.getDeclaredMethod("lastPitWasOwnEmptyPit", int.class, Game.class);
        openMethodForTest(lastPitWasOwnEmptyPit);
        board.replace(3, 1);
        boolean trueResult = (boolean) lastPitWasOwnEmptyPit.invoke(facade, 3, game);
        assertTrue(trueResult);

        board.replace(8, 1);
        boolean falseResult = (boolean) lastPitWasOwnEmptyPit.invoke(facade, 8, game);
        assertFalse(falseResult);
    }

    @Test
    public void testCheckTheResult() throws Exception {
        Method checkTheResult = clazz.getDeclaredMethod("checkTheResult", Game.class);
        openMethodForTest(checkTheResult);

        prepareBoardForResult(23, 49, board);
        Status firstWinner = (Status) checkTheResult.invoke(facade, game);
        assertEquals(Status.SECOND_PLAYER_WIN, firstWinner);

        prepareBoardForResult(49, 23, board);
        Status secondWinner = (Status) checkTheResult.invoke(facade, game);
        assertEquals(Status.FIRST_PLAYER_WIN, secondWinner);

        prepareBoardForResult(36, 36, board);
        Status draw = (Status) checkTheResult.invoke(facade, game);
        assertEquals(Status.SCORELESS, draw);
    }

    @Test
    public void testMoveStonesFromPitsToKalah() throws Exception {
        Method moveStonesFromPitsToKalah = clazz.getDeclaredMethod("moveStonesFromPitsToKalah", Player.class, Map.class);
        openMethodForTest(moveStonesFromPitsToKalah);

        fillBoard(0, 0, 3, board);
        moveStonesFromPitsToKalah.invoke(facade, Player.FIRST_PLAYER, board);
        Integer amount = board.get(Player.FIRST_PLAYER.getKalahId());
        assertEquals(18, amount.intValue());
    }

    @Test
    public void testGameIsCompleted() throws Exception {
        Method gameIsCompleted = clazz.getDeclaredMethod("gameIsCompleted", Game.class);
        openMethodForTest(gameIsCompleted);

        boolean falseResult = (boolean) gameIsCompleted.invoke(facade, game);
        assertFalse(falseResult);

        prepareBoardForResult(36, 36, board);
        boolean trueResult = (boolean) gameIsCompleted.invoke(facade, game);
        assertTrue(trueResult);
    }

    @Test
    public void testCheckLastPit() throws Exception {
        Method checkLastPit = clazz.getDeclaredMethod("checkLastPit", int.class, Game.class);
        openMethodForTest(checkLastPit);

        board.replace(3, 1);
        checkLastPit.invoke(facade, 3, game);

        int pit = board.get(3);
        int oppositePit = board.get(11);
        int kalahAmount = board.get(Player.FIRST_PLAYER.getKalahId());

        assertEquals(0, pit);
        assertEquals(0, oppositePit);
        assertEquals(7, kalahAmount);
    }

    @Test
    public void testChoosePit() {
        int pitId = 3;
        int pitAmount = board.get(pitId);
        Map<Integer, Integer> beforeMove = new HashMap<>(board);
        facade.choosePit(game, pitId);

        IntStream.range(pitId + 1, pitAmount + 1).forEach(pit -> {
            int amount = board.get(pit);
            assertEquals(amount, beforeMove.get(pit) + 1);
        });
    }

    private void openMethodForTest(Method method) {
        ReflectionUtils.makeAccessible(method);
    }
}
