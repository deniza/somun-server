package gss.network;

public abstract class GssModule {

    public abstract void clientDisconnected(GssConnection cb);
    public abstract void clientConnected(GssConnection cb);

}
