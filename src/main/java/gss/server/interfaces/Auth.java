package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.AuthenticationManager;
import gss.server.manager.ConnectionManager;
import gss.server.manager.PlayerManager;

import java.util.ArrayList;

public class Auth extends GssInterface {

    public void loginUsingIdPassword(int playerId, String password, GssConnection con) {
        
        GssLogger.info("[Auth] loginUsingIdPassword called playerId: %d password: %s", playerId, password);

        if (AuthenticationManager.get().authenticate(playerId, password) == true) {

            ConnectionManager.get().register(playerId, con);
            PlayerManager.get().registerExistingPlayer(playerId);

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
