package gss;

import gss.server.Somun;

/**
 *
 * @author deniz
 */
public class Main {
    
    public static void main(String args[]) {
        
        GssLogger.info("Running GSS test driver");

        Somun server = new Somun();
        server.start();

        SimpleClient client = new SimpleClient(0, "deniz", "8uh32etn8b8zn7de");
        client.start();

    }
    
}
