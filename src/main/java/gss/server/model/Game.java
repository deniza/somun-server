package gss.server.model;

public class Game {
    
    //private ArrayList<Player> players = new ArrayList<>();

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
