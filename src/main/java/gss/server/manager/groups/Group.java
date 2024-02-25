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

    public int getGroupId() {
        return groupId;
    }

    public void setOwner(int playerId) {
        ownerId = playerId;
        members.add(new GroupMember(groupId, playerId, GroupMember.GroupMemberRole.OWNER));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public GroupType getType() {
        return type;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public ArrayList<GroupMember> getMembers() {
        return members;
    }

    public ArrayList<GroupMember> getAdmins() {
        return admins;
    }

    public ArrayList<GroupInvitation> getInvitations() {
        return invitations;
    }

    public void addMember(int playerId) {
        addMember(playerId, GroupMember.GroupMemberRole.MEMBER);
    }

    public void addMember(int playerId, GroupMember.GroupMemberRole role) {
        members.add(new GroupMember(groupId, playerId, role));
    }

    public void addAdmin(int playerId) {
        admins.add(new GroupMember(groupId, playerId, GroupMember.GroupMemberRole.ADMIN));
    }

    public void invite(int inviterId, int playerId) {
        invitations.add(new GroupInvitation(groupId, playerId, inviterId));
    }

    public void promoteToAdmin(int playerId) {
        removeMember(playerId);
        addAdmin(playerId);
    }

    public void demoteToMember(int playerId) {
        removeAdmin(playerId);
        addMember(playerId);
    }

    public void removeAdmin(int playerId) {
        admins.removeIf(admin -> admin.getPlayerId() == playerId);
    }

    public void removeMember(int playerId) {
        members.removeIf(member -> member.getPlayerId() == playerId);
    }

}
