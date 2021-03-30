package edu.kit.informatik.firebreaker.game.board.field;

import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.board.Position;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A forest field.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Forest extends Field {

    private final Map<String, FireEngine> engines;

    private State state;

    /**
     * Creates a new forest with the given position and state.
     *
     * @param position The position this field is located at.
     * @param state The initial state of this forest.
     */
    public Forest(Position position, State state) {
        super(position);
        this.state = state;
        this.engines = new HashMap<>();
    }

    /**
     * Moves this forest to the next state as defined by {@link State#successor()}.
     */
    public void increaseBurning() {
        this.state = state.successor();
    }

    /**
     * Returns whether this forest is burning at all, i.e. if its state is
     * either {@link State#LIGHT_BURN} or {@link State#STRONG_BURN}.
     *
     * @return {@code true} if it is burning, {@code false} if not.
     */
    public boolean isBurning() {
        return state == State.LIGHT_BURN || state == State.STRONG_BURN;
    }

    /**
     * Returns the current state of this forest.
     *
     * @return The state.
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the state of this forest.
     *
     * @param state The new state of this forest.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Places a fire engine on this forest.
     *
     * @param engine The engine to add to this forest.
     */
    public void addFireEngine(FireEngine engine) {
        engines.put(engine.getId(), engine);
    }

    /**
     * Removes the engine with the given id from this forest.
     *
     * @param id The id of the engine to remove.
     */
    public void removeFireEngine(String id) {
        engines.remove(id);
    }

    /**
     * Removes all fire engines from this forest.
     */
    public void removeAllFireEngines() {
        engines.clear();
    }

    /**
     * Returns whether there are any engines on this field.
     *
     * @return {@code true} if there are engines in this forest, {@code false} if not.
     */
    public boolean isOccupied() {
        return !engines.isEmpty();
    }

    /**
     * Returns a Collection of the engines currently in this forest.
     *
     * @return an unmodifiable view of the fire engines.
     */
    public Collection<FireEngine> getFireEngines() {
        return Collections.unmodifiableCollection(engines.values());
    }

    @Override
    public String getDisplayString() {
        return state.indicator + (isOccupied() ? engines.values().stream()
                .sorted(Comparator.<FireEngine, String>comparing((e) -> e.getOwner().getName())
                        .thenComparingInt(FireEngine::getNumber))
                .map(FireEngine::getId)
                .collect(Collectors.joining(",", ",", "")) : "");
    }

    /**
     * An enum listing all possible states of a forest.
     */
    public enum State {
        /**
         * The wet (w) state.
         */
        WET('w'),
        /**
         * The dry (d) state.
         */
        DRY('d'),
        /**
         * The light burn (+) state.
         */
        LIGHT_BURN('+'),
        /**
         * The strong burn (*) state.
         */
        STRONG_BURN('*');

        private final char indicator;

        /**
         * The internal enum constructor.
         *
         * @param indicator The indicator char of this state.
         */
        State(char indicator) {
            this.indicator = indicator;
        }

        /**
         * Returns the indicator character of this state (w, d, + or *).
         *
         * @return The indicator representing this state.
         */
        public char getIndicator() {
            return indicator;
        }

        /**
         * Returns the next state, i.e. the state that results when fire is applied to this state.
         *
         * @return This state's successor.
         */
        public State successor() {
            State[] values = values();
            return values[Math.min(values.length - 1, ordinal() + 1)];
        }

        /**
         * Returns the state associated with the given indicator char.
         *
         * @param indicator The indicator - one of w, d, + or *.
         * @return An Optional containing the state or an empty Optional if the indicator is unknown.
         */
        public static Optional<State> getByIndicator(char indicator) {
            return Arrays.stream(values())
                    .filter((s) -> s.getIndicator() == indicator)
                    .findFirst();
        }
    }
}
