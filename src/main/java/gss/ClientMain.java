package gss;

import gss.client.SimpleClient;

public class ClientMain {

    public static void main(String args[]) {

        GssLogger.info("Running GSS sample client");

        SimpleClient client = new SimpleClient(1, "guest_1", "99ucis8fexowmlsq");
        //SimpleClient client = new SimpleClient(2, "guest_2", "x1oschdtwgjegy8v");
        client.start();

    }

}
