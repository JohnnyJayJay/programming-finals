package edu.kit.informatik.escapenetworks.network;

import edu.kit.informatik.util.Checks;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A self-contained class to calculate the maximum possible flow through
 * a network from a source vertex to a sink vertex.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Flow {

    private final Network network;
    private final Graph residualGraph;
    private final Vertex source;
    private final Vertex sink;
    private final Map<Edge, Integer> flow;

    private Flow(Network network, Graph residualGraph, Vertex source, Vertex sink, Map<Edge, Integer> flow) {
        this.network = network;
        this.residualGraph = residualGraph;
        this.source = source;
        this.sink = sink;
        this.flow = flow;
    }

    private static Flow create(Network network, Vertex source, Vertex sink) {
        Checks.notNull(network, "Network");
        Checks.notNull(source, "Source vertex");
        Checks.notNull(sink, "Sink vertex");
        Checks.argument(network.isValidSource(source),
                "Given vertex is not a valid source");
        Checks.argument(network.isValidSink(sink),
                "Given vertex is not a valid sink");

        Set<Edge> originalEdges = network.getGraph().getEdges();
        // add residual edges
        Set<Edge> completeEdges = Stream.concat(originalEdges.stream(), originalEdges.stream().map(Edge::reverse))
                .collect(Collectors.toSet());
        Map<Edge, Integer> initialFlow = completeEdges.stream()
                .collect(Collectors.toMap((e) -> e, (e) -> 0, (m1, m2) -> m1, HashMap::new));
        Graph residualGraph = Graph.create(network.getGraph().getVertices(), completeEdges);
        return new Flow(network, residualGraph, source, sink, initialFlow);
    }

    private int getFlow(Edge edge) {
        return flow.get(edge);
    }

    private int getRemainingCapacity(Edge edge) {
        return (network.getGraph().contains(edge) ? network.getCapacity(edge) : 0)
                - getFlow(edge);
    }

    private Edge getBottleneck(Path path) {
        return path.getEdges().stream()
                .min(Comparator.comparingInt(this::getRemainingCapacity))
                .orElseThrow(AssertionError::new);
    }

    private void augmentFlow(Path path) {
        int bottleneckCapacity = getRemainingCapacity(getBottleneck(path));
        for (Edge edge : path.getEdges()) {
            flow.compute(edge, (e, flow) -> flow + bottleneckCapacity);
            flow.compute(edge.reverse(), (e, flow) -> flow - bottleneckCapacity);
        }
    }

    private Graph computeRemainingGraph() {
        return Graph.create(
                residualGraph.getVertices(),
                residualGraph.getEdges().stream()
                        .filter((e) -> getRemainingCapacity(e) > 0)
                        .collect(Collectors.toSet())
        );
    }

    private void maximize() {
        while (true) {
            Graph remaining = computeRemainingGraph();
            Optional<Path> path = remaining.findPath(source, sink);
            if (path.isPresent()) {
                augmentFlow(path.get());
            } else {
                break;
            }
        }
    }

    private long getTotalFlow() {
        return network.getGraph().getIncomingEdges(sink)
                .stream()
                .mapToLong(this::getFlow)
                .sum();
    }

    /**
     * Calculates the maximum possible flow to put on the given network,
     * starting from the source vertex and ending on the sink vertex.
     *
     * @param network The network to calculate the flow for.
     * @param source  The vertex where the flow starts.
     * @param sink    The vertex where the flow ends.
     * @return The maximum flow found from {@code source} to {@code sink} in {@code network}.
     * @throws NullPointerException     if one of the arguments is {@code null}.
     * @throws IllegalArgumentException if the given source/sink vertex is not a valid source/sink in the network.
     */
    public static long computeMaxFlow(Network network, Vertex source, Vertex sink) {
        Flow flow = Flow.create(network, source, sink);
        flow.maximize();
        return flow.getTotalFlow();
    }

}
