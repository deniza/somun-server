package gss.server.model;

public abstract class GameHandler {
    
    public void start() {
    }

    public void updateState(int gameId, GameState state) {
        //gameStates.put(gameId, state);
        //state.updated(true);
    }

    public void onGameCreated(GameSession session) {
    }

    public void onGameFinished(GameSession session) {
    }

    public void onPlayerMakeMove(GameSession session, String jsonData) {
    }

}
