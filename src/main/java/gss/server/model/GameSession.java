package gss.server.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class GameSession {
 
    protected final int gameId;
    protected final ArrayList<Player> players;
    protected Player turnOwner;
    protected GameState state;

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

    public String serialize() {

        GameSessionData sessionData = new GameSessionData(this);

        return sessionData.serialize();

    }

    private class GameSessionData {

        final int turnOwnerPid;
        final int[] playerPids;
        final String gameStateData;

        public GameSessionData(GameSession session) {

            turnOwnerPid = session.turnOwner.getPlayerId();

            playerPids = new int[session.players.size()];
            int playerIndex = 0;
            for (Player p : session.players) {
                playerPids[playerIndex] = p.getPlayerId();
            }

            gameStateData = state.saveState();

        }

        public String serialize() {
            return new Gson().toJson(this);
        }

    }

}
