package gss.server.interfaces;

import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.GameManager;
import gss.server.manager.PlayerManager;
import gss.server.model.Player;

public class Play extends GssInterface {
    
    public void makeMove(int gameId, String jsonData, GssConnection con) {

        Player player = getPlayer(con);

        GameManager.get().makeMove(player, gameId, jsonData);

    }

    private Player getPlayer(GssConnection con) {
        return PlayerManager.get().getPlayer(con);
    }

}
