package gss;

import gss.client.SimpleClient;

public class ClientMain {

    public static void main(String args[]) {

        GssLogger.info("Running GSS sample client");

        SimpleClient client = new SimpleClient(0, "deniz", "clpfwg0nc8n5nsfa");
        client.start();

    }

}
