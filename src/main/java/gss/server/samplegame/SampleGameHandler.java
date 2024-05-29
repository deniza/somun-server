package gss.server.samplegame;

import java.util.HashSet;
import java.util.Random;

import gss.GssLogger;
import gss.server.manager.hooks.*;
import gss.server.model.GameHandler;
import gss.server.model.GameSession;
import gss.server.model.GameState;

public class SampleGameHandler extends GameHandler {

    private final String VAR_NUMBER_TO_FIND = "numberToFind";

    private final HashSet<String> forbiddenUsernames = new HashSet<>();

    @Override
    public void start() {

        GssLogger.info("SampleGameHandler started");

        // add some forbidden usernames for testing hook functionality
        forbiddenUsernames.add("admin");
        forbiddenUsernames.add("root");
        forbiddenUsernames.add("moderator");

        // We add a sample auth hook to demonstrate how to use hooks
        // This hook will log the player id and password when a player logs in
        HookManager.get().addHook(AuthHook_loginUsingIdPassword.class, hook -> {
            AuthHook_loginUsingIdPassword hook_loginUsingIdPassword = (AuthHook_loginUsingIdPassword) hook;
            GssLogger.info("SampleGameHandler hook called: " + hook_loginUsingIdPassword.playerId + " " + hook_loginUsingIdPassword.password);
        });

        // This hook will prevent players from using forbidden usernames
        HookManager.get().addHook(AccountHook_changeCredentials.class, hook -> {
            AccountHook_changeCredentials hook_changeCredentials = (AccountHook_changeCredentials) hook;

            if (forbiddenUsernames.contains(hook_changeCredentials.username)) {
                hook_changeCredentials.cancel("Username is forbidden");
            }

        });

    }

    @Override
    public void onGameCreated(GameSession session) {

        // This function is called once when a game is created.
        // We will generate a random number between 1 and 100 and store it in the private state.
        // This number will be the number that the players will try to guess.

        GameState privateState = session.getPrivateState();
        privateState.setData(VAR_NUMBER_TO_FIND, new Random().nextInt(100) + 1);

    }

    @Override
    public void onPlayerMakeMove(GameSession session, String jsonData) {

        // This function is called when a player makes a move.
        // We will check if the number the player guessed is the same as the number we generated in onGameCreated.

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

    @Override
    public void onGameFinished(GameSession session) {

        // This function is called when a game is finished.
        // We don't need to do anything here, but we could do some cleanup if needed.

        super.onGameFinished(session);

    }

    @Override
    public void onRpcCall(String functionName, String jsonData) {

        // This is the place we handle custom RPC calls.
        // We can define custom RPC calls in the client and call them from the client.
        // We can use this to implement custom game logic or any other functionality we need.

        if (functionName.equals("testFunction")) {
            System.out.println("testFunction called with data: " + jsonData);
        }

    }
}
