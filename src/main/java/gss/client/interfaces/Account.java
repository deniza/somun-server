package gss.client.interfaces;

import java.util.LinkedList;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;

public class Account extends GssInterface {
    
    private LinkedList<AccountListener> listeners = new LinkedList<>();    

    public void addListener(AccountListener accountListener) {
        listeners.add(accountListener);
    }
    
    public void removeListener(AccountListener listener) {
        listeners.remove(listener);
    }

    public void createGuestAccountResponse(int playerId, String username, String password, GssConnection con) {

        GssLogger.info("[Account] createGuestAccountResponse called playerId: %d username: %s password: %s", playerId, username, password);

        for (AccountListener l : listeners) {
            l.createGuestAccountResponse(playerId, username, password);
        }

    }

    public static interface AccountListener {
        public void createGuestAccountResponse(int playerId, String username, String password);
    }

}
