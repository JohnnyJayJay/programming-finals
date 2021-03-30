package edu.kit.informatik.parser;

/**
 * Thrown to indicate that something went wrong parsing one of the arguments.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public class ArgumentException extends ParsingException {

    private int argumentPosition;
    private String rawArgument;
    private String parameterName;

    /**
     * Same as the equivalent constructor of {@link Exception}.
     */
    public ArgumentException() {
        super();
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param message See above.
     */
    public ArgumentException(String message) {
        super(message);
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param message See above
     * @param cause See above.
     */
    public ArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Same as the equivalent constructor of {@link Exception}.
     *
     * @param cause See above.
     */
    public ArgumentException(Throwable cause) {
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
    protected ArgumentException(String message,
                                Throwable cause,
                                boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    /**
     * Sets the position where the erroneous argument was found in the input.
     *
     * @param argumentPosition The index in the arguments array that contains the erroneous argument.
     */
    public void setArgumentPosition(int argumentPosition) {
        this.argumentPosition = argumentPosition;
    }

    /**
     * Sets the raw, unparsed argument that caused this exception.
     *
     * @param rawArgument The argument, as a string.
     */
    public void setRawArgument(String rawArgument) {
        this.rawArgument = rawArgument;
    }

    /**
     * Returns the position where the erroneous argument was found in the input.
     *
     * @return The index in the arguments array that contains the erroneous argument.
     */
    public int getArgumentPosition() {
        return argumentPosition;
    }

    /**
     * Returns the raw argument that caused this exception.
     *
     * @return The argument, as a string.
     */
    public String getRawArgument() {
        return rawArgument;
    }

    /**
     * Returns the name of the parameter the erroneous argument was supposed to be parsed for.
     *
     * @return The name of the parameter.
     */
    public String getParameterName() {
        return parameterName;
    }

    /**
     * Sets the name of the parameter the erroneous argument was supposed to be parsed for.
     *
     * @param parameterName The name of the parameter.
     */
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Override
    public String getMessage() {
        return "For parameter '" + parameterName + "' at position " + argumentPosition
                + ": Couldn't process '" + rawArgument + "': " + super.getMessage();
    }
}
