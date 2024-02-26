package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.groups.Group;
import gss.server.manager.groups.GroupsManager;
import gss.server.model.Player;

public class Groups extends GssInterface {

    public void createGroup(String groupName, GssConnection con) {

        GssLogger.info("[Groups] createGroup called");

        Player player = getPlayer(con);

        GroupsManager.get().createGroup(player, groupName, Group.GroupType.PUBLIC);

        con.invokeMethod("Groups_createGroupResponse", new Object[]{1});

    }

    public void requestJoinGroup(int groupId, GssConnection con) {

        GssLogger.info("[Groups] requestJoinGroup called");

        Player player = getPlayer(con);

        GroupsManager.get().requestJoinGroup(player, groupId);

    }

    public void leaveGroup(int groupId, GssConnection con) {

        GssLogger.info("[Groups] leaveGroup called");

        Player player = getPlayer(con);

        GroupsManager.get().leaveGroup(player, groupId);

    }

    public void processJoinRequest(int groupId, int requesterId, int accepted, GssConnection con) {

        GssLogger.info("[Groups] processJoinRequest called");

        Player player = getPlayer(con);

        GroupsManager.get().processJoinRequest(player, groupId, requesterId, accepted == 1);

    }

    public void inviteToJoinGroup(int groupId, int inviteeId, GssConnection con) {

        GssLogger.info("[Groups] inviteToJoinGroup called");

        Player player = getPlayer(con);

        GroupsManager.get().inviteToJoinGroup(player, inviteeId, groupId);

    }

    public void processGroupInvitation(int groupId, int accepted, GssConnection con) {

        GssLogger.info("[Groups] processGroupInvitation called");

        Player player = getPlayer(con);

        GroupsManager.get().processGroupInvitation(player, groupId, accepted == 1);

    }

    public void kickFromGroup(int groupId, int playerId, GssConnection con) {

        GssLogger.info("[Groups] kickFromGroup called");

        Player player = getPlayer(con);

        GroupsManager.get().kickFromGroup(player, playerId, groupId);

    }

    public void setGroupType(int groupId, int groupType, GssConnection con) {

        GssLogger.info("[Groups] setGroupType called");

        Player player = getPlayer(con);

        GroupsManager.get().setGroupType(player, groupId, groupType);

    }

}
