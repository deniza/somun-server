package gss.network;

import java.util.ArrayList;

/**
 *
 * @author deniz
 */
public class GssConfig {
    
    private final String DefaultLocalIp = "127.0.0.1";
    private final String DefaultRemoteIp = "127.0.0.1";
    private final int DefaultLocalPort = 27888;
    private final int DefaultRemotePort = 27889;
    
    private int threadsCount;
    private String localIp;
    private int localPort;
    private String remoteIp;
    private int remotePort;
    private final ArrayList<GssInterface> interfaces = new ArrayList<GssInterface>();
    
    public GssConfig() {
        
        applyDefaults();
        
    }
    
    public void setThreadsCount(int count) {
        this.threadsCount = count;
    }
    
    public int getThreadsCount() {
        return threadsCount;
    }
    
    public void setLocalIp(String ip) {
        this.localIp = ip;
    }
    
    public String getLocalIp() {
        return localIp;
    }
    
    public void setRemoteIp(String ip) {
        this.remoteIp = ip;
    }
    
    public String getRemoteIp() {
        return remoteIp;
    }
    
    public void setLocalPort(int port) {
        this.localPort = port;
    }
    
    public int getLocalPort() {
        return localPort;
    }
    
    public void setRemotePort(int port) {
        this.remotePort = port;
    }
    
    public int getRemotePort() {
        return remotePort;
    }
    
    public void addInterface(GssInterface module) {
        interfaces.add(module);
    }
    
    public ArrayList<GssInterface> getMoules() {
        return interfaces;
    }
    
    private void applyDefaults() {
        
        final int processorCount = Runtime.getRuntime().availableProcessors();
        
        setThreadsCount(processorCount);
        setLocalIp(DefaultLocalIp);
        setRemoteIp(DefaultRemoteIp);
        setLocalPort(DefaultLocalPort);
        setRemotePort(DefaultRemotePort);
        
    }
    
}
