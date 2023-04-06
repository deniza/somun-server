package gss.network;

import java.util.*;
import java.net.*;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class GssIoHandler extends IoHandlerAdapter {
    
    private ConcurrentHashMap<Long,IoSession> sessions = new ConcurrentHashMap<Long,IoSession>();
    private ConcurrentHashMap<IoSession,GssConnection> connections = new ConcurrentHashMap<IoSession,GssConnection>();
    
    private GssConnection firstConnection;

    private final Collection<GssInterface> interfaces;
    
    public GssIoHandler(Collection<GssInterface> interfaces){
        this.interfaces = interfaces;
    }

    public GssConnection getConnection(IoSession session) {
        return connections.get(session);
    }

    public GssConnection getFirstConnection() {
        return firstConnection;
    }

    @Override
    public void exceptionCaught( IoSession session, Throwable cause ) {
        
        System.out.println("[HandlerException] caught! at session: "+session.getId()+" at "+(new Date())+"\n"+ cause.toString());
        if(!cause.toString().equals("java.io.IOException: Connection reset by peer") &&
                !cause.toString().equals("java.io.IOException: Connection timed out") &&
                !cause.toString().equals("java.io.IOException: No route to host") &&
                !cause.toString().equals("java.io.IOException: Broken pipe")) {
            
            cause.printStackTrace(System.out);
        }

    }
    
    @Override
    //NioProcessor threads icinde cagirilir
    public void sessionCreated(IoSession session) {
        
        String peerIp;
        int peerPort;
        
        SocketAddress remoteAddress = session.getRemoteAddress();
        
        if (remoteAddress instanceof InetSocketAddress) {
            InetSocketAddress addr = (InetSocketAddress) remoteAddress; 
            peerIp = addr.getAddress().getHostAddress();
            peerPort = addr.getPort();
        }
        else {
            log("sessionOpened: peerIp not defined!");
            peerIp = "not-defined";
            peerPort = 0;
        }
        
        GssConnection cb = new GssConnection(interfaces);
        cb.setSession(session);
        cb.setPeerIp(peerIp);
        cb.setPeerPort(peerPort);
        
        session.setAttribute("connectionbase", cb);
        connections.put(session, cb);

        if (firstConnection == null) {
            firstConnection = cb;
        }
        
    }

    @Override
    //Nio worker threads icinde cagirilir
    public synchronized void sessionOpened(IoSession session) {
        
        GssConnection cb = (GssConnection) session.getAttribute("connectionbase");        
        
        session.setAttribute("ip",cb.getPeerIp());
        session.setAttribute("active",true);
        sessions.put(session.getId(), session);
        
        cb.start();                
        
    }
    
    @Override
    public void sessionClosed(IoSession session){

        sessions.remove(session.getId());
        
        getConnection(session).end();
        
        connections.remove(session);                
        
    }
            
    // called by our protocol codec after decoding client message
    @Override
    public void messageReceived( IoSession session, Object message ) {
           
        if (message != null) {
                                
            GssMethod mc = (GssMethod) message;
            
            mc.invoke();
        
        }
        else {
            log("ERROR: Handler.messageReceived message is null! possibly data decoding error occured. Check exceptions.");
        }
        
    }

    // Messages sent asynchronously. This method is called after a message is sent through socket pipe. 
    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        // Empty handler
    }
    
    private void log(String message) {

        StringBuilder output = new StringBuilder();                
        output.append(new Date()).append(" : ").append( message );
        System.out.println(output);        
        
    }
    
}