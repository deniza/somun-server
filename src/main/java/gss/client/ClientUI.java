package gss.client;

public class ClientUI {
    
    public enum UIState {notconnected, connected, login, ingame}

    private static ClientUI instance = new ClientUI();
    private UIState state = UIState.notconnected;
    private boolean isRunning = true;

    private UIListener listener;

    public static ClientUI get() {
        return instance;
    }

    public void start(UIListener listener) {

        this.listener = listener;

        Thread uithread = new Thread(new Runnable() {

            @Override
            public void run() {

                while (isRunning) {

                    display();

                }

            }
            
        });

        uithread.start();

    }    

    public synchronized void update(UIState state) {
        this.state = state;
    }

    private synchronized void display() {

        print("[ ** CLIENT MENU ** (" + state + ")]");

        int menuStatus = 0;

        if (state == UIState.notconnected) {
            menuStatus = displayNotConnected();
        }
        else if (state == UIState.connected) {
            menuStatus = displayConnected();
        }
        else if (state == UIState.login) {
            menuStatus = displayLogin();
        }
        else if (state == UIState.ingame) {
            menuStatus = displayInGame();            
        }

        if (menuStatus == 0) {
            print("# menu command error #");
        }

    }

    private int displayNotConnected() {
        
        print("1) Connect To Server");
        print("2) Exit App");

        int command = read("?> ");
        if (command == 1) {
            listener.onConnectServerUICommand();
        }
        if (command == 2) {
            executeExitApp();
        }
        else {
            return 0;
        }

        return 1;

    }
    private int displayConnected() {
    
        print("1) Create New Account");
        print("2) Login Existing Account");        
        print("3) Exit App");

        int command = read("?> ");
        if (command == 1) {
            listener.onCreateNewAccountUICommand();
        }
        else if (command == 2) {
            listener.onLoginUICommand();            
        }
        else if (command == 3) {
            executeExitApp();
        }
        else {
            return 0;
        }

        return 1;

    }

    private int displayLogin() {

        print("1) Create New Random Game");
        print("2) List Games");
        print("3) Enter Game");
        print("4) Exit App");

        int command = read("?> ");
        if (command == 1) {
            listener.onCreateRandomGameUICommand();
        }
        else
        if (command == 2) {
            listener.onListGamesUICommand();
        }
        else
        if (command == 3) {
            listener.onEnterGameUICommand();
        }
        else
        if (command == 4) {
            executeExitApp();            
        }
        else {
            return 0;
        }

        return 1;

    }

    private int displayInGame() {

        print("1) Make Move");
        print("2) Exit Game (to lobby)");
        print("3) Quit Game");
        print("4) Exit App");

        int command = read("?> ");
        if (command == 1) {
            
            int move = read("move?> ");
            listener.onMakeMoveUICommand(move);

        }
        if (command == 4) {
            executeExitApp();
        }
        else {
            return 0;
        }

        return 1;

    }

    private void executeExitApp() {

        isRunning = false;
        listener.onExitAppUICommand();

    }

    private void print(String message) {
        System.out.println(message);
    }

    private int read(String prompt) {
        try {
            return Integer.parseInt(ConsoleInputReader.readCommand(prompt));
        }
        catch (Exception e) {
            return -1;
        }        
    }

    public static interface UIListener {
        public void onExitAppUICommand();
        public void onConnectServerUICommand();
        public void onCreateNewAccountUICommand();
        public void onLoginUICommand();        
        public void onMakeMoveUICommand(int number);
        public void onEnterGameUICommand();
        public void onExitGameUICommand();
        public void onResignGameUICommand();
        public void onListGamesUICommand();
        public void onCreateRandomGameUICommand();
    }

}
