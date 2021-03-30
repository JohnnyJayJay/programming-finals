package edu.kit.informatik.escapenetworks.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.escapenetworks.program.EscapeNetworkSystem;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.escapenetworks.program.mapping.Mapping;
import edu.kit.informatik.escapenetworks.network.Network;
import edu.kit.informatik.escapenetworks.network.Vertex;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.util.Pair;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Command to list all registered networks or all the cached flow results for a specific network.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class ListCommand implements Command<EscapeNetworkSystem> {

    private static final ArgumentParser<EscapeNetworkSystem> PARSER = ArgumentParser.<EscapeNetworkSystem>builder()
            .param("network", Mapping.NETWORK_ID_STAGE.thenValidate(Mapping.REGISTERED_NETWORK_ID_CONSTRAINT))
            .build();

    @Override
    public void execute(TerminalSession<EscapeNetworkSystem> session, String[] args) throws ParsingException {
        EscapeNetworkSystem env = session.getEnvironment();
        if (args.length == 0) {
            Map<String, Network> networks = env.getRegisteredNetworks();
            Terminal.printLine(networks.isEmpty()
                    ? "EMPTY"
                    : networks.entrySet().stream()
                    .sorted(Comparator.<Map.Entry<String, Network>>comparingInt(
                        (entry) -> entry.getValue().getGraph().getVertices().size())
                            .reversed()
                            .thenComparing(Map.Entry.comparingByKey()))
                    .map((entry) -> entry.getKey() + " " + entry.getValue().getGraph().getVertices().size())
                    .collect(Collectors.joining("\n")));
        } else {
            Arguments arguments = PARSER.parse(env, args);
            String network = arguments.get("network", String.class);
            Map<Pair<Vertex, Vertex>, Long> cache = env.getCachedFlows(network);
            Terminal.printLine(cache.isEmpty()
                    ? "EMPTY"
                    : cache.entrySet().stream()
                    .sorted(Map.Entry.<Pair<Vertex, Vertex>, Long>comparingByValue()
                            .thenComparing((entry) -> entry.getKey().getLeft().getIdentifier())
                            .thenComparing((entry) -> entry.getKey().getRight().getIdentifier()))
                    .map((entry) -> String.format("%d %s %s", entry.getValue(),
                            entry.getKey().getLeft(), entry.getKey().getRight()))
                    .collect(Collectors.joining("\n")));
        }
    }
}
