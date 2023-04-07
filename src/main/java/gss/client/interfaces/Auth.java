package gss.client.interfaces;

import java.util.LinkedList;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;

public class Auth extends GssInterface {
    
    private LinkedList<AuthListener> listeners = new LinkedList<>();

    public void addListener(AuthListener listener) {
        listeners.add(listener);
    }
    
    public void removeListener(AuthListener listener) {
        listeners.remove(listener);
    }
    
    public void loginResponse(int status, GssConnection con) {

        GssLogger.info("[Auth] loginResponse called status: %d", status);

        for (AuthListener l : listeners) {
            l.loginResponse(status);
        }
        
    }    

    public static interface AuthListener {
        public void loginResponse(int status);
    }

}
