package edu.kit.informatik.firebreaker.game.board.field;

import edu.kit.informatik.firebreaker.game.board.Position;

/**
 * A field on a fire-breaker {@link edu.kit.informatik.firebreaker.game.board.Board}.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public abstract class Field {

    private final Position position;

    /**
     * Common constructor of all field types.
     *
     * @param position The position of the field on the board
     */
    Field(Position position) {
        this.position = position;
    }

    /**
     * Returns the position of this field on the board.
     *
     * @return The field position.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Returns a string representation of this field to use in the UI.
     *
     * @return This field as a string.
     */
    public abstract String getDisplayString();

}
