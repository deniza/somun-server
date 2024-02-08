package gss.server.manager.storage;

import gss.server.model.GameSession;
import gss.server.model.Player;

import java.util.ArrayList;

public interface StorageInterface {

    public void initialize();
    public void shutdown();

    public void storePlayer(Player player);

    public Player loadPlayer(int playerId);

    public void storeGameSession(GameSession session);

    public GameSession loadGameSession(int gameId);

    public ArrayList<GameSession> loadGameSessions(ArrayList<Integer> gameIds);

    public int getAndIncrementNextAvailableGameId();

    public int getAndIncrementNextAvailablePlayerId();

}
