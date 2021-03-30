package edu.kit.informatik.escapenetworks.program;

import edu.kit.informatik.escapenetworks.network.Edge;
import edu.kit.informatik.escapenetworks.network.Network;
import edu.kit.informatik.escapenetworks.network.Vertex;

import java.util.stream.Collectors;

/**
 * Utility class for generating the string representations of domain objects used in the program.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Display {

    private Display() {

    }

    /**
     * Stringifies the given vertex.
     *
     * @param vertex The vertex to stringify.
     * @return The string representation of a vertex as used by the program.
     */
    public static String stringify(Vertex vertex) {
        return vertex.getIdentifier();
    }

    /**
     * Stringifies the given escape section.
     *
     * @param section The section to stringify.
     * @return The string representation of an escape section as used by the program.
     */
    public static String stringify(EscapeSection section) {
        Edge edge = section.getEdge();
        return stringify(edge.getFrom()) + section.getCapacity() + stringify(edge.getTo());
    }

    /**
     * Stringifies the given network.
     *
     * @param network The network to stringify.
     * @return The string representation of a network as used by the program.
     */
    public static String stringify(Network network) {
        return network.getGraph().getEdges().stream()
                .map((edge) -> EscapeSection.of(edge, network.getCapacity(edge)))
                .map(Display::stringify)
                .collect(Collectors.joining(";"));
    }

}
