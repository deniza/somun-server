package gss.client;

public class ClientUI {
    
    public enum UIState {notconnected, connected, loginerr, login, ingame, makingmove, rpccall, createnewaccount}

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
                listener.onCreateNewGuestAccountUICommand();
            }
            else if (command.equals("2")) {
                update(UIState.createnewaccount);
            }
            else if (command.equals("3")) {
                listener.onLoginUICommand();
            }
            else if (command.equals("4")) {
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
                update(UIState.rpccall);
            }
            else if (command.equals("5")) {
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
        else if (state == UIState.rpccall) {
            String[] parts = command.split(" ");
            String functionName = parts[0];
            String jsonArgs = parts[1];
            listener.onRpcCallUICommand(functionName, jsonArgs);

            update(UIState.login);
        }
        else if (state == UIState.createnewaccount) {
            String[] parts = command.split(" ");
            String username = parts[0];
            String password = parts[1];
            listener.onCreateNewAccountUICommand(username, password);
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

        String header = "[ ** CLIENT MENU ** (" + state + ")]";
        
        if (state == UIState.login) {
            header += " [PlayerId: " + SimpleClient.playerId + ", Name: " + SimpleClient.playerName + "]";
        }

        print(header);

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
        else if (state == UIState.rpccall) {
            displayRpcCall();
        }
        else if (state == UIState.createnewaccount) {
            displayCreateNewAccount();
        }

    }

    private void displayNotConnected() {
        
        print("1) Connect To Server");
        print("2) Exit App");

    }
    private void displayConnected() {
    
        print("1) Create New Guest Account");
        print("2) Create New Account");
        print("3) Login Existing Account");
        print("4) Exit App");

    }

    private void displayLogin() {

        print("1) Create New Random Game");
        print("2) List Games");
        print("3) Enter Game " + SimpleClient.currentGameId);
        print("4) Rpc Call");
        print("5) Exit App");

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

    private void displayRpcCall() {

        print("> Enter RPC function name and arguments: ");

    }

    private void displayCreateNewAccount() {

        print("> Enter username and password: ");

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
        void onCreateNewGuestAccountUICommand();
        void onCreateNewAccountUICommand(String username, String password);
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

        void onRpcCallUICommand(String functionName, String jsonArgs);

    }

}
