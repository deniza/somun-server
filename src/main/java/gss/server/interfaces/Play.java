package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.GameManager;
import gss.server.manager.PlayerManager;
import gss.server.model.Player;
import gss.server.util.ArrayHelper;

public class Play extends GssInterface {
    
    public void enterGame(int gameId, GssConnection con) {

        GssLogger.info("[Play] enterGame called");

        Player player = getPlayer(con);

        if (player.getActiveGameId() == -1) {
            GameManager.get().enterGame(player, gameId);
        }
        else {
            call(con, "Play", "enterGameResponse", 0);
        }

    }

    public void exitGame(int gameId, GssConnection con) {

        GssLogger.info("[Play] exitGame called");

        Player player = getPlayer(con);

        if (player.getActiveGameId() == gameId) {
            GameManager.get().exitGame(player, gameId);
            call(con, "Play", "exitGameResponse", 1);
        }
        else {
            call(con, "Play", "exitGameResponse", 0);
        }

    }

    public void resignGame(int gameId, GssConnection con) {

        GssLogger.info("[Play] resignGame called");

        //Player player = getPlayer(con);

        // resign here

    }

    public void listGames(GssConnection con) {

        GssLogger.info("[Play] listGames called");

        Player player = getPlayer(con);

        call(con, "Play", "listGamesResponse", ArrayHelper.toIntArray(player.getGameIds()));

    }

    public void makeMove(int gameId, String jsonData, GssConnection con) {

        GssLogger.info("[Play] makeMove called");

        Player player = getPlayer(con);

        if (player.getActiveGameId() == gameId) {
            GameManager.get().makeMove(player, gameId, jsonData);
        }

    }

    public void createRandomGame(GssConnection con) {

        GssLogger.info("[Play] createRandomGame called");

        Player player = getPlayer(con);

        GameManager.get().registerToCreateRandomGame(player);

        call(con, "Play", "createRandomGameResponse", new Object[]{1});

    }

    public void createInvitation(int invitee, int gametype, byte shouldStartOnline, GssConnection con) {

        GssLogger.info("[Play] createInvitation called");

        GameManager.get().createInvitation(getPlayer(con), invitee, gametype, shouldStartOnline==1);

    }
    public void listInvitations(GssConnection con) {

        GssLogger.info("[Play] listInvitations called");

        call(con, "Play", "invitationsList", ArrayHelper.toIntArray(GameManager.get().listInvitations(getPlayer(con))));

    }
    public void acceptInvitation(int invitationId, GssConnection con) {

        GssLogger.info("[Play] acceptInvitation called");

        GameManager.get().acceptInvitation(getPlayer(con), invitationId);

    }

    public void clientDisconnected(GssConnection con) {

        Player player = getPlayer(con);

        GameManager.get().playerDisconnected(player);
        PlayerManager.get().playerDisconnected(player);

    }

}
