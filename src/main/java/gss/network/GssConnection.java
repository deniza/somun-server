package gss.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.apache.mina.core.session.IoSession;

public class GssConnection {

    private IoSession session;
    private String peerIp = "";
    private int peerPort;

    private HashMap<String, GssModule> modules = new HashMap<String, GssModule>();
    private HashMap<String, Object> attributes = new HashMap<String, Object>();

    protected GssConnection(Collection<GssModule> moduleList){
        for(GssModule m:moduleList){            
            modules.put(m.getClass().getSimpleName(), m);            
        }
    }
    
    public void setSession(IoSession ses){
        session = ses;
    }
    
    public long getSessionId() {
        return session.getId();
    }
    
    public boolean isConnected() {
        return session.isConnected();
    }

    public void setPeerIp(String ip) {
        this.peerIp = ip;
    }
    
    public void setPeerPort(int port) {
        this.peerPort = port;
    }
    
    public String getPeerIp() {
        return peerIp;
    }
    
    public int getPeerPort() {
        return peerPort;
    }
    
    public void invokeMethod( String methodName, Object[] params){
        GssMethod mc = new GssMethod(methodName, params);        
        session.write(mc);                
    }

    public static void broadCastMethod(ArrayList<GssConnection> cbs, String methodName, Object[] params){
        for(GssConnection cb: cbs){
            cb.invokeMethod(methodName, params);
        }
    }

    public void start(){
        for(GssModule m:modules.values()){
            m.clientConnected(this);
        }
    }
    
    public void end(){
        for(GssModule m:modules.values()){
            m.clientDisconnected(this);
        }
    }

    public void addModule(GssModule m){
        modules.put(m.getClass().getSimpleName(), m);
    }

    GssModule getModule(String moduleName) {
        return modules.get(moduleName);
    }

    public void setAttribute(String key, Object value){
        attributes.put(key, value);
    }

    public Object getAttribute(String key){
        return attributes.get(key);
    }
    
    public void closeConnection(){
        session.setAttribute("active", false);
        session.closeOnFlush();
    }

}
