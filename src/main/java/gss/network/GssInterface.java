package gss.network;

import gss.server.manager.PlayerManager;
import gss.server.model.Player;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class GssInterface {

    private final HashMap<String, Method> methods = new HashMap<String, Method>();

    public GssInterface() {
        // get all methods from the class
        for (Method m : this.getClass().getMethods()) {
            methods.put(m.getName(), m);
        }
    }

    public Method getMethod(String name) {
        return methods.get(name);
    }

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
