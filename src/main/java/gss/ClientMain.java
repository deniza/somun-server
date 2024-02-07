package gss;

import gss.client.SimpleClient;

public class ClientMain {

    public static void main(String args[]) {

        GssLogger.info("Running GSS sample client");

        SimpleClient client = new SimpleClient(14, "guest_14", "7v3phcukpp8xs2ua");
        client.start();

    }

}
