package gss.network;

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

}
