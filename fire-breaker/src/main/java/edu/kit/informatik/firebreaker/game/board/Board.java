package edu.kit.informatik.firebreaker.game.board;

import edu.kit.informatik.firebreaker.game.board.field.Field;
import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.board.field.FireStation;
import edu.kit.informatik.firebreaker.game.board.field.Forest;
import edu.kit.informatik.firebreaker.game.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A playing board for the fire-breaker game.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Board {

    private final Dimensions dimensions;
    private final Map<Player, Position> fireStations;
    private final Map<Position, Field> fields;
    private final Map<FireEngine, Position> fireEngineToPosition;

    /**
     * Creates a board based on the given dimensions and fields.
     * The fields should cover every position within the given dimensions.
     *
     * @param dimensions The dimensions of the board.
     * @param fields     The fields on the board.
     */
    public Board(Dimensions dimensions, Map<Position, Field> fields) {
        this.dimensions = dimensions;
        this.fields = Map.copyOf(fields);
        this.fireStations = streamFields(FireStation.class)
                .collect(Collectors.toMap(FireStation::getOwner, FireStation::getPosition));
        this.fireEngineToPosition = new HashMap<>();
        streamFields(Forest.class)
                .filter(Forest::isOccupied)
                .forEach((forest) ->
                        forest.getFireEngines().forEach((engine) ->
                                fireEngineToPosition.put(engine, forest.getPosition())));
    }

    /**
     * Returns the field at the given position on this board.
     *
     * @param position The position to look up.
     * @return An Optional containing the field or an empty Optional if the Position does not exist on this board.
     */
    public Optional<Field> getField(Position position) {
        return Optional.ofNullable(fields.get(position));
    }

    /**
     * Returns the position of a fire engine currently deployed on the board.
     *
     * @param engine The engine to find.
     * @return An Optional containing the position or an empty Optional if the engine is not on this board.
     */
    public Optional<Position> getPosition(FireEngine engine) {
        return Optional.ofNullable(fireEngineToPosition.get(engine));
    }

    /**
     * Returns the position of the fire station of the given player.
     *
     * @param player The player to find the station position for.
     * @return The position of the player's fire station.
     */
    public Position getFireStation(Player player) {
        return fireStations.get(player);
    }

    /**
     * Removes the given fire engine from the board. Does nothing if the fire engine is not on this board.
     *
     * @param engine The engine to remove.
     */
    public void removeFireEngine(FireEngine engine) {
        getPosition(engine)
                .flatMap(this::getField)
                .map(Forest.class::cast)
                .ifPresent((forest) -> {
                    fireEngineToPosition.remove(engine);
                    forest.removeFireEngine(engine.getId());
                });
    }

    /**
     * Places the given fire engine at the given position.
     *
     * @param engine The engine to place.
     * @param position The position to place it at.
     * @throws IllegalArgumentException if the field at the given position is not a {@link Forest}.
     */
    public void placeFireEngine(FireEngine engine, Position position) {
        Forest destination = getField(position)
                .filter(Forest.class::isInstance)
                .map(Forest.class::cast)
                .orElseThrow(() -> new IllegalArgumentException("Illegal destination " + position));
        fireEngineToPosition.put(engine, position);
        destination.addFireEngine(engine);
    }

    /**
     * Returns whether the given fire engine can move to the given position {@code to} in exactly
     * {@code steps} steps.
     *
     * @param engine The engine to be moved.
     * @param to     The destination position
     * @param steps  The number of steps to be taken.
     * @return {@code true}, if the engine can move to that position, {@code false} if not.
     * @throws IllegalArgumentException if the engine is not on the board.
     */
    public boolean canMove(FireEngine engine, Position to, int steps) {
        Position from = getPosition(engine)
                .orElseThrow(() -> new IllegalArgumentException(engine + " is not on the board"));
        if (from.equals(to)
                || !dimensions.bounds(from)
                || !dimensions.bounds(to)
                || !(fields.get(to) instanceof Forest)
                || ((Forest) fields.get(to)).isBurning()) {
            return false;
        }
        return pathExists(from, to, steps);
    }

    private boolean pathExists(Position from, Position to, int steps) {
        if (steps == 0) {
            return from.equals(to);
        }
        return from.streamAdjacents(false)
                .filter((pos) -> getField(pos).filter(Forest.class::isInstance)
                        .map(Forest.class::cast)
                        .map((forest) -> forest.getState() != Forest.State.STRONG_BURN)
                        .orElse(false))
                .anyMatch((pos) -> pathExists(pos, to, steps - 1));
    }

    /**
     * Returns a {@link Stream} of the fields on this board matching the given type.
     *
     * @param type The {@code Class} representing the type of fields to include.
     * @param <T>  The type of fields to include.
     * @return A Stream containing all fields of the given type.
     */
    public <T extends Field> Stream<T> streamFields(Class<T> type) {
        return fields.values().stream()
                .filter(type::isInstance)
                .map(type::cast);
    }

    /**
     * Returns the dimensions of this board.
     *
     * @return The dimensions.
     */
    public Dimensions getDimensions() {
        return dimensions;
    }

}
