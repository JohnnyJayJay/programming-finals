package edu.kit.informatik.firebreaker.game.board;

import java.util.Optional;

/**
 * An enum representing the 4 main orientations in 2-dimensional space.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public enum Orientation {

    /**
     * North, or relative "up"
     */
    NORTH(-1, 0),
    /**
     * East, or relative "right"
     */
    EAST(0, 1),
    /**
     * South, or relative "down"
     */
    SOUTH(1, 0),
    /**
     * West, or relative "left"
     */
    WEST(0, -1);

    private final int relativeX;
    private final int relativeY;

    /**
     * The internal enum constructor.
     *
     * @param relativeX The x component of the unit vector that points in this direction.
     * @param relativeY The y component of the unit vector that points in this direction.
     */
    Orientation(int relativeX, int relativeY) {
        this.relativeX = relativeX;
        this.relativeY = relativeY;
    }

    /**
     * Returns the position adjacent to the given position when orienting towards this Orientation.
     *
     * @param position The position to get the adjacent for.
     * @return An Optional containing the adjacent position if successful, else an empty Optional.
     * The operation succeeds if the adjacent position is still in bounds as defined by {@link Position#add(int, int)}.
     */
    public Optional<Position> adjacent(Position position) {
        return position.add(relativeX, relativeY);
    }

}
