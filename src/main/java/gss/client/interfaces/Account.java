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

    public void createGuestAccountAccepted(int playerId, String username, String password, GssConnection con) {

        GssLogger.info("[Account] createGuestAccountAccepted called playerId: %d username: %s password: %s", playerId, username, password);

        for (AccountListener l : listeners) {
            l.createGuestAccountAccepted(playerId, username, password);
        }

    }

    public void createGuestAccountRejected(String reason, GssConnection con) {

        GssLogger.info("[Account] createGuestAccountRejected called reason: %s", reason);

        for (AccountListener l : listeners) {
            l.createGuestAccountRejected(reason);
        }

    }

    public static interface AccountListener {
        public void createGuestAccountAccepted(int playerId, String username, String password);
        public void createGuestAccountRejected(String reason);
    }

}
