package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.Turn;
import edu.kit.informatik.firebreaker.game.action.Action;
import edu.kit.informatik.firebreaker.game.action.ExtinguishAction;
import edu.kit.informatik.firebreaker.game.action.IllegalActionException;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.board.field.Forest;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;
import edu.kit.informatik.firebreaker.program.mapping.Mapping;

/**
 * An implementation of the {@code extinguish} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class ExtinguishCommand extends ActionCommand {

    private static final ArgumentParser<GameEnvironment> PARSER = ArgumentParser.<GameEnvironment>builder()
            .param("engine", Mapping.FIRE_ENGINE)
            .param(Mapping.X_COORDINATE_NAME, Mapping.X_COORDINATE)
            .param(Mapping.Y_COORDINATE_NAME, Mapping.Y_COORDINATE)
            .build();


    @Override
    protected void execute(GameEnvironment env, String[] args) throws ParsingException, IllegalActionException {
        Arguments arguments = PARSER.parse(env, args);
        Position target = Mapping.positionFromArgs(arguments);
        FireEngine engine = arguments.get("engine", FireEngine.class);
        Game game = env.getActiveGame();
        Action action = new ExtinguishAction(engine, target);
        action.perform(game);
        if (game.isWon()) {
            Terminal.printLine("win");
        } else {
            Turn turn = game.getCurrentTurn();
            Forest field = (Forest) game.getBoard().getField(target).orElseThrow();
            Terminal.printLine(String.format("%c,%d", field.getState().getIndicator(), turn.getActionPoints(engine)));
        }
    }
}
