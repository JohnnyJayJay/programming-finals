package edu.kit.informatik.escapenetworks.network;

import edu.kit.informatik.util.Checks;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A path in a graph, connecting one vertex with another.
 * <p>
 * This class is immutable.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Path {

    private final List<Vertex> vertices;
    private final List<Edge> edges;

    private Path(List<Vertex> vertices) {
        int vertexCount = vertices.size();
        this.vertices = vertices;
        List<Edge> edges = new ArrayList<>(vertexCount - 1);
        for (int i = 0; i < vertexCount - 1; i++) {
            edges.add(Edge.create(vertices.get(i), vertices.get(i + 1)));
        }
        this.edges = List.copyOf(edges);
    }

    /*public static Path fromEdges(List<Edge> edges) {
        Checks.notNull(edges, "Edges");
        Checks.argument(!edges.isEmpty(), "Path must be at least of length 1");
        return new Path(List.copyOf(edges));
    }*/

    /**
     * Creates a new path from the given list of vertices.
     * <p>
     * This class does not represent paths of length zero, i.e. with only one vertex and no edge.
     * Therefore, the list of vertices must contain at least two entries.
     *
     * @param vertices The list of vertices describing the path.
     * @return the new Path object.
     * @throws NullPointerException     if the list is {@code null}.
     * @throws IllegalArgumentException if the list contains less than 2 vertices.
     */
    public static Path fromVertices(List<Vertex> vertices) {
        Checks.notNull(vertices, "Vertices");
        Checks.argument(vertices.size() >= 2, "Path must be at least of length 1");
        return new Path(List.copyOf(vertices));
    }

    /**
     * Returns the vertex this path starts on.
     *
     * @return The first vertex.
     */
    public Vertex getStart() {
        return vertices.get(0);
    }

    /**
     * Returns the vertex this path ends on.
     *
     * @return The last vertex.
     */
    public Vertex getEnd() {
        return vertices.get(length());
    }

    /**
     * Returns the list of vertices in this path.
     *
     * @return the vertices, in order.
     */
    public List<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Returns the list of edges this path traverses.
     *
     * @return the edges, in order.
     */
    public List<Edge> getEdges() {
        return edges;
    }

    /**
     * Returns the length of this path.
     * <p>
     * <strong>Important: this is equal to the amount of edges in this path,
     * i.e. the amount of vertices - 1.</strong> This is because the length represents
     * the amount of "steps" taken and not the amount of vertices visited.
     *
     * @return The length as an integer.
     */
    public int length() {
        return edges.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Path path = (Path) o;
        return Objects.equals(vertices, path.vertices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices);
    }

    @Override
    public String toString() {
        return vertices.stream()
                .map(Vertex::toString)
                .collect(Collectors.joining(", ", "(", ")"));
    }
}
