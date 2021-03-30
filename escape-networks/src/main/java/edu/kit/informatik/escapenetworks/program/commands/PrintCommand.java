package edu.kit.informatik.escapenetworks.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.escapenetworks.program.EscapeNetworkSystem;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.escapenetworks.program.Display;
import edu.kit.informatik.escapenetworks.program.EscapeSection;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.escapenetworks.program.mapping.Mapping;
import edu.kit.informatik.escapenetworks.network.Edge;
import edu.kit.informatik.escapenetworks.network.Network;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.parser.ParsingException;

import java.util.Comparator;

/**
 * Command to print the structure of a registered network to the console.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class PrintCommand implements Command<EscapeNetworkSystem> {

    private static final ArgumentParser<EscapeNetworkSystem> PARSER = ArgumentParser.<EscapeNetworkSystem>builder()
            .param("network", Mapping.REGISTERED_NETWORK_STAGE)
            .build();

    @Override
    public void execute(TerminalSession<EscapeNetworkSystem> session, String[] args) throws ParsingException {
        EscapeNetworkSystem env = session.getEnvironment();
        Arguments arguments = PARSER.parse(env, args);
        Network network = arguments.get("network", Network.class);
        network.getGraph().getEdges().stream()
                .sorted(Comparator.<Edge, String>comparing((edge) -> edge.getFrom().getIdentifier())
                        .thenComparing((edge) -> edge.getTo().getIdentifier()))
                .map((edge) -> EscapeSection.of(edge, network.getCapacity(edge)))
                .map(Display::stringify)
                .forEach(Terminal::printLine);
    }
}
