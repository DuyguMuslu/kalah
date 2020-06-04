package com.kalah.service;

import com.kalah.domain.Game;
import com.kalah.domain.enums.Player;
import com.kalah.domain.enums.Status;
import com.kalah.exception.InvalidPitNumberException;
import com.kalah.configuration.Settings;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

import static com.kalah.domain.enums.Player.FIRST_PLAYER;
import static com.kalah.domain.enums.Player.SECOND_PLAYER;

/**
 * Represents and encapsulates the Kalah game
 * logical components.
 *
 */

@Service
public class GameActionsFacade {

    /**
     * Operates all rules and operations based on the play of current associated player.
     *
     * @param game
     * @param pitId id of the pit selected to make a move
     */
    public void choosePit(Game game, int pitId) {
        checkPitNumber(pitId, game);

        int lastPit = distributeToPits(game,pitId);

        checkLastPit(lastPit, game);

        if (!playOneMoreTurn(lastPit, game.getPlayer())) {
            game.setPlayer(game.getPlayer().getOppositePlayer());
        }
        if (gameIsCompleted(game)) {
            Status winner = checkTheResult(game);
            game.setStatus(winner);
        }
    }

    /**
     * Distributes stones of the selected pit to next pits
     *
     * @param game
     * @param pitId
     */
    private int distributeToPits(Game game, int pitId) {
        Map<Integer, Integer> board = game.getBoard();
        int amount = board.get(pitId);
        int lastPitIndex = pitId + amount;
        int lastPit = lastPitIndex;

        resetPit(pitId, game.getBoard());


        for (int currentIndex = pitId + 1; currentIndex <= lastPitIndex; currentIndex++) {
            int currentPit = currentIndex;
            if (currentIndex == Settings.INDEX_OF_LAST_PIT && lastPitIndex != Settings.INDEX_OF_LAST_PIT) {
                lastPitIndex = lastPitIndex - currentIndex;
                currentIndex = 0;
            }
            if (game.getPlayer().getOppositePlayer().getKalahId() != currentPit) {
                increaseStonesOfPit(currentPit, board, 1);
            } else {
                if (currentIndex != Settings.INDEX_OF_LAST_PIT) {
                    lastPitIndex++;
                }else {
                    lastPitIndex = Settings.INDEX_OF_FIRST_PIT;
                    currentIndex = 0;
                }
            }
        }
        lastPit = lastPit > Settings.INDEX_OF_LAST_PIT ? lastPitIndex : lastPit;
        return lastPit;
    }
    /**
     *  If the last pit is player's own empty pit,
     * gather this stone and all stones in the opposite pit
     * and puts them in player's Kalah.
     *
     * @param lastPit
     * @param game
     */
    private void checkLastPit(int lastPit, Game game) {
        if (lastPitWasOwnEmptyPit(lastPit, game)) {
            int oppositePit = getOppositePit(lastPit);
            int oppositePitAmount = game.getBoard().get(oppositePit);
            if (oppositePitAmount != 0) {
                resetPit(oppositePit, game.getBoard());
                resetPit(lastPit, game.getBoard());
                increaseStonesOfPit(game.getPlayer().getKalahId(), game.getBoard(), oppositePitAmount + 1);
            }
        }
    }

    /**
     * If the game is completed adds all remained stones
     * to Kalah of each player.
     *
     * @param game
     * @return true if the game is completed.
     */
    private boolean gameIsCompleted(Game game) {
        Player player = game.getPlayer();
        List<Integer> pits = player.getPits();
        Map<Integer, Integer> board = game.getBoard();

        boolean playerPitsAreEmpty = pits.stream()
                .map(board::get)
                .allMatch(stoneNumbers -> stoneNumbers == 0);

        boolean oppositePlayerPitsAreEmpty = player.getOppositePlayer().getPits().stream()
                .map(board::get)
                .allMatch(stoneNumbers -> stoneNumbers == 0);

        if (playerPitsAreEmpty || oppositePlayerPitsAreEmpty) {
            moveStonesFromPitsToKalah(player, board);
            moveStonesFromPitsToKalah(player.getOppositePlayer(), board);
            return true;
        }
        return false;
    }

    private void moveStonesFromPitsToKalah(Player player, Map<Integer, Integer> board ) {
        player.getPits().forEach(pit -> {
            int amount = board.get(pit);
            if (amount != 0) {
                int kalahId = player.getKalahId();
                board.replace(kalahId, board.get(kalahId) + amount);
                resetPit(pit, board);
            }
        });
    }

    /**
     * Determines who won the game by checking by the stones in Kalahs
     *
     * @param game
     * @return status of the game.
     */
    private Status checkTheResult(Game game) {
        Map<Integer, Integer> board = game.getBoard();
        int firstPlayerStones = board.get(FIRST_PLAYER.getKalahId());
        int secondPlayerStones = board.get(SECOND_PLAYER.getKalahId());
        if (firstPlayerStones > secondPlayerStones) {
            return Status.FIRST_PLAYER_WIN;
        }else if (firstPlayerStones < secondPlayerStones) {
            return Status.SECOND_PLAYER_WIN;
        }else {
            return Status.SCORELESS;
        }
    }

    /**
     * Checks if the player chooses from the pits
     * that he/she can play
     *
     * @param pitId
     * @param game
     */
    private void checkPitNumber(int pitId, Game game) {
        Player player = game.getPlayer();
        if (pitId == player.getKalahId() || pitId == player.getOppositePlayer().getKalahId()) {
            throw new InvalidPitNumberException("You can not select Kalah!");
        }

        if (pitId < Settings.INDEX_OF_FIRST_PIT || pitId > Settings.INDEX_OF_LAST_PIT) {
            throw new InvalidPitNumberException("Provided pitId is out of bounds...");
        }

        if (!isUserPit(pitId, player)) {
            throw new InvalidPitNumberException("It is not your turn!");
        }
        if (game.getBoard().get(pitId) == 0) {
            throw new InvalidPitNumberException("You can not select empty pit!");
        }
    }

    private boolean lastPitWasOwnEmptyPit(int lastPitId, Game game) {
        Map<Integer, Integer> board = game.getBoard();
        return board.get(lastPitId) == 1 && isUserPit(lastPitId, game.getPlayer());
    }

    private boolean isUserPit(int pitId, Player player) {
        return player.getPits().contains(pitId);
    }

    private int getOppositePit(int pitId) {
        return Settings.INDEX_OF_LAST_PIT - pitId;
    }
    /**
     * If the last pit is Kalah of the player
     * player will have another turn to play.
     *
     * @param lastPitId
     * @param player
     * @return true if the player has one more turn.
     */
    private boolean playOneMoreTurn(int lastPitId, Player player) {
        return player.getKalahId() == lastPitId;
    }

    private void increaseStonesOfPit(int pitId, Map<Integer, Integer> board, int amount) {
        board.replace(pitId, board.get(pitId) + amount);
    }

    private void resetPit(int pitId, Map<Integer, Integer> board) {
        board.replace(pitId, 0);
    }
}