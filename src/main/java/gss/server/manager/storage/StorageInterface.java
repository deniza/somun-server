package gss.server.manager.storage;

import gss.server.model.GameSession;
import gss.server.model.GameState;
import gss.server.model.Player;

public interface StorageInterface {

    public void initialize();
    public void shutdown();

    public void storePlayer(Player player);

    public Player loadPlayer(int playerId);

    public void storeGameSession(GameSession session);

    public GameSession loadGameSession(int gameId);

}
