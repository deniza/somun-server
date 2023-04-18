package gss;

import gss.client.SimpleClient;

public class ClientMain {

    public static void main(String args[]) {

        GssLogger.info("Running GSS sample client");

        SimpleClient client = new SimpleClient(0, "deniz", "5hoe3ea5zdw0lcd0");
        client.start();

    }

}
