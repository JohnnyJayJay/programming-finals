package edu.kit.informatik.parser;

import edu.kit.informatik.util.Checks;

import java.util.Map;

/**
 * Arguments parsed by an {@link ArgumentParser}.
 * <p>
 * This class is immutable.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Arguments {

    private final Map<String, Object> arguments;

    private Arguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    /**
     * Creates a new arguments instance based on the given map of parsed arguments.
     * <p>
     * The keys of the map are the names of the parameters the arguments stand in for,
     * the values are the corresponding arguments parsed to an object representation.
     *
     * @param arguments the arguments map.
     * @return a new arguments instance.
     * @throws NullPointerException if {@code arguments} is {@code null}.
     */
    public static Arguments create(Map<String, Object> arguments) {
        return new Arguments(Map.copyOf(arguments));
    }

    /**
     * Returns the object associated with the given name, or {@code null} if it doesn't exist.
     * <p>
     * Automatically performs a cast to the type determined by the caller.
     * It is expected that the caller knows and uses the correct type since they set it through their
     * {@link ArgumentParser} definition.
     *
     * @param name The name to get the argument for.
     * @param type The (parent) {@code Class} the argument was parsed to.
     * @param <T>  The type of the parsed argument.
     * @return an Optional containing the parsed argument or {@code null} if no argument exists for the given name.
     * @throws ClassCastException if {@code type} is not assignable from the type of the argument.
     */
    public <T> T get(String name, Class<T> type) {
        Object value = arguments.get(name);
        Checks.validate(type.isInstance(value),
            () -> new ClassCastException(value.getClass() + " cannot be cast to " + type));
        return type.cast(value);
    }

}
