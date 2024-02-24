package gss.server.manager.storage;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import gss.GssLogger;
import gss.server.manager.GameInvitations;
import gss.server.manager.MessageManager;
import gss.server.model.GameSession;
import gss.server.model.GameState;
import gss.server.model.Player;
import gss.server.util.Config;
import gss.server.util.JsonHelper;
import gss.server.util.Time;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

public class StorageManagerMongo implements StorageInterface {

    private final String MongoUri = Config.get("mongo_uri");
    private final String MongoDatabase = Config.get("mongo_database");
    private final String PlayersCollection = "players";
    private final String GamesCollection = "games";
    private final String InvitationsCollection = "invitations";
    private final String ConfigCollection = "config";

    private MongoClient client;
    private MongoDatabase database;

    private final HashSet<String> collectionNames = new HashSet<>();

    @Override
    public void initialize() {

        try {
            client = MongoClients.create(MongoUri);
            database = client.getDatabase(MongoDatabase);

            for (String collName : database.listCollectionNames()) {
                collectionNames.add(collName);
            }

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
    public void setup() {

        // iterate thtough all databases and check if the database already exists
        for (String dbName : client.listDatabaseNames()) {
            if (dbName.equals(MongoDatabase)) {

                System.out.print("Database " + MongoDatabase + " already exists. All data will be deleted. Are you sure? (y/N) ");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                if (input.equals("y")) {
                    client.getDatabase(dbName).drop();
                }
                else {
                    GssLogger.info("Setup cancelled");
                    return;
                }

            }
        }

        MongoCollection<Document> config = database.getCollection(ConfigCollection);
        Document doc = new Document()
                .append("nextPlayerId", 1)
                .append("nextGameId", 1)
                .append("nextMessageId", 1)
                .append("nextInvitationId", 1)
                .append("nextGuestAccountPostfixId",1);
        config.insertOne(doc);
        GssLogger.info("config collection created with default values");

        database.createCollection(PlayersCollection);
        GssLogger.info("players collection created");

        database.createCollection(GamesCollection);
        GssLogger.info("games collection created");

        database.createCollection(InvitationsCollection);
        GssLogger.info("invitations collection created");

        // create indexes
        MongoCollection<Document> players = database.getCollection(PlayersCollection);
        players.createIndex(new Document("playerId", 1), new IndexOptions().unique(true));
        players.createIndex(new Document("name", 1), new IndexOptions().unique(true));
        players.createIndex(new Document("fbuid", 1), new IndexOptions().unique(true));

        MongoCollection<Document> games = database.getCollection(GamesCollection);
        games.createIndex(new Document("gameId", 1), new IndexOptions().unique(true));

        MongoCollection<Document> invitations = database.getCollection(InvitationsCollection);
        invitations.createIndex(new Document("invitationId", 1), new IndexOptions().unique(true));

        GssLogger.info("Indexes created");

        GssLogger.info("Setup completed");

    }

    @Override
    public void storePlayer(Player player) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        Document playerDoc = new Document()
            .append("playerId", player.getPlayerId())
            .append("fbuid", player.getFbuid())
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
    public synchronized CreatePlayerResult createPlayer(Player player) {

        int playerId = getAndIncrementConfigValue("nextPlayerId");
        player.setPlayerId(playerId);

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        Document doc = collection.find(Filters.eq("name", player.getName())).first();
        if (doc != null) {
            return CreatePlayerResult.USERNAME_ALREADY_EXISTS;
        }
        else {
            storePlayer(player);
            return CreatePlayerResult.SUCCESS;
        }

    }

    @Override
    public Player loadPlayer(int playerId) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        Document doc = collection.find(Filters.eq("playerId", playerId)).first();

        if (doc != null) {

            return createPlayerFromDocument(doc);

        }
        else {
            return null;
        }

    }

    @Override
    public Player loadPlayerByFbuid(String fbuid) {

        MongoCollection<Document> collection = database.getCollection(PlayersCollection);

        Document doc = collection.find(Filters.eq("fbuid", fbuid)).first();

        if (doc != null) {

            return createPlayerFromDocument(doc);

        }
        else {
            return null;
        }

    }

    private Player createPlayerFromDocument(Document doc) {

        final int playerId = doc.getInteger("playerId");
        final String name = doc.getString("name");
        final String password = doc.getString("password");
        final String fbuid = doc.getOrDefault("fbuid", "").toString();
        final List<Integer> gameIds = doc.getList("games", Integer.class);

        Player player = new Player(playerId);
        player.setName(name);
        player.setPassword(password);
        player.setFbuid(fbuid);
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

    @Override
    public ArrayList<GameInvitations.InvitationRequest> loadAllInvitations() {

        MongoCollection<Document> collection = database.getCollection(InvitationsCollection);
        FindIterable<Document> results = collection.find(Filters.empty());

        ArrayList<GameInvitations.InvitationRequest> invitations = new ArrayList<>();
        for (Document doc : results) {

            GameInvitations.InvitationRequest req = new GameInvitations.InvitationRequest();
            req.invitationId = doc.getInteger("invitationId");
            req.inviter = doc.getInteger("inviter");
            req.invitee = doc.getInteger("invitee");
            req.date = doc.getLong("date");
            req.gametype = doc.getInteger("gametype");
            req.shouldStartAllOnline = doc.getInteger("shouldStartAllOnline") == 0 ? false : true;

            invitations.add(req);

        }

        return invitations;
    }

    public int createInvitation(GameInvitations.InvitationRequest inv) {

        MongoCollection<Document> collection = database.getCollection(InvitationsCollection);

        int invitationId = getAndIncrementConfigValue("nextInvitationId");

        Document invDoc = new Document()
                .append("invitationId", invitationId)
                .append("inviter", inv.inviter)
                .append("invitee", inv.invitee)
                .append("gametype", inv.gametype)
                .append("shouldStartAllOnline", inv.shouldStartAllOnline?1:0)
                .append("date", inv.date);

        collection.insertOne(
                invDoc
        );

        return invitationId;

    }

    @Override
    public void deleteInvitation(int invitationId) {

        MongoCollection<Document> collection = database.getCollection(InvitationsCollection);

        collection.deleteOne(
                Filters.eq("invitationId", invitationId)
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

    @Override
    public int getAndIncrementConfigValue(String configKey) {

        MongoCollection<Document> config = database.getCollection(ConfigCollection);

        Document doc = config.findOneAndUpdate(
                Filters.empty(),
                new Document("$inc", new Document(configKey, 1)),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.BEFORE)
        );

        return doc.getInteger(configKey);

    }

    public void storeDataObject(int ownerId, String collection, String key, String valueJson) {

        if (collectionNames.contains(collection) == false) {

            database.createCollection(collection);
            collectionNames.add(collection);

            MongoCollection<Document> coll = database.getCollection(collection);

            coll.createIndex(new Document("ownerId", 1), new IndexOptions().unique(true));

        }

        MongoCollection<Document> coll = database.getCollection(collection);

        coll.findOneAndUpdate(
                Filters.and(
                    Filters.eq("ownerId", ownerId), Filters.eq("key", key)
                ),
                new Document("$set", new Document("value", Document.parse(valueJson))),
                new FindOneAndUpdateOptions().upsert(true)
        );

    }

    public String loadDataObject(int ownerId, String collection, String key) {

        if (collectionNames.contains(collection) == false) {
            database.createCollection(collection);
            collectionNames.add(collection);
        }

        MongoCollection<Document> coll = database.getCollection(collection);
        Document doc = coll.find(
                Filters.and(
                        Filters.eq("ownerId", ownerId), Filters.eq("key", key)
                )
            ).first();

        if (doc != null) {
            Document valueDoc = (Document) doc.get("value");
            return valueDoc.toJson();
        }
        else {
            return null;
        }

    }


}
