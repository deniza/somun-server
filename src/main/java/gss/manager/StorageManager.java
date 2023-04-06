package gss.manager;

import java.util.concurrent.ConcurrentMap;

import org.mapdb.*;
import gss.model.Player;
import gss.model.PlayerSerializer;

public class StorageManager {
    
    private static StorageManager instance;
    private DB db;
    private ConcurrentMap<Integer, Player> playersMap;

    private StorageManager() {

        initialize();

    }

    private void initialize() {

        db = DBMaker.fileDB("players.db")
                    .fileMmapEnable()
                    .closeOnJvmShutdown()
                    .make();
        
        playersMap = db.hashMap("players")                    
                        .keySerializer(Serializer.INTEGER)
                        .valueSerializer(new PlayerSerializer())
                        .createOrOpen();

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

    public void storePlayer(Player player) {
        
        playersMap.put(player.getPlayerId(), player);
        db.commit();

    }

    public void createPlayer(Player player) {

        storePlayer(player);

    }

    public Player loadPlayer(int playerId) {

        return playersMap.get(playerId);

    }

}
