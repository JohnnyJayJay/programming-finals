package edu.kit.informatik.firebreaker.game.action;

import edu.kit.informatik.firebreaker.game.board.Board;
import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.board.field.Forest;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.Turn;
import edu.kit.informatik.util.Checks;

import java.util.Map;

/**
 * The action to make a fire engine extinguish a dry or burning field.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class ExtinguishAction extends EngineAction {

    private static final int REPUTATION_GAIN = 1;

    private static final Map<Forest.State, Forest.State> TRANSITIONS
            = Map.of(Forest.State.STRONG_BURN, Forest.State.LIGHT_BURN,
            Forest.State.LIGHT_BURN, Forest.State.WET,
            Forest.State.DRY, Forest.State.WET);

    private final Position target;

    /**
     * Constructs an ExtinguishAction.
     *
     * @param engine The fire engine to use.
     * @param target The target position to extinguish.
     */
    public ExtinguishAction(FireEngine engine, Position target) {
        super(engine, true);
        this.target = target;
    }

    @Override
    public void perform(Game game, Position position) throws IllegalActionException {
        Board board = game.getBoard();
        Forest targetField = board.getField(target)
                .filter(Forest.class::isInstance)
                .map(Forest.class::cast)
                .filter((forest) -> forest.getState() != Forest.State.WET)
                .orElseThrow(() -> new IllegalActionException("Cannot extinguish " + target));
        Checks.validate(position.streamAdjacents(false).anyMatch(target::equals),
            () -> new IllegalActionException("Engine " + engine + " is not adjacent to target " + target));
        Turn turn = game.getCurrentTurn();
        Checks.validate(!turn.hasExtinguished(engine, target),
            () -> new IllegalActionException(engine + " has already extinguished " + target + " in this turn"));
        Checks.validate(!engine.isEmpty(),
            () -> new IllegalActionException(engine + " does not have enough water"));

        boolean wasBurning = targetField.isBurning();
        engine.useWater();
        targetField.setState(TRANSITIONS.get(targetField.getState()));
        turn.markExtinguished(engine, target);
        if (wasBurning) {
            game.updateReputation(turn.getPlayer(), REPUTATION_GAIN);
        }
    }

}
