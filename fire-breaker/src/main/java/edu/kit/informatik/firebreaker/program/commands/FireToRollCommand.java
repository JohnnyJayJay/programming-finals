package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.SemanticException;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.board.Orientation;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;
import edu.kit.informatik.firebreaker.program.mapping.Mapping;
import edu.kit.informatik.util.Checks;

import java.util.EnumSet;
import java.util.Set;

/**
 * An implementation of the {@code fire-to-roll} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class FireToRollCommand implements Command<GameEnvironment> {

    private static final ArgumentParser<GameEnvironment> PARSER = ArgumentParser.<GameEnvironment>builder()
            .param("number", Mapping.INTEGER.thenValidate(Mapping.inRange(1, 7)))
            .build();

    @Override
    public void execute(TerminalSession<GameEnvironment> session, String[] args)
            throws ParsingException, SemanticException {
        GameEnvironment env = session.getEnvironment();
        Game game = env.getActiveGame();
        Checks.validate(!game.isOver(),
            () -> new SemanticException("Game is over"));
        Checks.validate(game.shouldSpreadFire(),
            () -> new SemanticException("cannot execute fire-to-roll in this state"));
        Arguments arguments = PARSER.parse(env, args);
        int number = arguments.get("number", Integer.class);
        int before = game.getPlayerCount();
        Set<Orientation> wind = windFromDiceRoll(number);
        game.spreadFire(wind);
        int after = game.getPlayerCount();
        // The case distinction below is unclear in the task.
        if (game.isLost()) {
            Terminal.printLine("lose");
        } else if (before == after) {
            Terminal.printLine("OK");
        } else {
            Terminal.printLine(game.getCurrentTurn().getPlayer().getName());
        }
    }

    private Set<Orientation> windFromDiceRoll(int roll) {
        if (roll == 1) {
            return EnumSet.allOf(Orientation.class);
        } else if (roll == 6) {
            return EnumSet.noneOf(Orientation.class);
        } else {
            return EnumSet.of(Orientation.values()[roll - 2]);
        }
    }
}
