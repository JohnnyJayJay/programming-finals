package edu.kit.informatik.map;

/**
 * Thrown to indicate that an (expected) exception happened during the application of a {@link MappingStage}.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class MappingException extends Exception {

    /**
     * Same as the equivalent constructor of {@link Exception}.
     */
    public MappingException() {
        super();
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param message See above.
     */
    public MappingException(String message) {
        super(message);
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param message See above.
     * @param cause See above.
     */
    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param cause See above.
     */
    public MappingException(Throwable cause) {
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
    protected MappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
