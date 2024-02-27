package gss.client.interfaces;

import java.util.LinkedList;

import gss.GssLogger;
import gss.network.GssCallable;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.util.StringUtils;

public class Play extends GssInterface {
    
    private LinkedList<PlayListener> listeners = new LinkedList<>();

    public void addListener(PlayListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(PlayListener listener) {
        listeners.remove(listener);
    }

    @GssCallable
    public void enterGameResponse(int status, int turnOwner, GssConnection con) {

        GssLogger.info("[Play] enterGameResponse called status: %d", status);

        for (PlayListener l : listeners) {
            l.enterGameResponse(status, turnOwner);
        }
        
    }

    @GssCallable
    public void exitGameResponse(int status, GssConnection con) {

        GssLogger.info("[Play] exitGameResponse called status: %d", status);

        for (PlayListener l : listeners) {
            l.exitGameResponse(status);
        }        

    }

    @GssCallable
    public void resignGameResponse(int status, GssConnection con) {

        GssLogger.info("[Play] resignGameResponse called status: %d", status);

        for (PlayListener l : listeners) {
            l.resignGameResponse(status);
        }        

    }

    @GssCallable
    public void listGamesResponse(int[] gameIds, GssConnection con) {        

        String ids = StringUtils.convertToDelimitedString(gameIds);

        GssLogger.info("[Play] listGamesResponse called gameIds: %s", ids);

        for (PlayListener l : listeners) {
            l.listGamesResponse(gameIds);
        }        

    }

    @GssCallable
    public void makeMoveResponse(int status, GssConnection con) {

        GssLogger.info("[Play] makeMoveResponse called status: %d", status);

        for (PlayListener l : listeners) {
            l.makeMoveResponse(status);
        }
        
    }

    @GssCallable
    public void createRandomGameResponse(int status, GssConnection con) {

        GssLogger.info("[Play] createRandomGameResponse called status: %d", status);

        for (PlayListener l : listeners) {
            l.createRandomGameResponse(status);
        }
        
    }

    @GssCallable
    public void gameCreated(int gameId, int[] pids, int turnOwner, String state, GssConnection con) {

        GssLogger.info("[Play] gameCreated called state: %s", state);

        for (PlayListener l : listeners) {
            l.gameCreated(gameId, pids, turnOwner, state);
        }
        
    }

    @GssCallable
    public void gameStateUpdated(int gameId, String state, GssConnection con) {

        GssLogger.info("[Play] gameStateUpdated called gameId: %d state: %s", gameId, state);

        for (PlayListener l : listeners) {
            l.gameStateUpdated(gameId, state);
        }
        
    }

    @GssCallable
    public void turnOwnerChanged(int gameId, int turnOwner, GssConnection con) {

        GssLogger.info("[Play] turnOwnerChanged called gameId: %d turnOwner: %d", gameId, turnOwner);

        for (PlayListener l : listeners) {
            l.turnOwnerChanged(gameId, turnOwner);
        }
        
    }

    public static interface PlayListener {
        public void enterGameResponse(int status, int turnOwner);
        public void exitGameResponse(int status);
        public void resignGameResponse(int status);
        public void listGamesResponse(int[] gameIds);
        public void makeMoveResponse(int status);
        public void createRandomGameResponse(int status);
        public void gameCreated(int gameId, int[] pids, int turnOwner, String state);
        public void gameStateUpdated(int gameId, String state);
        public void turnOwnerChanged(int gameId, int turnOwner);
    }

}
