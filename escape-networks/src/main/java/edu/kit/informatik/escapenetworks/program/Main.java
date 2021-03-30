package edu.kit.informatik.escapenetworks.program;

import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.escapenetworks.program.commands.AddCommand;
import edu.kit.informatik.escapenetworks.program.commands.FlowCommand;
import edu.kit.informatik.escapenetworks.program.commands.ListCommand;
import edu.kit.informatik.escapenetworks.program.commands.PrintCommand;
import edu.kit.informatik.escapenetworks.program.commands.QuitCommand;

import java.util.Map;

/**
 * The entry point of the program.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class Main {

    private static final char ARG_DELIMITER = ' ';

    /**
     * The entry point of the program.
     *
     * @param args The input arguments. Should be empty.
     */
    public static void main(String[] args) {
        EscapeNetworkSystem system = new EscapeNetworkSystem();
        Map<String, Command<EscapeNetworkSystem>> commands = Map.of(
                "add", new AddCommand(),
                "flow", new FlowCommand(),
                "list", new ListCommand(),
                "print", new PrintCommand(),
                "quit", new QuitCommand()
        );
        TerminalSession<EscapeNetworkSystem> session = new TerminalSession<>(system, commands, ARG_DELIMITER);
        session.run();
    }
}
