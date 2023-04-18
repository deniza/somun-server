package gss.server.model;

import java.util.ArrayList;

public class Player {

    private final int playerId;
    private String name;
    private String password;
    private ArrayList<Integer> gameIds = new ArrayList<>();

    public Player(int playerId) {
        this.playerId = playerId;
        setName("");
        setPassword("");
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


}
