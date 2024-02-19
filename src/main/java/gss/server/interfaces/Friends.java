package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.FriendsManager;

public class Friends extends GssInterface {

    public void requestFriends(GssConnection con) {

        GssLogger.info("[Friends] requestFriends called");

        FriendsManager.get().getFriends(getPlayer(con));

    }

    public void requestAddFriend(int playerId, GssConnection con) {

        GssLogger.info("[Friends] requestAddFriend called");

        FriendsManager.get().requestAddFriend(getPlayer(con), playerId);

    }

    public void requestAcceptFriend(int playerId, GssConnection con) {

        GssLogger.info("[Friends] requestAcceptFriend called");

        FriendsManager.get().acceptFriendRequest(getPlayer(con), playerId);

    }

    public void requestRejectFriend(int playerId, GssConnection con) {

        GssLogger.info("[Friends] requestRejectFriend called");

        FriendsManager.get().rejectFriendRequest(getPlayer(con), playerId);

    }

    public void requestRemoveFriend(int playerId, GssConnection con) {

        GssLogger.info("[Friends] requestRemoveFriend called");

        FriendsManager.get().removeFriend(getPlayer(con), playerId);

    }

}
