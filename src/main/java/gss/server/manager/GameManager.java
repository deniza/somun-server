package gss.server.manager;

import java.util.ArrayList;
import java.util.HashMap;

import gss.server.event.EventManager;
import gss.server.event.GameEventListener.*;
import gss.server.model.GameHandler;
import gss.server.model.GameRules;
import gss.server.model.GameSession;
import gss.server.model.Player;
import gss.server.model.PlayerWaitingList;
import gss.server.model.ServiceUpdateInterface;

public class GameManager implements ServiceUpdateInterface {
    
    private static GameManager instance;

    private GameRules gameRules;
    private GameHandler gameHandler;
    private final PlayerWaitingList waitingList = new PlayerWaitingList();
    private final HashMap<Integer, GameSession> gameSessions = new HashMap<>();

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

    public void makeMove(Player player, int gameId, String jsonData) {

        GameSession session = gameSessions.get(gameId);

        if (session.checkIfPlayerIsTurnOwner(player) == false) {
            return;
        }

        EventManager.get().dispatch(new PlayerMakeMove(session, player.getPlayerId(), jsonData));

        session.iterateTurnOwner();
        session.saveState();

    }

    private void createRandomGames() {

        ArrayList<ArrayList<Player>> matchedPlayers = waitingList.matchRandomPlayers(gameRules.playerCount);

        if (matchedPlayers.size() > 0) {

            for (ArrayList<Player> pairs : matchedPlayers) {

                int gameId = StorageManager.get().getAndIncrementNextAvailableGameId();

                GameSession session = gameHandler.createGameSession(gameId, pairs);
                gameSessions.put(gameId, session);

                EventManager.get().dispatch(new GameCreated(session));

            }
            
        }
        
    }

    @Override
    public void updateService(long deltaTime) {

        createRandomGames();
        
    }

}
