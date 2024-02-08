package gss.server.manager;

import gss.server.manager.storage.StorageManager;
import gss.server.model.Player;

public class AuthenticationManager {

    private static AuthenticationManager instance;

    private AuthenticationManager() {        
    }
    
    public static AuthenticationManager get() {
        if (instance == null) {
            instance = new AuthenticationManager();
        }
        return instance;
    }

    public boolean authenticate(int playerId, String password) {

        Player player = StorageManager.get().loadPlayer(playerId);

        if (player == null) {
            return false;
        }
        else {
            return  player.getPassword().equals(password);
        }

    }

}
