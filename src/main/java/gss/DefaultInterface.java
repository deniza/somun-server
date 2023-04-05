package gss;

import gss.network.GssConnection;
import gss.network.GssInterface;

public class DefaultInterface implements GssInterface {

    @Override
    public void clientConnected(GssConnection cb) {
        GssLogger.info("client connected sid: " + cb.getSessionId());
    }

    @Override
    public void clientDisconnected(GssConnection cb) {
        GssLogger.info("client disconnected sid: " + cb.getSessionId());
    }
    
}
