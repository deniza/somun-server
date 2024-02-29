package gss.server.samplegame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import gss.GssLogger;
import gss.server.manager.ConnectionManager;
import gss.server.manager.hooks.*;
import gss.server.model.GameHandler;
import gss.server.model.GameSession;
import gss.server.model.GameState;
import gss.server.util.JsonHelper;

public class SampleGameHandler extends GameHandler {

    private final String VAR_NUMBER_TO_FIND = "numberToFind";

    private final HashSet<String> forbiddenUsernames = new HashSet<>();

    @Override
    public void start() {

        GssLogger.info("SampleGameHandler started");

        // add some forbidden usernames for testing
        forbiddenUsernames.add("admin");
        forbiddenUsernames.add("root");
        forbiddenUsernames.add("moderator");

        HookManager.get().addHook(AuthHook_loginUsingIdPassword.class, hook -> {
            AuthHook_loginUsingIdPassword hook_loginUsingIdPassword = (AuthHook_loginUsingIdPassword) hook;
            GssLogger.info("SampleGameHandler hook called: " + hook_loginUsingIdPassword.playerId + " " + hook_loginUsingIdPassword.password);
        });

        HookManager.get().addHook(AccountHook_changeCredentials.class, hook -> {
            AccountHook_changeCredentials hook_changeCredentials = (AccountHook_changeCredentials) hook;

            if (forbiddenUsernames.contains(hook_changeCredentials.username)) {
                hook_changeCredentials.cancel("Username is forbidden");
            }

        });

    }

    @Override
    public void onGameCreated(GameSession session) {

        GameState privateState = session.getPrivateState();
        privateState.setData(VAR_NUMBER_TO_FIND, new Random().nextInt(100) + 1);
        session.setPrivateState(privateState);

    }

    @Override
    public void onPlayerMakeMove(GameSession session, String jsonData) {

        GameState privateState = session.getPrivateState();
        int numberToFind = privateState.getInteger(VAR_NUMBER_TO_FIND);

        Move move = Move.fromJson(jsonData);
        
        if (move.number == numberToFind) {

            // player wins!

            session.setWinner(session.getTurnOwner());
            session.setCompleted(true);

            GameState publicState = session.getPublicState();
            publicState.setData("numberToFind", numberToFind);
            publicState.setData("winner", session.getWinner().getPlayerId());

        }
        else {

            // update public state and continue the game
            GameState publicState = session.getPublicState();
            publicState.setData("target", move.number > numberToFind ? -1 : 1);

        }

    }

    @Override
    public void onGameFinished(GameSession session) {
        super.onGameFinished(session);
    }

    @Override
    public void onRpcCall(String functionName, String jsonData) {

        if (functionName.equals("testFunction")) {
            System.out.println("testFunction called with data: " + jsonData);
        }

    }
}
