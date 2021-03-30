package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.SemanticException;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.action.IllegalActionException;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;
import edu.kit.informatik.util.Checks;

/**
 * A command template that ensures that the game isn't over and
 * that fire-to-roll isn't currently expected before executing the actual command.
 * <p>
 * It also allows for {@link IllegalActionException}s to be thrown in the command code.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public abstract class ActionCommand implements Command<GameEnvironment> {

    @Override
    public final void execute(TerminalSession<GameEnvironment> session, String[] args)
            throws ParsingException, SemanticException {
        Game game = session.getEnvironment().getActiveGame();
        Checks.validate(!game.isOver(), () -> new SemanticException("Game is over"));
        Checks.validate(!game.shouldSpreadFire(), () -> new SemanticException("Need to run fire-to-roll first"));
        try {
            execute(session.getEnvironment(), args);
        } catch (IllegalActionException e) {
            throw new SemanticException("Illegal move: " + e.getMessage(), e);
        }

    }

    /**
     * Executes the actual command.
     *
     * @param env  The game environment in which it was executed.
     * @param args The arguments.
     * @throws ParsingException       If something goes wrong while parsing the arguments.
     * @throws SemanticException      If a semantic problem occurs.
     * @throws IllegalActionException If this command performs an
     *                                {@link edu.kit.informatik.firebreaker.game.action.Action}
     *                                that throws an IllegalActionException.
     */
    protected abstract void execute(GameEnvironment env, String[] args)
            throws ParsingException, SemanticException, IllegalActionException;
}
