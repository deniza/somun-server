package gss.server.interfaces;

import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.GameManager;
import gss.server.manager.PlayerManager;
import gss.server.model.Player;
import gss.server.util.ArrayHelper;

public class Play extends GssInterface {
    
    public void enterGame(int gameId, GssConnection con) {

        //Player player = getPlayer(con);

        // enter game here

    }

    public void exitGame(int gameId, GssConnection con) {

        //Player player = getPlayer(con);

        // exit game here

    }

    public void resignGame(int gameId, GssConnection con) {

        //Player player = getPlayer(con);

        // resign here

    }

    public void listGames(GssConnection con) {

        Player player = getPlayer(con);

        call(con, "Play", "listGamesResponse", ArrayHelper.toIntArray(player.getGameIds()));

    }

    public void makeMove(int gameId, String jsonData, GssConnection con) {

        Player player = getPlayer(con);

        GameManager.get().makeMove(player, gameId, jsonData);

    }

    public void createRandomGame(GssConnection con) {

        Player player = getPlayer(con);

        GameManager.get().registerToCreateRandomGame(player);

        call(con, "Play", "createRandomGameResponse", new Object[]{1});

    }

    public void clientDisconnected(GssConnection con) {

        Player player = getPlayer(con);

        GameManager.get().playerDisconnected(player);

    }

    private Player getPlayer(GssConnection con) {
        return PlayerManager.get().getPlayer(con);
    }

}
