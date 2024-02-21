package gss.server.manager;

import java.util.ArrayList;
import java.util.HashMap;

import gss.server.manager.storage.StorageManager;
import gss.server.model.GameHandler;
import gss.server.model.GameRules;
import gss.server.model.GameSession;
import gss.server.model.Player;
import gss.server.model.PlayerWaitingList;
import gss.server.model.ServiceUpdateInterface;
import gss.server.util.ArrayHelper;

public class GameManager implements ServiceUpdateInterface {
    
    private static GameManager instance;

    private GameRules gameRules;
    private GameHandler gameHandler;
    private final HashMap<Integer, PlayerWaitingList> waitingLists = new HashMap<>();  // gametype, waitingList
    private final HashMap<Integer, GameSession> gameSessions = new HashMap<>();  // gameId, gameSession
    private final GameInvitations invitations = new GameInvitations();

    private GameManager() {

        GameRules rules = new GameRules();
        rules.addGameType(0);
        rules.setPlayersPerGame(0, 2);

        setGameRules(rules);

        invitations.init();

    }

    public static GameManager get() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public GameHandler getGameHandler() {
        return gameHandler;
    }

    public void setGameRules(GameRules rules) {
        this.gameRules = rules;
    }

    public void setGameHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void registerToCreateRandomGame(Player player, int gametype) {

        if (gameRules.isGameTypeValid(gametype) == false) {
            ConnectionManager.get().call(player, "Play", "createRandomGameResponse", 0);
            return;
        }

        PlayerWaitingList playerWaitingList = waitingLists.get(gametype);
        if (playerWaitingList == null) {
            playerWaitingList = new PlayerWaitingList();
            waitingLists.put(gametype, playerWaitingList);
        }

        playerWaitingList.addPlayer(player);

        ConnectionManager.get().call(player, "Play", "createRandomGameResponse", 1);

    }

    public void loadPlayerGameSessions(Player player) {

        ArrayList<GameSession> loadedGameSessions = StorageManager.get().loadGameSessions(player.getGameIds());
        for (GameSession session : loadedGameSessions) {
            gameSessions.put(session.getGameId(), session);
        }

    }

    public void enterGame(Player player, int gameId) {

        player.setActiveGameId(gameId);

        ConnectionManager.get().call(player, "Play", "enterGameResponse", 1, gameSessions.get(gameId).getTurnOwner().getPlayerId());

    }
    public void exitGame(Player player, int gameId) {

        player.setActiveGameId(-1);

    }

    public void makeMove(Player player, int gameId, String jsonData) {

        GameSession session = gameSessions.get(gameId);

        if (session.checkIfPlayerIsTurnOwner(player) == false) {
            ConnectionManager.get().call(player, "Play", "makeMoveResponse", 0);
            return;
        }

        gameHandler.onPlayerMakeMove(session, jsonData);

        if (gameHandler.isGameFinished()) {

        }
        else {
            session.iterateTurnOwner();
        }

        ConnectionManager.get().call(player, "Play", "makeMoveResponse", 1);

        StorageManager.get().storeGameSession(session);

    }

    public void createInvitation(Player player, int invitee, int gametype, boolean shouldStartOnline) {

        if (gameRules.isGameTypeValid(gametype)) {
            invitations.createInvitation(player.getPlayerId(), invitee, gametype, shouldStartOnline);
        }

    }

    public ArrayList<Integer> listInvitations(Player player) {

        return invitations.getInvitationList(player.getPlayerId());

    }

    public void acceptInvitation(Player player, int invitationId) {

        if (invitations.isOnlineGameInvitation(invitationId)) {

            GameInvitations.InvitationRequest invitation = invitations.getInvitation(invitationId);

            Player inviter = PlayerManager.get().getPlayer(invitation.inviter);
            if (inviter.isOnline()) {

                ArrayList<Player> pairs = new ArrayList<Player>();
                pairs.add(player);
                pairs.add(inviter);

                createGameAmongPlayers(pairs);

                invitations.removeInvitation(invitationId);

            }
            else {
                //TODO implement
                // send message to inviter that invitee is not online
                // register to create game when inviter comes online
            }
        }
        else {
            //TODO implement
            // send message to inviter that invitee accepted the invitation
        }

    }

    public void playerDisconnected(Player player) {

        for (PlayerWaitingList waitingList : waitingLists.values()) {
            waitingList.removePlayer(player);
        }

    }

    private void createRandomGames() {

        for (int gametype : waitingLists.keySet()) {

            PlayerWaitingList waitingList = waitingLists.get(gametype);
            ArrayList<ArrayList<Player>> matched = waitingList.matchRandomPlayers(gameRules.getPlayersPerGame(gametype));

            for (ArrayList<Player> players : matched) {
                createGameAmongPlayers(players);
            }

        }

    }

    private void createGameAmongPlayers(ArrayList<Player> players) {

        int gameId = StorageManager.get().getAndIncrementNextAvailableGameId();

        GameSession session = new GameSession(gameId, players);
        gameSessions.put(gameId, session);

        gameHandler.onGameCreated(session);

        StorageManager.get().storeGameSession(session);
        for (Player player : players) {
            player.addGameId(gameId);
            StorageManager.get().storePlayer(player);
        }

        ConnectionManager.get().call(players, "Play", "gameCreated", gameId,
                ArrayHelper.toIntArray(session.getPlayerIds()), session.getTurnOwner().getPlayerId(), session.getPublicState().serialize());

    }

    @Override
    public void updateService(long deltaTime) {

        createRandomGames();

    }

}
