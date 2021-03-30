package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.action.Action;
import edu.kit.informatik.firebreaker.game.action.BuyFireEngineAction;
import edu.kit.informatik.firebreaker.game.action.IllegalActionException;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;
import edu.kit.informatik.firebreaker.program.mapping.Mapping;

/**
 * An implementation of the {@code buy-fire-engine} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class BuyFireEngineCommand extends ActionCommand {

    private static final ArgumentParser<GameEnvironment> PARSER = ArgumentParser.<GameEnvironment>builder()
            .param(Mapping.X_COORDINATE_NAME, Mapping.X_COORDINATE)
            .param(Mapping.Y_COORDINATE_NAME, Mapping.Y_COORDINATE)
            .build();

    @Override
    protected void execute(GameEnvironment env, String[] args) throws ParsingException, IllegalActionException {
        Arguments arguments = PARSER.parse(env, args);
        Position spawnPoint = Mapping.positionFromArgs(arguments);
        Action action = new BuyFireEngineAction(spawnPoint);
        Game game = env.getActiveGame();
        action.perform(game);
        Terminal.printLine(game.getReputation(game.getCurrentTurn().getPlayer()));
    }
}
