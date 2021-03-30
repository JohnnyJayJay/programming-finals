package edu.kit.informatik.firebreaker.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A class encapsulating a mutable cycle over a list of elements.
 * <p>
 * Cycles "iterate" over a list of elements, starting from the beginning
 * when they've reached the end. For this purpose the class defines {@link #current()}
 * and {@link #advance()}.
 * <p>
 * Cycles can also specify a rotation which rotates the order of elements by a specified amount
 * at the end of each completed iteration.
 * <p>
 * Finally, elements can be removed during a cycle. Changes in order take effect at the end
 * of one full iteration.
 *
 * @param <E> The element type of the cycle
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Cycle<E> {

    private final List<E> elements;
    private final int rotation;
    private int position;
    private Collection<E> eliminations;

    private Cycle(List<E> elements, int rotation) {
        this.elements = elements;
        this.rotation = rotation;
        this.position = 0;
        this.eliminations = Set.of();
    }

    /**
     * Creates a new cycle based on the given list of elements and the given rotation.
     *
     * @param elements The elements to cycle over.
     * @param rotation The rotation to apply to the list of elements after each iteration.
     *                 The number specifies by how many elements to rotate to the right.
     * @param <E> The element type of the list.
     * @return A new cycle.
     */
    public static <E> Cycle<E> of(List<E> elements, int rotation) {
        return new Cycle<>(new ArrayList<>(elements), rotation);
    }

    /**
     * Returns the current element of this cycle.
     *
     * @return The element at the cursor.
     */
    public E current() {
        return elements.get(position);
    }

    /**
     * Moves the cursor to the next element in this cycle, skipping elements
     * marked for elimination if necessary.
     */
    public void advance() {
        if (eliminations.containsAll(elements)) {
            return;
        }
        do {
            position++;
        } while (position < elements.size() && eliminations.contains(current()));

        if (position == elements.size()) {
            position = 0;
            Collections.rotate(elements, rotation);
            elements.removeAll(eliminations);
        }
    }

    /**
     * Returns the current position, or cursor of this cycle.
     *
     * @return the current position.
     */
    public int position() {
        return position;
    }

    /**
     * Marks the given elements for elimination in the next iteration.
     * <p>
     * After calling this method, the elements in the given collection won't be returned
     * by this cycle anymore, but they will still affect positioning to ensure a consistent
     * rotation in the next iteration.
     *
     * @param elements The elements to remove.
     */
    public void eliminate(Collection<E> elements) {
        eliminations = elements;
    }


}
