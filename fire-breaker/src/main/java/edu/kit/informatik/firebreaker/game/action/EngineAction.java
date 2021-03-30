package edu.kit.informatik.firebreaker.game.action;

import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.Turn;
import edu.kit.informatik.util.Checks;

/**
 * An action template for actions involving existing fire engines.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public abstract class EngineAction implements Action {

    /**
     * The engine used in this action.
     */
    protected final FireEngine engine;

    private final boolean markStationary;

    /**
     * Constructs an EngineAction.
     *
     * @param engine         The fire engine to use.
     * @param markStationary Whether or not to {@link Turn#markStationary(FireEngine) mark the engine as stationary}
     *                       in the current turn after performing the action.
     */
    protected EngineAction(FireEngine engine, boolean markStationary) {
        this.engine = engine;
        this.markStationary = markStationary;
    }

    @Override
    public final void perform(Game game) throws IllegalActionException {
        Position position = game.getBoard().getPosition(engine)
                .orElseThrow(() -> new IllegalActionException("Unknown engine: " + engine));
        Turn turn = game.getCurrentTurn();
        Checks.validate(turn.getActionPoints(engine) > 0,
            () -> new IllegalActionException(engine + " does not have enough action points"));
        perform(game, position);
        turn.usePoint(engine);
        if (markStationary) {
            turn.markStationary(engine);
        }
    }

    /**
     * Performs the action.
     *
     * @param game     The game to use for the action.
     * @param position The position of the fire engine used in this action.
     * @throws IllegalActionException If the action cannot be performed due to a rule violation.
     */
    protected abstract void perform(Game game, Position position) throws IllegalActionException;
}
