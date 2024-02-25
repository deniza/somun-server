package gss.server.manager.groups;

public class GroupJoinRequest {

    private int groupId;

    private int playerId;

    public GroupJoinRequest(int groupId, int playerId) {
        this.groupId = groupId;
        this.playerId = playerId;
    }

}
