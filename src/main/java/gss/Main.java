package gss;

import gss.experiments.MongoExperiments;
import gss.server.Somun;
import gss.server.manager.storage.StorageManager;
import gss.server.samplegame.SampleGameHandler;

/**
 *
 * @author deniz
 */
public class Main {
    
    public static void main(String args[]) {

        //MongoExperiments.run();

        if (args.length > 0 && args[0].equals("--setup")) {
            setup();
            return;
        }

        GssLogger.info("Running GSS server");

        Somun server = new Somun();
        server.start(new SampleGameHandler());

    }

    private static void setup() {

        GssLogger.info("Running GSS server in setup mode");

        StorageManager.get().initialize();
        StorageManager.get().setup();
        StorageManager.get().shutdown();

        GssLogger.info("Done");

    }
}
