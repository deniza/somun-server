package gss.server.manager;

import gss.GssLogger;
import gss.server.util.Config;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MailManager {

    private final boolean MailEnabled = Config.getBoolean("mail_enabled");
    private final int WorkerThreadCount = Config.getInt("mail_threads");
    private final String mailHost = Config.get("mail_host");
    private final String fromAddr = Config.get("mail_from_addr");

    private Session session;
    private ExecutorService execService;

    private static volatile MailManager instance;

    private MailManager() {

        // Prevent form the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use get() method to get the single instance of this class.");
        }

        if (MailEnabled == false) {
            return;
        }

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", mailHost);

        // Get the default Session object.
        session = Session.getDefaultInstance(properties);

        execService = Executors.newFixedThreadPool(WorkerThreadCount);

    }

    public static MailManager get() {
        // Double check locking pattern
        if (instance == null) {
            synchronized (MailManager.class) {
                if (instance == null) {
                    instance = new MailManager();
                }
            }
        }
        return instance;
    }

    public void sendMessage(String toAddr, String subject, String message) {

        if (MailEnabled == false) {
            return;
        }

        execService.execute(new SendMailWorker(session, fromAddr, toAddr, subject, message));

    }

    private static class SendMailWorker implements Runnable {

        private final String fromAddr;
        private final String toAddr;
        private final String subject;
        private final String message;
        private final Session session;

        public SendMailWorker(Session session, String fromAddr, String toAddr, String subject, String message) {
            this.session = session;
            this.fromAddr = fromAddr;
            this.toAddr = toAddr;
            this.subject = subject;
            this.message = message;
        }

        private boolean sendMessage(String toAddr, String subject, String message) {

            try {
                // Create a default MimeMessage object.
                MimeMessage msg = new MimeMessage(session);

                // Set From: header field of the header.
                msg.setFrom(new InternetAddress(fromAddr));

                // Set To: header field of the header.
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(toAddr));

                // Set Subject: header field
                msg.setSubject(subject, "UTF-8");

                // Now set the actual message
                msg.setText(message, "UTF-8");

                // Send message
                Transport.send(msg);

                GssLogger.info("mail sent to " + toAddr);
            }
            catch (AddressException aex) {
                GssLogger.error("error sending mail to " + toAddr + " " + aex.toString());
                return false;
            }
            catch (MessagingException mex) {
                GssLogger.error("error sending mail to " + toAddr + " " + mex.toString());
                return false;
            }

            return true;

        }

        @Override
        public void run() {
            sendMessage(toAddr, subject, message);
        }

    }


}