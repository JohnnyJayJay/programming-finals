package edu.kit.informatik.firebreaker.game.action;

import edu.kit.informatik.firebreaker.game.board.Board;
import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.board.field.FireStation;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.board.field.Pond;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.util.Checks;

import java.util.Optional;

/**
 * The action to refill a fire engine's water tank at a {@link Pond} or {@link FireStation}.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class RefillAction extends EngineAction {

    /**
     * Constructs a RefillAction.
     *
     * @param engine The fire engine to refill.
     */
    public RefillAction(FireEngine engine) {
        super(engine, true);
    }

    @Override
    public void perform(Game game, Position position) throws IllegalActionException {
        Board board = game.getBoard();
        Checks.validate(!engine.isFull(),
            () -> new IllegalActionException(engine + " is already full"));
        Checks.validate(
            position.streamAdjacents(true)
                        .map(board::getField)
                        .flatMap(Optional::stream)
                        .anyMatch((field) -> field instanceof FireStation || field instanceof Pond),
            () -> new IllegalActionException(engine + " cannot be refilled in this position")
        );
        engine.refill();
    }

}
