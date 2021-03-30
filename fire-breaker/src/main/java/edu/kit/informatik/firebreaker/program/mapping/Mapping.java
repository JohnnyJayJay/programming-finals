package edu.kit.informatik.firebreaker.program.mapping;

import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.map.MappingConstraint;
import edu.kit.informatik.map.MappingStage;
import edu.kit.informatik.parser.Arguments;
import edu.kit.informatik.firebreaker.program.GameEnvironment;

/**
 * A utility class that contains a few useful {@link MappingStage} and {@link MappingConstraint}
 * definitions for use in parsers in this program.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Mapping {

    /**
     * The name of x coordinate parameters.
     */
    public static final String X_COORDINATE_NAME = "i";

    /**
     * The name of y coordinate parameters.
     */
    public static final String Y_COORDINATE_NAME = "j";

    /**
     * An instance of {@link IntegerMapper}.
     */
    public static final IntegerMapper<GameEnvironment> INTEGER = new IntegerMapper<>();

    /**
     * An instance of {@link FireEngineMapper}.
     */
    public static final FireEngineMapper FIRE_ENGINE = new FireEngineMapper();

    /**
     * A {@code MappingStage} that first parses an input string to an int and then validates that this int
     * is within the vertical bounds of the current game's board.
     */
    public static final MappingStage<GameEnvironment, String, Integer> X_COORDINATE
            = INTEGER.thenValidate(
                (env, arg) -> env.getActiveGame().getBoard().getDimensions().bounds(arg, 0),
                (arg) -> arg + " is not a valid x coordinate"
    );

    /**
     * A {@code MappingStage} that first parses an input string to an int and then validates that this int
     * is within the horizontal bounds of the current game's board.
     */
    public static final MappingStage<GameEnvironment, String, Integer> Y_COORDINATE
            = INTEGER.thenValidate(
                (env, arg) -> env.getActiveGame().getBoard().getDimensions().bounds(0, arg),
                (arg) -> arg + " is not a valid y coordinate"
    );

    private Mapping() {

    }

    /**
     * Creates a {@code MappingConstraint} that validates that an int is within the specified range.
     *
     * @param from The start of the range, inclusive.
     * @param to   The end of the range, exclusive.
     * @return A constraint as defined above.
     */
    public static MappingConstraint<GameEnvironment, Integer> inRange(int from, int to) {
        return MappingConstraint.of((env, arg) -> arg >= from && arg < to,
            (arg) -> String.format("%d is out of range [%d, %d)", arg, from, to));
    }

    /**
     * A helper method to extract a {@code Position} object from two parsed coordinate arguments.
     *
     * @param arguments The parsed arguments. Must contain
     *                  {@link #X_COORDINATE_NAME} and {@link #Y_COORDINATE_NAME} parameters.
     * @return an unbounded Position object combining the two.
     */
    public static Position positionFromArgs(Arguments arguments) {
        return Position.of(arguments.get(X_COORDINATE_NAME, Integer.class),
                arguments.get(Y_COORDINATE_NAME, Integer.class));
    }
}
