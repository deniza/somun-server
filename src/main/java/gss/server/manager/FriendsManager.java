package gss.server.manager;

import gss.server.manager.storage.StorageManager;
import gss.server.model.Player;
import gss.server.util.ArrayHelper;

import java.util.ArrayList;
import java.util.List;

public class FriendsManager {

    private static FriendsManager instance;

    private FriendsManager() {}

    public static FriendsManager get() {
        if (instance == null) {
            instance = new FriendsManager();
        }
        return instance;
    }

    public void getFriends(Player player) {

        ArrayList<Integer> friends = new ArrayList<>(player.getFriends());

        ConnectionManager.get().call(player, "Friends", "friendList", ArrayHelper.toIntArray(friends));

    }

    public void requestAddFriend(Player player, int friendId) {

        Player targetPlayer = PlayerManager.get().getPlayer(friendId);

        if (targetPlayer != null) {

            player.sendFriendRequest(friendId);
            targetPlayer.receiveFriendRequest(player.getPlayerId());

            StorageManager.get().storePlayer(player);
            StorageManager.get().storePlayer(targetPlayer);

        }

    }

    public void acceptFriendRequest(Player player, int friendId) {

        Player targetPlayer = PlayerManager.get().getPlayer(friendId);

        if (targetPlayer != null) {

            if (targetPlayer.isFriendRequestReceived(player.getPlayerId()) && player.isFriendRequestSent(friendId)) {

                player.acceptFriendRequest(friendId);
                targetPlayer.acceptFriendRequest(player.getPlayerId());

                StorageManager.get().storePlayer(player);
                StorageManager.get().storePlayer(targetPlayer);

            }

        }

    }
    public void rejectFriendRequest(Player player, int friendId) {

        Player requester = PlayerManager.get().getPlayer(friendId);

        if (requester != null) {

            if (requester.isFriendRequestSent(player.getPlayerId()) && player.isFriendRequestReceived(friendId)) {

                player.rejectFriendRequest(friendId);
                requester.cancelFriendRequest(player.getPlayerId());

                StorageManager.get().storePlayer(player);
                StorageManager.get().storePlayer(requester);

            }

        }

    }

    public void removeFriend(Player player, int friendId) {

        Player friend = PlayerManager.get().getPlayer(friendId);

        if (friend != null) {

            if (player.hasFriend(friendId)) {

                player.removeFriend(friendId);
                friend.removeFriend(player.getPlayerId());

                StorageManager.get().storePlayer(player);
                StorageManager.get().storePlayer(friend);

            }

        }

    }

}
