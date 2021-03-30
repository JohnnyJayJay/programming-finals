package edu.kit.informatik.escapenetworks.program.mapping;

import edu.kit.informatik.escapenetworks.program.EscapeNetworkSystem;
import edu.kit.informatik.escapenetworks.network.Network;
import edu.kit.informatik.map.MappingConstraint;
import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.map.MappingStage;

/**
 * A utility class that contains a few useful {@link MappingStage} and {@link MappingConstraint}
 * definitions for use in parsers in this program.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Mapping {

    /**
     * A {@code MappingConstraint} that validates that an input string matches the
     * {@link EscapeNetworkSystem#NETWORK_NAME_PATTERN network name pattern}.
     */
    public static final MappingConstraint<EscapeNetworkSystem, String> VALID_NETWORK_ID_CONSTRAINT
            = MappingConstraint.of(
                (env, arg) -> EscapeNetworkSystem.NETWORK_NAME_PATTERN.matcher(arg).matches(),
                (arg) -> "illegal escape network name " + arg);

    /**
     * A {@code MappingConstraint} that validates that an input string is the id of a
     * registered network in the {@link EscapeNetworkSystem environment}.
     */
    public static final MappingConstraint<EscapeNetworkSystem, String> REGISTERED_NETWORK_ID_CONSTRAINT
            = MappingConstraint.of(
                (env, arg) -> env.findNetwork(arg).isPresent(),
                (arg) -> "network " + arg + " does not exist");

    /**
     * A {@code MappingStage} that takes a string as input and validates it as
     * defined by {@link #VALID_NETWORK_ID_CONSTRAINT}.
     */
    public static final MappingStage<EscapeNetworkSystem, String, String> NETWORK_ID_STAGE
            = MappingStage.<EscapeNetworkSystem, String>identity()
            .thenValidate(VALID_NETWORK_ID_CONSTRAINT);

    /**
     * A {@code MappingStage} that is the composition of {@link #NETWORK_ID_STAGE} and a
     * stage that maps the id to a registered {@link Network} object in
     * the environment or fails if there is not registered network for that id.
     */
    public static final MappingStage<EscapeNetworkSystem, String, Network> REGISTERED_NETWORK_STAGE
            = Mapping.NETWORK_ID_STAGE
            .thenApply((env, arg) -> env.findNetwork(arg)
                    .orElseThrow(() -> new MappingException("network " + arg + " does not exist")));

    /**
     * An instance of {@link EscapeNetworkMapper}.
     */
    public static final EscapeNetworkMapper NETWORK_MAPPER = new EscapeNetworkMapper();

    /**
     * An instance of {@link VertexMapper}.
     */
    public static final VertexMapper VERTEX_MAPPER = new VertexMapper();

    /**
     * An instance of {@link EscapeSectionMapper}.
     */
    public static final EscapeSectionMapper SECTION_MAPPER = new EscapeSectionMapper();

    /**
     * An instance of {@link IntegerMapper} using the {@link EscapeNetworkSystem} environment.
     */
    public static final IntegerMapper<EscapeNetworkSystem> INTEGER_MAPPER = new IntegerMapper<>();

    private Mapping() {

    }

}
