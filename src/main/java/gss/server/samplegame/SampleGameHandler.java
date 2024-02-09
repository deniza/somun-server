package gss.server.samplegame;

import java.util.Random;

import gss.server.model.GameHandler;
import gss.server.model.GameSession;
import gss.server.model.GameState;

public class SampleGameHandler extends GameHandler {

    private final String VAR_NUMBER_TO_FIND = "numberToFind";

    @Override
    public void start() {
    }

    @Override
    public void onGameCreated(GameSession session) {

        GameState state = new GameState();

        state.setData(VAR_NUMBER_TO_FIND, new Random().nextInt(100) + 1);
        session.setState(state);

    }

    @Override
    public void onPlayerMakeMove(GameSession session, String jsonData) {

        GameState state = session.getState();
        int numberToFind = (Integer) state.getData(VAR_NUMBER_TO_FIND);

        Move move = Move.fromJson(jsonData);
        
        if (move.number == numberToFind) {

            // player wins!

        }
        else {

            // keep continue the game

        }

    }

    @Override
    public void onGameFinished(GameSession session) {
        super.onGameFinished(session);
    }

}
