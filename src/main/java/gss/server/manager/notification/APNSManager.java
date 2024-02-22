/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gss.server.manager.notification;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.eatthepath.pushy.apns.util.concurrent.PushNotificationFuture;
import gss.GssLogger;
import gss.server.util.Config;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class APNSManager {

    private final String ApnsDefaultTopic = Config.get("notification_apns_topic");
    private final String ApnsSound = Config.get("notification_apns_sound");
    private ApnsClient apnsClient;

    private int totalSentNotifications = 0;
    private int successCount = 0;
    private int failCount = 0;
    private int errorCount = 0;
    
    public APNSManager() {

        initApns();

    }

    private void initApns() {

        try {

            apnsClient = new ApnsClientBuilder()
                    .setApnsServer(Config.getBoolean("notification_apns_enable_sandbox") ? ApnsClientBuilder.DEVELOPMENT_APNS_HOST : ApnsClientBuilder.PRODUCTION_APNS_HOST)
                    .setClientCredentials(new File(Config.get("notification_apns_credentials")), Config.get("notification_apns_credentials_password"))
                    .build();

        }
        catch (Exception ex) {
            Logger.getLogger(APNSManager.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }

    }

    public void shutdown() {

        final CompletableFuture<Void> closeFuture = apnsClient.close();
        try {
            closeFuture.get(10, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            Logger.getLogger(APNSManager.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (ExecutionException e) {
            Logger.getLogger(APNSManager.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (TimeoutException e) {
            Logger.getLogger(APNSManager.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public void printReport() {

        GssLogger.info("[REPORT] TotalSentNotifications: " + totalSentNotifications);
        GssLogger.info("[REPORT] Success: " + successCount);
        GssLogger.info("[REPORT] Failed: " + failCount);
        GssLogger.info("[REPORT] Error: " + errorCount);

    }
    public void sendMessage(String apnsToken, String messageText) {

        ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();
        payloadBuilder.setAlertBody(messageText);
        payloadBuilder.setSound(ApnsSound);

        final String payload = payloadBuilder.build();
        final String token = TokenUtil.sanitizeTokenString(apnsToken);

        SimpleApnsPushNotification pushNotification = new SimpleApnsPushNotification(token, ApnsDefaultTopic, payload);

        final PushNotificationFuture<SimpleApnsPushNotification, PushNotificationResponse<SimpleApnsPushNotification>>
                sendNotificationFuture = apnsClient.sendNotification(pushNotification);

        totalSentNotifications++;

        sendNotificationFuture.whenComplete((response, cause) -> {
            if (response != null) {

                if (response.isAccepted()) {
                    GssLogger.info("Push notification accepted by APNs gateway.");
                    successCount++;
                }
                else {
                    GssLogger.error("Notification rejected by the APNs gateway: " + response.getRejectionReason());

                    response.getTokenInvalidationTimestamp().ifPresent(timestamp -> {
                        GssLogger.error("\t…and the token is invalid as of " + timestamp);
                    });

                    failCount++;
                }

            } else {
                // Something went wrong when trying to send the notification to the
                // APNs server. Note that this is distinct from a rejection from
                // the server, and indicates that something went wrong when actually
                // sending the notification or waiting for a reply.
                cause.printStackTrace();

                errorCount++;
            }
        });

    }
  
}