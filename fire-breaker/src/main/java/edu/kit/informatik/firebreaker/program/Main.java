package edu.kit.informatik.firebreaker.program;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.firebreaker.program.commands.BuyFireEngineCommand;
import edu.kit.informatik.firebreaker.program.commands.ExtinguishCommand;
import edu.kit.informatik.firebreaker.program.commands.FireToRollCommand;
import edu.kit.informatik.firebreaker.program.commands.MoveCommand;
import edu.kit.informatik.firebreaker.program.commands.QuitCommand;
import edu.kit.informatik.firebreaker.program.commands.RefillCommand;
import edu.kit.informatik.firebreaker.program.commands.ResetCommand;
import edu.kit.informatik.firebreaker.program.commands.ShowBoardCommand;
import edu.kit.informatik.firebreaker.program.commands.ShowFieldCommand;
import edu.kit.informatik.firebreaker.program.commands.ShowPlayerCommand;
import edu.kit.informatik.firebreaker.program.commands.TurnCommand;

import java.util.Map;

/**
 * The Main-Class of the program.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Main {

    private static final char ARG_DELIMITER = ',';

    private Main() {

    }

    /**
     * The entry point of the program.
     *
     * @param args an array of length 1 containing the board input as its only element.
     */
    public static void main(String[] args) {
        if (args.length == 1) {
            String boardInput = args[0];
            GameEnvironment env = new GameEnvironment(boardInput);
            try {
                env.reset();
            } catch (MappingException e) {
                Terminal.printError(e.getMessage());
                return;
            }
            Map<String, Command<GameEnvironment>> commands = Map.ofEntries(
                    Map.entry("move", new MoveCommand()),
                    Map.entry("extinguish", new ExtinguishCommand()),
                    Map.entry("refill", new RefillCommand()),
                    Map.entry("buy-fire-engine", new BuyFireEngineCommand()),
                    Map.entry("fire-to-roll", new FireToRollCommand()),
                    Map.entry("turn", new TurnCommand()),
                    Map.entry("reset", new ResetCommand()),
                    Map.entry("show-board", new ShowBoardCommand()),
                    Map.entry("show-field", new ShowFieldCommand()),
                    Map.entry("show-player", new ShowPlayerCommand()),
                    Map.entry("quit", new QuitCommand())
            );
            TerminalSession<GameEnvironment> session = new TerminalSession<>(env, commands, ARG_DELIMITER);
            session.run();
        } else {
            Terminal.printError("Expected 1 input arg, got " + args.length);
        }
    }
}
