package gss.client;

public class ClientUI {
    
    public enum UIState {notconnected, connected, loginerr, login, ingame, makingmove}

    private static final ClientUI instance = new ClientUI();
    private UIState state = UIState.notconnected;
    private boolean isRunning = true;

    private UIListener listener;

    public static ClientUI get() {
        return instance;
    }

    public void start(UIListener listener) {

        this.listener = listener;
        display();

        new Thread(() -> {

            while (isRunning) {

                String command = ConsoleInputReader.get().readCommand();
                if (command.isEmpty() == false) {
                    evaluateCommand(command);                    
                }                

            }

        }).start();        

    }    

    private void evaluateCommand(String command) {

        if (state == UIState.notconnected) {
            if (command.equals("1")) {
                listener.onConnectServerUICommand();
            }
            else if (command.equals("2")) {
                executeExitApp();
            }
        }
        else if (state == UIState.connected) {
            if (command.equals("1")) {
                listener.onCreateNewAccountUICommand();
            }
            else if (command.equals("2")) {
                listener.onLoginUICommand();
            }
            else if (command.equals("3")) {
                executeExitApp();
            }
        }
        else if (state == UIState.login) {
            if (command.equals("1")) {
                listener.onCreateRandomGameUICommand();
            }
            else if (command.equals("2")) {
                listener.onListGamesUICommand();
            }
            else if (command.equals("3")) {
                listener.onEnterGameUICommand();
            }
            else if (command.equals("4")) {
                executeExitApp();
            }
        }
        else if (state == UIState.ingame) {
            if (command.equals("1")) {
                if (SimpleClient.isTurnOwner == false) {
                    System.out.println("Not your turn");
                    update();
                }
                else {
                    update(UIState.makingmove);
                }                
            }
            else if (command.equals("2")) {
                listener.onExitGameUICommand();
            }
            else if (command.equals("3")) {
                listener.onResignGameUICommand();
            }
            else if (command.equals("4")) {
                executeExitApp();
            }
        }
        else if (state == UIState.makingmove) {
            listener.onMakeMoveUICommand(Integer.parseInt(command));
        }

    }

    public synchronized void update() {
        display();
    }

    public synchronized void update(UIState state) {
        this.state = state;
        display();
    }

    private synchronized void display() {

        print("[ ** CLIENT MENU ** (" + state + ")]");

        if (state == UIState.notconnected) {
            displayNotConnected();
        }
        else if (state == UIState.connected) {
            displayConnected();
        }
        else if (state == UIState.login) {
            displayLogin();
        }
        else if (state == UIState.loginerr) {
            displayConnected();
        }
        else if (state == UIState.ingame) {
            displayInGame();            
        }
        else if (state == UIState.makingmove) {
            displayMakingMove();
        }

    }

    private void displayNotConnected() {
        
        print("1) Connect To Server");
        print("2) Exit App");

    }
    private void displayConnected() {
    
        print("1) Create New Account");
        print("2) Login Existing Account");        
        print("3) Exit App");

    }

    private void displayLogin() {

        print("1) Create New Random Game");
        print("2) List Games");
        print("3) Enter Game " + SimpleClient.currentGameId);
        print("4) Exit App");

    }

    private void displayInGame() {

        print("1) Make Move");
        print("2) Exit Game (to lobby)");
        print("3) Quit Game");
        print("4) Exit App");

    }

    private void displayMakingMove() {

        print("> Guess the number: ");

    }

    private void executeExitApp() {

        isRunning = false;
        listener.onExitAppUICommand();

    }

    private void print(String message) {
        System.out.println(message);
    }

    public interface UIListener {
        
        void onExitAppUICommand();
        void onConnectServerUICommand();
        void onCreateNewAccountUICommand();
        void onLoginUICommand();
        
        void onMakeMoveUICommand(int number);
        void onEnterGameUICommand();
        void onExitGameUICommand();
        void onResignGameUICommand();
        void onListGamesUICommand();
        void onCreateRandomGameUICommand();
        
        void onCreateInvitationUICommand(int invitee, int gametype, boolean shouldStartOnline);
        void onListInvitationsUICommand();
        void onAcceptInvitationUICommand(int invitationId);
        
        void onRequestFriendsUICommand();
        void onAddFriendUICommand(int playerId);
        void onAcceptFriendUICommand(int playerId);
        void onRejectFriendUICommand(int playerId);
        void onRemoveFriendUICommand(int playerId);
        void onRequestPrivateMessagesListUICommand();
        void onSendPrivateMessageUICommand(int playerId, String message);
        void onReadPrivateMessageUICommand(int messageId);
        void onDeletePrivateMessageUICommand(int messageId);

    }

}
