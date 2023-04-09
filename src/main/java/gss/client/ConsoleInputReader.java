package gss.client;

import java.util.Scanner;

public final class ConsoleInputReader {
    
    private static Scanner scanner;
    
    private ConsoleInputReader() {
        throw new AssertionError("This class should not be instantiated.");
    }
    
    public static String readCommand(String prompt) {
        
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        
        System.out.print(prompt);
        String command = scanner.nextLine();        
        
        return command;
    }
}
