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
    public static int currentGameId = -1;
    public static boolean isTurnOwner = false;
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

        Gss.enableDebugFunctionCalls(true);
        
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
                if (status == 0) {
                    System.out.println("login failed");
                    ClientUI.get().update(UIState.loginerr);
                }
                else {
                    System.out.println("login success");
                    ClientUI.get().update(UIState.login);
                }
            }            
        });

        play.addListener(new PlayListener() {
            @Override
            public void enterGameResponse(int status, int turnOwner) {                
                if (status == 0) {
                    System.out.println("enter game failed");
                }
                else {
                    System.out.println("enter game success");
                    isTurnOwner = (turnOwner == playerId);
                    ClientUI.get().update(UIState.ingame);
                }
            }
            @Override
            public void exitGameResponse(int status) {
                if (status == 0) {
                    System.out.println("exit game failed");
                }
                else {
                    System.out.println("exit game success");
                    currentGameId = -1;
                    ClientUI.get().update(UIState.login);
                }                                
            }
            @Override
            public void resignGameResponse(int status) {
            }
            @Override
            public void listGamesResponse(int[] gameIds) {
                if (gameIds.length == 0) {
                    System.out.println("no games");
                    currentGameId = -1;
                }
                else {
                    System.out.println("games: " + gameIds);
                    currentGameId = gameIds[0];
                }
                ClientUI.get().update();
            }
            @Override
            public void makeMoveResponse(int status) {
                System.out.println("make move response: " + status);
            }
            @Override
            public void createRandomGameResponse(int status) {
                ClientUI.get().update();
            }
            @Override
            public void gameCreated(int gameId, int[] pids, int turnOwner, String state) {                
                currentGameId = gameId;
                isTurnOwner = (turnOwner == playerId);
                System.out.println("game created: " + gameId + " " + pids + " " + turnOwner + " " + state);
            }
            @Override
            public void gameStateUpdated(int gameId, String state) {
                System.out.println("game state updated: " + gameId + " " + state);
                ClientUI.get().update(ClientUI.UIState.ingame);
            }
            @Override
            public void turnOwnerChanged(int gameId, int turnOwner) {
                System.out.println("turn owner changed: " + gameId + " " + turnOwner);
                isTurnOwner = (turnOwner == playerId);
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

    }

    @Override
    public void onLoginUICommand() {

        con.invokeMethod("Auth_loginUsingIdPassword", new Object[]{ playerId, playerPassword });        

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

        System.out.println("create random game called");

        con.invokeMethod("Play_createRandomGame");

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
