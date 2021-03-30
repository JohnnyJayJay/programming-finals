package edu.kit.informatik.firebreaker.program.mapping;

import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.Player;
import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.map.MappingStage;
import edu.kit.informatik.firebreaker.program.GameEnvironment;
import edu.kit.informatik.util.Checks;

import java.util.regex.Matcher;

/**
 * A {@link MappingStage} parsing an input string to a {@link FireEngine} from the current game in
 * the environment.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class FireEngineMapper implements MappingStage<GameEnvironment, String, FireEngine> {

    @Override
    public FireEngine apply(GameEnvironment environment, String input) throws MappingException {
        Matcher matcher = GameEnvironment.FIRE_ENGINE_ID.matcher(input);
        Checks.validate(matcher.matches(),
            () -> new MappingException(input + " is not a valid fire engine id"));
        Game game = environment.getActiveGame();
        Player player = game.getCurrentTurn().getPlayer();
        Checks.validate(player.getName().equals(matcher.group("player")),
            () -> new MappingException("You can only use your own fire engines"));
        int number = Mapping.INTEGER.apply(environment, matcher.group("number"));
        return player.getFireEngine(number)
                .orElseThrow(() -> new MappingException("Fire engine " + number + " does not exist"));
    }
}
