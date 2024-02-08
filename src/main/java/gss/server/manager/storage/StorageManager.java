package gss.server.manager.storage;

import gss.server.model.GameSession;
import gss.server.model.Player;

import java.util.ArrayList;

public class StorageManager {
    
    private static StorageManager instance;

    private final StorageManagerMongo mongo = new StorageManagerMongo();

    private StorageManager() {
    }

    public void initialize() {

        mongo.initialize();

    }

    public void shutdown() {

        mongo.shutdown();

    }

    public static StorageManager get() {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    public int getNextAvailablePlayerId() {
        return mongo.getAndIncrementNextAvailablePlayerId();
    }

    public synchronized int getAndIncrementNextAvailableGameId() {
        return mongo.getAndIncrementNextAvailableGameId();
    }

    public synchronized void storePlayer(Player player) {
        
        mongo.storePlayer(player);

    }

    public synchronized Player loadPlayer(int playerId) {

        return mongo.loadPlayer(playerId);

    }

    public synchronized void storeGameSession(GameSession session) {

        mongo.storeGameSession(session);

    }    

    public synchronized ArrayList<GameSession> loadGameSessions(ArrayList<Integer> gameIds) {

        return mongo.loadGameSessions(gameIds);

    }

}
