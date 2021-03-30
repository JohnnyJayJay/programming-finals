package edu.kit.informatik.escapenetworks.network;

import edu.kit.informatik.util.Checks;
import edu.kit.informatik.util.ImmutableCollections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A directed graph defined by a set of {@link Vertex vertices} and {@link Edge edges}.
 * <p>
 * This class is immutable.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Graph {

    private final Set<Vertex> vertices;
    private final Set<Edge> edges;

    private Graph(Set<Vertex> vertices, Set<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }

    /**
     * Creates and returns a graph given a set of {@link Vertex vertices} and
     * a set of {@link Edge edges}.
     *
     * @param vertices The vertices of the graph to be created. Must not be or contain {@code null}.
     * @param edges    The edges of the graph to be created. Must not be or contain {@code null},
     *                 must also not reference vertices not contained in the given set of vertices.
     * @return the new graph.
     * @throws NullPointerException     if vertices or edges is {@code null}.
     * @throws IllegalArgumentException if vertices or edges contain {@code null} values
     *                                  or edges contain vertices that are not contained in the given set of vertices.
     */
    public static Graph create(Set<Vertex> vertices, Set<Edge> edges) {
        Checks.notNull(vertices, "Vertices");
        Checks.argument(vertices.stream().noneMatch(Objects::isNull),
                "Vertices contain null values");
        Checks.notNull(edges, "Edges");
        Checks.argument(edges.stream().noneMatch(Objects::isNull),
                "Edges contain null values");
        Checks.argument(edges.stream()
                        .map(Edge::asList)
                        .allMatch(vertices::containsAll),
                "Edges reference vertices that are not part of the graph");
        return new Graph(Set.copyOf(vertices), Set.copyOf(edges));
    }

    /**
     * Returns the set of vertices in this graph.
     *
     * @return the immutable set of vertices.
     */
    public Set<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Returns the set of edges in this graph
     *
     * @return the immutable set of edges.
     */
    public Set<Edge> getEdges() {
        return edges;
    }

    /**
     * Returns whether the graph contains the vertex.
     *
     * @param vertex The vertex to look for.
     * @return {@code true} if the set of vertices contains the vertex, {@code false} if not.
     */
    public boolean contains(Vertex vertex) {
        return vertices.contains(vertex);
    }

    /**
     * Returns whether the graph contains the edge.
     *
     * @param edge The edge to look for.
     * @return {@code true} if the set of edges contains the edge, {@code false} if not.
     */
    public boolean contains(Edge edge) {
        return edges.contains(edge);
    }

    /**
     * Returns a graph of the vertices and edges in this graph
     * + the vertices in the given edge + the given edge.
     *
     * @param edge The edge to add. Must not be {@code null}.
     * @return a new graph based on this graph containing the new edge and the vertices in this edge.
     * @throws NullPointerException if the edge is {@code null}.
     */
    public Graph update(Edge edge) {
        Checks.notNull(edge, "Edge");
        return contains(edge) ? this : Graph.create(
                ImmutableCollections.union(vertices, Set.copyOf(edge.asList())),
                ImmutableCollections.union(edges, Set.of(edge))
        );
    }

    private Stream<Edge> streamAdjacentEdges(Vertex vertex, Function<Edge, Vertex> side) {
        Checks.notNull(vertex, "Vertex");
        Checks.argument(contains(vertex), "Graph does not contain vertex");
        return edges.stream()
                .filter((e) -> side.apply(e).equals(vertex));
    }

    /**
     * Returns the set of edges that start from the given vertex.
     *
     * @param vertex The vertex to find adjacent edges of. Must not be {@code null} and must be part of this graph.
     * @return The set of edges that connect the given vertex to another vertex and
     * thus the empty set if no such edge exists.
     * @throws NullPointerException     if the vertex is {@code null}.
     * @throws IllegalArgumentException if the vertex is not part of this graph.
     */
    public Set<Edge> getOutgoingEdges(Vertex vertex) {
        return streamAdjacentEdges(vertex, Edge::getFrom)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the set of edges that end at the given vertex.
     *
     * @param vertex The vertex to find adjacent edges of. Must not be {@code null} and must be part of this graph.
     * @return The set of edges that connect another vertex to the given vertex and
     * thus the empty set if no such edge exists.
     * @throws NullPointerException     if the vertex is {@code null}.
     * @throws IllegalArgumentException if the vertex is not part of this graph.
     */
    public Set<Edge> getIncomingEdges(Vertex vertex) {
        return streamAdjacentEdges(vertex, Edge::getTo)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the set of vertices that have outgoing edges.<br>
     * If this entire graph is connected, this is the set of all vertices.
     *
     * @return the set of open vertices.
     */
    public Set<Vertex> getOpenVertices() {
        return edges.stream().map(Edge::getFrom).collect(Collectors.toSet());
    }

    /**
     * Returns the set of vertices that have incoming edges.<br>
     * If this entire graph is connected, this is the set of all vertices.
     *
     * @return the set of closed vertices.
     */
    public Set<Vertex> getClosedVertices() {
        return edges.stream().map(Edge::getTo).collect(Collectors.toSet());
    }

    /**
     * Performs a <a href="https://en.wikipedia.org/wiki/Breadth-first_search">Breadth-first-search</a>
     * to find a path in this graph that connects the start vertex to the end vertex.
     *
     * @param start The start vertex. Must be part of this graph and not {@code null}.
     * @param goal  The end vertex. Must be part of this graph and not {@code null}.
     * @return An Optional containing the a path if found, or the empty Optional
     * if there is no path that connects the two vertices.
     * @implNote This implementation is based on the pseudocode
     * found in the wikipedia article linked above.
     */
    public Optional<Path> findPath(Vertex start, Vertex goal) {
        Checks.notNull(start, "Start vertex");
        Checks.notNull(goal, "Goal vertex");
        Checks.argument(contains(start), "Graph does not contain start vertex");
        Checks.argument(contains(goal), "Graph does not contain goal vertex");

        Set<Vertex> encountered = new HashSet<>();
        Queue<Vertex> queue = new ArrayDeque<>();
        Map<Vertex, Vertex> parents = new HashMap<>();
        encountered.add(start);
        queue.offer(start);

        while (!queue.isEmpty()) {
            Vertex current = queue.poll();
            if (current.equals(goal)) {
                List<Vertex> backtrack
                        = Stream.iterate(current, Objects::nonNull, parents::get)
                        .collect(Collectors.toCollection(ArrayList::new));
                Collections.reverse(backtrack);
                return Optional.of(Path.fromVertices(backtrack));
            } else {
                streamAdjacentEdges(current, Edge::getFrom)
                        .filter((e) -> !encountered.contains(e.getTo()))
                        .forEach((e) -> {
                            Vertex next = e.getTo();
                            Vertex parent = e.getFrom();
                            queue.offer(next);
                            encountered.add(next);
                            parents.put(next, parent);
                        });
            }
        }

        return Optional.empty();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Graph graph = (Graph) o;
        return Objects.equals(vertices, graph.vertices)
                && Objects.equals(edges, graph.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, edges);
    }

    @Override
    public String toString() {
        return "Graph{"
                + "vertices=" + vertices
                + ", edges=" + edges
                + '}';
    }
}
