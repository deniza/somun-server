package gss.server.manager.groups;

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

    @Override
    public void updateService(long deltaTime) {
    }
}
