package edu.kit.informatik.firebreaker.game;

import edu.kit.informatik.firebreaker.game.board.Board;
import edu.kit.informatik.firebreaker.game.board.Orientation;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.board.field.Field;
import edu.kit.informatik.firebreaker.game.board.field.Forest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class representing a game of fire-breaker.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Game {

    private static final int TURN_ROTATION = -1;

    private final Board board;
    private final List<Player> players;
    private final Cycle<Player> turns;
    private final Map<Player, Integer> reputationPoints;

    private Turn currentTurn;
    private boolean shouldSpreadFire;

    /**
     * Creates a new game with the given players and board.
     *
     * @param players The players participating in this game, in order.
     * @param board The board this game should be played on.
     */
    public Game(List<Player> players, Board board) {
        this.board = board;
        this.players = new ArrayList<>(players);
        this.turns = Cycle.of(players, TURN_ROTATION);
        this.reputationPoints = players.stream()
                .collect(Collectors.toMap((p) -> p, (p) -> 0, (l, r) -> l, HashMap::new));
        this.currentTurn = null;
        this.shouldSpreadFire = false;
        beginTurn();
    }

    private void beginTurn() {
        Player current = turns.current();
        currentTurn = new Turn(current);
    }

    /**
     * Returns whether the {@link #spreadFire(Set)} method should be called,
     * i.e. whether it is the beginning of a round.
     *
     * @return {@code true} if it's the beginning of a round (excluding the first), {@code false} if not.
     */
    public boolean shouldSpreadFire() {
        return shouldSpreadFire;
    }

    /**
     * Spreads fire in all given orientations and eliminates burnt fire engines where necessary.
     * <p>
     * The fire is spread according to the specification.
     *
     * @param wind the directions in which to spread the fire.
     */
    public void spreadFire(Set<Orientation> wind) {
        this.shouldSpreadFire = false;
        if (!wind.isEmpty()) {
            Stream<Position> toIncrease = Stream.concat(
                    board.streamFields(Forest.class)
                            .filter((forest) -> forest.getState() == Forest.State.STRONG_BURN)
                            .map(Field::getPosition)
                            .flatMap((pos) -> wind.stream().map((orientation) -> orientation.adjacent(pos)))
                            .flatMap(Optional::stream),
                    board.streamFields(Forest.class)
                            .filter((forest) -> forest.getState() == Forest.State.LIGHT_BURN)
                            .map(Field::getPosition)
            );
            Set<Forest> forests = toIncrease.distinct()
                    .map(board::getField)
                    .flatMap(Optional::stream)
                    .filter(Forest.class::isInstance)
                    .map(Forest.class::cast)
                    .collect(Collectors.toSet());
            forests.forEach(Forest::increaseBurning);
        }
        eliminateBurntFireEngines();
    }

    private void eliminateBurntFireEngines() {
        board.streamFields(Forest.class)
                .filter(Forest::isOccupied)
                .filter((forest) -> forest.getState() == Forest.State.STRONG_BURN)
                .forEach(forest -> {
                    // copy to avoid ConcurrentModificationException
                    Set<FireEngine> engines = Set.copyOf(forest.getFireEngines());
                    engines.forEach((engine) -> {
                        board.removeFireEngine(engine);
                        engine.getOwner().removeFireEngine(engine.getNumber());
                    });
                });
        Collection<Player> eliminated = players.stream()
                .filter((player) -> player.getFireEngines().isEmpty())
                .collect(Collectors.toSet());
        players.removeAll(eliminated);
        turns.eliminate(eliminated);
        if (eliminated.contains(currentTurn.getPlayer())) {
            nextTurn();
        }
    }

    /**
     * Returns whether this game is over.
     *
     * @return {@code true} if it is over, {@code false} if it is still going.
     */
    public boolean isOver() {
        return isLost() || isWon();
    }

    /**
     * Returns whether this game is lost, i.e., all fire engines have been burnt.
     *
     * @return {@code true} if the players lost this game, {@code false} if not.
     */
    public boolean isLost() {
        return players.isEmpty();
    }

    /**
     * Returns whether this game is won, i.e., all fires have been extinguished.
     *
     * @return {@code true} if the players won this game, {@code false} if not.
     */
    public boolean isWon() {
        return board.streamFields(Forest.class).noneMatch(Forest::isBurning);
    }

    /**
     * Ends the current player's turn and advances to the next turn.
     */
    public void nextTurn() {
        turns.advance();
        currentTurn = null;
        beginTurn();
        this.shouldSpreadFire = turns.position() == 0;
    }

    /**
     * Returns the currently active turn.
     *
     * @return The turn object.
     */
    public Turn getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Returns the board this game is using.
     *
     * @return the game board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns how many players are still playing.
     *
     * @return The amount of active players.
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Returns the reputation points of a player.
     *
     * @param player The player.
     * @return The reputation points of that player.
     */
    public int getReputation(Player player) {
        return reputationPoints.get(player);
    }

    /**
     * Updates the given player's reputation points by adding the specified amount.
     *
     * @param player The player to update the reputation for.
     * @param delta The amount to add to the current reputation points.
     */
    public void updateReputation(Player player, int delta) {
        reputationPoints.computeIfPresent(player, (p, points) -> points + delta);
    }
}
