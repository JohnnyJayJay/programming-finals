package edu.kit.informatik.firebreaker.game.action;

import edu.kit.informatik.firebreaker.game.board.Board;
import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.util.Checks;

/**
 * The action to move a fire engine from its current position to another.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class MoveAction extends EngineAction {

    private static final int MAXIMUM_MOVE_DISTANCE = 2;
    private final Position destination;

    /**
     * Constructs a MoveAction.
     *
     * @param engine      The fire engine to move.
     * @param destination The destination position to move the engine to.
     */
    public MoveAction(FireEngine engine, Position destination) {
        super(engine, false);
        this.destination = destination;
    }

    @Override
    public void perform(Game game, Position position) throws IllegalActionException {
        Board board = game.getBoard();
        Checks.validate(game.getCurrentTurn().canMove(engine),
            () -> new IllegalActionException(engine + " cannot move anymore"));
        Checks.validate(board.canMove(engine, destination, MAXIMUM_MOVE_DISTANCE - 1)
                        || board.canMove(engine, destination, MAXIMUM_MOVE_DISTANCE),
            () -> new IllegalActionException("Cannot move " + engine + " to " + destination));
        board.removeFireEngine(engine);
        board.placeFireEngine(engine, destination);
    }
}
