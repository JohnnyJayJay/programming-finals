package edu.kit.informatik.firebreaker.game.action;

import edu.kit.informatik.firebreaker.game.Game;

/**
 * An action (also called a move) that can be performed in a game of fire-breaker.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public interface Action {

    /**
     * Attempts to apply this action's changes to the given game.
     *
     * @param game The active game.
     * @throws IllegalActionException If the rules don't allow for this
     *                                action to be performed in the current state of the game.
     */
    void perform(Game game) throws IllegalActionException;

}
