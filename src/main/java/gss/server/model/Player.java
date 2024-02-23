package gss.server.model;

import gss.server.manager.MessageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Player {

    public enum DeviceType {
        NotDefined,
        IOS,
        ANDROID
    }

    private int playerId;
    private String name;
    private String password;
    private ArrayList<Integer> gameIds = new ArrayList<>();
    private HashSet<Integer> friends = new HashSet<>();
    private HashSet<Integer> friendRequestsSent = new HashSet<>();
    private HashSet<Integer> friendRequestsReceived = new HashSet<>();
    private HashMap<Integer, MessageManager.PrivateMessage> privateMessages = new HashMap<>();
    private boolean online;
    private String messagingToken; // push notification token
    private DeviceType deviceType;
    private int activeGameId;

    public Player(int playerId) {

        this.playerId = playerId;
        setName("");
        setPassword("");
        setOnline(false);
        setActiveGameId(-1);

        setMessagingToken("");
        setDeviceType(DeviceType.NotDefined);
    }

    public Player() {
        this(0);
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public ArrayList<Integer> getGameIds() {
        return gameIds;
    }

    public void setGameIds(ArrayList<Integer> gameIds) {
        this.gameIds = gameIds;
    }

    public void addGameId(int gameId) {
        gameIds.add(gameId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMessagingToken(String token) {
        messagingToken = token;
    }

    public String getMessagingToken() {
        return messagingToken;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    public void setActiveGameId(int gameId) {
        this.activeGameId = gameId;
    }
    public int getActiveGameId() {
        return activeGameId;
    }

    public HashSet<Integer> getFriends() {
        return friends;
    }
    public HashSet<Integer> getFriendRequestsSent() {
        return friendRequestsSent;
    }
    public HashSet<Integer> getFriendRequestsReceived() {
        return friendRequestsReceived;
    }

    public void setFriends(List<Integer> friends) {
        friends.addAll(friends);
    }
    public void setFriendRequestsSent(List<Integer> friends) {
        friendRequestsSent.addAll(friends);
    }
    public void setFriendRequestsReceived(List<Integer> friends) {
        friendRequestsReceived.addAll(friends);
    }

    public void sendFriendRequest(int friendId) {
        friendRequestsSent.add(friendId);
    }

    public void receiveFriendRequest(int requesterId) {
        friendRequestsReceived.add(requesterId);
    }

    public void acceptFriendRequest(int friendId) {
        friendRequestsReceived.remove(friendId);
        friendRequestsSent.remove(friendId);
        friends.add(friendId);
    }

    public void rejectFriendRequest(int friendId) {
        friendRequestsReceived.remove(friendId);
    }

    public void cancelFriendRequest(int friendId) {
        friendRequestsSent.remove(friendId);
    }

    public void removeFriend(int friendId) {
        friends.remove(friendId);
    }

    public boolean hasFriend(int friendId) {
        return friends.contains(friendId);
    }

    public boolean isFriendRequestReceived(int playerId) {
        return friendRequestsReceived.contains(playerId);
    }
    public boolean isFriendRequestSent(int playerId) {
        return friendRequestsSent.contains(playerId);
    }

    public void setPrivateMessages(List<MessageManager.PrivateMessage> messages) {
        for (MessageManager.PrivateMessage pmsg : messages) {
            privateMessages.put(pmsg.messageId, pmsg);
        }
    }

    public ArrayList<Integer> getPrivateMessages() {

        return new ArrayList<>(privateMessages.keySet());

    }

    public String getMessageContent(int messageId) {

        MessageManager.PrivateMessage message = privateMessages.get(messageId);
        if (message != null) {

            return message.content;

        }
        else {
            return "";
        }

    }

}
