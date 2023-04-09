package gss.server.event;

import gss.server.model.GameSession;

public abstract class GameEventHandler {
    
    public void onGameCreated(GameSession session) {
    }
    
    public void onGameFinished(GameSession session) {
    }

    public void onPlayerMakeMove(GameSession session, String jsonData) {
    }

}
