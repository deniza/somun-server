package gss.server.manager.storage;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
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
    private final String ConfigCollection = "config";

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
    public int getAndIncrementNextAvailableGameId() {

        return getAndIncrementConfigValue("nextGameId");

    }
    @Override
    public int getAndIncrementNextAvailablePlayerId() {

        return getAndIncrementConfigValue("nextPlayerId");

    }

    @Override
    public void storePlayer(Player player) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        Document playerDoc = new Document()
            .append("playerId", player.getPlayerId())
            .append("name", player.getName())
            .append("password", player.getPassword())
            .append("games", player.getGameIds());

        collection.findOneAndUpdate(
                Filters.eq("playerId", 1),
                new Document("$set", playerDoc),
                new FindOneAndUpdateOptions().upsert(true)
        );

    }

    @Override
    public Player loadPlayer(int playerId) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        Document doc = collection.find(Filters.eq("playerId", playerId)).first();
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

        Document gameDoc = new Document()
                .append("gameId", session.getGameId())
                .append("turnowner", session.getTurnOwner().getPlayerId())
                .append("players", session.getPlayerIds())
                .append("state", session.getState().getDataMap());

        collection.findOneAndUpdate(
                Filters.eq("gameId", session.getGameId()),
                new Document("$set", gameDoc),
                new FindOneAndUpdateOptions().upsert(true)
        );

    }

    @Override
    public GameSession loadGameSession(int gameId) {

        MongoCollection<Document> collection = database.getCollection(GamesCollection);

        Document doc = collection.find(Filters.eq("gameId", gameId)).first();
        final Integer turnOwnerId = doc.getInteger("turnowner");
        final List<Integer> playerIds = doc.getList("players", Integer.class);
        final HashMap<String, Object> stateMap = (HashMap<String, Object>) doc.get("state");

        GameState state = new GameState();
        state.deserialize(stateMap);

        GameSession session = new GameSession();
        session.deserialize(gameId, turnOwnerId, new ArrayList<>(playerIds), state);

        return session;
    }

    private synchronized int getAndIncrementConfigValue(String configKey) {

        MongoCollection<Document> config = database.getCollection(ConfigCollection);

        Document doc = config.findOneAndUpdate(
                Filters.empty(),
                new Document("$inc", new Document(configKey, 1)),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.BEFORE)
        );

        return doc.getInteger(configKey);

    }


}
