package edu.kit.informatik.cli;

import edu.kit.informatik.parser.ParsingException;

/**
 * A command is an action that is triggered by certain user input.
 *
 * @param <E> The environment the command uses.
 * @author JohnnyJayJay
 * @version 1.0.0
 */
@FunctionalInterface
public interface Command<E> {

    /**
     * Executes the command action.
     *
     * @param session The session in which this command was invoked.
     * @param args    The arguments passed to this command.
     * @throws ParsingException  If something went wrong while parsing the arguments.
     * @throws SemanticException If a semantic error occurred during execution.
     */
    void execute(TerminalSession<E> session, String[] args) throws ParsingException, SemanticException;
}
