package gss.server.manager.groups;

public class GroupInvitation {

    private int groupId;
    private int playerId;
    private int inviterId;

    public GroupInvitation(int groupId, int inviterId, int playerId) {
        this.groupId = groupId;
        this.playerId = playerId;
        this.inviterId = inviterId;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getInviterId() {
        return inviterId;
    }


}
