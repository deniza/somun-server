package gss.server.model;

import java.util.HashMap;
import java.util.HashSet;

public class GameRules {
    
    private HashMap<Integer, Integer> playersPerGame = new HashMap<>();  // gametype, playerCount
    private HashSet<Integer> gameTypes = new HashSet<>();
    public int turnDurationInSeconds = 60;
    public int randomMatchingPeriodInSeconds = 60;

    public GameRules() {
    }

    public void setPlayersPerGame(int gametype, int playerCount) {
        playersPerGame.put(gametype, playerCount);
    }

    public int getPlayersPerGame(int gametype) {
        return playersPerGame.get(gametype);
    }

    public void addGameType(int gametype) {
        gameTypes.add(gametype);
    }

    public boolean isGameTypeValid(int gametype) {
        return gameTypes.contains(gametype);
    }

}
