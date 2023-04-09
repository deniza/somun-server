package gss.client.interfaces;

import java.util.LinkedList;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;

public class Play extends GssInterface {
    
    private LinkedList<PlayListener> listeners = new LinkedList<>();

    public void addListener(PlayListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(PlayListener listener) {
        listeners.remove(listener);
    }
    
    public void makeMoveResponse(int status, GssConnection con) {

        GssLogger.info("[Play] makeMoveResponse called status: %d", status);

        for (PlayListener l : listeners) {
            l.makeMoveResponse(status);
        }
        
    }    

    public static interface PlayListener {
        public void makeMoveResponse(int status);
    }

}
