package gss.server.manager.hooks;

import gss.network.GssConnection;

public class AuthHook_loginUsingIdPassword extends Hook {

    public int playerId;
    public String password;

    public AuthHook_loginUsingIdPassword(int playerId, String password, GssConnection con) {
        super(con);
        this.playerId = playerId;
        this.password = password;
    }

}
