package gss.client;

import java.util.concurrent.LinkedBlockingQueue;

public final class ConsoleInputReader {
    
    private static ConsoleInputReader instance = new ConsoleInputReader();
    //private static Scanner scanner;
    
    private LinkedBlockingQueue<String> commandQueue = new LinkedBlockingQueue<>();

    private ConsoleInputReader() {
        //throw new AssertionError("This class should not be instantiated.");

        new Thread() {
            public void run() {
                String lastCommand = "";
                while (true) {
                    try {
                        while (System.in.available() > 0) {
                            int readByte = System.in.read();                            
                            if (readByte == 10) {
                                commandQueue.put(lastCommand);
                                lastCommand = "";
                            }
                            else {
                                lastCommand += (char) readByte;
                            }
                        }
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    public static ConsoleInputReader get() {
        return instance;
    }

    public String readCommand() {
        try {
            return commandQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }
    
    /*
    public static String readCommand(String prompt) {
        
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        
        System.out.print(prompt);
        String command = scanner.nextLine();        
        
        return command;
    }
    */
}
