package gss.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PlayerWaitingList {
        
    private ArrayList<Player> players;
    private Random rand = new Random();

    public PlayerWaitingList() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public Player getRandomPlayer() {        
        int index = rand.nextInt(players.size());
        return players.get(index);
    }

    public ArrayList<ArrayList<Player>> matchRandomPlayers(int count) {

        ArrayList<ArrayList<Player>> matched = new ArrayList<>();

        if (players.size() < count) {
            return matched;
        }

        Collections.shuffle(players);

        while (players.size() >= count) {

            ArrayList<Player> pairs = new ArrayList<>();
            for (int i=0;i<count;++i) {
                Player p = getRandomPlayer();
                pairs.add(p);
                removePlayer(p);
            }

            matched.add(pairs);

        }

        return matched;

    }
}