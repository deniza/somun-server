package gss;

import gss.network.GssConnection;
import gss.network.GssInterface;

public class DefaultServerInterface implements GssInterface {

    public void serverTestFunction(String stringParam, GssConnection con) {
        GssLogger.info("[DefaultServerInterface] serverTestFunction called with param " + stringParam);
    }

    @Override
    public void clientConnected(GssConnection cb) {
        GssLogger.info("[DefaultServerInterface] client connected sid: " + cb.getSessionId());
    }

    @Override
    public void clientDisconnected(GssConnection cb) {
        GssLogger.info("[DefaultServerInterface] client disconnected sid: " + cb.getSessionId());
    }
    
}
