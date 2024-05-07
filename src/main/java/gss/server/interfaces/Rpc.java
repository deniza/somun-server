package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssCallable;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.GameManager;

public class Rpc extends GssInterface {

    @GssCallable
    public void call(String functionName, String jsonData, GssConnection con) {

        GameManager.get().getGameHandler().onRpcCall(functionName, jsonData);

    }

    @GssCallable
    public void test(String p1, int p2, GssConnection con) {

        GssLogger.info("[Rpc] test called p1: %s p2: %d", p1, p2);

    }

}
