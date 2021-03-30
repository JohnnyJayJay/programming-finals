package edu.kit.informatik.escapenetworks.network;

import edu.kit.informatik.util.Checks;

import java.util.List;
import java.util.Objects;

/**
 * A directed edge between to {@link Vertex vertices}.
 * <p>
 * This class is immutable.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Edge {

    private final Vertex from;
    private final Vertex to;
    private final List<Vertex> vertices;

    private Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
        this.vertices = List.of(from, to);
    }

    /**
     * Creates and returns an edge between the two vertices.
     *
     * @param from The vertex the edge starts from. Must not be {@code null}.
     * @param to   The vertex the edge points to. Must not be {@code null}.
     * @return the edge object.
     * @throws NullPointerException if one of the non-null parameter contracts is violated.
     */
    public static Edge create(Vertex from, Vertex to) {
        Checks.notNull(from, "From vertex");
        Checks.notNull(to, "To vertex");
        return new Edge(from, to);
    }

    /**
     * Returns the vertex this edge starts from.
     *
     * @return The first vertex.
     */
    public Vertex getFrom() {
        return from;
    }

    /**
     * Returns the vertex this edge points to.
     *
     * @return The second vertex.
     */
    public Vertex getTo() {
        return to;
    }

    /**
     * Returns this edge as a list, i.e. [{@link #getFrom() from}, {@link #getTo() to}].
     *
     * @return This edge as an immutable list.
     */
    public List<Vertex> asList() {
        return vertices;
    }

    /**
     * Returns the an edge, directed in the opposite direction.
     * <p>
     * I.e., {@code Edge.create(a, b).reverse().equals(Edge.create(b, a))}
     *
     * @return This edge, reversed.
     */
    public Edge reverse() {
        return new Edge(to, from);
    }

    /**
     * Returns whether this edge is a loop, i.e. if it connects a vertex to itself.
     *
     * @return {@code true} if this is a loop, {@code false} if not.
     */
    public boolean isLoop() {
        return from.equals(to);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Edge edge = (Edge) o;
        return Objects.equals(from, edge.from) && Objects.equals(to, edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "(" + from + ", " + to + ")";
    }
}
