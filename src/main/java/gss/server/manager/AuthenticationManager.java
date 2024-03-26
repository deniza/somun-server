package gss.server.manager;

import gss.network.GssConnection;
import gss.server.manager.facebook.FacebookAuthenticationManager;
import gss.server.manager.storage.StorageManager;
import gss.server.model.Player;

public class AuthenticationManager {

    private static volatile AuthenticationManager instance;

    private AuthenticationManager() {
        // Prevent form the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use get() method to get the single instance of this class.");
        }
    }
    
    public static AuthenticationManager get() {
        // Double check locking pattern
        if (instance == null) {
            synchronized (AuthenticationManager.class) {
                if (instance == null) {
                    instance = new AuthenticationManager();
                }
            }
        }
        return instance;
    }

    public boolean authenticate(int playerId, String password) {

        Player player = PlayerManager.get().getPlayer(playerId);

        if (player == null) {
            return false;
        }
        else {
            if (player.getPassword().equals(password)) {

                player.setOnline(true);

                PlayerManager.get().addPlayer(player);
                GameManager.get().loadPlayerGameSessions(player);

                return true;
            }
            else {
                return false;
            }
        }

    }

    public Player authenticateUsernamePassword(String username, String password) {

        Player player = StorageManager.get().loadPlayerByUsernamePassword(username, password);

        if (player == null) {
            return null;
        }
        else {

            player.setOnline(true);

            PlayerManager.get().addPlayer(player);
            GameManager.get().loadPlayerGameSessions(player);

            return player;

        }

    }

    public void authenticateFacebook(String accessToken, GssConnection con) {

        FacebookAuthenticationManager.get().addAuthRequest(accessToken, con);

    }

}
