package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssCallable;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.AuthenticationManager;
import gss.server.manager.ConnectionManager;
import gss.server.manager.PlayerManager;
import gss.server.manager.hooks.AuthHook_loginUsingIdPassword;
import gss.server.manager.hooks.HookManager;
import gss.server.model.Player;

public class Auth extends GssInterface {

    @GssCallable
    public void loginUsingIdPassword(int playerId, String password, GssConnection con) {
        
        GssLogger.info("[Auth] loginUsingIdPassword called playerId: %d password: %s", playerId, password);

        AuthHook_loginUsingIdPassword hook = new AuthHook_loginUsingIdPassword(playerId, password, con);
        HookManager.get().processHook(hook);

        if (hook.isCancelled()) {
            return;
        }

        if (AuthenticationManager.get().authenticate(hook.playerId, hook.password) == true) {

            ConnectionManager.get().register(hook.playerId, con);

            Player player = PlayerManager.get().getPlayer(hook.playerId);

            call(con, "Auth", "loginResponse", 1, player.getName());

        }
        else {

            call(con, "Auth", "loginResponse", 0, "");

        }
    }

    @GssCallable
    public void loginUsingFacebook(String accessToken, GssConnection con) {

        GssLogger.info("[Auth] loginUsingFacebook called accessToken: %s", accessToken);

        AuthenticationManager.get().authenticateFacebook(accessToken, con);

    }

    @Override
    public void clientDisconnected(GssConnection con) {
        
        ConnectionManager.get().unregister(con);

    }

}
