package edu.kit.informatik.escapenetworks.program.mapping;

import edu.kit.informatik.escapenetworks.program.EscapeNetworkSystem;
import edu.kit.informatik.escapenetworks.network.Vertex;
import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.map.MappingStage;
import edu.kit.informatik.util.Checks;

/**
 * A {@link MappingStage} implementation that parses string-encoded escape networks
 * to {@link Vertex} objects or throws a {@link MappingException} if conversion fails.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class VertexMapper implements MappingStage<EscapeNetworkSystem, String, Vertex> {

    @Override
    public Vertex apply(EscapeNetworkSystem environment, String input) throws MappingException {
        Checks.validate(EscapeNetworkSystem.VERTEX_NAME_PATTERN.matcher(input).matches(),
            () -> new MappingException(input + " is not a legal vertex name")
        );
        return Vertex.create(input);
    }
}
