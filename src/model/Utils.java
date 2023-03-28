package model;

import java.util.Scanner;

public class Utils {
    private static Scanner scanner = new Scanner(System.in);

    public static String getUserInput() {
        String input = scanner.nextLine();
        return input;
    }
}
