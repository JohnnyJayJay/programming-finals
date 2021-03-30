package edu.kit.informatik.cli;

/**
 * Thrown to indicate that a semantic error occurred in a command,
 * i.e., the arguments could be parsed, but not interpreted logically.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class SemanticException extends Exception {

    /**
     * Same as the equivalent constructor of {@link Exception}.
     */
    public SemanticException() {
        super();
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param message See above.
     */
    public SemanticException(String message) {
        super(message);
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param message See above.
     * @param cause See above.
     */
    public SemanticException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param cause See above.
     */
    public SemanticException(Throwable cause) {
        super(cause);
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param message See above.
     * @param cause See above.
     * @param enableSuppression See above.
     * @param writableStackTrace See above.
     */
    protected SemanticException(String message,
                                Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
