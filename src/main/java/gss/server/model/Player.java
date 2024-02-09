package gss.server.model;

import java.util.ArrayList;

public class Player {

    private final int playerId;
    private String name;
    private String password;
    private ArrayList<Integer> gameIds = new ArrayList<>();
    private boolean online;
    private int activeGameId;

    public Player(int playerId) {
        this.playerId = playerId;
        setName("");
        setPassword("");
        setOnline(false);
        setActiveGameId(-1);
    }

    public int getPlayerId() {
        return playerId;
    }

    public ArrayList<Integer> getGameIds() {
        return gameIds;
    }

    public void setGameIds(ArrayList<Integer> gameIds) {
        this.gameIds = gameIds;
    }

    public void addGameId(int gameId) {
        gameIds.add(gameId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    public void setActiveGameId(int gameId) {
        this.activeGameId = gameId;
    }
    public int getActiveGameId() {
        return activeGameId;
    }


}
