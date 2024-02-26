package gss.server.manager.groups;

import gss.server.manager.ConnectionManager;
import gss.server.manager.storage.StorageManager;
import gss.server.model.Player;
import gss.server.model.ServiceUpdateInterface;
import gss.server.util.ArrayHelper;

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

            // promote a new owner from admins
            if (group.promoteAdminToOwner()) {
                // new owner found
                StorageManager.get().storeGroup(group);
                ConnectionManager.get().call(player, "Groups", "leaveGroupResponse", 1, "left group");
            }
            else {
                // no new owner found, delete group
                for (GroupMember groupMember : group.getMembers()) {
                    Player member = StorageManager.get().loadPlayer(groupMember.getPlayerId());
                    member.removeGroup(groupId);
                    StorageManager.get().storePlayer(member);
                }

                groups.remove(groupId);
                StorageManager.get().deleteGroup(groupId);

                ConnectionManager.get().call(player, "Groups", "leaveGroupResponse", 2, "group deleted");

            }

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

        if (group.isOwner(player.getPlayerId()) == false && group.isAdmin(player.getPlayerId()) == false) {
            ConnectionManager.get().call(player, "Groups", "inviteToJoinGroupResponse", 2, "restricted to process");
            return;
        }

        Player invitee = StorageManager.get().loadPlayer(inviteeId);
        if (invitee == null) {
            ConnectionManager.get().call(player, "Groups", "inviteToJoinGroupResponse", 3, "invitee not found");
            return;
        }

        group.invite(player.getPlayerId(), inviteeId);
        invitee.addGroupInvitation(groupId);

        StorageManager.get().storeGroup(group);
        StorageManager.get().storePlayer(invitee);

        ConnectionManager.get().call(player, "Groups", "inviteToJoinGroupResponse", 1, "invite sent");

    }

    public void processGroupInvitation(Player player, int groupId, boolean accepted) {

        Group group = groups.get(groupId);

        if (group == null) {
            ConnectionManager.get().call(player, "Groups", "processGroupInvitationResponse", 0, "group not found");
            return;
        }

        if (player.hasGroupInvitation(groupId) == false) {
            ConnectionManager.get().call(player, "Groups", "processGroupInvitationResponse", 2, "no invitation found");
            return;
        }

        if (accepted) {

            group.addMember(player.getPlayerId());
            player.addGroup(groupId);

            group.removeInvitation(player.getPlayerId());
            player.removeGroupInvitation(groupId);

            StorageManager.get().storeGroup(group);
            StorageManager.get().storePlayer(player);

            ConnectionManager.get().call(player, "Groups", "processGroupInvitationResponse", 1, "invitation accepted");

        }
        else {
            ConnectionManager.get().call(player, "Groups", "processGroupInvitationResponse", 3, "invitation rejected");
        }

    }

    public void kickFromGroup(Player player, int groupId, int memberToKickId) {

        Group group = groups.get(groupId);

        if (group == null) {
            ConnectionManager.get().call(player, "Groups", "kickFromGroup", 0, "group not found");
            return;
        }

        if (group.isOwner(player.getPlayerId()) == false && group.isAdmin(player.getPlayerId()) == false) {
            ConnectionManager.get().call(player, "Groups", "kickFromGroup", 2, "restricted to process");
            return;
        }

        if (group.isOwner(memberToKickId)) {
            ConnectionManager.get().call(player, "Groups", "kickFromGroup", 3, "owner cannot be kicked");
            return;
        }

        if (group.isOwner(player.getPlayerId()) == false && group.isAdmin(memberToKickId)) {
            // only owner can kick admin
            ConnectionManager.get().call(player, "Groups", "kickFromGroup", 4, "restricted to process");
            return;
        }

        group.removeMember(memberToKickId);

        Player member = StorageManager.get().loadPlayer(memberToKickId);
        member.removeGroup(groupId);

        StorageManager.get().storeGroup(group);
        StorageManager.get().storePlayer(member);

        ConnectionManager.get().call(player, "Groups", "kickFromGroup", 1, "member kicked");

    }

    public void setGroupType(Player player, int groupId, int groupType) {

        Group group = groups.get(groupId);

        if (group == null) {
            ConnectionManager.get().call(player, "Groups", "setGroupType", 0, "group not found");
            return;
        }

        if (group.isOwner(player.getPlayerId()) == false) {
            ConnectionManager.get().call(player, "Groups", "setGroupType", 2, "restricted to process");
            return;
        }

        group.setType(Group.GroupType.values()[groupType]);

        StorageManager.get().storeGroup(group);

        ConnectionManager.get().call(player, "Groups", "setGroupType", 1, "group type set");

    }

    public void requestGroupList(Player player, int startId, int count) {

        int[] groupIds = groups.keySet().stream().mapToInt(Integer::intValue).toArray();

        ConnectionManager.get().call(player, "Groups", "groupList", groupIds);

    }

    public void requestGroupInfo(Player player, int groupId) {

        Group group = groups.get(groupId);

        if (group == null) {
            ConnectionManager.get().call(player, "Groups", "groupInfo", 0, 0, "", "", 0, 0, 0);
            return;
        }

        ConnectionManager.get().call(player, "Groups", "groupInfo", 1, group.getGroupId(), group.getName(), group.getDescription(), group.getType().ordinal(), group.getOwnerId(), group.getMembers().size());

    }

    public void requestGroupMembers(Player player, int groupId) {

        Group group = groups.get(groupId);

        if (group == null) {
            ConnectionManager.get().call(player, "Groups", "groupMembers", 0, 0);
            return;
        }

        if (group.isMember(player.getPlayerId()) == false) {
            // only members can request members
            ConnectionManager.get().call(player, "Groups", "groupMembers", 2, 0);
            return;
        }

        int[] memberIds = group.getMembers().stream().mapToInt(GroupMember::getPlayerId).toArray();

        ConnectionManager.get().call(player, "Groups", "groupMembers", 1, memberIds);

    }

    @Override
    public void updateService(long deltaTime) {
    }
}
