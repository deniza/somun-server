package gss;

import gss.network.GssConnection;
import gss.network.GssInterface;

public class DefaultClientInterface implements GssInterface {

    public void clientTestFunction(String stringParam, GssConnection con) {
        GssLogger.info("[DefaultClientInterface] clientTestFunction called with param " + stringParam);
    }

    @Override
    public void clientConnected(GssConnection cb) {
        GssLogger.info("[DefaultClientInterface] client connected sid: " + cb.getSessionId());
    }

    @Override
    public void clientDisconnected(GssConnection cb) {
        GssLogger.info("[DefaultClientInterface] client disconnected sid: " + cb.getSessionId());
    }
    
}
