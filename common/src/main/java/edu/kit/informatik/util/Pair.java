package edu.kit.informatik.util;

import java.util.Objects;

/**
 * An immutable 2-tuple.
 *
 * @param <L> The type of the first element in the pair.
 * @param <R> The type of the second element in the pair.
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Pair<L, R> {

    private final L left;
    private final R right;

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Creates a new pair with the given elements.
     *
     * @param left The first element.
     * @param right The second element
     * @param <T> The type of the first element.
     * @param <U> The type of the second element.
     * @return {@code (left, right)}
     */
    public static <T, U> Pair<T, U> of(T left, U right) {
        return new Pair<>(left, right);
    }

    /**
     * Returns the left element in this pair.
     *
     * @return The first element.
     */
    public L getLeft() {
        return left;
    }

    /**
     * Returns the right element in this pair.
     *
     * @return The second element.
     */
    public R getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(left, pair.left) && Objects.equals(right, pair.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    @Override
    public String toString() {
        return "(" + left + ", " + right + ")";
    }
}
