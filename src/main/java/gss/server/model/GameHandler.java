package gss.server.model;

public abstract class GameHandler {

    private boolean isGameFinished = false;

    public void start() {
    }

    public final void updateState(int gameId, GameState state) {
        //gameStates.put(gameId, state);
        //state.updated(true);
    }

    public void onGameCreated(GameSession session) {
    }

    public void onGameFinished(GameSession session) {
    }

    public void onPlayerMakeMove(GameSession session, String jsonData) {
    }

    public void onRpcCall(String functionName, String jsonData) {
    }

    public final void setGameFinished() {
        isGameFinished = true;
    }

    public final boolean isGameFinished() {
        return isGameFinished;
    }

}
