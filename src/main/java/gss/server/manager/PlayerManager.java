package gss.server.manager;

import java.util.HashMap;

import gss.network.GssConnection;
import gss.server.model.Player;

public class PlayerManager {
    
    private static PlayerManager instance;

    private final HashMap<Integer, Player> players = new HashMap<>();

    private PlayerManager() {
    }

    public static PlayerManager get() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public synchronized int createPlayer() {
        
        int playerId = StorageManager.get().getNextAvailablePlayerId();
        Player player = new Player(playerId);

        players.put(player.getPlayerId(), player);

        return playerId;

    }

    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }

    public Player getPlayer(GssConnection con) {
        int playerId = ConnectionManager.get().getPlayerId(con);
        return getPlayer(playerId);
    }

}
