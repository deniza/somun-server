package gss.server.model;

public class Player {

    private final int playerId;
    private String name;
    private String password;

    public Player(int playerId) {
        this.playerId = playerId;
        setName("");
        setPassword("");
    }

    public int getPlayerId() {
        return playerId;
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
