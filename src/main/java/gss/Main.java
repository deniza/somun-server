package gss;

import gss.client.SimpleClient;
import gss.server.Somun;
import gss.server.samplegame.SampleGameHandler;

/**
 *
 * @author deniz
 */
public class Main {
    
    public static void main(String args[]) {
        
        GssLogger.info("Running GSS server");

        Somun server = new Somun();
        server.start(new SampleGameHandler());

    }
    
}
