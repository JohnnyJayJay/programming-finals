package edu.kit.informatik.cli;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.parser.ParsingException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A terminal session reads user input from the standard input and interprets it as commands.
 * <p>
 * A terminal session also holds a reference to an environment object that
 * should encapsulate the state of the program.
 *
 * @param <E> The type of the environment.
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class TerminalSession<E> implements Runnable {

    private final E environment;
    private final Map<String, ? extends Command<E>> commands;
    private final String argDelimiter;
    private final Pattern validInput;

    private boolean quit;

    /**
     * Creates a new terminal session with the given environment and command maps.
     *
     * @param environment The environment object.
     * @param commands The map of command names -> command this session will use.
     * @param argDelimiter The character used to separate arguments.
     */
    public TerminalSession(E environment, Map<String, ? extends Command<E>> commands, char argDelimiter) {
        this.environment = environment;
        this.commands = commands;
        this.argDelimiter = String.valueOf(argDelimiter);

        this.validInput = Pattern.compile(
                String.format("(?<cmd>\\S+)( (?<args>([^%1$c]+%1$c)*[^%1$c]+))?", argDelimiter)
        );
        this.quit = false;
    }

    @Override
    public void run() {
        while (!quit) {
            String input = Terminal.readLine();
            Matcher matcher = validInput.matcher(input);
            if (!matcher.matches()) {
                Terminal.printError("Malformed input");
                continue;
            }
            String commandName = matcher.group("cmd");
            String argsString = matcher.group("args");
            String[] args = argsString == null ? new String[0] : argsString.split(argDelimiter);
            Command<E> command = commands.get(commandName);
            if (command != null) {
                try {
                    command.execute(this, args);
                } catch (ParsingException | SemanticException e) {
                    Terminal.printError(e.getMessage());
                }
            } else {
                Terminal.printError("Unknown command: " + commandName);
            }
        }
    }

    /**
     * Returns the environment of this terminal.
     *
     * @return The environment object.
     */
    public E getEnvironment() {
        return environment;
    }

    /**
     * Instructs this session to quit.
     */
    public void quit() {
        quit = true;
    }

}
