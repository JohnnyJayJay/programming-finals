package edu.kit.informatik.map;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * An abstract, composable, exception-aware function. In short:
 * A function (E, T) -> R, where T is the input, R is the output, and E is a
 * fixed environment type that is preserved through composition.
 * <p>
 * The environment type constitutes the first major difference to {@link BiFunction}:<br>
 * Composing two {@code MappingStage}s f : (E, T) -> R and g : (E, R) -> U
 * results in a {@code MappingStage} h : (E, T) -> U that first applies f, then g.
 * The environment type stays the same. This enables you to "remember" context across different stages.
 * <p>
 * The second difference is that {@code MappingStage} explicitly allows failure in the form of
 * {@link MappingException}. It is therefore more suitable to transform possibly erroneous
 * user input than {@link BiFunction}.
 *
 * @param <E> The environment type.
 * @param <T> The input type.
 * @param <R> The return type.
 * @author JohnnyJayJay
 * @version 1.0.0
 */
@FunctionalInterface
public interface MappingStage<E, T, R> {

    /**
     * Returns the identity stage.
     * <p>
     * The identity stage just returns its input, independent of the environment.
     *
     * @param <E> The environment type.
     * @param <T> The input (and output) type.
     * @return A mapping stage representing the identity function.
     */
    static <E, T> MappingStage<E, T, T> identity() {
        return (env, arg) -> arg;
    }

    /**
     * Composes this mapping stage with a mapping stage that performs a check and
     * throws a {@link MappingException} with the given message on failure.<br>
     * I.e., the resulting mapping stage will first apply this stage and then
     * validate the given predicate.
     *
     * @param predicate The predicate to check.
     * @param errorMessage A function generating an error message from the
     *                     object that couldn't be validated.
     * @return a new mapping stage that is the composition of this and the validation stage.
     * @see #thenValidate(MappingConstraint)
     */
    default MappingStage<E, T, R> thenValidate(BiPredicate<E, R> predicate, Function<R, String> errorMessage) {
        return thenValidate(MappingConstraint.of(predicate, errorMessage));
    }

    /**
     * Composes this mapping stage with a mapping stage validates the given constraint.<br>
     * I.e., the resulting mapping stage will first apply this stage and then
     * validate the constraint.
     *
     * @param constraint The constraint to validate.
     * @return a new mapping stage that is the composition of this and the validation stage.
     * @see #thenValidate(BiPredicate, Function)
     */
    default MappingStage<E, T, R> thenValidate(MappingConstraint<E, R> constraint) {
        return (env, arg) -> {
            R result = apply(env, arg);
            constraint.validate(env, result);
            return result;
        };
    }

    /**
     * Composes this mapping stage with another mapping stage.<br>
     * I.e., the resulting mapping stage will first apply this stage and then the given stage.
     *
     * @param stage The stage to apply after this stage.
     * @param <U> The return type of the new stage.
     * @return The composition of this and the given stage.
     */
    default <U> MappingStage<E, T, U> thenApply(MappingStage<E, R, U> stage) {
        return (env, arg) -> stage.apply(env, apply(env, arg));
    }

    /**
     * Applies this stage to an environment and an input.
     *
     * @param environment The environment/context value to use in the computation.
     * @param input The input that should be mapped.
     * @return The result of this stage.
     * @throws MappingException If something goes wrong while mapping.
     */
    R apply(E environment, T input) throws MappingException;

}
