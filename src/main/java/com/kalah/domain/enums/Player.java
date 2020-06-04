package com.kalah.domain.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * enum for player and their boards
 */
public enum Player {

    FIRST_PLAYER( Arrays.asList(1, 2, 3, 4, 5, 6),7),

    SECOND_PLAYER( Arrays.asList(8, 9, 10, 11, 12, 13),14);

    private final int kalahId;

    private final List<Integer> pits;

    Player(List<Integer> pits,int kalahId) {
        this.kalahId = kalahId;
        this.pits = Collections.unmodifiableList(pits);
    }

    public int getKalahId() {
        return kalahId;
    }

    public List<Integer> getPits() {
        return pits;
    }

    public Player getOppositePlayer() {
        return this == FIRST_PLAYER ? SECOND_PLAYER : FIRST_PLAYER;
    }
}
