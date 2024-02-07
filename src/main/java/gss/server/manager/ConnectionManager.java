package gss.server.manager;

import java.util.HashMap;
import java.util.List;

import gss.network.GssConnection;
import gss.server.model.Player;

public class ConnectionManager {
    
    private static ConnectionManager instance;

    private HashMap<Integer, GssConnection> pidConMap = new HashMap<>();
    private HashMap<GssConnection, Integer> conPidMap = new HashMap<>();

    public static ConnectionManager get() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }

    public void register(int playerId, GssConnection con) {
        pidConMap.put(playerId, con);
        conPidMap.put(con, playerId);
    }

    public void unregister(int playerId) {
        GssConnection con = pidConMap.remove(playerId);
        conPidMap.remove(con);
    }

    public void unregister(GssConnection con) {
        Integer playerId = conPidMap.remove(con);
        if (playerId != null) {
            pidConMap.remove(playerId);
        }
    }

    public int getPlayerId(GssConnection con) {
        return conPidMap.get(con);
    }

    public void call(Player player, String module, String function, Object... args) {
        
        GssConnection con = pidConMap.get(player.getPlayerId());
        
        if (con != null) {
            con.invokeMethod(module + "_" + function, args);
        }
    }

    public void call(List<Player> players, String module, String function, Object... args) {

        for (Player p : players) {
            call(p, module, function, args);
        }

    }

}
