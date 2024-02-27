package gss.server.manager.groups;

public class GroupMessage {

    private int messageId;
    private int groupId;
    private int senderId;
    private String message;
    private long timestamp;

    public GroupMessage(int messageId, int groupId, int senderId, String message, long timestamp) {
        this.messageId = messageId;
        this.groupId = groupId;
        this.senderId = senderId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
