package edu.kit.informatik.parser;

/**
 * Thrown to indicate that something went wrong while parsing an array of arguments.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class ParsingException extends Exception {

    private String[] input;

    /**
     * Same as the default constructor of {@link Exception}.
     */
    public ParsingException() {
        super();
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param message See above.
     */
    public ParsingException(String message) {
        super(message);
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param message See above.
     * @param cause See above.
     */
    public ParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param cause See above.
     */
    public ParsingException(Throwable cause) {
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
    protected ParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Returns the original input that caused this exception.
     *
     * @return The array of strings originally passed to the parser.
     */
    public String[] getInput() {
        return input;
    }

    /**
     * Sets the original input that caused this exception.
     *
     * @param input The array of strings originally passed to the parser.
     */
    public void setInput(String[] input) {
        this.input = input;
    }
}
