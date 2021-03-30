package edu.kit.informatik.firebreaker.game;

/**
 * A fire engine belonging to a {@link Player} in a game of fire-breaker.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class FireEngine {

    /**
     * The capacity of the water tank of one fire engine.
     */
    public static final int TANK_CAPACITY = 3;

    private final Player owner;
    private final int number;
    private int waterStones;

    /**
     * Creates a fire engine object with the given number and belonging to the given player.
     * <p>
     * The initial amount of {@link #getWaterStones() water stones} is {@link #TANK_CAPACITY}.
     *
     * @param owner The player this fire engine belongs to.
     * @param number The number of this fire engine.
     */
    public FireEngine(Player owner, int number) {
        this.owner = owner;
        this.number = number;
        this.waterStones = TANK_CAPACITY;
    }

    /**
     * Returns the String id used to uniquely identify fire engines in one game.
     *
     * @return The id, i.e. player name + number
     */
    public String getId() {
        return owner.getName() + number;
    }

    /**
     * Returns the owner of this fire engine.
     *
     * @return The owner.
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Returns the number of this fire engine.
     *
     * @return The number.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Returns the number of water stones currently in this engine's tank.
     *
     * @return the number of water stones.
     */
    public int getWaterStones() {
        return waterStones;
    }

    /**
     * Returns whether the number of water stones is at its maximum of {@link #TANK_CAPACITY}.
     *
     * @return {@code true} if it is, {@code false} if not.
     */
    public boolean isFull() {
        return waterStones == TANK_CAPACITY;
    }

    /**
     * Returns whether this engine's tank contains no more water stones.
     *
     * @return {@code true} if the number of water stones is 0, {@code false} if not.
     */
    public boolean isEmpty() {
        return waterStones == 0;
    }

    /**
     * Sets the amount of water stones in this engine to {@link #TANK_CAPACITY}.
     */
    public void refill() {
        this.waterStones = TANK_CAPACITY;
    }

    /**
     * Decrements the amount of water stones by one, if any are left. Does nothing if empty.
     */
    public void useWater() {
        if (!isEmpty()) {
            this.waterStones--;
        }
    }

    @Override
    public String toString() {
        return getId();
    }
}
