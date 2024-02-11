package gss.server.model;

import gss.server.manager.PlayerManager;
import gss.server.util.JsonHelper;

import java.util.ArrayList;
import java.util.Iterator;

public class GameSession {
 
    protected int gameId;
    protected ArrayList<Player> players;
    protected Player turnOwner;
    protected Player winner;
    protected GameState privateState = new GameState();
    protected GameState publicState = new GameState();

    public GameSession(int gameId, ArrayList<Player> players) {
        this.gameId = gameId;
        this.players = players;

        randomizeTurnOwner();
    }

    public GameSession() {
    }

    public int getGameId() {
        return gameId;
    }

    public GameState getPrivateState() {
        return privateState;
    }
    public GameState getPublicState() {
        return publicState;
    }

    public Player getTurnOwner() {
        return turnOwner;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player player) {
        this.winner = player;
    }

    public void setPrivateState(GameState state) {
        this.privateState = state;
    }
    public void setPublicState(GameState state) {
        this.publicState = state;
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

    public void deserialize(int gameId, int turnOwnerId, int winnerId, ArrayList<Integer> playerIds, GameState privateState, GameState publicState) {

        this.gameId = gameId;
        this.turnOwner = PlayerManager.get().getPlayer(turnOwnerId);
        this.winner = PlayerManager.get().getPlayer(winnerId);

        this.players = new ArrayList<>(playerIds.size());
        for (Integer pid : playerIds) {
            Player player = PlayerManager.get().getPlayer(pid);
            this.players.add(player);
        }

        setPrivateState(privateState);
        setPublicState(publicState);

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

        this.privateState = new GameState();
        this.privateState.deserialize(sessionData.gameStateDataPrivate);
        this.publicState = new GameState();
        this.publicState.deserialize(sessionData.gameStateDataPublic);

    }

    private void randomizeTurnOwner() {

        turnOwner = players.get((int) Math.random() * players.size());

    }

    private static class GameSessionData {

        protected int turnOwnerPid;
        protected int[] playerPids;
        protected String gameStateDataPrivate;
        protected String gameStateDataPublic;

        public static GameSessionData createUsingGameSession(GameSession session) {

            GameSessionData data = new GameSessionData();

            data.turnOwnerPid = session.turnOwner.getPlayerId();
            data.playerPids = new int[session.players.size()];

            int playerIndex = 0;
            for (Player p : session.players) {
                data.playerPids[playerIndex] = p.getPlayerId();
            }

            data.gameStateDataPrivate = session.privateState.serialize();
            data.gameStateDataPublic = session.publicState.serialize();

            return data;

        }
        public static GameSessionData createUsingJson(String sessionDataJson) {

            GameSessionData gameSessionData = JsonHelper.fromJson(sessionDataJson, GameSessionData.class);
            return gameSessionData;

        }

        private GameSessionData() {
        }

        public String serialize() {
            return JsonHelper.toJson(this);
        }

    }

}
