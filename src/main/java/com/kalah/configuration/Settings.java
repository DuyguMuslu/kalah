package com.kalah.configuration;

/**
 * Utility class for game constants.
 *
 */
public final class Settings {

    public static final int INITIAL_STONES_QUANTITY = 6;

    public static final int INDEX_OF_FIRST_PIT= 1;

    public static final int INDEX_OF_LAST_PIT = 14;

    private Settings() {
        throw new AssertionError("Current class can not be instantiated");
    }
}
