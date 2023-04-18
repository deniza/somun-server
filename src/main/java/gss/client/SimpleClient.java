package gss.client;

import gss.GssLogger;
import gss.client.ClientUI.UIListener;
import gss.client.ClientUI.UIState;
import gss.client.interfaces.*;
import gss.client.interfaces.Play.*;
import gss.client.interfaces.Auth.*;
import gss.client.interfaces.Account.*;
import gss.network.Gss;
import gss.network.GssConfig;
import gss.network.GssConnection;
import gss.server.samplegame.Move;

public class SimpleClient implements UIListener {
    
    private int playerId;
    private String playerName;
    private String playerPassword;
    private int currentGameId = 0;
    private GssConnection con;

    private Auth auth = new Auth();
    private Account account = new Account();
    private Play play = new Play();

    public SimpleClient(int playerId, String name, String password) {
        this.playerId = playerId;
        this.playerName = name;
        this.playerPassword = password;
    }

    public void start() {

        ClientUI.get().start(this);        

        account.addListener(new AccountListener() {
            @Override
            public void createGuestAccountResponse(int _playerId, String username, String password) {
                
                playerId = _playerId;
                playerName = username;
                playerPassword = password;

                con.invokeMethod("Auth_loginUsingIdPassword", new Object[]{ playerId, playerPassword });

            }            
        });

        auth.addListener(new AuthListener() {
            @Override
            public void loginResponse(int status) {
            }            
        });

        play.addListener(new PlayListener() {
            @Override
            public void enterGameResponse(int status) {
            }
            @Override
            public void exitGameResponse(int status) {
                currentGameId = 0;
            }
            @Override
            public void resignGameResponse(int status) {
            }
            @Override
            public void listGamesResponse(int[] gameIds) {
            }
            @Override
            public void makeMoveResponse(int status) {
            }
        });

    }

    @Override
    public void onExitAppUICommand() {

        GssLogger.info("shutdown client");

        Gss.shutdownClient();

    }

    @Override
    public void onCreateNewAccountUICommand() {

        con.invokeMethod("Account_createGuestAccount");

        ClientUI.get().update(UIState.login);

    }

    @Override
    public void onLoginUICommand() {

        con.invokeMethod("Auth_loginUsingIdPassword", new Object[]{ playerId, playerPassword });

        ClientUI.get().update(UIState.login);

    }

    @Override
    public void onEnterGameUICommand() {

        con.invokeMethod("Play_enterGame", new Object[]{ currentGameId });
    }

    @Override
    public void onExitGameUICommand() {

        con.invokeMethod("Play_exitGame", new Object[]{ currentGameId });

    }

    @Override
    public void onResignGameUICommand() {

        con.invokeMethod("Play_resignGame", new Object[]{ currentGameId });

    }

    @Override
    public void onMakeMoveUICommand(int number) {

        con.invokeMethod("Play_makeMove", new Object[]{ currentGameId, new Move(number).toJson() });

    }

    @Override
    public void onListGamesUICommand() {

        con.invokeMethod("Play_listGames", new Object[]{});

    }

    @Override
    public void onCreateRandomGameUICommand() {
    }

    @Override
    public void onConnectServerUICommand() {

        GssLogger.info("connected");

        GssConfig config = new GssConfig();
        config.setRemotePort(16666);
        config.addInterface(account);
        config.addInterface(auth);    
        config.addInterface(play);

        con = Gss.startClient(config);

        ClientUI.get().update(UIState.connected);

    }

    
}
