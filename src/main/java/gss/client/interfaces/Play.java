package gss.client.interfaces;

import java.util.LinkedList;

import gss.GssLogger;
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

    public void enterGameResponse(int status, GssConnection con) {

        GssLogger.info("[Play] enterGameResponse called status: %d", status);

        for (PlayListener l : listeners) {
            l.enterGameResponse(status);
        }
        
    }

    public void exitGameResponse(int status, GssConnection con) {

        GssLogger.info("[Play] exitGameResponse called status: %d", status);

        for (PlayListener l : listeners) {
            l.exitGameResponse(status);
        }        

    }

    public void resignGameResponse(int status, GssConnection con) {

        GssLogger.info("[Play] resignGameResponse called status: %d", status);

        for (PlayListener l : listeners) {
            l.resignGameResponse(status);
        }        

    }

    public void listGamesResponse(int[] gameIds, GssConnection con) {        

        String ids = StringUtils.convertToDelimitedString(gameIds);

        GssLogger.info("[Play] listGamesResponse called gameIds: %s", ids);

        for (PlayListener l : listeners) {
            l.listGamesResponse(gameIds);
        }        

    }

    public void makeMoveResponse(int status, GssConnection con) {

        GssLogger.info("[Play] makeMoveResponse called status: %d", status);

        for (PlayListener l : listeners) {
            l.makeMoveResponse(status);
        }
        
    }

    public static interface PlayListener {
        public void enterGameResponse(int status);
        public void exitGameResponse(int status);
        public void resignGameResponse(int status);
        public void listGamesResponse(int[] gameIds);
        public void makeMoveResponse(int status);
    }

}
