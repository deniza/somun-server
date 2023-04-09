package gss.server.manager;

import java.util.HashMap;

import gss.network.GssConnection;

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

    public void call(int playerId, String module, String function, Object... args) {
        
        GssConnection con = pidConMap.get(playerId);
        
        if (con != null) {
            con.invokeMethod(module + "_" + function, args);
        }
    }

}
