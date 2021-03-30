package edu.kit.informatik.firebreaker.program.args;

import edu.kit.informatik.firebreaker.game.board.Dimensions;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.board.field.Field;
import edu.kit.informatik.firebreaker.game.board.field.Forest;

import java.util.Map;
import java.util.stream.Stream;

/**
 * An intermediate representation of a parsed {@link edu.kit.informatik.firebreaker.game.board.Board}
 * instance where the constraints have not been fully validated yet.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class BoardParseResult {

    private final Dimensions dimensions;
    private final Map<Position, Field> fields;

    /**
     * Creates a new BoardParseResult instance from the given dimensions and fields.
     *
     * @param dimensions The dimensions of the resulting board.
     * @param fields The fields of the resulting board.
     */
    BoardParseResult(Dimensions dimensions, Map<Position, Field> fields) {
        this.dimensions = dimensions;
        this.fields = fields;
    }

    /**
     * Returns the dimensions of this result.
     *
     * @return The dimensions.
     */
    public Dimensions getDimensions() {
        return dimensions;
    }

    /**
     * Returns the fields of this result.
     *
     * @return The fields.
     */
    public Map<Position, Field> getFields() {
        return fields;
    }

    /**
     * Returns a stream of all {@link Forest} fields (useful for multiple validation stages).
     *
     * @return A stream containing all forest fields.
     */
    public Stream<Forest> forests() {
        return fields.values().stream().filter(Forest.class::isInstance).map(Forest.class::cast);
    }
}
