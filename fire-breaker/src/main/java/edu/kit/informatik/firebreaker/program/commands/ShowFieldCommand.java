package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.board.field.Field;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;
import edu.kit.informatik.firebreaker.program.mapping.Mapping;

/**
 * An implementation of the {@code show-field} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class ShowFieldCommand implements Command<GameEnvironment> {

    private static final ArgumentParser<GameEnvironment> PARSER = ArgumentParser.<GameEnvironment>builder()
            .param(Mapping.X_COORDINATE_NAME, Mapping.X_COORDINATE)
            .param(Mapping.Y_COORDINATE_NAME, Mapping.Y_COORDINATE)
            .build();

    @Override
    public void execute(TerminalSession<GameEnvironment> session, String[] args) throws ParsingException {
        GameEnvironment env = session.getEnvironment();
        Arguments arguments = PARSER.parse(env, args);
        Position position = Mapping.positionFromArgs(arguments);
        Field field = env.getActiveGame().getBoard().getField(position).orElseThrow();
        Terminal.printLine(field.getDisplayString());
    }
}
