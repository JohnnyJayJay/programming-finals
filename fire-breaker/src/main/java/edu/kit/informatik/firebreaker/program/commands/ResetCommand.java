package edu.kit.informatik.firebreaker.program.commands;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.ParsingException;
import edu.kit.informatik.firebreaker.program.GameEnvironment;

/**
 * An implementation of the {@code reset} command.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class ResetCommand implements Command<GameEnvironment> {

    @Override
    public void execute(TerminalSession<GameEnvironment> session, String[] args) throws ParsingException {
        GameEnvironment env = session.getEnvironment();
        ArgumentParser.noArgs().parse(env, args);
        try {
            env.reset();
        } catch (MappingException e) {
            // exception should not occur because reset has been successfully called before with the same parameters
            throw new AssertionError(e);
        }
        Terminal.printLine("OK");
    }
}
