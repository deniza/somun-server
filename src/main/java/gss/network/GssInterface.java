package gss.network;

import gss.server.manager.PlayerManager;
import gss.server.model.Player;

public abstract class GssInterface {

    public void call(GssConnection con, String module, String function, Object... args) {
        if (con != null) {
            con.invokeMethod(module + "_" + function, args);
        }
    }

    public void clientDisconnected(GssConnection con) {
    }

    public void clientConnected(GssConnection con) {
    }

    protected Player getPlayer(GssConnection con) {
        return PlayerManager.get().getPlayer(con);
    }

}
