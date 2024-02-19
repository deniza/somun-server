package gss.server.manager;

import gss.server.manager.storage.StorageManager;
import gss.server.model.Player;

public class FriendsManager {

    private static FriendsManager instance;

    private FriendsManager() {}

    public static FriendsManager get() {
        if (instance == null) {
            instance = new FriendsManager();
        }
        return instance;
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

        player.rejectFriendRequest(friendId);

        StorageManager.get().storePlayer(player);

    }

    public void removeFriend(Player player, int friendId) {

        player.removeFriend(friendId);

        StorageManager.get().storePlayer(player);

    }

}
