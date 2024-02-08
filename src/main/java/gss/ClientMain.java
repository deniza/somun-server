package gss;

import gss.client.SimpleClient;

public class ClientMain {

    public static void main(String args[]) {

        GssLogger.info("Running GSS sample client");

        int pid = 1;
        String password = "qwerty";

        if (args.length > 1) {
            pid = Integer.parseInt(args[0]);
            password = args[1];
        }

        SimpleClient client = new SimpleClient(pid, "guest_" + pid, password);
        //SimpleClient client = new SimpleClient(2, "guest_2", "qwerty");
        client.start();

    }

}
