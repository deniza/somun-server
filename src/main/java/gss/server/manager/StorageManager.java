package gss.server.manager;

import java.util.concurrent.ConcurrentMap;

import org.mapdb.*;

import gss.server.model.Player;
import gss.server.model.PlayerSerializer;

public class StorageManager {
    
    private static StorageManager instance;
    private DB db;
    private ConcurrentMap<Integer, Player> playersMap;

    private volatile int nextAvailablePlayerId;
    private volatile int nextAvailableGameId = 0;

    private StorageManager() {

        initialize();

    }

    private void initialize() {

        db = DBMaker.fileDB("players.db")
                    .closeOnJvmShutdown()
                    .transactionEnable()
                    .make();
        
        playersMap = db.hashMap("players")                    
                        .keySerializer(Serializer.INTEGER)
                        .valueSerializer(new PlayerSerializer())
                        .createOrOpen();

        nextAvailablePlayerId = playersMap.size();
    }

    public void shutdown() {
        db.close();
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

    public void storePlayer(Player player) {
        
        playersMap.put(player.getPlayerId(), player);
        db.commit();

        nextAvailablePlayerId = playersMap.size();

    }

    public void createPlayer(Player player) {

        storePlayer(player);

    }

    public Player loadPlayer(int playerId) {

        return playersMap.get(playerId);

    }

}
