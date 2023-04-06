package gss.network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.mina.core.future.IoFuture;
import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

public class GssConnection {

    private IoSession session;
    private String peerIp = "";
    private int peerPort;

    private HashMap<String, GssInterface> interfaces = new HashMap<String, GssInterface>();
    private HashMap<String, Object> attributes = new HashMap<String, Object>();

    protected GssConnection(Collection<GssInterface> interfaceList){
        for(GssInterface m : interfaceList){            
            interfaces.put(m.getClass().getSimpleName(), m);            
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
    
    public void invokeMethod(String methodName, Object[] params){
        GssMethod mc = new GssMethod(methodName, params);        
        session.write(mc);
    }

    public void invokeMethod(String methodName, Object[] params, GssEventListener listener){
        
        GssMethod mc = new GssMethod(methodName, params);        
        
        WriteFuture future = session.write(mc);
        future.addListener(new IoFutureListener<IoFuture>() {
            @Override
            public void operationComplete(IoFuture future) {
                listener.messageSent();
            }            
        });

        //future.awaitUninterruptibly();

    }

    public static void broadCastMethod(ArrayList<GssConnection> cbs, String methodName, Object[] params){
        for(GssConnection cb: cbs){
            cb.invokeMethod(methodName, params);
        }
    }

    public void start(){
        for(GssInterface m : interfaces.values()){
            m.clientConnected(this);
        }
    }
    
    public void end(){
        for(GssInterface m : interfaces.values()){
            m.clientDisconnected(this);
        }
    }

    public void addInterface(GssInterface m){
        interfaces.put(m.getClass().getSimpleName(), m);
    }

    GssInterface getInterface(String interfaceName) {
        return interfaces.get(interfaceName);
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
