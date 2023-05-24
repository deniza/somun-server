package gss.server.manager.storage;

import java.util.concurrent.ConcurrentMap;

import gss.server.Somun;
import org.mapdb.*;

import gss.server.model.GameHandler;
import gss.server.model.Player;
import gss.server.model.PlayerSerializer;

public class StorageManager {
    
    private static StorageManager instance;
    private DB db;
    private ConcurrentMap<Integer, Player> playersMap;
    private ConcurrentMap<Integer, String> gameStateMap;

    private volatile int nextAvailablePlayerId;
    private volatile int nextAvailableGameId;

    private StorageManager() {

        initialize();

    }

    private void initialize() {

        db = DBMaker.fileDB("datastore.db")
        .closeOnJvmShutdown()
        .transactionEnable()
        .make();

        playersMap = db.hashMap("players")
        .keySerializer(Serializer.INTEGER)
        .valueSerializer(new PlayerSerializer())
        .createOrOpen();

        gameStateMap = db.hashMap("games")
        .keySerializer(Serializer.INTEGER)
        .valueSerializer(Serializer.STRING)
        .createOrOpen();

        nextAvailablePlayerId = playersMap.size();
        nextAvailableGameId = gameStateMap.size();
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

    public synchronized void storePlayer(Player player) {
        
        playersMap.put(player.getPlayerId(), player);
        db.commit();

        nextAvailablePlayerId = playersMap.size();

    }

    public synchronized Player loadPlayer(int playerId) {

        return playersMap.get(playerId);

    }

    public synchronized void storeGameState(int gameId) {

        GameHandler gameHandler = Somun.getGameHandler();
        String state = gameHandler.save(gameId);

        gameStateMap.put(gameId, state);
        db.commit();

    }    

}
