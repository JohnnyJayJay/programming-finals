package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.firebreaker.game.board.Board;
import edu.kit.informatik.firebreaker.game.board.Dimensions;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.board.field.Forest;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;

import java.util.StringJoiner;

/**
 * An implementation of the {@code show-board} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class ShowBoardCommand implements Command<GameEnvironment> {

    @Override
    public void execute(TerminalSession<GameEnvironment> session, String[] args) throws ParsingException {
        GameEnvironment env = session.getEnvironment();
        ArgumentParser.noArgs().parse(env, args);
        Board board = env.getActiveGame().getBoard();
        Dimensions dimensions = board.getDimensions();
        int height = dimensions.getHeight();
        int width = dimensions.getWidth();
        for (int i = 0; i < height; i++) {
            StringJoiner joiner = new StringJoiner(",");
            for (int j = 0; j < width; j++) {
                char symbol = board.getField(Position.of(i, j))
                        .filter(Forest.class::isInstance)
                        .map(Forest.class::cast)
                        .filter(Forest::isBurning)
                        .map(Forest::getState)
                        .map(Forest.State::getIndicator)
                        .orElse('x');
                joiner.add(String.valueOf(symbol));
            }
            Terminal.printLine(joiner.toString());
        }
    }
}
