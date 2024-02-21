package gss.server.manager.storage;

import gss.server.manager.GameInvitations;
import gss.server.model.GameSession;
import gss.server.model.Player;

import java.util.ArrayList;

public interface StorageInterface {

    public void initialize();
    public void shutdown();

    public void storePlayer(Player player);

    public Player loadPlayer(int playerId);

    public void storeGameSession(GameSession session);

    public GameSession loadGameSession(int gameId);

    public ArrayList<GameSession> loadGameSessions(ArrayList<Integer> gameIds);

    public int getAndIncrementNextAvailableGameId();

    public int getAndIncrementNextAvailablePlayerId();

    public void storePrivateMessage(int senderId, int receiverId, String message);

    public void markPrivateMessageRead(int playerId, int messageId);
    public void deletePrivateMessage(int playerId, int messageId);
    public ArrayList<GameInvitations.InvitationRequest> loadAllInvitations();
    public int createInvitation(GameInvitations.InvitationRequest inv);
    public void deleteInvitation(int invitationId);
    public void storeDataObject(int ownerId, String collection, String key, String value);
    public String loadDataObject(int ownerId, String collection, String key);

}
