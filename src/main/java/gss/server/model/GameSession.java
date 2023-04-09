package gss.server.model;

import java.util.ArrayList;
import java.util.Iterator;

public class GameSession {
 
    private final int gameId;
    private final ArrayList<Player> players;
    private Player turnOwner;
    private GameState state;

    public GameSession(int gameId, ArrayList<Player> players) {
        this.gameId = gameId;
        this.players = players;
    }

    public int getGameId() {
        return gameId;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public boolean checkIfPlayerIsTurnOwner(Player player) {
        return turnOwner == player;
    }

    public void iterateTurnOwner() {

        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player cp = it.next();
            if (cp == turnOwner) {
                if (it.hasNext()) {
                    turnOwner = it.next();
                }
                else {
                    turnOwner = players.get(0);
                }
            }
        }

    }

    public void saveState() {        
    }

}
