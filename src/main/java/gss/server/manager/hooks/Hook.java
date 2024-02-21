package gss.server.manager.hooks;

import gss.network.GssConnection;

public abstract class Hook {

    private final GssConnection con;

    private boolean isCancelled = false;
    private String cancelReason = "";

    protected Hook(GssConnection con) {
        this.con = con;
    }

    public GssConnection getCon() {
        return con;
    }

    public void cancel(String reason) {
        isCancelled = true;
    }

    public void cancel() {
        isCancelled = true;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public String getCancelReason() {
        return cancelReason;
    }

}
