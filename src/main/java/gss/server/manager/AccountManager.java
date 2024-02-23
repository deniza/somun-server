package gss.server.manager;

import gss.server.manager.storage.StorageInterface;
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

        Player player = new Player();
        player.setName(guestAccountNamePrefix + StorageManager.get().getAndIncrementConfigValue("nextGuestAccountPostfixId"));
        player.setPassword(PasswordGenerator.generatePassword(16));

        StorageInterface.CreatePlayerResult result;
        while ((result = StorageManager.get().createPlayer(player)) != StorageInterface.CreatePlayerResult.SUCCESS) {

            if (result == StorageInterface.CreatePlayerResult.USERNAME_ALREADY_EXISTS) {
                player.setName(guestAccountNamePrefix + StorageManager.get().getAndIncrementConfigValue("nextGuestAccountPostfixId"));
            }
            else if (result == StorageInterface.CreatePlayerResult.ERROR) {
                return null;
            }

        }

        PlayerManager.get().addPlayer(player);

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

            ConnectionManager.get().call(player, "Account", "changeCredentialsResponse", 0, "Invalid username or password");

        }

    }

}
