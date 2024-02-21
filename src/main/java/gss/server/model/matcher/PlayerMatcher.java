package gss.server.model.matcher;

import gss.server.model.Player;

import java.util.ArrayList;

public interface PlayerMatcher {

    public ArrayList<ArrayList<Player>> matchPlayers(int playerPerGame, ArrayList<Player> players);

}
