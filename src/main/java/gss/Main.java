package gss;

import gss.network.Gss;
import gss.network.GssConfig;
import gss.network.GssConnection;

/**
 *
 * @author deniz
 */
public class Main {
    
    public static void main(String args[]) {
        
        GssLogger.info("Running GSS test driver");
        
        GssConfig config = new GssConfig();
        config.setLocalPort(16666);
        config.addInterface(new DefaultServerInterface());
        
        Gss.startServer(config);

        //test();
        
    }

    public static void test() {
        
        GssConfig config = new GssConfig();
        config.setRemotePort(16666);
        config.addInterface(new DefaultClientInterface());
        
        GssConnection con = Gss.startClient(config);
        con.invokeMethod(
            "DefaultServerInterface_serverTestFunction",
            new Object[]{"hello world!"}
        );

        Gss.shutdownClient();

        //System.exit(0);
    }
    
}
