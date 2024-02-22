package gss.server.manager.notification;

import gss.server.model.ServiceUpdateInterface;
import gss.server.util.Config;

public class NotificationManager implements ServiceUpdateInterface {

    private boolean notificationsEnabled = Config.getBoolean("notification_enabled");
    private APNSManager apnsManager;
    private FirebaseMessagingManager firebaseMessagingManager;
    private static NotificationManager instance;

    private NotificationManager() {

        if (notificationsEnabled) {
            apnsManager = new APNSManager();
            firebaseMessagingManager = new FirebaseMessagingManager();
        }

    }

    private void shutdown() {

        if (notificationsEnabled) {
            apnsManager.shutdown();
            firebaseMessagingManager.shutdown();
        }

    }

    public static NotificationManager get() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }

    @Override
    public void updateService(long deltaTime) {
    }

}
