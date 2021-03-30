package edu.kit.informatik;

import java.util.Scanner;

/**
 * An adaptation of the Terminal class used in the KIT programming lecture.
 *
 * @author JohnnyJayJay
 * @version 1.0.0
 */
public final class Terminal {

    private static final Scanner SCANNER = new Scanner(System.in);

    private Terminal() {

    }

    /**
     * Reads the next line from {@code System.in}, blocking if necessary.
     *
     * @return The next line, as a String.
     */
    public static String readLine() {
        return SCANNER.nextLine();
    }

    /**
     * Prints the given object to {@code System.out}.
     *
     * @param object The object to output to the console.
     */
    public static void printLine(Object object) {
        System.out.println(object);
    }

    /**
     * Prints an error message to {@code System.out}.
     * <p>
     * The error message will simply be the message, prefixed with {@code "Error, "}.
     *
     * @param message The message to report.
     */
    public static void printError(String message) {
        System.out.println("Error, " + message);
    }

}
