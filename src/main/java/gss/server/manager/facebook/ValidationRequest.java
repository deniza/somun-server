/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gss.server.manager.facebook;

import gss.network.GssConnection;
import gss.server.util.Time;

/**
 *
 * @author deniz
 */
public class ValidationRequest {

    public static final int RESULT_VALID = 0;
    public static final int RESULT_NOTVALID = 1;
    private GssConnection con;
    private int pid;
    private String fbuid;
    private String name;
    private String fullName;
    private String accessToken;
    private int result;
    private long creationTime;
    
    public ValidationRequest(int pid, String accessToken, GssConnection con) {
        this.pid = pid;
        this.accessToken = accessToken;
        this.con = con;
        this.creationTime = Time.now();
    }
    
    public int getPid() {
        return pid;
    }
    
    public String getFbuid() {
        return fbuid;
    }
    
    public String getAccessToken() {
        return accessToken;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }    
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getFullName() {
        return fullName;
    }    
    
    public void setFbuid(String fbuid) {
        this.fbuid = fbuid;                
    }
    
    public void setResult(int result) {
        this.result = result;
    }
    
    public boolean isResultValid() {
        return result == RESULT_VALID;
    }
    
    public GssConnection getConnectionBase() {
        return con;
    }
    
    public boolean isConnectionStillActive() {
        return con.getAttribute("active").equals(true);
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(pid).append(" ").append(fbuid).append(" ").append(name).append(" ").append(result);
        
        return sb.toString();
        
    }
    
}
