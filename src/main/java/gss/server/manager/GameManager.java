package gss.server.manager;

import java.util.ArrayList;
import java.util.HashSet;

import gss.GssLogger;
import gss.server.model.GameRules;
import gss.server.model.Player;
import gss.server.model.PlayerWaitingList;
import gss.server.model.ServiceUpdateInterface;

public class GameManager implements ServiceUpdateInterface {
    
    private static GameManager instance;

    private GameRules gameRules;
    private final PlayerWaitingList waitingList = new PlayerWaitingList();

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

    public void registerToCreateRandomGame(Player player) {

        waitingList.addPlayer(player);

    }

    private void createRandomGames() {

        ArrayList<ArrayList<Player>> matchedPlayers = waitingList.matchRandomPlayers(gameRules.playerCount);

        if (matchedPlayers.size() > 0) {

            for (ArrayList<Player> pairs : matchedPlayers) {

                // create games here!

            }
            
        }
        
    }

    @Override
    public void updateService(long deltaTime) {

        createRandomGames();
        
    }

}
