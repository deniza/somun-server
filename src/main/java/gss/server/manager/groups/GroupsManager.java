package gss.server.manager.groups;

import gss.server.manager.ConnectionManager;
import gss.server.manager.storage.StorageManager;
import gss.server.model.Player;
import gss.server.model.ServiceUpdateInterface;

import java.util.HashMap;

public class GroupsManager implements ServiceUpdateInterface {

    private static GroupsManager instance;

    private final HashMap<Integer, Group> groups;

    private GroupsManager() {

        groups = StorageManager.get().loadAllGroups();

    }

    public static GroupsManager get() {
        if (instance == null) {
            instance = new GroupsManager();
        }
        return instance;
    }

    public void createGroup(Player player, String name, Group.GroupType type) {

        int groupId = StorageManager.get().getAndIncrementConfigValue("nextGroupId");

        Group group = new Group(groupId, name, type, player.getPlayerId());
        group.setOwner(player.getPlayerId());

        groups.put(group.getGroupId(), group);
        player.addGroup(group.getGroupId());

        StorageManager.get().storeGroup(group);
        StorageManager.get().storePlayer(player);

    }

    public void requestJoinGroup(Player player, int groupId) {

        Group group = groups.get(groupId);

        if (group == null) {
            ConnectionManager.get().call(player,"Groups","joinGroupResponse", 0, "group not found");
            return;
        }

        if (group.isAllowedToJoin()) {

            group.addJoinRequest(player.getPlayerId());
            StorageManager.get().storeGroup(group);

            ConnectionManager.get().call(player, "Groups", "joinGroupResponse", 1, "request saved");

        }
        else {
            ConnectionManager.get().call(player, "Groups", "joinGroupResponse", 0, "group not allowed to join");
        }

    }

    public void leaveGroup(Player player, int groupId) {

        Group group = groups.get(groupId);

        if (group == null) {
            ConnectionManager.get().call(player, "Groups", "leaveGroupResponse", 0, "group not found");
            return;
        }

        if (group.getOwnerId() == player.getPlayerId()) {
            ConnectionManager.get().call(player, "Groups", "leaveGroupResponse", 0, "owner cannot leave group");
            return;
        }

        group.removeMember(player.getPlayerId());
        player.removeGroup(groupId);

        StorageManager.get().storeGroup(group);
        StorageManager.get().storePlayer(player);

        ConnectionManager.get().call(player, "Groups", "leaveGroupResponse", 1, "left group");

    }

    public void processJoinRequest(Player player, int groupId, int requesterId, boolean accepted) {

        Group group = groups.get(groupId);

        if (group == null) {
            ConnectionManager.get().call(player, "Groups", "processJoinRequestResponse", 0, "group not found");
            return;
        }

        if (group.isOwner(player.getPlayerId()) == false && group.isAdmin(player.getPlayerId()) == false) {
            ConnectionManager.get().call(player, "Groups", "processJoinRequestResponse", 0, "restricted to process");
            return;
        }

        if (group.hasJoinRequest(requesterId) == false) {
            ConnectionManager.get().call(player, "Groups", "processJoinRequestResponse", 0, "player not found in join requests");
            return;
        }

        group.removeJoinRequest(requesterId);

        if (accepted) {
            group.addMember(requesterId);

            Player member = StorageManager.get().loadPlayer(requesterId);
            member.addGroup(groupId);

            StorageManager.get().storePlayer(member);

            ConnectionManager.get().call(player, "Groups", "processJoinRequestResponse", 1, "player added to group");

        }
        else {

            ConnectionManager.get().call(player, "Groups", "processJoinRequestResponse", 1, "request rejected");

        }

        StorageManager.get().storeGroup(group);

    }

    public void inviteToJoinGroup(Player player, int inviteeId, int groupId) {

        Group group = groups.get(groupId);

        if (group == null) {
            ConnectionManager.get().call(player, "Groups", "inviteToJoinGroupResponse", 0, "group not found");
            return;
        }

    }

    @Override
    public void updateService(long deltaTime) {
    }
}
