package gss;

import gss.network.Gss;
import gss.network.GssConfig;

/**
 *
 * @author deniz
 */
public class Main {
    
    public static void main(String args[]) {
        
        GssLogger.info("Running GSS test driver");
        
        GssConfig config = new GssConfig();
        config.setLocalPort(16666);
        config.addInterface(new DefaultInterface());
        
        Gss.startServer(config);       
        
        GssLogger.info("done");
        
    }
    
}
