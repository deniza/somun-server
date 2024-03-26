package gss.server.manager;

import java.util.HashMap;

import gss.network.GssConnection;
import gss.server.manager.storage.StorageManager;
import gss.server.model.Player;

public class PlayerManager {
    
    private static volatile PlayerManager instance;

    private final HashMap<Integer, Player> players = new HashMap<>();

    private PlayerManager() {
        // Prevent form the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use get() method to get the single instance of this class.");
        }
    }

    public static PlayerManager get() {
        // Double check locking pattern
        if (instance == null) {
            synchronized (PlayerManager.class) {
                if (instance == null) {
                    instance = new PlayerManager();
                }
            }
        }
        return instance;
    }

    public synchronized void addPlayer(Player player) {

        players.put(player.getPlayerId(), player);

    }

    public Player getPlayer(int playerId) {

        // reserved for unset player
        if (playerId == 0) {
            return null;
        }

        Player player = players.get(playerId);

        if (player == null) {
            player = StorageManager.get().loadPlayer(playerId);
            if (player != null) {
                addPlayer(player);
            }
        }

        return player;
    }

    public Player getPlayer(GssConnection con) {
        Integer playerId = ConnectionManager.get().getPlayerId(con);
        if (playerId == null) {
            return null;
        }
        return getPlayer(playerId);
    }

    public void playerDisconnected(Player player) {
        player.setOnline(false);
    }

}
