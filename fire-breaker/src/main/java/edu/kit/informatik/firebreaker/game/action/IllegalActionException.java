package edu.kit.informatik.firebreaker.game.action;

/**
 * Thrown to indicate that an {@link Action} could not be performed
 * due to a violation of the game rules.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class IllegalActionException extends Exception {

    /**
     * Constructs a new IllegalActionException.
     *
     * @param message The detail message of the exception.
     */
    public IllegalActionException(String message) {
        super(message);
    }

}
