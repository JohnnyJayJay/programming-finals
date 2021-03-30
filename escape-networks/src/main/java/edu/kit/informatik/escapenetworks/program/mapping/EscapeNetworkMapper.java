package edu.kit.informatik.escapenetworks.program.mapping;

import edu.kit.informatik.escapenetworks.program.EscapeNetworkSystem;
import edu.kit.informatik.escapenetworks.program.EscapeSection;
import edu.kit.informatik.escapenetworks.network.Edge;
import edu.kit.informatik.escapenetworks.network.Graph;
import edu.kit.informatik.escapenetworks.network.Network;
import edu.kit.informatik.escapenetworks.network.Vertex;
import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.map.MappingStage;
import edu.kit.informatik.util.Checks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A {@link MappingStage} implementation that parses string-encoded escape networks
 * to {@link Network} objects or throws a {@link MappingException} if conversion fails.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class EscapeNetworkMapper implements MappingStage<EscapeNetworkSystem, String, Network> {

    private static final String EDGE_DELIMITER = ";";

    @Override
    public Network apply(EscapeNetworkSystem environment, String input)
            throws MappingException {
        Checks.validate(!(input.startsWith(EDGE_DELIMITER) || input.endsWith(EDGE_DELIMITER)),
            () -> new MappingException("malformed input"));
        String[] sections = input.split(EDGE_DELIMITER);
        Set<Vertex> vertices = new HashSet<>();
        Map<Edge, Integer> capacities = new HashMap<>(sections.length);
        for (String sectionString : sections) {
            EscapeSection section = Mapping.SECTION_MAPPER.apply(environment, sectionString);
            Edge edge = section.getEdge();
            vertices.add(edge.getFrom());
            vertices.add(edge.getTo());
            Checks.validate(!capacities.containsKey(edge), () -> new MappingException("duplicate edge " + edge));
            capacities.put(edge, section.getCapacity());
        }
        Graph graph = Graph.create(vertices, capacities.keySet());
        return Checks.wrapIllegalArgumentException(
            () -> Network.create(graph, capacities),
            (e) -> new MappingException(e.getMessage())
        );
    }
}
