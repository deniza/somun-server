package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.AccountManager;
import gss.server.manager.ConnectionManager;
import gss.server.manager.hooks.AccountHook_changeCredentials;
import gss.server.manager.hooks.AccountHook_createGuestAccount;
import gss.server.manager.hooks.HookManager;
import gss.server.model.Player;

public class Account extends GssInterface {

    public void createGuestAccount(GssConnection con) {
        
        GssLogger.info("[Account] createGuestAccount called");

        AccountHook_createGuestAccount hook = new AccountHook_createGuestAccount(con);
        HookManager.get().processHook(hook);

        if (hook.isCancelled()) {
            call(con, "Account", "createGuestAccountRejected", hook.getCancelReason());
            return;
        }

        Player player = AccountManager.get().createGuestAccount();
        if (player == null) {
            call(con, "Account", "createGuestAccountRejected", "Error creating guest account");
        }
        else  {
            call(con, "Account", "createGuestAccountAccepted", player.getPlayerId(), player.getName(), player.getPassword());
        }

    }

    public void createAccount(String username, String password, GssConnection con) {

        GssLogger.info("[Account] createAccount called");

        AccountManager.get().createAccount(username, password, con);

    }

    public void changeCredentials(String username, String password, GssConnection con) {

        GssLogger.info("[Account] changeCredentials called");

        AccountHook_changeCredentials hook = new AccountHook_changeCredentials(username, password, con);
        HookManager.get().processHook(hook);

        if (hook.isCancelled()) {
            call(con, "Account", "changeCredentialsResponse", 0, hook.getCancelReason());
            return;
        }

        Player player = getPlayer(con);
        AccountManager.get().changeCredentials(player, hook.username, hook.password);

    }

    public void setNotificationToken(String token, int deviceType, GssConnection con) {

        GssLogger.info("[Account] setNotificationToken called token: %s deviceType: %d", token, deviceType);

        Player player = getPlayer(con);
        player.setDeviceType(Player.DeviceType.values()[deviceType]);
        player.setMessagingToken(token);

    }

}
