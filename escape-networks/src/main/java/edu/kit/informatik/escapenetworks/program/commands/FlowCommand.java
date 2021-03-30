package edu.kit.informatik.escapenetworks.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.SemanticException;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.escapenetworks.network.Vertex;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.escapenetworks.program.EscapeNetworkSystem;
import edu.kit.informatik.escapenetworks.program.mapping.Mapping;
import edu.kit.informatik.util.Checks;

/**
 * Command to calculate the maximum possible flow in a registered network.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class FlowCommand implements Command<EscapeNetworkSystem> {

    private static final ArgumentParser<EscapeNetworkSystem> PARSER = ArgumentParser.<EscapeNetworkSystem>builder()
            .param("network", Mapping.NETWORK_ID_STAGE.thenValidate(Mapping.REGISTERED_NETWORK_ID_CONSTRAINT))
            .param("source", Mapping.VERTEX_MAPPER)
            .param("sink", Mapping.VERTEX_MAPPER)
            .build();

    @Override
    public void execute(TerminalSession<EscapeNetworkSystem> session, String[] args)
            throws ParsingException, SemanticException {
        EscapeNetworkSystem env = session.getEnvironment();
        Arguments arguments = PARSER.parse(env, args);
        String network = arguments.get("network", String.class);
        Vertex source = arguments.get("source", Vertex.class);
        Vertex sink = arguments.get("sink", Vertex.class);
        Terminal.printLine(Checks.wrapIllegalArgumentException(
            () -> env.computeMaxFlow(network, source, sink),
            (e) -> new SemanticException(e.getMessage())));
    }
}
