package edu.kit.informatik.parser;

import edu.kit.informatik.map.MappingStage;
import edu.kit.informatik.util.Checks;

import java.util.ArrayList;
import java.util.List;

/**
 * The class used to build {@link ArgumentParser}s.
 *
 * @param <E> the environment type of the parser.
 * @author JohnnyJayJay
 * @version 1.0.0
 * @see ArgumentParser
 */
public final class ArgumentParserBuilder<E> {

    private final List<Parameter<E, ?>> parameters = new ArrayList<>();

    /**
     * Adds a parameter specification to the parser.
     *
     * @param name The name of this parameter for use in {@link Arguments}.
     * @param mapper A MappingStage mapping the environment and a String to the type of this parameter.
     * @param <T> The type of the parameter that arguments should be parsed to.
     * @return this builder instance, for chaining.
     */
    public <T> ArgumentParserBuilder<E> param(String name, MappingStage<E, String, T> mapper) {
        Checks.notNull(name);
        Checks.notNull(mapper);
        parameters.add(new Parameter<>(name, mapper));
        return this;
    }

    /**
     * Creates a {@link ArgumentParser} instance based on the settings made in this builder.
     *
     * @return a new {@code ArgumentParser}.
     */
    public ArgumentParser<E> build() {
        return new ArgumentParser<>(parameters);
    }

}
