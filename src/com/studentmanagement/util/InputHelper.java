package com.studentmanagement.util;
import java.util.Scanner;

/**
 * Utility class: safe keyboard input. Keeps asking until the user types a valid value,
 * so the program never crashes on bad input like "abc" instead of a number.
 */
public class InputHelper {

    private final Scanner scanner = new Scanner(System.in);

    public String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readString(prompt));
            } catch (NumberFormatException e) {
                System.out.println("  ! Please enter a valid whole number.");
            }
        }
    }

    public double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readString(prompt));
            } catch (NumberFormatException e) {
                System.out.println("  ! Please enter a valid number.");
            }
        }
    }
    /** Shows the old value in brackets. If the user just presses Enter, returns the old value. */
    public String readStringOrDefault(String prompt, String oldValue) {
        String text = readString(prompt + " [" + oldValue + "]: ");
        return text.isEmpty() ? oldValue : text;
    }

    /** Same idea for numbers. Keeps asking if the user types something that isn't a number. */
    public double readDoubleOrDefault(String prompt, double oldValue) {
        while (true) {
            String text = readString(prompt + " [" + oldValue + "]: ");
            if (text.isEmpty()) {
                return oldValue;
            }
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                System.out.println("  ! Please enter a valid number, or press Enter to keep the old value.");
            }
        }
    }

}
