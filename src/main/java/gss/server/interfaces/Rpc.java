package gss.server.interfaces;

import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.GameManager;

public class Rpc extends GssInterface {

    public void call(String functionName, String jsonData, GssConnection con) {

        GameManager.get().getGameHandler().onRpcCall(functionName, jsonData);

    }

}
