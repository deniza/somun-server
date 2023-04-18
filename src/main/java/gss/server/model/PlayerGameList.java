package gss.server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class PlayerGameList {
    
    private final HashMap<Integer, ArrayList<GameSession>> sessionMap = new HashMap<>();  // playerId, gameSession list

    public PlayerGameList() {
    }

    public void create(int playerId, GameSession session) {

        ArrayList<GameSession> sessions = sessionMap.get(playerId);
        if (sessions == null) {
            sessions = new ArrayList<>();
            sessionMap.put(playerId, sessions);
        }

        sessions.add(session);

    }

    public void create(Collection<Player> players, GameSession session) {

        for (Player p : players) {
            create(p.getPlayerId(), session);
        }

    }

    public int[] getGameIdList(int playerId) {

        ArrayList<GameSession> slist = sessionMap.get(playerId);
        if (slist != null) {

            int[] gameIds = new int[slist.size()];

            for (int i=0;i<slist.size();++i) {
                gameIds[i] = slist.get(i).getGameId();
            }

            return gameIds;

        }
        else {
            return new int[0];
        }

    }

}
