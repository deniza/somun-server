package gss.server.interfaces;

import gss.network.GssConnection;
import gss.network.GssInterface;
import gss.server.manager.ConnectionManager;
import gss.server.manager.GameManager;
import gss.server.manager.PlayerManager;
import gss.server.model.Player;

public class Connection extends GssInterface  {

    @Override
    public void clientDisconnected(GssConnection con) {

        Player player = getPlayer(con);

        if (player != null) {
            GameManager.get().playerDisconnected(player);
            PlayerManager.get().playerDisconnected(player);
        }

        ConnectionManager.get().unregister(con);

    }

}
