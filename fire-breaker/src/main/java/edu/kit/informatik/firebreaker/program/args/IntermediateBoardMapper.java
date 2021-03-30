package edu.kit.informatik.firebreaker.program.args;

import edu.kit.informatik.firebreaker.game.FireEngine;
import edu.kit.informatik.firebreaker.game.Player;
import edu.kit.informatik.firebreaker.game.board.Dimensions;
import edu.kit.informatik.firebreaker.game.board.Position;
import edu.kit.informatik.firebreaker.game.board.field.Field;
import edu.kit.informatik.firebreaker.game.board.field.FireStation;
import edu.kit.informatik.firebreaker.game.board.field.Forest;
import edu.kit.informatik.firebreaker.game.board.field.Pond;
import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.map.MappingStage;
import edu.kit.informatik.firebreaker.program.GameEnvironment;
import edu.kit.informatik.firebreaker.program.mapping.Mapping;
import edu.kit.informatik.util.Checks;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * A {@link MappingStage} parsing an input string to a {@link BoardParseResult} performing
 * only some fundamental validation.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class IntermediateBoardMapper implements MappingStage<Map<String, Player>, String, BoardParseResult> {

    @Override
    public BoardParseResult apply(Map<String, Player> playerMap, String input) throws MappingException {
        Matcher matcher = GameEnvironment.INPUT_PATTERN.matcher(input);
        // Match against the basic regex
        Checks.validate(matcher.matches(), () -> new MappingException("Malformed input"));
        // parse and validate dimensions
        int height = Mapping.INTEGER.apply(null, matcher.group("height"));
        int width = Mapping.INTEGER.apply(null, matcher.group("width"));
        Checks.validate(width >= 5 && height >= 5,
            () -> new MappingException("Dimensions must be at least 5x5"));
        Checks.validate(width % 2 == 1 && height % 2 == 1,
            () -> new MappingException("Dimensions must be odd"));
        Dimensions dimensions = Dimensions.of(height, width);

        // parse fields
        String rest = matcher.group("board");
        String[] fieldStrings = rest.split(",");
        int size = width * height;
        Checks.validate(fieldStrings.length == size,
            () -> new MappingException("Expected " + size + " fields"));
        Map<Position, Field> fields = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            int x = i / width;
            int y = i % width;
            Position position = Position.of(dimensions, x, y).orElseThrow();
            Field field = parseField(playerMap, position, fieldStrings[i]);
            fields.put(position, field);
        }
        return new BoardParseResult(dimensions, fields);
    }

    private Field parseField(Map<String, Player> playerMap, Position position, String s) {
        if (s.equals("L")) {
            return new Pond(position);
        } else if (GameEnvironment.PLAYER_NAMES.contains(s)) {
            return new FireStation(position, playerMap.get(s));
        } else {
            Matcher engineMatcher = GameEnvironment.FIRE_ENGINE_ID.matcher(s);
            if (engineMatcher.matches()) {
                Player owner = playerMap.get(engineMatcher.group("player"));
                FireEngine engine = owner.createFireEngine();
                Forest forest = new Forest(position, Forest.State.DRY);
                forest.addFireEngine(engine);
                return forest;
            } else {
                Forest.State state = Forest.State.getByIndicator(s.charAt(0)).orElseThrow();
                return new Forest(position, state);
            }
        }
    }
}
