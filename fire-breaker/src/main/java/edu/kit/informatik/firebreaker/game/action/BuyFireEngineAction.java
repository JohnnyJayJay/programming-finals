package edu.kit.informatik.firebreaker.game.action;

import edu.kit.informatik.firebreaker.game.board.Board;
import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.board.field.Forest;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.Player;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.Turn;
import edu.kit.informatik.util.Checks;

/**
 * The action to buy a fire engine for 5 reputation points and placing it on a forest field.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class BuyFireEngineAction implements Action {

    private static final int REPUTATION_COST = 5;
    private final Position spawnPoint;

    /**
     * Constructs a new action object.
     *
     * @param spawnPoint The position where the new engine should be put on the board.
     */
    public BuyFireEngineAction(Position spawnPoint) {
        this.spawnPoint = spawnPoint;
    }


    @Override
    public void perform(Game game) throws IllegalActionException {
        Board board = game.getBoard();
        Turn turn = game.getCurrentTurn();
        Player current = turn.getPlayer();
        Position station = board.getFireStation(current);
        Checks.validate(game.getReputation(current) >= REPUTATION_COST,
            () -> new IllegalActionException("Not enough reputation points"));
        Checks.validate(station.streamAdjacents(true)
                        .filter((pos) -> canSpawn(board, pos))
                        .anyMatch(spawnPoint::equals),
            () -> new IllegalActionException("Cannot spawn an engine on " + spawnPoint));
        FireEngine engine = current.createFireEngine();
        turn.addFireEngine(engine);
        board.placeFireEngine(engine, spawnPoint);
        game.updateReputation(current, -REPUTATION_COST);
    }

    private boolean canSpawn(Board board, Position position) {
        return board.getField(position)
                .filter(Forest.class::isInstance)
                .map(Forest.class::cast)
                .map((forest) -> !forest.isBurning())
                .orElse(false);
    }
}
