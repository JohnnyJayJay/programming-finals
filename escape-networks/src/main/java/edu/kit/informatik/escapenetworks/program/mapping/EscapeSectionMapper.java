package edu.kit.informatik.escapenetworks.program.mapping;

import edu.kit.informatik.escapenetworks.program.EscapeNetworkSystem;
import edu.kit.informatik.escapenetworks.program.EscapeSection;
import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.map.MappingStage;
import edu.kit.informatik.escapenetworks.network.Edge;
import edu.kit.informatik.escapenetworks.network.Vertex;
import edu.kit.informatik.util.Checks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A {@link MappingStage} implementation that parses string-encoded escape networks
 * to {@link EscapeSection} objects or throws a {@link MappingException} if conversion fails.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class EscapeSectionMapper implements MappingStage<EscapeNetworkSystem, String, EscapeSection> {

    private static final String VERTEX_ID = EscapeNetworkSystem.VERTEX_NAME_PATTERN.pattern();
    private static final Pattern EDGE_FORMAT
            = Pattern.compile("(" + VERTEX_ID + ")(\\d+)(" + VERTEX_ID + ")");

    @Override
    public EscapeSection apply(EscapeNetworkSystem environment, String input) throws MappingException {
        Matcher matcher = EDGE_FORMAT.matcher(input);
        Checks.validate(matcher.matches(),
            () -> new MappingException("Edge '" + input + "' does not conform to edge pattern"));
        String fromVertexId = matcher.group(1);
        String capacityString = matcher.group(2);
        String toVertexId = matcher.group(3);
        int capacity = Mapping.INTEGER_MAPPER
                .thenValidate((env, cap) -> cap > 0,
                    (cap) -> "invalid capacity " + cap + "; capacities must be non-negative")
                .apply(environment, capacityString);
        Vertex from = Mapping.VERTEX_MAPPER.apply(environment, fromVertexId);
        Vertex to = Mapping.VERTEX_MAPPER.apply(environment, toVertexId);
        Edge edge = Edge.create(from, to);
        Checks.validate(!edge.isLoop(),
            () -> new MappingException("escape section must not be a loop: " + from));
        return EscapeSection.of(edge, capacity);
    }
}
