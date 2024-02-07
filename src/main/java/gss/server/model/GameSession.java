package gss.server.model;

import com.google.gson.Gson;
import gss.server.manager.PlayerManager;

import java.util.ArrayList;
import java.util.Iterator;

public class GameSession {
 
    protected int gameId;
    protected ArrayList<Player> players;
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

    public Player getTurnOwner() {
        return turnOwner;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
    public ArrayList<Integer> getPlayerIds() {

        ArrayList<Integer> pids = new ArrayList();

        for (Player p : players) {
            pids.add(p.getPlayerId());
        }

        return pids;

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

        GameSessionData sessionData = GameSessionData.createUsingGameSession(this);
        return sessionData.serialize();

    }

    public void deserialize(int gameId, int turnOwnerId, ArrayList<Integer> playerIds, GameState state) {

        this.gameId = gameId;
        this.turnOwner = PlayerManager.get().getPlayer(turnOwnerId);

        this.players = new ArrayList<>(playerIds.size());
        for (Integer pid : playerIds) {
            Player player = PlayerManager.get().getPlayer(pid);
            this.players.add(player);
        }

        setState(state);

    }

    public void deserialize(int gameId, String jsonData) {

        GameSessionData sessionData = GameSessionData.createUsingJson(jsonData);

        this.gameId = gameId;
        this.players = new ArrayList<>();

        for (int i=0;i<sessionData.playerPids.length;++i) {

            int pid = sessionData.playerPids[i];
            Player player = PlayerManager.get().getPlayer(pid);

            players.add(player);
            if (pid == sessionData.turnOwnerPid) {
                this.turnOwner = player;
            }

        }

        this.state = new GameState();
        this.state.deserialize(sessionData.gameStateData);

    }

    private static class GameSessionData {

        protected int turnOwnerPid;
        protected int[] playerPids;
        protected String gameStateData;

        public static GameSessionData createUsingGameSession(GameSession session) {

            GameSessionData data = new GameSessionData();

            data.turnOwnerPid = session.turnOwner.getPlayerId();
            data.playerPids = new int[session.players.size()];

            int playerIndex = 0;
            for (Player p : session.players) {
                data.playerPids[playerIndex] = p.getPlayerId();
            }

            data.gameStateData = session.state.serialize();

            return data;

        }
        public static GameSessionData createUsingJson(String sessionDataJson) {

            GameSessionData gameSessionData = new Gson().fromJson(sessionDataJson, GameSessionData.class);
            return gameSessionData;

        }

        private GameSessionData() {
        }

        public String serialize() {
            return new Gson().toJson(this);
        }

    }

}
