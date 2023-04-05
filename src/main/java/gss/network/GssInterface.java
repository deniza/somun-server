package gss.network;

public abstract interface GssInterface {

    public abstract void clientDisconnected(GssConnection cb);
    public abstract void clientConnected(GssConnection cb);

}
