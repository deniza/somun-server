package gss.server.model;

import java.util.ArrayList;

public class Game {
    
    private ArrayList<Player> players = new ArrayList<>();

    private int gameId;

    public Game(int gameId) { 
        setGameId(gameId);
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

}
