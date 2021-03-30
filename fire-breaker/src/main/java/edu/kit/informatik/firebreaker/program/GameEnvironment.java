package edu.kit.informatik.firebreaker.program;

import edu.kit.informatik.firebreaker.game.board.Board;
import edu.kit.informatik.firebreaker.game.Game;
import edu.kit.informatik.firebreaker.game.Player;
import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.map.MappingStage;
import edu.kit.informatik.firebreaker.program.args.IntermediateBoardMapper;
import edu.kit.informatik.firebreaker.program.args.ValidFireEnginesPredicate;
import edu.kit.informatik.firebreaker.program.args.ValidFireStationsPredicate;
import edu.kit.informatik.firebreaker.program.args.ValidForestStatePredicate;
import edu.kit.informatik.firebreaker.program.args.ValidPondsPredicate;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A class encapsulating the current state of the program.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class GameEnvironment {

    /**
     * A regex matching fire engine ids.
     */
    public static final Pattern FIRE_ENGINE_ID
            = Pattern.compile("(?<player>[A-D])(?<number>(0|[1-9]\\d*))");

    /**
     * A regex providing rough validation and matching for initial board inputs.
     */
    public static final Pattern INPUT_PATTERN
            = Pattern.compile("(?<height>\\d+),(?<width>\\d+),(?<board>(([A-D]0|[A-DLwd+*]),)*([A-D]0|[A-DLwd+*]))");

    /**
     * The amount of players supported by this program.
     */
    public static final int PLAYER_COUNT = 4;

    /**
     * The list of player (names) supported by this program.
     */
    public static final List<String> PLAYER_NAMES = List.of("A", "B", "C", "D");

    private static final MappingStage<Map<String, Player>, String, Board> MAPPER
            = new IntermediateBoardMapper()
            .thenValidate(new ValidFireStationsPredicate(), (r) -> "Misplaced fire stations")
            .thenValidate(new ValidPondsPredicate(), (r) -> "Misplaced ponds")
            .thenValidate(new ValidFireEnginesPredicate(), (r) -> "Misplaced fire engines")
            .thenValidate(new ValidForestStatePredicate(), (r) -> "Invalid forest state")
            .thenApply((players, result) -> new Board(result.getDimensions(), result.getFields()));

    private final String boardInput;
    private Game activeGame;

    /**
     * Creates a new, uninitialised environment with the given board input.
     *
     * @param boardInput The string representation of the initial state of the game board.
     */
    public GameEnvironment(String boardInput) {
        this.boardInput = boardInput;
    }

    /**
     * Returns the currently active game in this environment.
     *
     * @return The active game.
     */
    public Game getActiveGame() {
        return activeGame;
    }

    /**
     * Resets the active game by (re-)initialising the board using the stored board input.
     *
     * @throws MappingException If the board input cannot be parsed and/or validated.
     */
    public void reset() throws MappingException {
        List<Player> players = PLAYER_NAMES.stream().map(Player::new).collect(Collectors.toList());
        Map<String, Player> playerMap = players.stream().collect(Collectors.toMap(Player::getName, (p) -> p));
        Board board = MAPPER.apply(playerMap, boardInput);
        this.activeGame = new Game(players, board);
    }
}
