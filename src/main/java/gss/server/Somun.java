package gss.server;

import gss.GssLogger;
import gss.network.Gss;
import gss.network.GssConfig;
import gss.server.manager.GameManager;
import gss.server.manager.ServiceUpdateScheduler;
import gss.server.model.GameHandler;
import gss.server.util.Config;

public class Somun {
    
    private GameHandler gameHandler;

    public void start(GameHandler gameHandler) {

        GssLogger.info("Starting Somun Socket Server");

        this.gameHandler = gameHandler;

        GssConfig config = new GssConfig();
        config.setLocalPort(Config.getInt("port"));
        config.addInterface(new gss.server.interfaces.Auth());
        config.addInterface(new gss.server.interfaces.Account());
        config.addInterface(new gss.server.interfaces.Play());
        
        Gss.enableDebugFunctionCalls(true);
        Gss.startServer(config);

        ServiceUpdateScheduler.get().register(GameManager.get());
        ServiceUpdateScheduler.get().start();

        GameManager.get().setGameHandler(gameHandler);
        this.gameHandler.start();

    }

}
