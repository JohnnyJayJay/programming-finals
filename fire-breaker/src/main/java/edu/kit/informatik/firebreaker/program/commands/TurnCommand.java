package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;

/**
 * An implementation of the {@code turn} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class TurnCommand extends ActionCommand {

    @Override
    protected void execute(GameEnvironment env, String[] args) throws ParsingException {
        ArgumentParser.noArgs().parse(env, args);
        Game game = env.getActiveGame();
        game.nextTurn();
        Terminal.printLine(game.getCurrentTurn().getPlayer().getName());
    }
}
