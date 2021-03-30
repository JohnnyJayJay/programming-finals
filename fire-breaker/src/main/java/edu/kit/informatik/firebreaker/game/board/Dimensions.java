package edu.kit.informatik.firebreaker.game.board;

import edu.kit.informatik.util.Checks;

import java.util.Objects;

/**
 * A class representing the dimensions of a two-dimensional, rectangular board.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Dimensions {

    private final int width;
    private final int height;

    private Dimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a new dimensions object with the given width and height.
     *
     * @param height The height of the surface.
     * @param width The width of the surface.
     * @return the dimensions.
     * @throws IllegalArgumentException if width or height are <= 0
     */
    public static Dimensions of(int height, int width) {
        Checks.argument(width > 0 && height > 0, "Width and height must be positive");
        return new Dimensions(width, height);
    }

    /**
     * Returns the width part of these dimensions
     *
     * @return The width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height part of these dimensions
     *
     * @return The height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns whether the given coordinates are within the bounds imposed by these dimensions.
     *
     * @param x The x (vertical) coordinate.
     * @param y The y (horizontal) coordinate.
     * @return {@code true} if the coordinates are in bounds of these dimensions, {@code false} if not.
     */
    public boolean bounds(int x, int y) {
        return x >= 0 && y >= 0 && x < height && y < width;
    }

    /**
     * Returns whether the given position is within the bounds imposed by these dimensions.
     *
     * @param position The position to test.
     * @return {@code true} if the coordinates are in bounds of these dimensions, {@code false} if not.
     * @see #bounds(int, int)
     */
    public boolean bounds(Position position) {
        return bounds(position.getX(), position.getY());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dimensions that = (Dimensions) o;
        return width == that.width && height == that.height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, height);
    }
}
