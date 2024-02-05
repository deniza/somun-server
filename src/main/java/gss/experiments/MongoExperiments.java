package gss.experiments;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import gss.GssLogger;
import gss.server.model.Player;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MongoExperiments {

    public static void run() {

        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017/")) {

            //runExperiment1(mongoClient.getDatabase("somunDB"));
            runExperiment2(mongoClient.getDatabase("somunDB"));

        }

    }

    private static void runExperiment1(MongoDatabase database) {

        MongoCollection collection = database.getCollection("players");

        Document playerDoc = new Document()
                .append("playerId", 1)
                .append("name", "deniz")
                .append("password", "qwerty")
                .append("games", Arrays.asList(0,1,2,3,4,5,6));

        Document updateDoc = new Document()
                .append("$set", playerDoc);

        collection.findOneAndUpdate(
                Filters.eq("playerId", 1),
                new Document("$set", playerDoc),
                new FindOneAndUpdateOptions().upsert(true)
        );

        GssLogger.info("experiment done");

    }

    private static void runExperiment2(MongoDatabase database) {

        MongoCollection<Document> collection = database.getCollection("players");

        final int playerId = 1;

        Document doc = collection.find(Filters.eq("playerId", playerId)).first();

        final String name = doc.getString("name");
        final String password = doc.getString("password");
        final List<Integer> gameIds = doc.getList("games", Integer.class);

        Player player = new Player(playerId);
        player.setName(name);
        player.setPassword(password);
        player.setGameIds(new ArrayList(gameIds));

        GssLogger.info("experiment done");

    }

}
