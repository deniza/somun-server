package gss.server.samplegame;

import java.util.ArrayList;

import gss.server.model.GameSession;
import gss.server.model.Player;

public class SampleGameSession extends GameSession {

    public SampleGameSession(int gameId, ArrayList<Player> players) {
        super(gameId, players);
    }

}
