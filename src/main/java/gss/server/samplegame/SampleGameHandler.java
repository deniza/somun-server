package gss.server.samplegame;

import java.util.Random;

import gss.server.event.GameEventListener;
import gss.server.model.GameHandler;
import gss.server.model.GameSession;

public class SampleGameHandler extends GameHandler {
    
    @Override
    public void start() {

        GameEventListener.get().addHandler(this);

    }

    @Override
    public void onGameCreated(GameSession s) {

        SampleGameState state = new SampleGameState();
        state.numberToFind = new Random().nextInt(100) + 1;

        s.setState(state);

    }


    @Override
    public void onPlayerMakeMove(GameSession s, String jsonData) {

        SampleGameSession session = (SampleGameSession) s;
        SampleGameState state = (SampleGameState) session.getState();
        
        Move move = Move.fromJson(jsonData);
        
        if (move.number == state.numberToFind) {

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
