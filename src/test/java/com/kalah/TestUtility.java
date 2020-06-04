package com.kalah;

import com.kalah.domain.enums.Player;

import java.util.Map;
import java.util.stream.IntStream;

import static com.kalah.configuration.Settings.INDEX_OF_FIRST_PIT;
import static com.kalah.configuration.Settings.INDEX_OF_LAST_PIT;

/**
 */
public final class TestUtility {

    private TestUtility() { }

    public static final void fillBoard(int firstKalahStones, int secondKalahStones, int pitStones, Map<Integer, Integer> board) {
        IntStream.range(INDEX_OF_FIRST_PIT, INDEX_OF_LAST_PIT + 1)
                .forEach(pit -> {
                    int amount;
                    if (pit == Player.FIRST_PLAYER.getKalahId()) {
                        amount = firstKalahStones;
                    } else if (pit == Player.SECOND_PLAYER.getKalahId()) {
                        amount = secondKalahStones;

                    } else amount = pitStones;
                    board.put(pit, amount);
                });
    }

    public static void prepareBoardForResult(int firstKalahStones, int secondKalahStones, Map<Integer, Integer> board) {
        fillBoard(firstKalahStones, secondKalahStones, 0, board);
    }
}
