package gss.server;

import gss.GssLogger;
import gss.network.Gss;
import gss.network.GssConfig;
import gss.server.manager.GameManager;
import gss.server.manager.ServiceUpdateScheduler;
import gss.server.util.Config;

public class Somun {
    
    public void start() {

        GssLogger.info("Starting Somun Socket Server");

        GssConfig config = new GssConfig();
        config.setLocalPort(Config.getInt("port"));
        config.addInterface(new gss.server.interfaces.Auth());
        config.addInterface(new gss.server.interfaces.Account());
        
        Gss.startServer(config);

        ServiceUpdateScheduler.get().register(GameManager.get());
        ServiceUpdateScheduler.get().start();

    }

}
