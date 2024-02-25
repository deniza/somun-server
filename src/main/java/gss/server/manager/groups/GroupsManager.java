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

    public void joinGroup(Player player, int groupId) {

        Group group = groups.get(groupId);

        if (group != null) {

            if (group.isAllowedToJoin()) {

                group.addJoinRequest(player.getPlayerId());
                StorageManager.get().storeGroup(group);

                ConnectionManager.get().call(player, "Groups", "joinGroupResponse", 1, "request saved");

            }
            else {
                ConnectionManager.get().call(player, "Groups", "joinGroupResponse", 0, "group not allowed to join");
            }

        }
        else {
            ConnectionManager.get().call(player,"Groups","joinGroupResponse", 0, "group not found");
        }

    }

    public void leaveGroup(Player player, int groupId) {

        Group group = groups.get(groupId);

        if (group != null) {

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
        else {
            ConnectionManager.get().call(player, "Groups", "leaveGroupResponse", 0, "group not found");
        }

    }

    public void acceptJoinRequest(Player player, int groupId, int requesterId) {

        Group group = groups.get(groupId);

        if (group != null) {

            if (group.isOwner(player.getPlayerId()) || group.isAdmin(player.getPlayerId())) {

                if (group.hasJoinRequest(requesterId)) {

                    group.removeJoinRequest(requesterId);
                    group.addMember(requesterId);

                    Player member = StorageManager.get().loadPlayer(requesterId);
                    member.addGroup(groupId);

                    StorageManager.get().storeGroup(group);
                    StorageManager.get().storePlayer(member);

                    ConnectionManager.get().call(player, "Groups", "acceptJoinRequestResponse", 1, "player added to group");

                }
                else {
                    ConnectionManager.get().call(player, "Groups", "acceptJoinRequestResponse", 0, "player not found in join requests");
                }

            }
            else {
                ConnectionManager.get().call(player, "Groups", "acceptJoinRequestResponse", 0, "not owner of group");
            }

        }
        else {
            ConnectionManager.get().call(player, "Groups", "acceptJoinRequestResponse", 0, "group not found");
        }

    }

    @Override
    public void updateService(long deltaTime) {
    }
}
