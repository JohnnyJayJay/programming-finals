package edu.kit.informatik.escapenetworks.program;

import edu.kit.informatik.escapenetworks.network.Edge;
import edu.kit.informatik.util.Checks;

/**
 * An escape section consisting of an edge and a capacity.
 * <p>
 * Used as an intermediate representation in argument parsing.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class EscapeSection {

    private final Edge edge;
    private final int capacity;

    private EscapeSection(Edge edge, int capacity) {
        this.edge = edge;
        this.capacity = capacity;
    }

    /**
     * Creates a new escape section object.
     *
     * @param edge     The edge of the section.
     * @param capacity The capacity of the section.
     * @return the escape section.
     */
    public static EscapeSection of(Edge edge, int capacity) {
        Checks.argument(capacity >= 0, "Capacity must be non-negative");
        return new EscapeSection(Checks.notNull(edge, "Edge"), capacity);
    }

    /**
     * Returns the edge of this section.
     *
     * @return The edge.
     */
    public Edge getEdge() {
        return edge;
    }

    /**
     * Returns the capacity of this section.
     *
     * @return The capacity.
     */
    public int getCapacity() {
        return capacity;
    }
}
