package gss.server.model;

import gss.server.model.matcher.PlayerMatcher;

import java.util.ArrayList;

public class PlayerWaitingList {
        
    private ArrayList<Player> players;

    public PlayerWaitingList() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        if (players.contains(player) == false) {
            players.add(player);
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public ArrayList<ArrayList<Player>> matchPlayers(int playerPerGame, PlayerMatcher matcher) {

        if (players.size() < playerPerGame) {
            return new ArrayList<>();
        }

        return matcher.matchPlayers(playerPerGame, players);

    }
}