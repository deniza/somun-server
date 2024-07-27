package gss.server;

import gss.GssLogger;
import gss.network.Gss;
import gss.network.GssConfig;
import gss.server.manager.AdminDashboardServer;
import gss.server.manager.GameManager;
import gss.server.manager.ServiceUpdateScheduler;
import gss.server.manager.facebook.FacebookAuthenticationManager;
import gss.server.manager.groups.GroupsManager;
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
        config.setLocalIp(Config.getString("bind_host"));
        config.addInterface(new gss.server.interfaces.Connection());
        config.addInterface(new gss.server.interfaces.Auth());
        config.addInterface(new gss.server.interfaces.Account());
        config.addInterface(new gss.server.interfaces.Play());
        config.addInterface(new gss.server.interfaces.Rpc());
        config.addInterface(new gss.server.interfaces.Groups());

        Gss.enableDebugFunctionCalls(true);
        Gss.startServer(config);

        ServiceUpdateScheduler.get().register(GameManager.get());
        ServiceUpdateScheduler.get().register(NotificationManager.get());
        ServiceUpdateScheduler.get().register(FacebookAuthenticationManager.get());
        ServiceUpdateScheduler.get().register(GroupsManager.get());
        ServiceUpdateScheduler.get().start();

        if (Config.getBoolean("admin_interface_enabled")) {
            AdminDashboardServer.get().start();
        }

        GameManager.get().setGameHandler(gameHandler);
        this.gameHandler.start();

    }

    public static GameHandler getGameHandler() {
        return gameHandler;
    }

}
