package gss.server.manager.storage;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import gss.GssLogger;
import gss.server.manager.MessageManager;
import gss.server.model.GameSession;
import gss.server.model.GameState;
import gss.server.model.Player;
import gss.server.util.Config;
import gss.server.util.JsonHelper;
import gss.server.util.Time;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.LinkedList;
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

        Document friendsDoc = new Document();
        friendsDoc.append("sent", player.getFriendRequestsSent());
        friendsDoc.append("recv", player.getFriendRequestsReceived());
        friendsDoc.append("accepted", player.getFriends());
        playerDoc.append("friends", friendsDoc);

        collection.findOneAndUpdate(
                Filters.eq("playerId", player.getPlayerId()),
                new Document("$set", playerDoc),
                new FindOneAndUpdateOptions().upsert(true)
        );

    }

    @Override
    public Player loadPlayer(int playerId) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        Document doc = collection.find(Filters.eq("playerId", playerId)).first();

        if (doc != null) {

            final String name = doc.getString("name");
            final String password = doc.getString("password");
            final List<Integer> gameIds = doc.getList("games", Integer.class);

            Player player = new Player(playerId);
            player.setName(name);
            player.setPassword(password);
            player.setGameIds(new ArrayList(gameIds));

            Document friendsDoc = (Document) doc.get("friends");
            if (friendsDoc != null) {
                final List<Integer> friends = friendsDoc.getList("accepted", Integer.class);
                final List<Integer> friendsSent = friendsDoc.getList("sent", Integer.class);
                final List<Integer> friendsRecv = friendsDoc.getList("recv", Integer.class);

                player.setFriends(friends);
                player.setFriendRequestsSent(friendsSent);
                player.setFriendRequestsReceived(friendsRecv);
            }

            final List<Document> privateMessages = doc.getList("privateMessages", Document.class);
            if (privateMessages != null) {

                List<MessageManager.PrivateMessage> pmessages = new LinkedList<>();

                for (Document message : privateMessages) {

                    int msgId = message.getInteger("msgId");
                    int sender = message.getInteger("sender");
                    long date = message.getLong("date");
                    boolean readFlag = message.getInteger("read") == 0 ? false : true;
                    String content = message.getString("content");

                    MessageManager.PrivateMessage pmsg = new MessageManager.PrivateMessage(msgId, sender, playerId, readFlag, date, content);
                    pmessages.add(pmsg);

                }

                player.setPrivateMessages(pmessages);

            }
            return player;

        }
        else {
            return null;
        }

    }

    @Override
    public void storeGameSession(GameSession session) {

        MongoCollection<Document> collection = database.getCollection(GamesCollection);

        Document gameDoc = new Document()
                .append("gameId", session.getGameId())
                .append("turnowner", session.getTurnOwner().getPlayerId())
                .append("players", session.getPlayerIds())
                .append("privstate", session.getPrivateState().getDataMap());

        if (session.getWinner() != null) {
            gameDoc.append("winner", session.getWinner().getPlayerId());
        }

        collection.findOneAndUpdate(
                Filters.eq("gameId", session.getGameId()),
                new Document("$set", gameDoc),
                new FindOneAndUpdateOptions().upsert(true)
        );

    }

    @Override
    public GameSession loadGameSession(int gameId) {

        MongoCollection<Document> collection = database.getCollection(GamesCollection);
        Document gameDoc = collection.find(Filters.eq("gameId", gameId)).first();

        return createSessionFromDocument(gameDoc);

    }

    @Override
    public ArrayList<GameSession> loadGameSessions(ArrayList<Integer> gameIds) {

        MongoCollection<Document> collection = database.getCollection(GamesCollection);
        FindIterable<Document> results = collection.find(Filters.in("gameId", gameIds));

        ArrayList<GameSession> sessions = new ArrayList<>();
        for (Document gameDoc : results) {
            sessions.add(createSessionFromDocument(gameDoc));
        }

        return sessions;

    }

    @Override
    public void storePrivateMessage(int senderId, int receiverId, String message) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        int messageId = getAndIncrementConfigValue("nextMessageId");

        Document messageDoc = new Document()
                .append("msgId", messageId)
                .append("sender", senderId)
                .append("date", Time.now())
                .append("read", 0)
                .append("content", message);

        collection.findOneAndUpdate(
                Filters.eq("playerId", receiverId),
                Updates.combine(Updates.addToSet("privateMessages", messageDoc))
        );

    }

    @Override
    public void markPrivateMessageRead(int playerId, int messageId) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        collection.updateOne(
                Filters.and(
                        Filters.eq("playerId", playerId),
                        Filters.eq("privateMessages.msgId", messageId)
                ),
                Updates.set("privateMessages.$.read", 1) // Update the "read" field to 1
        );

    }

    @Override
    public void deletePrivateMessage(int playerId, int messageId) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        collection.updateOne(
                Filters.eq("playerId", playerId),
                Updates.pull("privateMessages", Filters.eq("msgId", messageId))
        );


    }

    private GameSession createSessionFromDocument(Document gameDoc) {

        final Integer gameId = gameDoc.getInteger("gameId");
        final Integer turnOwnerId = gameDoc.getInteger("turnowner");
        final Integer winnerId = gameDoc.getInteger("winner");
        final List<Integer> playerIds = gameDoc.getList("players", Integer.class);

        Object privStateObj = gameDoc.get("privstate");
        GameState privateState = new GameState(JsonHelper.toJson(privStateObj));

        Object pubStateObj = gameDoc.get("pubstate");
        GameState pubState = new GameState(JsonHelper.toJson(pubStateObj));

        GameSession session = new GameSession();
        session.deserialize(gameId, turnOwnerId, winnerId == null ? 0 : winnerId, new ArrayList<>(playerIds), privateState, pubState);

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
