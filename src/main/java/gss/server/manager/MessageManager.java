package gss.server.manager;

import gss.server.manager.storage.StorageManager;
import gss.server.model.Player;
import gss.server.util.ArrayHelper;

public class MessageManager {

    private static volatile MessageManager instance;

    private MessageManager() {
        // Prevent form the reflection api.
        if (instance != null) {
            throw new RuntimeException("Use get() method to get the single instance of this class.");
        }
    }

    public static MessageManager get() {
        // Double check locking pattern
        if (instance == null) {
            synchronized (MessageManager.class) {
                if (instance == null) {
                    instance = new MessageManager();
                }
            }
        }
        return instance;
    }

    public void sendMessageList(Player player) {

        ConnectionManager.get().call(player, "Friends", "messageList", ArrayHelper.toIntArray(player.getPrivateMessages()));

    }

    public void sendPrivateMessage(Player player, int receiverId, String message) {

        if (player.hasFriend(receiverId)) {

            StorageManager.get().storePrivateMessage(player.getPlayerId(), receiverId, message);

        }

    }

    public void readPrivateMessage(Player player, int messageId) {

        String content = player.getMessageContent(messageId);

        StorageManager.get().markPrivateMessageRead(player.getPlayerId(), messageId);

        ConnectionManager.get().call(player, "Friends", "privateMessageContent", messageId, content);

    }

    public void deletePrivateMessage(Player player, int messageId) {

        StorageManager.get().deletePrivateMessage(player.getPlayerId(), messageId);

    }

    public static class PrivateMessage {

        public int messageId;
        public int senderId;
        public int receiverId;
        public long date;
        public boolean readFlag;
        public String content;

        public PrivateMessage(int messageId, int senderId, int receiverId, boolean readFlag, long date, String content) {
            this.messageId = messageId;
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.date = date;
            this.readFlag = readFlag;
            this.content = content;
        }

    }

}
