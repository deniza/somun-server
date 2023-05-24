package gss.server.manager.storage;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import gss.GssLogger;
import gss.server.model.GameSession;
import gss.server.model.GameState;
import gss.server.model.Player;
import gss.server.util.Config;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StorageManagerMongo implements StorageInterface {

    private final String MongoUri = Config.get("mongo_uri");
    private final String MongoDatabase = Config.get("mongo_database");
    private final String PlayersCollection = "players";
    private final String GamesCollection = "games";

    MongoClient client;
    MongoDatabase database;

    @Override
    public void initialize() {

        try {
            client = MongoClients.create(MongoUri);
            database = client.getDatabase(MongoDatabase);
        }
        catch (Exception e) {
            return;
        }

        GssLogger.info("Connected to mongodb instance");

    }

    @Override
    public void shutdown() {

        client.close();

    }

    @Override
    public void storePlayer(Player player) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        Document playerDoc = new Document();
        playerDoc.put("playerId", player.getPlayerId());
        playerDoc.put("name", player.getName());
        playerDoc.put("password", player.getPassword());
        playerDoc.put("games", player.getGameIds());

        Document queryDocument = new Document("playerId", player.getPlayerId());
        Document updateDocument = new Document("$set", playerDoc);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().upsert(true);

        collection.findOneAndUpdate(queryDocument, updateDocument, options);

    }

    @Override
    public Player loadPlayer(int playerId) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        Document doc = collection.find(new Document("playerId", playerId)).first();
        final String name = doc.getString("name");
        final String password = doc.getString("password");
        final List<Integer> gameIds = doc.getList("games", Integer.class);

        Player player = new Player(playerId);
        player.setName(name);
        player.setPassword(password);
        player.setGameIds(new ArrayList(gameIds));

        return player;
    }

    @Override
    public void storeGameSession(GameSession session) {

        MongoCollection<Document> collection = database.getCollection(GamesCollection);

        Document gameDoc = new Document();
        gameDoc.put("gameId", session.getGameId());
        gameDoc.put("turnowner", session.getTurnOwner().getPlayerId());
        gameDoc.put("players", session.getPlayerIds());
        gameDoc.put("state", session.getState().getDataMap());

        Document queryDocument = new Document("gameId", session.getGameId());
        Document updateDocument = new Document("$set", gameDoc);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().upsert(true);

        collection.findOneAndUpdate(queryDocument, updateDocument, options);

    }

    @Override
    public GameSession loadGameSession(int gameId) {

        MongoCollection<Document> collection = database.getCollection(GamesCollection);

        Document doc = collection.find(new Document("gameId", gameId)).first();
        final Integer turnOwnerId = doc.getInteger("turnowner");
        final List<Integer> playerIds = doc.getList("players", Integer.class);
        final HashMap<String, Object> stateMap = (HashMap<String, Object>) doc.get("state");

        GameState state = new GameState();
        state.deserialize(stateMap);

        GameSession session = new GameSession();
        session.deserialize(gameId, turnOwnerId, new ArrayList<>(playerIds), state);

        return session;
    }

}
