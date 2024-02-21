package gss.server.manager.hooks;

import gss.network.GssConnection;

public abstract class Hook {

    private final GssConnection con;

    protected Hook(GssConnection con) {
        this.con = con;
    }

    public GssConnection getCon() {
        return con;
    }

}
