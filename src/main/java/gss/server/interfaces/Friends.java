package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.FriendsManager;
import gss.server.manager.MessageManager;

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

    public void requestPrivateMessagesList(GssConnection con) {

        GssLogger.info("[Friends] requestPrivateMessagesList called");

        MessageManager.get().sendMessageList(getPlayer(con));

    }

    public void requestSendPrivateMessage(int receiverId, String message, GssConnection con) {

        GssLogger.info("[Friends] requestSendPrivateMessage called");

        MessageManager.get().sendPrivateMessage(getPlayer(con), receiverId, message);

    }

    public void requestReadPrivateMessage(int messageId, GssConnection con) {

        GssLogger.info("[Friends] requestReadPrivateMessage called");

        MessageManager.get().readPrivateMessage(getPlayer(con), messageId);

    }

    public void requestDeletePrivateMessage(int messageId, GssConnection con) {

        GssLogger.info("[Friends] requestDeletePrivateMessage called");

        MessageManager.get().deletePrivateMessage(getPlayer(con), messageId);

    }

}
