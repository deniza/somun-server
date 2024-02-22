package gss.server.manager.notification;

import com.google.api.core.ApiFuture;
import com.google.firebase.*;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.messaging.*;
import gss.GssLogger;
import gss.server.util.Config;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author deniz
 */
public class FirebaseMessagingManager {

    private final String FirebaseClickAction = Config.get("notification_firebase_click_action");
    private final String FirebaseChannel = Config.get("notification_firebase_channel");
    private final String FirebaseSound = Config.get("notification_firebase_sound");
    private final boolean FirebaseEnableDryRun = Config.getBoolean("notification_firebase_enable_dryrun");
    private int totalSentNotifications = 0;
    private int successCount = 0;
    private int failCount = 0;
    private int errorCount = 0;

    public FirebaseMessagingManager() {

        initFirebase();

    }

    private void initFirebase() {

        FirebaseOptions options = null;
        try {
            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new FileInputStream(Config.get("notification_firebase_credentials"))))
                    .setDatabaseUrl(Config.get("notification_firebase_database_url"))
                    .build();
        }
        catch (Exception e) {
            GssLogger.error("[ERROR] NotificationManager error in Firebase initialization");
            Logger.getLogger(FirebaseMessagingManager.class.getName()).log(Level.SEVERE, null, e);
        }

        if (options != null) {
            FirebaseApp.initializeApp(options);
        }

    }

    public void shutdown() {
        // shutdown firebase messaging here
    }

    private Message buildFirebaseMessage(String messageText, String token) {

        Message message = Message.builder()
                    .setToken(token)
                    .setAndroidConfig(
                            AndroidConfig.builder()
                                    .setTtl(60*60*24*7 * 1000)
                                    .setNotification(AndroidNotification.builder()
                                            .setPriority(AndroidNotification.Priority.HIGH)
                                            .setTitle("")
                                            .setBody(messageText)
                                            .setChannelId(FirebaseChannel)
                                            .setClickAction(FirebaseClickAction)
                                            .setSound(FirebaseSound)
                                            .build())
                                    .build()
                    )
                    .build();

        return message;

    }

    public void sendMessageBatch(String messageText, List<String> tokens) {

        LinkedList<Message> messages = new LinkedList();

        for (String token : tokens) {
            Message message = buildFirebaseMessage(messageText, token);
            messages.add(message);
        }

        ApiFuture<BatchResponse> batchResponseApiFuture = FirebaseMessaging.getInstance().sendAllAsync(messages, FirebaseEnableDryRun);
        totalSentNotifications += messages.size();

        try {
            BatchResponse batchResponse = batchResponseApiFuture.get();

            int success = batchResponse.getSuccessCount();
            int fail = batchResponse.getFailureCount();

            successCount += success;
            failCount += fail;

            GssLogger.info("[FirebaseManager] Batch Msg Send Completed. Success: " + success + " Fail: " + fail);
        }
        catch (InterruptedException e) {
            Logger.getLogger(FirebaseMessagingManager.class.getName()).log(Level.SEVERE, null, e);
            errorCount++;
        }
        catch (ExecutionException e) {
            Logger.getLogger(FirebaseMessagingManager.class.getName()).log(Level.SEVERE, null, e);
            errorCount++;
        }

    }

    public void printReport() {

        GssLogger.info("[REPORT] TotalSentNotifications: " + totalSentNotifications);
        GssLogger.info("[REPORT] Success: " + successCount);
        GssLogger.info("[REPORT] Failed: " + failCount);
        GssLogger.info("[REPORT] Error: " + errorCount);

    }

}
