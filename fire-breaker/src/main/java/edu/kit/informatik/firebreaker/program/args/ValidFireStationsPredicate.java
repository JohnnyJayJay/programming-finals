package edu.kit.informatik.firebreaker.program.args;

import edu.kit.informatik.firebreaker.game.Player;
import edu.kit.informatik.firebreaker.game.board.Dimensions;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.board.field.Field;
import edu.kit.informatik.firebreaker.game.board.field.FireStation;
import edu.kit.informatik.firebreaker.program.GameEnvironment;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * A predicate that validates that the 4 fire stations are in the correct positions.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class ValidFireStationsPredicate implements BiPredicate<Map<String, Player>, BoardParseResult> {

    @Override
    public boolean test(Map<String, Player> playerMap, BoardParseResult result) {
        Map<Position, Field> fields = result.getFields();
        Dimensions dimensions = result.getDimensions();
        if (fields.values().stream()
                .filter(FireStation.class::isInstance)
                .count() != GameEnvironment.PLAYER_COUNT) {
            return false;
        }

        Map<String, Position> positions = Map.of(
                "A", Position.of(0, 0), // upper left corner
                "B", Position.of(dimensions.getHeight() - 1, dimensions.getWidth() - 1), // lower right corner
                "C", Position.of(dimensions.getHeight() - 1, 0), // lower left corner
                "D", Position.of(0, dimensions.getWidth() - 1) // upper right corner
        );

        for (Map.Entry<String, Position> entry : positions.entrySet()) {
            String name = entry.getKey();
            Position pos = entry.getValue();
            Field field = fields.get(pos);
            if (!(field instanceof FireStation
                    && ((FireStation) field).getOwner().equals(playerMap.get(name)))) {
                return false;
            }
        }
        return true;
    }
}
