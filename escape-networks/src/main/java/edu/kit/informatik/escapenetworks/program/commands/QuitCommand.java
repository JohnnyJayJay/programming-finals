package edu.kit.informatik.escapenetworks.program.commands;

import edu.kit.informatik.escapenetworks.program.EscapeNetworkSystem;
import edu.kit.informatik.cli.Command;
import edu.kit.informatik.cli.TerminalSession;
import edu.kit.informatik.parser.ArgumentParser;
import edu.kit.informatik.parser.ParsingException;

/**
 * Command to exit the program.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class QuitCommand implements Command<EscapeNetworkSystem> {

    @Override
    public void execute(TerminalSession<EscapeNetworkSystem> session, String[] args) throws ParsingException {
        ArgumentParser.noArgs().parse(session.getEnvironment(), args);
        session.quit();
    }
}
