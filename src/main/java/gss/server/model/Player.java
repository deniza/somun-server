package gss.server.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Player {

    private final int playerId;
    private String name;
    private String password;
    private ArrayList<Integer> gameIds = new ArrayList<>();
    private HashSet<Integer> friends = new HashSet<>();
    private HashSet<Integer> friendRequestsSent = new HashSet<>();
    private HashSet<Integer> friendRequestsReceived = new HashSet<>();
    private boolean online;
    private int activeGameId;

    public Player(int playerId) {
        this.playerId = playerId;
        setName("");
        setPassword("");
        setOnline(false);
        setActiveGameId(-1);
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


}
