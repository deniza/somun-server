package gss;

import gss.network.Gss;
import gss.network.GssConfig;
import gss.network.GssConnection;

public class SimpleClient {
    
    private int playerId;
    private String playerName;
    private String playerPassword;

    public SimpleClient(int playerId, String name, String password) {
        this.playerId = playerId;
        this.playerName = name;
        this.playerPassword = password;
    }

    public void start() {

        gss.client.interfaces.Auth auth = new gss.client.interfaces.Auth();
        gss.client.interfaces.Account account = new gss.client.interfaces.Account();

        GssConfig config = new GssConfig();
        config.setRemotePort(16666);
        config.addInterface(account);
        config.addInterface(auth);        
        
        GssConnection con = Gss.startClient(config);

        //con.invokeMethod("Account_createGuestAccount");
        con.invokeMethod("Auth_loginUsingIdPassword", new Object[]{ playerId, playerPassword });

        account.addListener(new gss.client.interfaces.Account.AccountListener() {
            @Override
            public void createGuestAccountResponse(int _playerId, String username, String password) {
                
                playerId = _playerId;
                playerName = username;
                playerPassword = password;

                con.invokeMethod("Auth_loginUsingIdPassword", new Object[]{ playerId, playerPassword });

            }            
        });

        auth.addListener(new gss.client.interfaces.Auth.AuthListener() {
            @Override
            public void loginResponse(int status) {
            }            
        });

        //Gss.shutdownClient();

    }
    
}
