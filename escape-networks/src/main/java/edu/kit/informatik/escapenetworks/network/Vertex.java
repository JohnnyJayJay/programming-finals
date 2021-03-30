package edu.kit.informatik.escapenetworks.network;

import edu.kit.informatik.util.Checks;

import java.util.Objects;

/**
 * A vertex in a {@link Graph}, identified by a String identifier field.
 * <p>
 * This class is immutable.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Vertex {

    private final String identifier;

    private Vertex(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Creates and returns a Vertex object with the given identifier.
     *
     * @param identifier The identifier of the vertex. Must not be {@code null}.
     * @return The new vertex
     * @throws NullPointerException if the identifier is {@code null}.
     */
    public static Vertex create(String identifier) {
        Checks.notNull(identifier, "Identifier");
        return new Vertex(identifier);
    }

    /**
     * Returns the identifier of this vertex.
     *
     * @return The identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Vertex vertex = (Vertex) o;
        return Objects.equals(identifier, vertex.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        return identifier;
    }
}
