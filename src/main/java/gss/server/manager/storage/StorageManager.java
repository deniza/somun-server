package gss.server.manager.storage;

import gss.server.model.GameSession;
import gss.server.model.Player;

public class StorageManager {
    
    private static StorageManager instance;

    private final StorageManagerMongo mongo = new StorageManagerMongo();

    private volatile int nextAvailablePlayerId;
    private volatile int nextAvailableGameId;

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
        return nextAvailablePlayerId;
    }

    public synchronized int getAndIncrementNextAvailableGameId() {
        return nextAvailableGameId++;
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

}
