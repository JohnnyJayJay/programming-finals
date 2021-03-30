package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;

/**
 * An implementation of the {@code quit} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class QuitCommand implements Command<GameEnvironment> {

    @Override
    public void execute(TerminalSession<GameEnvironment> session, String[] args) throws ParsingException {
        ArgumentParser.noArgs().parse(session.getEnvironment(), args);
        session.quit();
    }
}
