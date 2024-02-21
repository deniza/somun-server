package gss.server.samplegame;

import java.util.HashMap;
import java.util.Random;

import gss.server.manager.ConnectionManager;
import gss.server.model.GameHandler;
import gss.server.model.GameSession;
import gss.server.model.GameState;
import gss.server.util.JsonHelper;

public class SampleGameHandler extends GameHandler {

    private final String VAR_NUMBER_TO_FIND = "numberToFind";

    @Override
    public void start() {
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

            setGameFinished();
            session.setWinner(session.getTurnOwner());

            HashMap<String, Object> stateMap = new HashMap<>();
            stateMap.put("numberToFind", numberToFind);
            stateMap.put("winner", session.getTurnOwner().getPlayerId());

            ConnectionManager.get().call(session.getPlayers(), "Play", "gameStateUpdated", session.getGameId(), JsonHelper.hashmapToJson(stateMap));

        }
        else {

            // keep continue the game

            HashMap<String, Object> stateMap = new HashMap<>();
            if (move.number > numberToFind) {
                stateMap.put("target", "smaller");
            }
            else {
                stateMap.put("target", "bigger");
            }

            ConnectionManager.get().call(session.getPlayers(), "Play", "gameStateUpdated", session.getGameId(), JsonHelper.hashmapToJson(stateMap));
            ConnectionManager.get().call(session.getPlayers(), "Play", "turnOwnerChanged", session.getGameId(), session.getTurnOwner().getPlayerId());

        }

    }

    @Override
    public void onGameFinished(GameSession session) {
        super.onGameFinished(session);
    }

    @Override
    public void onRpcCall(String functionName, String jsonData) {

        super.onRpcCall(functionName, jsonData);

        if (functionName.equals("testFunction")) {
            System.out.println("testFunction called with data: " + jsonData);
        }

    }
}
