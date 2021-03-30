package edu.kit.informatik.firebreaker.program.args;

import edu.kit.informatik.firebreaker.game.Player;
import edu.kit.informatik.firebreaker.game.board.field.Forest;

import java.util.Map;
import java.util.function.BiPredicate;

/**
 * A predicate that validates that there is at least one strongly and lightly burning forest in the beginning.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class ValidForestStatePredicate implements BiPredicate<Map<String, Player>, BoardParseResult> {

    @Override
    public boolean test(Map<String, Player> playerMap, BoardParseResult result) {
        return result.forests().map(Forest::getState).anyMatch(Forest.State.STRONG_BURN::equals)
                && result.forests().map(Forest::getState).anyMatch(Forest.State.LIGHT_BURN::equals);
    }
}
