package gss.server.manager.groups;

import gss.server.model.ServiceUpdateInterface;

import java.util.HashMap;

public class GroupsManager implements ServiceUpdateInterface {

    private static GroupsManager instance;

    private final HashMap<Integer, Group> groups = new HashMap<>();

    private GroupsManager() {
    }

    public static GroupsManager get() {
        if (instance == null) {
            instance = new GroupsManager();
        }
        return instance;
    }

    @Override
    public void updateService(long deltaTime) {
    }
}
