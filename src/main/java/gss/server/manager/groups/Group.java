package gss.server.manager.groups;

import gss.server.util.Time;

import java.util.ArrayList;
import java.util.HashSet;

public class Group {

    public enum GroupType {
        PUBLIC,   // anyone can request to join
        PRIVATE   // only invited players can join
    };

    private int groupId;

    private String name;

    private String description;

    private GroupType type;

    private int ownerId;

    private final ArrayList<GroupMember> members;
    private final ArrayList<GroupMember> admins;

    private final ArrayList<GroupInvitation> invitations;

    private final HashSet<Integer> joinRequests;  // playerId

    private final ArrayList<GroupMessage> messages;

    public Group(int groupId, String name, GroupType type, int ownerId) {

        this.groupId = groupId;
        this.members = new ArrayList<>();
        this.admins = new ArrayList<>();
        this.invitations = new ArrayList<>();
        this.joinRequests = new HashSet<>();
        this.messages = new ArrayList<>();

        setName(name);
        setType(type);
        setOwner(ownerId);
        setDescription("");
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

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(GroupType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GroupType getType() {
        return type;
    }

    public boolean isAllowedToJoin() {
        return type == GroupType.PUBLIC;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public boolean isOwner(int playerId) {
        return ownerId == playerId;
    }

    public boolean isAdmin(int playerId) {
        return admins.stream().anyMatch(admin -> admin.getPlayerId() == playerId);
    }

    public boolean isMember(int playerId) {
        return members.stream().anyMatch(member -> member.getPlayerId() == playerId);
    }

    public void changeMemberRole(int playerId, GroupMember.GroupMemberRole newRole) {

        members.stream().filter(member -> member.getPlayerId() == playerId).forEach(member -> {
            if (newRole == GroupMember.GroupMemberRole.ADMIN) {
                admins.add(new GroupMember(groupId, playerId, GroupMember.GroupMemberRole.ADMIN));
            }
            else if (newRole == GroupMember.GroupMemberRole.MEMBER) {
                admins.removeIf(admin -> admin.getPlayerId() == playerId);
            }
            member.setRole(newRole);
        });

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

    public HashSet<Integer> getJoinRequests() {
        return joinRequests;
    }

    public void addJoinRequest(int playerId) {
        joinRequests.add(playerId);
    }

    public boolean hasJoinRequest(int playerId) {
        return joinRequests.contains(playerId);
    }

    public void removeJoinRequest(int playerId) {
        joinRequests.remove(playerId);
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

    public void removeInvitation(int playerId) {
        invitations.removeIf(invitation -> invitation.getPlayerId() == playerId);
    }

    public boolean hasInvitation(int playerId) {
        return invitations.stream().anyMatch(invitation -> invitation.getPlayerId() == playerId);
    }

    public void promoteToAdmin(int playerId) {
        removeMember(playerId);
        addAdmin(playerId);
    }

    public void demoteToMember(int playerId) {
        removeAdmin(playerId);
        addMember(playerId);
    }

    public boolean promoteAdminToOwner() {
        for (GroupMember admin : admins) {
            setOwner(admin.getPlayerId());
            removeAdmin(admin.getPlayerId());
            return true;
        }
        // no admin to promote
        return false;
    }

    public void removeAdmin(int playerId) {
        admins.removeIf(admin -> admin.getPlayerId() == playerId);
    }

    public void removeMember(int playerId) {
        members.removeIf(member -> member.getPlayerId() == playerId);
        admins.removeIf(admin -> admin.getPlayerId() == playerId);
    }

    public GroupMessage addMessage(int senderId, String message) {
        GroupMessage groupMessage = new GroupMessage(0, groupId, senderId, message, Time.now());
        messages.add(groupMessage);
        return groupMessage;
    }

    public ArrayList<GroupMessage> getMessages() {
        return messages;
    }

    public ArrayList<GroupMessage> getMessages(int page, int pageSize) {
        int start = page * pageSize;
        int end = start + pageSize;
        if (end > messages.size()) {
            end = messages.size();
        }
        return new ArrayList<>(messages.subList(start, end));
    }

    public int getMessagesCount() {
        return messages.size();
    }

    public void clearMessages() {
        messages.clear();
    }

}
