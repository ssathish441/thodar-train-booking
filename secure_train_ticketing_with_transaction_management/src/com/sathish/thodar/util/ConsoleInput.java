package com.sathish.thodar.util;

import java.util.Scanner;

public class ConsoleInput {
    private static final Scanner scanner = new Scanner(System.in);

    // Gets a normal string
    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    // Safely gets an Integer (for menus, passenger counts)
    public static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid input! Please enter a valid number.");
            }
        }
    }

    // Safely gets a Long (for IDs, Train Numbers)
    public static Long getLong(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid input! Please enter a valid numeric ID.");
            }
        }
    }
    
    // Safely gets a Double (for Ticket Fare, Amounts)
    public static Double getDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Invalid input! Please enter a valid decimal amount.");
            }
        }
    }
}