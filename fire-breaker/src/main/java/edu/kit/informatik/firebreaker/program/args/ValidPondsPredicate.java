package edu.kit.informatik.firebreaker.program.args;

import edu.kit.informatik.firebreaker.game.Player;
import edu.kit.informatik.firebreaker.game.board.Dimensions;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.board.field.Field;
import edu.kit.informatik.firebreaker.game.board.field.Pond;
import edu.kit.informatik.firebreaker.program.GameEnvironment;

import java.util.Map;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * A predicate that validates that the 4 ponds are in the correct positions.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class ValidPondsPredicate implements BiPredicate<Map<String, Player>, BoardParseResult> {

    @Override
    public boolean test(Map<String, Player> playerMap, BoardParseResult result) {
        Map<Position, Field> fields = result.getFields();
        Dimensions dimensions = result.getDimensions();
        if (fields.values().stream()
                .filter(Pond.class::isInstance)
                .count() != GameEnvironment.PLAYER_COUNT) {
            return false;
        }
        return Stream.of(Position.of(0, dimensions.getWidth() / 2),
                Position.of(dimensions.getHeight() / 2, 0),
                Position.of(dimensions.getHeight() - 1, dimensions.getWidth() / 2),
                Position.of(dimensions.getHeight() / 2, dimensions.getWidth() - 1))
                .map(fields::get).allMatch(Pond.class::isInstance);
    }
}
