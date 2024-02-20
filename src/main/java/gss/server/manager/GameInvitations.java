package gss.server.manager;

import gss.server.manager.storage.StorageManager;
import gss.server.util.Time;

import java.util.ArrayList;
import java.util.HashMap;

public class GameInvitations {

    private final HashMap<Integer, InvitationRequest> invitations = new HashMap<>();  // invitationId, Invitation
    private final HashMap<Integer, ArrayList<InvitationRequest>> inviteeInvitationMap = new HashMap<>();  // inviteeId, Invitation(s)

    public GameInvitations() {
    }

    public void init() {

        ArrayList<InvitationRequest> invitationRequests = StorageManager.get().loadAllInvitations();
        for (InvitationRequest req : invitationRequests) {

            invitations.put(req.invitationId, req);

            ArrayList<InvitationRequest> reqs = inviteeInvitationMap.get(req.invitee);
            if (reqs == null) {
                reqs = new ArrayList<InvitationRequest>();
                inviteeInvitationMap.put(req.invitee, reqs);
            }
            reqs.add(req);

        }

    }

    public synchronized void createInvitation(int inviter, int invitee,  int gametype, boolean shouldStartAllOnline) {

        InvitationRequest inv = new InvitationRequest();
        inv.invitationId = 0;
        inv.inviter = inviter;
        inv.invitee = invitee;
        inv.gametype = gametype;
        inv.shouldStartAllOnline = shouldStartAllOnline;
        inv.date = Time.now();

        int invitationId = StorageManager.get().createInvitation(inv);

        inv.invitationId = invitationId;
        invitations.put(invitationId, inv);

        ArrayList<InvitationRequest> reqs = inviteeInvitationMap.get(invitee);
        if (reqs == null) {
            reqs = new ArrayList<InvitationRequest>();
            inviteeInvitationMap.put(invitee, reqs);
        }
        reqs.add(inv);

    }

    public synchronized void removeInvitation(int invitationId) {

        InvitationRequest req = invitations.get(invitationId);
        ArrayList<InvitationRequest> reqs = inviteeInvitationMap.get(req.invitee);
        if (reqs != null) {
            reqs.remove(req);
            if (reqs.isEmpty()) {
                inviteeInvitationMap.remove(req.invitee);
            }
        }

        invitations.remove(invitationId);

    }

    public ArrayList<Integer> getInvitationList(int invitee) {

        ArrayList<Integer> list = new ArrayList<>();

        ArrayList<InvitationRequest> reqs = inviteeInvitationMap.get(invitee);
        if (reqs != null) {
            for (InvitationRequest req : reqs) {
                list.add(req.invitationId);
            }
        }

        return list;

    }

    public static class InvitationRequest {

        public int invitationId;
        public int inviter;
        public int invitee;
        public long date;
        public int gametype;
        public boolean shouldStartAllOnline;

    }

}
