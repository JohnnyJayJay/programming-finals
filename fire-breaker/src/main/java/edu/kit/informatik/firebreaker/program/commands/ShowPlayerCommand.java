package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.SemanticException;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.Player;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.Turn;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;
import edu.kit.informatik.util.Checks;

import java.util.Comparator;

/**
 * An implementation of the {@code show-player} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class ShowPlayerCommand implements Command<GameEnvironment> {

    @Override
    public void execute(TerminalSession<GameEnvironment> session, String[] args)
            throws ParsingException, SemanticException {
        GameEnvironment env = session.getEnvironment();
        Game game = env.getActiveGame();
        Checks.validate(!game.isOver(), () -> new SemanticException("Game is over"));
        ArgumentParser.noArgs().parse(env, args);
        Turn turn = game.getCurrentTurn();
        Player player = turn.getPlayer();
        Terminal.printLine(player.getName() + "," + game.getReputation(player));
        player.getFireEngines().stream()
                .sorted(Comparator.comparingInt(FireEngine::getNumber))
                .map((engine) -> {
                    Position position = game.getBoard().getPosition(engine).orElseThrow();
                    return String.format("%s,%d,%d,%d,%d",
                            engine.getId(), engine.getWaterStones(), turn.getActionPoints(engine),
                            position.getX(), position.getY());
                }).forEach(Terminal::printLine);
    }
}
