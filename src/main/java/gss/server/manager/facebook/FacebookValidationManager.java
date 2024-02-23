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
import gss.server.model.ServiceUpdateInterface;
import gss.server.util.Config;
import gss.server.util.Time;

/**
 *
 * @author deniz
 */
public class FacebookValidationManager implements ServiceUpdateInterface {

    private static FacebookValidationManager instance;

    private static final int workerThreadCount = Config.getInt("facebook_thread_count");
    
    private final LinkedList<ValidationRequest> validationRequests = new LinkedList<ValidationRequest>();
    private ExecutorService execService;    
    
    private FacebookValidationManager() {
        
        execService = Executors.newFixedThreadPool(workerThreadCount);
        
    }

    public static FacebookValidationManager get() {
        if (instance == null) {
            instance = new FacebookValidationManager();
        }
        return instance;
    }
        
    public synchronized void addValidationRequest(String accessToken, GssConnection con) {
        
        ValidationRequest vData = new ValidationRequest(0, accessToken, con);
        execService.execute(new ValidationWorker(vData));
        
    }
    
    public ValidationRequest getValidationResponse() {        
        synchronized (validationRequests) {
            return validationRequests.poll();
        }
    }
    
    public int getValidationQueueLength() {
        return validationRequests.size();
    }

    private synchronized void processValidationRequests() {

        ValidationRequest vData = getValidationResponse();
        if (vData != null) {

            if (vData.isConnectionStillActive() == false) {
                GssLogger.info("FacebookValidationManager: client disconnected before facebook response");
            }
            else {

                if (vData.isResultValid()) {
                    GssLogger.info("FacebookValidationManager: validation success - " + vData.toString());
                    vData.getConnection().invokeMethod("Auth_facebookLoginResponse", new Object[] {1, vData.getPid(), vData.getFbuid(), vData.getName(), vData.getFullName()});
                }
                else {
                    GssLogger.info("FacebookValidationManager: validation failed - " + vData.toString());
                    vData.getConnection().invokeMethod("Auth_facebookLoginResponse", new Object[] {0, vData.getPid(), "", "", ""});
                }

            }

        }

    }

    @Override
    public void updateService(long deltaTime) {

        processValidationRequests();

    }

    private class ValidationWorker implements Runnable {
        
        private ValidationRequest vData;
        
        public ValidationWorker(ValidationRequest vData) {
            this.vData = vData;
        }
        
        @Override
        public void run() {

            if (vData.isConnectionStillActive() == false) {
                
                GssLogger.info("FacebookValidationManager: client disconnected before facebook request");
                return;
                
            }
            
            long cTime = vData.getCreationTime();
            long t1 = Time.now();
            
            GssLogger.info("FacebookValidationManager: request will be executed - wait time = " + (t1 - cTime));
            
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
                    vData.setResult(ValidationRequest.RESULT_VALID);
                }
                else {
                    vData.setResult(ValidationRequest.RESULT_NOTVALID);
                }
                
                if (vData.getFbuid().equals("")) {
                    vData.setResult(ValidationRequest.RESULT_NOTVALID);
                }                    
                                
            }
            catch (FacebookException ex) {

                GssLogger.error("FacebookValidationManager: facebook exception: " + ex.getMessage());
                
                vData.setResult(ValidationRequest.RESULT_NOTVALID);
                
            }
            
            synchronized (validationRequests) {
                validationRequests.add(vData);
            }
            
            GssLogger.info("FacebookValidationManager: request executed - process time = " + (Time.now() - t1));
            GssLogger.info("FacebookValidationManager: " + vData.toString());
        }
        
    }
        
    
}