package gss.server.model;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import gss.server.event.GameEventHandler;

public abstract class GameHandler extends GameEventHandler {
    
    protected ConcurrentHashMap<Integer, GameSession> gameSessions = new ConcurrentHashMap<>();

    public void start() {
    }

    public GameSession createGameSession(int gameId, ArrayList<Player> players) {
        GameSession session = new GameSession(gameId, players);
        gameSessions.put(gameId, session);
        return session;
    }

    public void updateState(int gameId, GameState state) {
        //gameStates.put(gameId, state);
        //state.updated(true);
    }

    public String save(int gameId) {

        final String stateData = gameSessions.get(gameId).serialize();
        return stateData;

    }

    public void load(int gameId, String data) {
        //gameStates.get(gameId).loadState(data);
    }

}
