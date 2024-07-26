package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssCallable;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.GameManager;
import gss.server.manager.PlayerManager;
import gss.server.model.Player;
import gss.server.util.ArrayHelper;

public class Play extends GssInterface {

    @GssCallable
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

    @GssCallable
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

    @GssCallable
    public void resignGame(int gameId, GssConnection con) {

        GssLogger.info("[Play] resignGame called");

        //Player player = getPlayer(con);

        // resign here

    }

    @GssCallable
    public void listGames(GssConnection con) {

        GssLogger.info("[Play] listGames called");

        Player player = getPlayer(con);

        call(con, "Play", "listGamesResponse", ArrayHelper.toIntArray(player.getGameIds()));

    }

    @GssCallable
    public void makeMove(int gameId, String jsonData, GssConnection con) {

        GssLogger.info("[Play] makeMove called");

        Player player = getPlayer(con);

        if (player.getActiveGameId() == gameId) {
            GameManager.get().makeMove(player, gameId, jsonData);
        }

    }

    @GssCallable
    public void createRandomGame(int gametype, GssConnection con) {

        GssLogger.info("[Play] createRandomGame called");

        Player player = getPlayer(con);

        GameManager.get().registerToCreateRandomGame(player, gametype);

    }

    @GssCallable
    public void createInvitation(int invitee, int gametype, byte shouldStartOnline, GssConnection con) {

        GssLogger.info("[Play] createInvitation called");

        GameManager.get().createInvitation(getPlayer(con), invitee, gametype, shouldStartOnline==1);

    }
    @GssCallable
    public void listInvitations(GssConnection con) {

        GssLogger.info("[Play] listInvitations called");

        call(con, "Play", "invitationsList", ArrayHelper.toIntArray(GameManager.get().listInvitations(getPlayer(con))));

    }
    @GssCallable
    public void acceptInvitation(int invitationId, GssConnection con) {

        GssLogger.info("[Play] acceptInvitation called");

        GameManager.get().acceptInvitation(getPlayer(con), invitationId);

    }

    @GssCallable
    public void rejectInvitation(int invitationId, GssConnection con) {

        GssLogger.info("[Play] rejectInvitation called");

        GameManager.get().rejectInvitation(getPlayer(con), invitationId);

    }

}
