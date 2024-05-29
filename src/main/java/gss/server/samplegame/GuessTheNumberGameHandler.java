package gss.server.samplegame;

import java.util.HashSet;
import java.util.Random;

import gss.GssLogger;
import gss.server.manager.hooks.*;
import gss.server.model.GameHandler;
import gss.server.model.GameSession;
import gss.server.model.GameState;

public class GuessTheNumberGameHandler extends GameHandler {

    // We will store the number to find in the private state using this key to access it
    private final String VAR_NUMBER_TO_FIND = "numberToFind";

    // We will use this set to test hook functionality
    private final HashSet<String> forbiddenUsernames = new HashSet<>();

    /**
     * This function is called when the game handler is created.
     * We will use this function to set up our test hooks.
     */
    @Override
    public void start() {

        GssLogger.info("GuessTheNumberGameHandler started");

        // add some forbidden usernames for testing hook functionality
        forbiddenUsernames.add("admin");
        forbiddenUsernames.add("root");
        forbiddenUsernames.add("moderator");

        // We add a sample auth hook to demonstrate how to use hooks
        // This hook will log the player id and password when a player logs in
        HookManager.get().addHook(AuthHook_loginUsingIdPassword.class, hook -> {
            AuthHook_loginUsingIdPassword hook_loginUsingIdPassword = (AuthHook_loginUsingIdPassword) hook;
            GssLogger.info("GuessTheNumberGameHandler hook called: " + hook_loginUsingIdPassword.playerId + " " + hook_loginUsingIdPassword.password);
        });

        // This hook will prevent players from using forbidden usernames
        HookManager.get().addHook(AccountHook_changeCredentials.class, hook -> {
            AccountHook_changeCredentials hook_changeCredentials = (AccountHook_changeCredentials) hook;

            if (forbiddenUsernames.contains(hook_changeCredentials.username)) {
                hook_changeCredentials.cancel("Username is forbidden");
            }

        });

    }

    /**
     * This function is called when a new game is created.
     */
    @Override
    public void onGameCreated(GameSession session) {

        // We will generate a random number between 1 and 100 and store it in the private state.
        // This number will be the number that the players will try to guess.

        GameState privateState = session.getPrivateState();
        privateState.setData(VAR_NUMBER_TO_FIND, new Random().nextInt(100) + 1);

    }

    /**
     * This function is called when a player makes a move.
     * jsonData is the data sent by the player.
     */
    @Override
    public void onPlayerMakeMove(GameSession session, String jsonData) {

        // Get the number to find from the private state
        GameState privateState = session.getPrivateState();
        int numberToFind = privateState.getInteger(VAR_NUMBER_TO_FIND);

        // Get the number the player guessed. We will use the Move class to deserialize the JSON data.
        // The Move class is defined in the same package as this class. You don't need to use a Move class, you can use any class you like or
        // just use plain JSON strings.
        Move move = Move.fromJson(jsonData);
        
        if (move.number == numberToFind) {

            // Player wins!

            // Update session data to reflect the winner and that the game is completed
            session.setWinner(session.getTurnOwner());
            session.setCompleted(true);

            // Update public state
            // Public state is shared with all players
            GameState publicState = session.getPublicState();
            publicState.setData("numberToFind", numberToFind);
            publicState.setData("winner", session.getWinner().getPlayerId());

        }
        else {

            // Update public state and continue the game.
            // This state will be sent to all players.

            GameState publicState = session.getPublicState();
            publicState.setData("target", move.number > numberToFind ? -1 : 1);

        }

    }

    /**
     * This function is called when a game is finished.
     * We don't need to do anything here, but we could do some cleanup if needed.
     */
    @Override
    public void onGameFinished(GameSession session) {
        // Called when a game is finished
    }

    /**
     * This function is called when a player issues an RPC call.
     * We can use this to handle custom RPC calls if needed.
     */
    @Override
    public void onRpcCall(String functionName, String jsonData) {

        // We can define custom RPC calls in the client and call them from the client.
        // We can use this to implement custom game logic or any other functionality we need.

        if (functionName.equals("testFunction")) {
            System.out.println("testFunction called with data: " + jsonData);
        }

    }
}
