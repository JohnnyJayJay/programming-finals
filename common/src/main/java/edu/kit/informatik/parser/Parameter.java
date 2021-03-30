package edu.kit.informatik.parser;

import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.map.MappingStage;

class Parameter<E, T> {

    private final String name;
    private final MappingStage<E, String, T> mapper;

    /**
     * Creates a parameter spec. Internal use only.
     *
     * @param name The name of the parameter.
     * @param mapper The mapping stage used for the parameter.
     */
    Parameter(String name, MappingStage<E, String, T> mapper) {
        this.name = name;
        this.mapper = mapper;
    }

    /**
     * Parses a String input given an environment.
     * Internal use only.
     *
     * @param environment The environment of the command.
     * @param input The input to parse.
     * @return The result of the mapping stage.
     * @throws ArgumentException If parsing fails.
     */
    T parse(E environment, String input) throws ArgumentException {
        try {
            return mapper.apply(environment, input);
        } catch (MappingException e) {
            ArgumentException exception = new ArgumentException(e.getMessage(), e);
            exception.setParameterName(name);
            exception.setRawArgument(input);
            throw exception;
        }
    }

    /**
     * Returns the name of the parameter. Internal use only.
     *
     * @return The parameter name.
     */
    String getName() {
        return name;
    }
}
