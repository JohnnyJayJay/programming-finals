package edu.kit.informatik.util;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class to perform runtime checks in a program.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Checks {

    private Checks() {

    }

    /**
     * Validates the given condition by throwing the exception returned
     * by the supplier if its false.
     *
     * @param condition The {@code boolean} to validate.
     * @param exception A {@link Supplier} supplying the exception that should be
     *                  thrown if {@code condition} is {@code false}.
     * @param <X>       The type of the exception to be thrown.
     * @throws X if {@code condition} is {@code false}.
     */
    public static <X extends Throwable> void validate(boolean condition, Supplier<X> exception) throws X {
        if (!condition) {
            throw exception.get();
        }
    }

    /**
     * Attempts to perform the {@code computation} and return its result.
     * If an {@code IllegalArgumentException}
     * occurs, it is caught, wrapped using {@code wrapper} and finally thrown again in its new form.
     *
     * @param computation The computation that may fail with an {@link IllegalArgumentException}.
     * @param wrapper A function wrapping an {@code IllegalArgumentException} in a different exception.
     * @param <T> The return type of the computation.
     * @param <X> The type of the wrapped exception.
     * @return The result of the computation if it succeeds.
     * @throws X if the computation fails with an {@link IllegalArgumentException}.
     */
    public static <T, X extends Throwable> T wrapIllegalArgumentException(
            Supplier<T> computation,
            Function<? super IllegalArgumentException, X> wrapper
    ) throws X {
        try {
            return computation.get();
        } catch (IllegalArgumentException e) {
            throw wrapper.apply(e);
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} with the given message if the condition is {@code false}.
     *
     * @param condition The condition to check.
     * @param message The message of the possible exception.
     * @throws IllegalArgumentException if {@code condition} is {@code false}.
     */
    public static void argument(boolean condition, String message) {
        validate(condition, () -> new IllegalArgumentException(message));
    }

    /**
     * Same as {@link #argument(boolean, String)}, but with {@code null} as the message.
     *
     * @param condition The condition to check.
     * @throws IllegalArgumentException if {@code condition} is {@code false}.
     */
    public static void argument(boolean condition) {
        argument(condition, null);
    }

    /**
     * Checks if the given reference is {@code null} and throws a {@link NullPointerException} if applicable.
     *
     * @param reference The reference to check.
     * @param name The name of the reference to be used in the error message.
     * @param <T> The type of the reference.
     * @return The reference, if no exception is thrown.
     * @throws NullPointerException if the reference is {@code null}.
     */
    public static <T> T notNull(T reference, String name) {
        validate(reference != null, () -> new NullPointerException(name + " must not be null"));
        return reference;
    }

    /**
     * Same as {@link #notNull(Object, String)}, but without an error message.
     *
     * @param reference The reference to check.
     * @param <T> The type of the reference.
     * @return The reference, if no exception is thrown.
     * @throws NullPointerException if the reference is {@code null}.
     */
    public static <T> T notNull(T reference) {
        validate(reference != null, NullPointerException::new);
        return reference;
    }

}
