package gss.server.manager;

import java.util.ArrayList;
import java.util.HashMap;

import gss.server.manager.storage.StorageManager;
import gss.server.model.GameHandler;
import gss.server.model.GameRules;
import gss.server.model.GameSession;
import gss.server.model.Player;
import gss.server.model.PlayerWaitingList;
import gss.server.model.ServiceUpdateInterface;
import gss.server.util.ArrayHelper;

public class GameManager implements ServiceUpdateInterface {
    
    private static GameManager instance;

    private GameRules gameRules;
    private GameHandler gameHandler;
    private final PlayerWaitingList waitingList = new PlayerWaitingList();
    private final HashMap<Integer, GameSession> gameSessions = new HashMap<>();  // gameId, gameSession

    private GameManager() {

        setGameRules(new GameRules());

    }

    public static GameManager get() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void setGameRules(GameRules rules) {
        this.gameRules = rules;
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void registerToCreateRandomGame(Player player) {

        waitingList.addPlayer(player);

    }

    public void loadPlayerGameSessions(Player player) {

        ArrayList<GameSession> loadedGameSessions = StorageManager.get().loadGameSessions(player.getGameIds());
        for (GameSession session : loadedGameSessions) {
            gameSessions.put(session.getGameId(), session);
        }

    }

    public void enterGame(Player player, int gameId) {

        player.setActiveGameId(gameId);

    }
    public void exitGame(Player player, int gameId) {

        player.setActiveGameId(-1);

    }

    public void makeMove(Player player, int gameId, String jsonData) {

        GameSession session = gameSessions.get(gameId);

        if (session.checkIfPlayerIsTurnOwner(player) == false) {
            return;
        }

        gameHandler.onPlayerMakeMove(session, jsonData);

        if (gameHandler.isGameFinished()) {

        }
        else {
            session.iterateTurnOwner();
        }

        StorageManager.get().storeGameSession(session);

    }

    public void playerDisconnected(Player player) {

        waitingList.removePlayer(player);

    }

    private void createRandomGames() {

        ArrayList<ArrayList<Player>> matchedPlayers = waitingList.matchRandomPlayers(gameRules.playerCount);

        if (matchedPlayers.size() > 0) {

            for (ArrayList<Player> pairs : matchedPlayers) {

                createGameAmongPlayers(pairs);

            }
            
        }
        
    }

    private void createGameAmongPlayers(ArrayList<Player> players) {

        int gameId = StorageManager.get().getAndIncrementNextAvailableGameId();

        GameSession session = new GameSession(gameId, players);
        gameSessions.put(gameId, session);

        gameHandler.onGameCreated(session);

        StorageManager.get().storeGameSession(session);
        for (Player player : players) {
            player.addGameId(gameId);
            StorageManager.get().storePlayer(player);
        }

        ConnectionManager.get().call(players, "Play", "gameCreated", gameId,
                ArrayHelper.toIntArray(session.getPlayerIds()), session.getTurnOwner().getPlayerId(), session.getPrivateState().serialize());

    }

    @Override
    public void updateService(long deltaTime) {

        createRandomGames();

    }

}
