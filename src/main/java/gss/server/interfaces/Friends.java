package gss.server.interfaces;

import gss.GssLogger;
import gss.network.GssConnection;
import gss.network.GssInterface;

public class Friends extends GssInterface {

    public void requestAddFriend(int playerId, GssConnection con) {

        GssLogger.info("[Friends] requestAddFriend called");

    }

    public void requestAcceptFriend(int playerId, GssConnection con) {

        GssLogger.info("[Friends] requestAcceptFriend called");

    }

    public void requestRejectFriend(int playerId, GssConnection con) {

        GssLogger.info("[Friends] requestRejectFriend called");

    }

}
