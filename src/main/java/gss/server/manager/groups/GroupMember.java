package gss.server.manager.groups;

public class GroupMember {

    public enum GroupMemberRole {
        OWNER,  // The owner of the group. There can only be one owner.
        ADMIN,  // An admin of the group. Can invite, kick, and promote members.
        MEMBER  // A regular member of the group.
    };

    private int groupId;
    private int playerId;
    private GroupMemberRole role;

}
