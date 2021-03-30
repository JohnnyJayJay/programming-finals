package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.action.IllegalActionException;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.action.MoveAction;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;
import edu.kit.informatik.firebreaker.program.mapping.Mapping;

/**
 * An implementation of the {@code move} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class MoveCommand extends ActionCommand {

    private static final ArgumentParser<GameEnvironment> PARSER = ArgumentParser.<GameEnvironment>builder()
            .param("engine", Mapping.FIRE_ENGINE)
            .param(Mapping.X_COORDINATE_NAME, Mapping.X_COORDINATE)
            .param(Mapping.Y_COORDINATE_NAME, Mapping.Y_COORDINATE)
            .build();

    @Override
    protected void execute(GameEnvironment env, String[] args) throws ParsingException, IllegalActionException {
        Arguments arguments = PARSER.parse(env, args);
        Game game = env.getActiveGame();
        Position destination = Mapping.positionFromArgs(arguments);
        FireEngine engine = arguments.get("engine", FireEngine.class);
        MoveAction action = new MoveAction(engine, destination);
        action.perform(game);
        Terminal.printLine("OK");
    }
}
