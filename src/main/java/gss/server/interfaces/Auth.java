package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.AuthenticationManager;
import gss.server.manager.ConnectionManager;
import gss.server.manager.HookManager;

public class Auth extends GssInterface {

    public void loginUsingIdPassword(int playerId, String password, GssConnection con) {
        
        GssLogger.info("[Auth] loginUsingIdPassword called playerId: %d password: %s", playerId, password);

        HookManager.AuthHook_loginUsingIdPassword hook = new HookManager.AuthHook_loginUsingIdPassword(playerId, password);
        HookManager.get().processHook(hook);

        if (AuthenticationManager.get().authenticate(hook.playerId, hook.password) == true) {

            ConnectionManager.get().register(hook.playerId, con);

            call(con, "Auth", "loginResponse", 1);

        }
        else {

            call(con, "Auth", "loginResponse", 0);

        }
    }

    @Override
    public void clientDisconnected(GssConnection con) {
        
        ConnectionManager.get().unregister(con);

    }

}
