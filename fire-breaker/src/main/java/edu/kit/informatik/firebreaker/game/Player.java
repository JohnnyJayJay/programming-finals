package edu.kit.informatik.firebreaker.game;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A player in a game of fire-breaker.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Player {

    private final String name;
    private final Map<Integer, FireEngine> fireEngines;

    private int nextFireEngineNumber;

    /**
     * Creates a new player with the given name and no fire engines.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        fireEngines = new HashMap<>();
        this.nextFireEngineNumber = 0;
    }

    /**
     * Returns the name of this player.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Creates and registers a new fire engine for this player.
     *
     * @return a new fire engine with the next incremental number and this player as its owner.
     */
    public FireEngine createFireEngine() {
        int number = nextFireEngineNumber++;
        FireEngine engine = new FireEngine(this, number);
        fireEngines.put(number, engine);
        return engine;
    }

    /**
     * Returns this player's fire engine with the given number.
     *
     * @param number The number of the fire engine to look for.
     * @return An Optional containing the fire engine if found, or an empty optional
     * if this player has no fire engine with the number (anymore).
     */
    public Optional<FireEngine> getFireEngine(int number) {
        return Optional.ofNullable(fireEngines.get(number));
    }

    /**
     * Returns all fire engines in this player's possession.
     *
     * @return An unmodifiable view of this player's fire engines.
     */
    public Collection<FireEngine> getFireEngines() {
        return Collections.unmodifiableCollection(fireEngines.values());
    }

    /**
     * Removes the fire engine with the given number from this player's inventory.
     *
     * @param number The number of the fire engine to remove.
     */
    public void removeFireEngine(int number) {
        fireEngines.remove(number);
    }
}
