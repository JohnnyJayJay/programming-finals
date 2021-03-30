package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.action.Action;
import edu.kit.informatik.firebreaker.game.action.IllegalActionException;
import edu.kit.informatik.firebreaker.game.action.RefillAction;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;
import edu.kit.informatik.firebreaker.program.mapping.Mapping;

/**
 * An implementation of the {@code refill} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class RefillCommand extends ActionCommand {

    private static final ArgumentParser<GameEnvironment> PARSER = ArgumentParser.<GameEnvironment>builder()
            .param("engine", Mapping.FIRE_ENGINE)
            .build();

    @Override
    protected void execute(GameEnvironment env, String[] args) throws ParsingException, IllegalActionException {
        Arguments arguments = PARSER.parse(env, args);
        FireEngine engine = arguments.get("engine", FireEngine.class);
        Action action = new RefillAction(engine);
        Game game = env.getActiveGame();
        action.perform(game);
        Terminal.printLine(game.getCurrentTurn().getActionPoints(engine));
    }
}
