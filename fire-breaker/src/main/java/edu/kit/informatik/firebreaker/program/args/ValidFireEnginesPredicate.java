package edu.kit.informatik.firebreaker.program.args;

import edu.kit.informatik.firebreaker.game.Player;
import edu.kit.informatik.firebreaker.game.board.Dimensions;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.board.field.Field;
import edu.kit.informatik.firebreaker.game.board.field.Forest;
import edu.kit.informatik.firebreaker.program.GameEnvironment;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * A predicate that validates that the initial 4 fire engines are in the correct positions.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class ValidFireEnginesPredicate implements BiPredicate<Map<String, Player>, BoardParseResult> {

    @Override
    public boolean test(Map<String, Player> playerMap, BoardParseResult result) {
        Dimensions dimensions = result.getDimensions();
        Map<Position, Field> fields = result.getFields();
        if (result.forests()
                .filter(Forest::isOccupied)
                .count() != GameEnvironment.PLAYER_COUNT) {
            return false;
        }

        Map<String, Position> positions = Map.of(
                "A", Position.of(1, 1),
                "B", Position.of(dimensions.getHeight() - 2, dimensions.getWidth() - 2),
                "C", Position.of(dimensions.getHeight() - 2, 1),
                "D", Position.of(1, dimensions.getWidth() - 2)
        );

        for (Map.Entry<String, Position> entry : positions.entrySet()) {
            String name = entry.getKey();
            Position pos = entry.getValue();
            Field field = fields.get(pos);
            Player player = playerMap.get(name);
            if (!(field instanceof Forest
                    && ((Forest) field).getFireEngines().containsAll(player.getFireEngines()))) {
                return false;
            }
        }
        return true;
    }
}
