package gss.server.manager.storage;

import gss.server.manager.GameInvitations;
import gss.server.manager.groups.Group;
import gss.server.model.GameSession;
import gss.server.model.Player;

import java.util.ArrayList;
import java.util.HashMap;

public interface StorageInterface {

    public enum CreatePlayerResult {
        SUCCESS,
        USERNAME_ALREADY_EXISTS,
        ERROR
    };

    public void initialize();
    public void shutdown();
    public void setup();
    public CreatePlayerResult createPlayer(Player player);

    public void storePlayer(Player player);

    public Player loadPlayer(int playerId);

    public Player loadPlayerByFbuid(String fbuid);

    public void storeGameSession(GameSession session);

    public GameSession loadGameSession(int gameId);

    public ArrayList<GameSession> loadGameSessions(ArrayList<Integer> gameIds);

    public int getAndIncrementConfigValue(String configKey);

    public void storePrivateMessage(int senderId, int receiverId, String message);

    public void markPrivateMessageRead(int playerId, int messageId);
    public void deletePrivateMessage(int playerId, int messageId);
    public ArrayList<GameInvitations.InvitationRequest> loadAllInvitations();
    public int createInvitation(GameInvitations.InvitationRequest inv);
    public void deleteInvitation(int invitationId);
    public void storeDataObject(int ownerId, String collection, String key, String value);
    public String loadDataObject(int ownerId, String collection, String key);
    public void storeGroup(Group group);
    public Group loadGroup(int groupId);
    public HashMap<Integer, Group> loadAllGroups();

}
