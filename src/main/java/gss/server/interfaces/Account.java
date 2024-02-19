package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.AccountManager;
import gss.server.manager.PlayerManager;
import gss.server.model.Player;

public class Account extends GssInterface {

    public void createGuestAccount(GssConnection con) {
        
        GssLogger.info("[Account] createGuestAccount called");

        Player player = AccountManager.get().createGuestAccount();

        call(con, "Account", "createGuestAccountResponse", player.getPlayerId(), player.getName(), player.getPassword());
    }

    public void changeCredentials(String username, String password, GssConnection con) {

        GssLogger.info("[Account] changeCredentials called");

        Player player = getPlayer(con);
        AccountManager.get().changeCredentials(player, username, password);

    }

}
