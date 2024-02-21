package gss.server.manager.hooks;

import gss.network.GssConnection;

public class AccountHook_changeCredentials extends Hook {

    public String username;
    public String password;

    public AccountHook_changeCredentials(String username, String password, GssConnection con) {
        super(con);
        this.username = username;
        this.password = password;
    }

}
