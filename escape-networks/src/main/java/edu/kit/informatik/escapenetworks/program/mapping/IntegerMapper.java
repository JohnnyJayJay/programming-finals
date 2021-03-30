package edu.kit.informatik.escapenetworks.program.mapping;

import edu.kit.informatik.map.MappingException;
import edu.kit.informatik.map.MappingStage;

/**
 * A {@link MappingStage} implementation that parses string-encoded escape networks
 * to integers or throws a {@link MappingException} if conversion fails.
 *
 * @param <E> The environment type. The environment is not used in the implementation
 *          of this stage, so it can be chosen freely.
 * @author JohnnyJayJay
 * @version 1.0.0
 * @see Integer#valueOf(int)
 */
public class IntegerMapper<E> implements MappingStage<E, String, Integer> {

    @Override
    public Integer apply(E environment, String input) throws MappingException {
        try {
            return Integer.valueOf(input);
        } catch (NumberFormatException e) {
            throw new MappingException(input + " is not a number", e);
        }
    }

}
