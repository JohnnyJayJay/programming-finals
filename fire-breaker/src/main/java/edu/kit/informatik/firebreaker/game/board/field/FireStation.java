package edu.kit.informatik.firebreaker.game.board.field;

import edu.kit.informatik.firebreaker.game.Player;
import edu.kit.informatik.firebreaker.game.board.Position;

/**
 * A fire station field.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class FireStation extends Field {

    private final Player owner;

    /**
     * Creates a new fire station field with the given position and owned by the given player.
     *
     * @param position The position this field resides at on the board.
     * @param owner The owner of the fire station.
     */
    public FireStation(Position position, Player owner) {
        super(position);
        this.owner = owner;
    }

    /**
     * Returns the owner of this fire station.
     *
     * @return The player who owns this field.
     */
    public Player getOwner() {
        return owner;
    }


    @Override
    public String getDisplayString() {
        return owner.getName();
    }
}
