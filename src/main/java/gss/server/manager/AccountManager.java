package gss.server.manager;

import gss.server.manager.storage.StorageManager;
import gss.server.model.Player;
import gss.server.util.CredentialUtils;
import gss.server.util.PasswordGenerator;

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

    public Player createGuestAccount() {
        
        int playerId = PlayerManager.get().createPlayer();

        Player player = PlayerManager.get().getPlayer(playerId);
        player.setName(guestAccountNamePrefix + playerId);
        player.setPassword(PasswordGenerator.generatePassword(16));

        StorageManager.get().storePlayer(player);

        return player;

    }

    public void changeCredentials(Player player, String username, String password) {

        if (CredentialUtils.checkUsernameValidity(username) && CredentialUtils.checkPasswordValidity(password)) {

            player.setName(username);
            player.setPassword(password);

            StorageManager.get().storePlayer(player);

            ConnectionManager.get().call(player, "Account", "changeCredentialsResponse", 1);

        }
        else {

            ConnectionManager.get().call(player, "Account", "changeCredentialsResponse", 0);

        }

    }

}
