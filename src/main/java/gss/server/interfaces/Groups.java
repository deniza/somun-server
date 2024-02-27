package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssCallable;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.groups.Group;
import gss.server.manager.groups.GroupsManager;
import gss.server.model.Player;

public class Groups extends GssInterface {

    @GssCallable
    public void createGroup(String groupName, GssConnection con) {

        GssLogger.info("[Groups] createGroup called");

        Player player = getPlayer(con);

        GroupsManager.get().createGroup(player, groupName, Group.GroupType.PUBLIC);

        con.invokeMethod("Groups_createGroupResponse", new Object[]{1});

    }

    @GssCallable
    public void requestJoinGroup(int groupId, GssConnection con) {

        GssLogger.info("[Groups] requestJoinGroup called");

        Player player = getPlayer(con);

        GroupsManager.get().requestJoinGroup(player, groupId);

    }

    @GssCallable
    public void leaveGroup(int groupId, GssConnection con) {

        GssLogger.info("[Groups] leaveGroup called");

        Player player = getPlayer(con);

        GroupsManager.get().leaveGroup(player, groupId);

    }

    @GssCallable
    public void processJoinRequest(int groupId, int requesterId, int accepted, GssConnection con) {

        GssLogger.info("[Groups] processJoinRequest called");

        Player player = getPlayer(con);

        GroupsManager.get().processJoinRequest(player, groupId, requesterId, accepted == 1);

    }

    @GssCallable
    public void inviteToJoinGroup(int groupId, int inviteeId, GssConnection con) {

        GssLogger.info("[Groups] inviteToJoinGroup called");

        Player player = getPlayer(con);

        GroupsManager.get().inviteToJoinGroup(player, inviteeId, groupId);

    }

    @GssCallable
    public void processGroupInvitation(int groupId, int accepted, GssConnection con) {

        GssLogger.info("[Groups] processGroupInvitation called");

        Player player = getPlayer(con);

        GroupsManager.get().processGroupInvitation(player, groupId, accepted == 1);

    }

    @GssCallable
    public void kickFromGroup(int groupId, int playerId, GssConnection con) {

        GssLogger.info("[Groups] kickFromGroup called");

        Player player = getPlayer(con);

        GroupsManager.get().kickFromGroup(player, playerId, groupId);

    }

    @GssCallable
    public void setGroupType(int groupId, int groupType, GssConnection con) {

        GssLogger.info("[Groups] setGroupType called");

        Player player = getPlayer(con);

        GroupsManager.get().setGroupType(player, groupId, groupType);

    }

    @GssCallable
    public void requestGroupList(int startId, int count, GssConnection con) {

        GssLogger.info("[Groups] requestGroupList called");

        Player player = getPlayer(con);

        GroupsManager.get().requestGroupList(player, startId, count);

    }

    @GssCallable
    public void requestGroupInfo(int groupId, GssConnection con) {

        GssLogger.info("[Groups] requestGroupInfo called");

        Player player = getPlayer(con);

        GroupsManager.get().requestGroupInfo(player, groupId);

    }

    @GssCallable
    public void requestGroupMembers(int groupId, GssConnection con) {

        GssLogger.info("[Groups] requestGroupMembers called");

        Player player = getPlayer(con);

        GroupsManager.get().requestGroupMembers(player, groupId);

    }

    @GssCallable
    public void requestGroupJoinRequests(int groupId, GssConnection con) {

        GssLogger.info("[Groups] requestGroupJoinRequests called");

        Player player = getPlayer(con);

        GroupsManager.get().requestGroupJoinRequests(player, groupId);

    }

    @GssCallable
    public void changeGroupMemberRole(int groupId, int playerId, int role, GssConnection con) {

        GssLogger.info("[Groups] changeGroupMemberRole called");

        Player player = getPlayer(con);

        GroupsManager.get().changeGroupMemberRole(player, groupId, playerId, role);

    }

    @GssCallable
    public void changeGroupDescription(int groupId, String description, GssConnection con) {

        GssLogger.info("[Groups] changeGroupDescription called");

        Player player = getPlayer(con);

        GroupsManager.get().changeGroupDescription(player, groupId, description);

    }

}
