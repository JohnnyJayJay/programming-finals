package edu.kit.informatik.firebreaker.game.board.field;

import edu.kit.informatik.firebreaker.game.board.Position;

/**
 * A pond (or lake) field.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Pond extends Field {

    /**
     * Creates a new pond with the given position.
     *
     * @param position The position this field is located at on the board.
     */
    public Pond(Position position) {
        super(position);
    }

    @Override
    public String getDisplayString() {
        return "L";
    }
}
