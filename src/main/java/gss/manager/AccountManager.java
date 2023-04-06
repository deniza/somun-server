package gss.manager;

import gss.model.Player;
import gss.util.PasswordGenerator;

public class AccountManager {
    
    private static AccountManager instance;

    private final String guestAccountNamePrefix = "guest_";

    private AccountManager() {        
    }

    public static AccountManager get() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    public void createGuestAccount() {
        
        int playerId = PlayerManager.get().createPlayer();

        Player player = PlayerManager.get().getPlayer(playerId);        
        player.setName(guestAccountNamePrefix + playerId);
        player.setPassword(PasswordGenerator.generatePassword(16));

        StorageManager.get().createPlayer(player);

    }

}
