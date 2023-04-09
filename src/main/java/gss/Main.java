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
        
        GssLogger.info("Running GSS test driver");

        Somun server = new Somun();
        server.start(new SampleGameHandler());

        SimpleClient client = new SimpleClient(0, "deniz", "5hoe3ea5zdw0lcd0");
        client.start();

    }
    
}
