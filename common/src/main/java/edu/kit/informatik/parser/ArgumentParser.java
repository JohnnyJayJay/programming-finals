package edu.kit.informatik.parser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * An {@code ArgumentParser} can parse an array of {@code String}s (the input arguments)
 * into a map of generic objects ({@link Arguments}) according to a list of {@link Parameter parameter specifications}.
 * <p>
 * This class is immutable.
 *
 * @param <E> The environment type used in parsing.
 * @author JohnnyJayJay
 * @version 1.0.0
 * @see ArgumentParserBuilder
 */
public final class ArgumentParser<E> {

    private final List<Parameter<E, ?>> parameters;

    /**
     * The constructor for internal use.
     *
     * @param parameters the parameter specs for this parser.
     * @see ArgumentParserBuilder
     */
    ArgumentParser(List<Parameter<E, ?>> parameters) {
        this.parameters = parameters;
    }

    /**
     * Returns a parser that accepts no arguments, or, in other words, only empty string arrays.
     * <p>
     * It contains no parameter specifications and is therefore only useful to validate
     * that no arguments are given.
     *
     * @param <E> The environment type of the parser.
     * @return The empty parser.
     */
    public static <E> ArgumentParser<E> noArgs() {
        return ArgumentParser.<E>builder().build();
    }

    /**
     * Given an instance of the environment type and the string arguments to parse,
     * parses the arguments sequentially according to the parameter specification and
     * returns an {@link Arguments} instance as a result.
     *
     * @param environment The environment/context.
     * @param args        The arguments to parse.
     * @return A result {@link Arguments} instance containing the parsed arguments.
     * @throws ParsingException If the arguments cannot be parsed using the specification of this parser.
     */
    public Arguments parse(E environment, String[] args) throws ParsingException {
        int actualLength = args.length;
        int expectedLength = parameters.size();
        if (actualLength != expectedLength) {
            ParsingException exception = new ParsingException("Expected " + expectedLength
                    + " arguments, got: " + actualLength);
            exception.setInput(args);
            throw exception;
        } else {
            Map<String, Object> result = new LinkedHashMap<>(actualLength);
            for (int i = 0; i < expectedLength; i++) {
                Parameter<E, ?> paramSpec = parameters.get(i);
                try {
                    result.put(paramSpec.getName(), paramSpec.parse(environment, args[i]));
                } catch (ArgumentException e) {
                    e.setInput(args);
                    e.setArgumentPosition(i + 1);
                    throw e;
                }
            }
            return Arguments.create(result);
        }
    }

    /**
     * Creates and returns an {@link ArgumentParserBuilder} instance used to construct parsers.
     *
     * @param <E> The environment type of the resulting parser.
     * @return A new builder.
     */
    public static <E> ArgumentParserBuilder<E> builder() {
        return new ArgumentParserBuilder<>();
    }
}
