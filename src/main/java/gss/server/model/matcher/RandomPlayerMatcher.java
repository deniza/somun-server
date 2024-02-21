package gss.server.model.matcher;

import gss.server.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomPlayerMatcher implements PlayerMatcher {

    private Random rand = new Random();

    public ArrayList<ArrayList<Player>> matchPlayers(int playerPerGame, ArrayList<Player> players) {

        ArrayList<ArrayList<Player>> matched = new ArrayList<>();

        Collections.shuffle(players);

        while (players.size() >= playerPerGame) {

            ArrayList<Player> pairs = new ArrayList<>();
            for (int i=0;i<playerPerGame;++i) {
                Player p = getRandomPlayer(players);
                pairs.add(p);
                players.remove(p);
            }

            matched.add(pairs);

        }

        return matched;

    }

    private Player getRandomPlayer(ArrayList<Player> players) {
        int index = rand.nextInt(players.size());
        return players.get(index);
    }

}
