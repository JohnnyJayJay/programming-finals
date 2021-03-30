package edu.kit.informatik.escapenetworks.network;

import edu.kit.informatik.util.Checks;
import edu.kit.informatik.util.ImmutableCollections;

import java.util.Map;
import java.util.Objects;

/**
 * A network defined by a {@link Graph graph} and a capacities map, assigning
 * each {@link Edge edge} in the graph a capacity.
 * <p>
 * The graph of any network does not contain looping edges, or, more generally,
 * parallel edges (e.g. (a, b) and (b, a)). Every network also has at least one valid
 * source and sink vertex, i.e. vertices that do not have any incoming or outgoing edges
 * respectively.
 * <br>
 * Furthermore, all capacities are non-negative.
 * <p>
 * This class is immutable.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Network {

    private final Graph graph;
    private final Map<Edge, Integer> capacities;

    private Network(Graph graph, Map<Edge, Integer> capacities) {
        this.graph = graph;
        this.capacities = capacities;
    }

    /**
     * Creates and returns a new network based on the given graph and capacities.
     * The graph and capacities must reference the same set of edges.
     *
     * @param graph      The underlying graph of this network.
     * @param capacities A map assigning each edge an integer capacity.
     * @return The network object.
     * @throws NullPointerException     if one of the arguments is {@code null}.
     * @throws IllegalArgumentException if
     *                                  <ul>
     *                                      <li>The graph contains parallel edges</li>
     *                                      <li>The graph does not contain a valid source/sink vertex</li>
     *                                      <li>The edges in the graph are not the same as the key set
     *                                          of the capacities map</li>
     *                                      <li>Any capacity is negative</li>
     *                                  </ul>
     */
    public static Network create(Graph graph, Map<Edge, Integer> capacities) {
        Checks.notNull(graph, "Graph");
        Checks.argument(graph.getEdges().stream().map(Edge::reverse).noneMatch(graph::contains),
                "Graph contains parallel edges");
        Checks.argument(graph.getClosedVertices().size() < graph.getVertices().size(),
                "Graph does not contain a valid source");
        Checks.argument(graph.getOpenVertices().size() < graph.getVertices().size(),
                "Graph does not contain a valid sink");
        Checks.notNull(capacities, "Capacities map");
        Checks.argument(graph.getEdges().equals(capacities.keySet()),
                "Capacities map does not match edges in graph");
        Checks.argument(capacities.values().stream().allMatch((c) -> c >= 0),
                "Capacities must be non-negative");
        return new Network(graph, Map.copyOf(capacities));
    }

    /**
     * Creates a new network based on this one, with the edge and its capacity added to
     * the underlying graph/the capacities map.
     * <p>
     * This will update the capacity of the given edge even if it already exists in the graph.
     * <p>
     * See {@link Network#create(Graph, Map)} for possible exceptions.
     *
     * @param edge     The edge to add to the network.
     * @param capacity The capacity of that edge.
     * @return A new network with the given changes.
     * @see Graph#update(Edge)
     * @see Network#create(Graph, Map)
     */
    public Network update(Edge edge, int capacity) {
        return Network.create(
                graph.update(edge),
                ImmutableCollections.merge(capacities, Map.of(edge, capacity))
        );
    }

    /**
     * Returns the underlying graph of this network.
     *
     * @return The graph.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Returns the capacity for the given edge stored in this network.
     *
     * @param edge The edge to retrieve the capacity for.
     * @return The capacity of the edge.
     * @throws IllegalArgumentException if the given edge is not part of this network.
     */
    public int getCapacity(Edge edge) {
        Checks.argument(graph.contains(edge), "Network does not contain edge");
        return capacities.get(edge);
    }

    /**
     * Returns whether the given vertex is a valid source in this network,
     * i.e. whether it has no incoming edges.
     *
     * @param vertex the vertex to check.
     * @return {@code true} if it is a valid source, {@code false} if not.
     * @throws IllegalArgumentException if the vertex is not part of this network.
     */
    public boolean isValidSource(Vertex vertex) {
        return graph.getIncomingEdges(vertex).isEmpty();
    }

    /**
     * Returns whether the given vertex is a valid sink in this network,
     * i.e. whether it has no outgoing edges.
     *
     * @param vertex the vertex to check.
     * @return {@code true} if it is a valid sink, {@code false} if not.
     * @throws IllegalArgumentException if the vertex is not part of this network.
     */
    public boolean isValidSink(Vertex vertex) {
        return graph.getOutgoingEdges(vertex).isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Network network = (Network) o;
        return Objects.equals(graph, network.graph)
                && Objects.equals(capacities, network.capacities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(graph, capacities);
    }

    @Override
    public String toString() {
        return "Network{"
                + "graph=" + graph
                + ", capacities=" + capacities
                + '}';
    }
}
