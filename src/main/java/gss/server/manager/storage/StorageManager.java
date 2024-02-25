package gss.server.manager.storage;

import gss.server.manager.GameInvitations;
import gss.server.manager.groups.Group;
import gss.server.model.GameSession;
import gss.server.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class StorageManager {

    private static StorageManager instance;

    private final StorageManagerMongo mongo = new StorageManagerMongo();

    private StorageManager() {
    }

    public void initialize() {

        mongo.initialize();

    }

    public void setup() {

        mongo.setup();

    }

    public void shutdown() {

        mongo.shutdown();

    }

    public static StorageManager get() {
        if (instance == null) {
            instance = new StorageManager();
        }
        return instance;
    }

    public int getAndIncrementNextAvailableGameId() {
        return getAndIncrementConfigValue("nextGameId");
    }

    public int getAndIncrementConfigValue(String configKey) {
        return mongo.getAndIncrementConfigValue(configKey);
    }

    public void storePlayer(Player player) {
        
        mongo.storePlayer(player);

    }

    public synchronized StorageInterface.CreatePlayerResult createPlayer(Player player) {

        return mongo.createPlayer(player);

    }

    public Player loadPlayer(int playerId) {

        return mongo.loadPlayer(playerId);

    }

    public Player loadPlayerByFbuid(String fbuid) {

        return mongo.loadPlayerByFbuid(fbuid);

    }

    public void storeGameSession(GameSession session) {

        mongo.storeGameSession(session);

    }    

    public ArrayList<GameSession> loadGameSessions(ArrayList<Integer> gameIds) {

        return mongo.loadGameSessions(gameIds);

    }

    public void storePrivateMessage(int senderId, int receiverId, String message) {

        mongo.storePrivateMessage(senderId, receiverId, message);

    }

    public void markPrivateMessageRead(int playerId, int messageId) {

        mongo.markPrivateMessageRead(playerId, messageId);

    }

    public void deletePrivateMessage(int playerId, int messageId) {

        mongo.deletePrivateMessage(playerId, messageId);

    }

    public ArrayList<GameInvitations.InvitationRequest> loadAllInvitations() {

        return mongo.loadAllInvitations();

    }

    public int createInvitation(GameInvitations.InvitationRequest inv) {

        return mongo.createInvitation(inv);

    }

    public void deleteInvitation(int invitationId) {

        mongo.deleteInvitation(invitationId);

    }

    public void storeDataObject(int ownerId, String collection, String key, String value) {

        mongo.storeDataObject(ownerId, collection, key, value);

    }

    public String loadDataObject(int ownerId, String collection, String key) {

        return mongo.loadDataObject(ownerId, collection, key);

    }

    public HashMap<Integer, Group> loadAllGroups() {

        return mongo.loadAllGroups();

    }

    public void storeGroup(Group group) {

        mongo.storeGroup(group);

    }

    public Group loadGroup(int groupId) {

        return mongo.loadGroup(groupId);

    }

}
