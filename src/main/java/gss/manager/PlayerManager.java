package gss.manager;

import java.util.HashMap;

import gss.model.Player;

public class PlayerManager {
    
    private static PlayerManager instance;

    private volatile int nextAvailablePlayerId = 0;
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

        int playerId = nextAvailablePlayerId++;
        Player player = new Player(playerId);

        players.put(player.getPlayerId(), player);

        return playerId;

    }

    public Player getPlayer(int playerId) {
        return players.get(playerId);
    }

}
