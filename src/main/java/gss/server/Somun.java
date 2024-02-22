package gss.server;

import gss.GssLogger;
import gss.network.Gss;
import gss.network.GssConfig;
import gss.server.manager.GameManager;
import gss.server.manager.ServiceUpdateScheduler;
import gss.server.manager.notification.NotificationManager;
import gss.server.manager.storage.StorageManager;
import gss.server.model.GameHandler;
import gss.server.util.Config;

public class Somun {
    
    private static GameHandler gameHandler;

    public void start(GameHandler gameHandler) {

        GssLogger.info("Starting Somun Socket Server");

        this.gameHandler = gameHandler;

        StorageManager.get().initialize();

        GssConfig config = new GssConfig();
        config.setLocalPort(Config.getInt("port"));
        config.addInterface(new gss.server.interfaces.Auth());
        config.addInterface(new gss.server.interfaces.Account());
        config.addInterface(new gss.server.interfaces.Play());
        config.addInterface(new gss.server.interfaces.Rpc());

        Gss.enableDebugFunctionCalls(true);
        Gss.startServer(config);

        ServiceUpdateScheduler.get().register(GameManager.get());
        ServiceUpdateScheduler.get().register(NotificationManager.get());
        ServiceUpdateScheduler.get().start();

        GameManager.get().setGameHandler(gameHandler);
        this.gameHandler.start();

    }

    public static GameHandler getGameHandler() {
        return gameHandler;
    }

}
