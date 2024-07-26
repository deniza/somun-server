/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gss.server.manager.facebook;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.json.JsonObject;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.server.manager.GameManager;
import gss.server.manager.PlayerManager;
import gss.server.manager.storage.StorageInterface;
import gss.server.manager.storage.StorageManager;
import gss.server.model.Player;
import gss.server.model.ServiceUpdateInterface;
import gss.server.util.Config;
import gss.server.util.PasswordGenerator;
import gss.server.util.Time;

/**
 *
 * @author deniz
 */
public class FacebookAuthenticationManager implements ServiceUpdateInterface {

    private static FacebookAuthenticationManager instance;

    private static final int workerThreadCount = Config.getInt("facebook_thread_count");
    
    private final LinkedList<FacebookAuthRequest> authRequests = new LinkedList<FacebookAuthRequest>();
    private ExecutorService execService;    
    
    private FacebookAuthenticationManager() {
        
        execService = Executors.newFixedThreadPool(workerThreadCount);
        
    }

    public static FacebookAuthenticationManager get() {
        if (instance == null) {
            instance = new FacebookAuthenticationManager();
        }
        return instance;
    }
        
    public synchronized void addAuthRequest(String accessToken, GssConnection con) {
        
        FacebookAuthRequest vData = new FacebookAuthRequest(0, accessToken, con);
        execService.execute(new ValidationWorker(vData));
        
    }
    
    public FacebookAuthRequest getAuthResponse() {
        synchronized (authRequests) {
            return authRequests.poll();
        }
    }
    
    private synchronized void processAuthRequests() {

        FacebookAuthRequest vData = getAuthResponse();
        if (vData != null) {

            if (vData.isConnectionStillActive() == false) {
                GssLogger.info("FacebookAuthenticationManager: client disconnected before facebook response");
            }
            else {

                if (vData.isResultValid()) {
                    GssLogger.info("FacebookAuthenticationManager: validation success - " + vData.toString());

                    Player player = StorageManager.get().loadPlayerByFbuid(vData.getFbuid());
                    if (player != null) {

                        player.setOnline(true);

                        PlayerManager.get().addPlayer(player);
                        PlayerManager.get().incrementOnlineCount();

                        GameManager.get().loadPlayerGameSessions(player);

                        vData.getConnection().invokeMethod("Auth_facebookLoginResponse", new Object[] {1, vData.getPid(), vData.getFbuid(), vData.getName(), vData.getFullName()});

                    }
                    else {

                        // player account not found, create a new one
                        player = new Player();
                        player.setPlayerId(vData.getPid());
                        player.setFbuid(vData.getFbuid());
                        player.setName(vData.getName() + "_" + Math.min(100, 10000));
                        player.setPassword(PasswordGenerator.generatePassword(16));

                        StorageInterface.CreatePlayerResult result = StorageManager.get().createPlayer(player);
                        if (result == StorageInterface.CreatePlayerResult.SUCCESS) {

                            PlayerManager.get().addPlayer(player);
                            PlayerManager.get().incrementOnlineCount();

                            player.setOnline(true);

                            vData.getConnection().invokeMethod("Account_createAccountAccepted", new Object[] {player.getPlayerId(), player.getName(), player.getPassword()});

                        }
                        else if (result == StorageInterface.CreatePlayerResult.USERNAME_ALREADY_EXISTS) {

                            vData.getConnection().invokeMethod("Account_createAccountRejected", new Object[] {"Username already exists"});

                        }
                        else if (result == StorageInterface.CreatePlayerResult.ERROR) {

                            vData.getConnection().invokeMethod("Account_createAccountRejected", new Object[] {"Error creating account"});

                        }

                    }

                }
                else {
                    GssLogger.info("FacebookAuthenticationManager: validation failed - " + vData.toString());
                    vData.getConnection().invokeMethod("Auth_facebookLoginResponse", new Object[] {0, vData.getPid(), "", "", ""});
                }

            }

        }

    }

    @Override
    public void updateService(long deltaTime) {

        processAuthRequests();

    }

    private class ValidationWorker implements Runnable {
        
        private FacebookAuthRequest vData;
        
        public ValidationWorker(FacebookAuthRequest vData) {
            this.vData = vData;
        }
        
        @Override
        public void run() {

            if (vData.isConnectionStillActive() == false) {
                
                GssLogger.info("FacebookAuthenticationManager: client disconnected before facebook request");
                return;
                
            }
            
            long cTime = vData.getCreationTime();
            long t1 = Time.now();
            
            GssLogger.info("FacebookAuthenticationManager: request will be executed - wait time = " + (t1 - cTime));
            
            try {
            
                FacebookClient facebookClient = new DefaultFacebookClient(vData.getAccessToken(), Version.LATEST);               
                JsonObject me = facebookClient.fetchObject("me", JsonObject.class, Parameter.with("fields","name,id,first_name,last_name"));
                
                if (me != null) {
                    
                    String fbuidOfMe = me.getString("id","");
                    String firstName = me.getString("first_name", "").trim();
                    String lastName = me.getString("last_name", "").trim();
                    String fullName = me.getString("name", "").trim();
                                
                    int firstNameDelimiter = firstName.indexOf(" ");
                    if (firstNameDelimiter != -1) {
                        firstName = firstName.substring(0, firstNameDelimiter);
                    }

                    String name = firstName + lastName.substring(0, Math.min(1,lastName.length()));

                    vData.setFbuid(fbuidOfMe);
                    vData.setName(name);
                    vData.setFullName(fullName);
                    vData.setResult(FacebookAuthRequest.RESULT_VALID);
                }
                else {
                    vData.setResult(FacebookAuthRequest.RESULT_NOTVALID);
                }
                
                if (vData.getFbuid().equals("")) {
                    vData.setResult(FacebookAuthRequest.RESULT_NOTVALID);
                }                    
                                
            }
            catch (FacebookException ex) {

                GssLogger.error("FacebookAuthenticationManager: facebook exception: " + ex.getMessage());
                
                vData.setResult(FacebookAuthRequest.RESULT_NOTVALID);
                
            }
            
            synchronized (authRequests) {
                authRequests.add(vData);
            }
            
            GssLogger.info("FacebookAuthenticationManager: request executed - process time = " + (Time.now() - t1));
            GssLogger.info("FacebookAuthenticationManager: " + vData.toString());
        }
        
    }
        
    
}
