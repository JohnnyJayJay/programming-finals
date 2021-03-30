package edu.kit.informatik.firebreaker.game.board;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A two-dimensional point with integer components that denotes a position on a
 * rectangular field and is optionally bounded by certain {@link Dimensions}.
 * <p>
 * This class is immutable.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Position {

    private final Dimensions bound;
    private final int x;
    private final int y;

    private Position(Dimensions bound, int x, int y) {
        this.bound = bound;
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new position object consisting of the two given components.
     *
     * @param x The x (vertical) coordinate.
     * @param y The y (horizontal) coordinate.
     * @return the Position object.
     */
    public static Position of(int x, int y) {
        return of(null, x, y).orElseThrow(AssertionError::new);
    }

    /**
     * Creates a new position object consisting of the two given components and bounded by the given dimensions.
     *
     * @param bound The underlying bound of the coordinates. May be null.
     * @param x     The x (vertical) coordinate.
     * @param y     The y (horizontal) coordinate.
     * @return An Optional containing the position, if successful an empty Optional
     * if the coordinates are out of bounds for the given dimensions.
     */
    public static Optional<Position> of(Dimensions bound, int x, int y) {
        return bound == null || bound.bounds(x, y)
                ? Optional.of(new Position(bound, x, y))
                : Optional.empty();
    }

    /**
     * Returns the vertical component of this position.
     *
     * @return The x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the horizontal component of this position.
     *
     * @return The y coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns whether this position is bounded by a {@link Dimensions} instance.
     *
     * @return {@code true} if it is bounded, {@code false} if not.
     */
    public boolean isBounded() {
        return bound != null;
    }

    /**
     * Adds the given deltas to the respective coordinates of this position.
     *
     * @param x The value to add to the x (vertical) component.
     * @param y The value to add to the y (horizontal) component.
     * @return an Optional containing a new position with the updated coordinates
     * if successful, or an empty optional if the resulting position is not bounded
     * by the underlying {@link Dimensions} of this position anymore, if any.
     */
    public Optional<Position> add(int x, int y) {
        int newX = this.x + x;
        int newY = this.y + y;
        boolean inBounds = !isBounded() || bound.bounds(newX, newY);
        return inBounds ? Optional.of(new Position(bound, newX, newY)) : Optional.empty();
    }

    /**
     * Creates and returns a stream of the relative adjacent positions of this position.
     * The stream only contains positions that are also bounded by the underlying {@link Dimensions}, if any.
     *
     * @param includeDiagonals Whether to include diagonally adjacent positions or not.
     * @return A {@link Stream} containing those positions.
     */
    public Stream<Position> streamAdjacents(boolean includeDiagonals) {
        Stream<Optional<Position>> positions = Stream.of(add(1, 0), add(-1, 0), add(0, 1), add(0, -1));
        if (includeDiagonals) {
            positions = Stream.concat(
                    positions,
                    Stream.of(add(1, -1), add(-1, 1), add(1, 1), add(-1, -1))
            );
        }
        return positions.flatMap(Optional::stream);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
