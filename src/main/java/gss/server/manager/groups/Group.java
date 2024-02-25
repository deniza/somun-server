package gss.server.manager.groups;

import java.util.ArrayList;

public class Group {

    public enum GroupType {
        PUBLIC, PRIVATE
    };

    private int groupId;

    private String name;

    private GroupType type;

    private int ownerId;

    private final ArrayList<GroupMember> members;
    private final ArrayList<GroupMember> admins;

    private final ArrayList<GroupInvitation> invitations;

    public Group(int groupId, String name, GroupType type, int ownerId) {
        this.groupId = groupId;
        this.name = name;
        this.type = type;
        this.ownerId = ownerId;
        this.members = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.invitations = new ArrayList<>();
    }

}
