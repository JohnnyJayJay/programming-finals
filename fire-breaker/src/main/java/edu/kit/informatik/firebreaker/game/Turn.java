package edu.kit.informatik.firebreaker.game;

import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.util.Checks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A class representing a turn in a game of fire-breaker, encapsulating action
 * points and other things that need to be stored during one turn.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Turn {

    private static final int INITIAL_ACTION_POINTS = 3;

    private final Player player;
    private final Map<FireEngine, Integer> actionPoints;
    private final Map<FireEngine, Set<Position>> positionsExtinguished;
    private final Set<FireEngine> stationaryEngines;

    /**
     * Creates a new turn for the given player.
     *
     * @param player The player who is in turn.
     */
    public Turn(Player player) {
        this.player = player;
        this.actionPoints = player.getFireEngines().stream()
                .collect(Collectors.toMap((f) -> f, (f) -> INITIAL_ACTION_POINTS, (l, r) -> l, HashMap::new));
        this.positionsExtinguished = new HashMap<>();
        this.stationaryEngines = new HashSet<>();
    }

    /**
     * Uses one action point of the given fire engine.
     *
     * @param engine The engine to decrement the action points for.
     */
    public void usePoint(FireEngine engine) {
        actionPoints.computeIfPresent(engine, (e, p) -> p - 1);
    }

    /**
     * Returns the action points of a fire engine.
     *
     * @param engine The fire engine.
     * @return The action points remaining for that fire engine.
     */
    public int getActionPoints(FireEngine engine) {
        return actionPoints.get(engine);
    }

    /**
     * Marks the given position as having been extinguished by the given fire engine.
     * <p>
     * This is needed because fire engines aren't allowed to extinguish the same field twice in a single turn.
     *
     * @param engine The fire engine that extinguished the field.
     * @param position The position of the field.
     */
    public void markExtinguished(FireEngine engine, Position position) {
        positionsExtinguished.computeIfAbsent(engine, (k) -> new HashSet<>()).add(position);
    }

    /**
     * Returns whether the given fire engine has extinguished the field at the given position
     * already in this turn.
     *
     * @param engine The engine to check.
     * @param position The position to check.
     * @return {@code true} if the position was marked as extinguished by the engine, {@code false} if not.
     * @see #markExtinguished(FireEngine, Position)
     */
    public boolean hasExtinguished(FireEngine engine, Position position) {
        return positionsExtinguished.getOrDefault(engine, Set.of()).contains(position);
    }

    /**
     * Returns whether the given fire engine is still allowed to move in this turn.
     *
     * @param engine The engine to check.
     * @return {@code true} if it is allowed to move, {@code false} if not.
     * @see #markStationary(FireEngine)
     */
    public boolean canMove(FireEngine engine) {
        return !stationaryEngines.contains(engine);
    }

    /**
     * Marks the given fire engine to be stationary for the rest of this turn, meaning that
     * it will be marked as not allowed to move anymore.
     *
     * @param engine The engine to mark.
     */
    public void markStationary(FireEngine engine) {
        stationaryEngines.add(engine);
    }

    /**
     * Adds a fire engine to the active turn. The fire engine
     * immediately receives {@link #INITIAL_ACTION_POINTS} action points.
     *
     * @param engine The engine to add.
     * @throws IllegalArgumentException if the owner of the engine is not the player of this turn.
     */
    public void addFireEngine(FireEngine engine) {
        Checks.argument(player.equals(engine.getOwner()),
                "Engine does not belong to the player of this turn");
        actionPoints.put(engine, INITIAL_ACTION_POINTS);
    }

    /**
     * Returns the player of this turn.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }
}
