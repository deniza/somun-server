package gss.server.manager.storage;

import gss.server.manager.GameInvitations;
import gss.server.model.GameSession;
import gss.server.model.Player;

import java.util.ArrayList;

public class StorageManager {
    
    private static StorageManager instance;

    private final StorageManagerMongo mongo = new StorageManagerMongo();

    private StorageManager() {
    }

    public void initialize() {

        mongo.initialize();

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

    public int getNextAvailablePlayerId() {
        return mongo.getAndIncrementNextAvailablePlayerId();
    }

    public int getAndIncrementNextAvailableGameId() {
        return mongo.getAndIncrementNextAvailableGameId();
    }

    public void storePlayer(Player player) {
        
        mongo.storePlayer(player);

    }

    public Player loadPlayer(int playerId) {

        return mongo.loadPlayer(playerId);

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

}
