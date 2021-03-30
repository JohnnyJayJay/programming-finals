package edu.kit.informatik.escapenetworks.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.SemanticException;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.escapenetworks.network.Edge;
import edu.kit.informatik.escapenetworks.network.Graph;
import edu.kit.informatik.escapenetworks.network.Network;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.escapenetworks.program.Display;
import edu.kit.informatik.escapenetworks.program.EscapeNetworkSystem;
import edu.kit.informatik.escapenetworks.program.EscapeSection;
import edu.kit.informatik.escapenetworks.program.mapping.Mapping;
import edu.kit.informatik.util.Checks;

import java.util.Optional;

/**
 * Command to add new networks to the system or add escape sections to existing escape networks in the system.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class AddCommand implements Command<EscapeNetworkSystem> {

    private static final ArgumentParser<EscapeNetworkSystem> NETWORK_PARSER
            = ArgumentParser.<EscapeNetworkSystem>builder()
            .param("identifier", Mapping.NETWORK_ID_STAGE)
            .param("network", Mapping.NETWORK_MAPPER)
            .build();

    @Override
    public void execute(TerminalSession<EscapeNetworkSystem> session, String[] args)
            throws SemanticException, ParsingException {
        EscapeNetworkSystem env = session.getEnvironment();
        Arguments arguments = NETWORK_PARSER.parse(env, args);
        String identifier = arguments.get("identifier", String.class);
        Network changes = arguments.get("network", Network.class);
        Optional<Network> existingNetwork = env.findNetwork(identifier);
        if (existingNetwork.isPresent()) {
            Network network = existingNetwork.get();
            Graph graph = changes.getGraph();
            Checks.validate(graph.getEdges().size() == 1,
                () -> new SemanticException("you may only add one section at a time"));
            Edge newEdge = graph.getEdges().iterator().next();
            int capacity = changes.getCapacity(newEdge);
            Network updated = Checks.wrapIllegalArgumentException(
                () -> network.update(newEdge, capacity),
                (e) -> new SemanticException(e.getMessage(), e)
            );
            env.invalidateFlowCache(identifier);
            env.registerNetwork(identifier, updated);
            Terminal.printLine("Added new section " + Display.stringify(EscapeSection.of(newEdge, capacity))
                    + " to escape network " + identifier + ".");
        } else {
            Checks.validate(changes.getGraph().getEdges().size() > 1,
                () -> new SemanticException("networks must consist of more than one edge"));
            env.registerNetwork(identifier, changes);
            Terminal.printLine("Added new escape network with identifier " + identifier + ".");
        }

    }
}
