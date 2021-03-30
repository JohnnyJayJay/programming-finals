package edu.kit.informatik.map;

import edu.kit.informatik.util.Checks;

import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * A constraint that can be validated as part of a {@link MappingStage} to ensure
 * that a certain condition is fulfilled.
 *
 * @param <E> The environment type.
 * @param <T> The type of the objects to validate.
 * @author JohnnyJayJay
 * @version 1.0.0
 * @see MappingStage#thenValidate(MappingConstraint)
 */
public final class MappingConstraint<E, T> {

    private final BiPredicate<E, T> predicate;
    private final Function<T, String> errorMessage;

    private MappingConstraint(BiPredicate<E, T> predicate, Function<T, String> errorMessage) {
        this.predicate = predicate;
        this.errorMessage = errorMessage;
    }

    /**
     * Creates a constraint using the given predicate and error message function.
     *
     * @param predicate    A predicate testing an input object (T) in an environment (E).
     * @param errorMessage A function mapping from an object that failed
     *                     the predicate to an error message for a
     *                     {@link MappingException}.
     * @param <E>          The environment type.
     * @param <T>          The input type.
     * @return A new {@code MappingConstraint}.
     */
    public static <E, T> MappingConstraint<E, T> of(BiPredicate<E, T> predicate,
                                                    Function<T, String> errorMessage) {
        return new MappingConstraint<>(predicate, errorMessage);
    }

    /**
     * Returns an error message generated for an object that failed validation.
     *
     * @param subject The subject that failed the test.
     * @return An error message generated using the error message generator function of this constraint.
     */
    public String getErrorMessage(T subject) {
        return errorMessage.apply(subject);
    }

    /**
     * Validates a subject in an environment against this constraint.
     *
     * @param environment The environment to test in.
     * @param subject     The subject to test.
     * @throws MappingException if validation fails.
     */
    public void validate(E environment, T subject) throws MappingException {
        Checks.validate(predicate.test(environment, subject),
            () -> new MappingException(errorMessage.apply(subject)));
    }
}
