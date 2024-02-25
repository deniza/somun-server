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

}