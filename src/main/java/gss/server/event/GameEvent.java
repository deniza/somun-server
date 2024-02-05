package gss.server.event;

import gss.server.model.GameSession;

public class GameEvent implements EventManager.SomunEvent {

    public GameSession session;

    public GameEvent(GameSession session) {
        this.session = session;
    }

    public static class GameCreated extends GameEvent {

        public GameCreated(GameSession session) {
            super(session);
        }
    }

    public static class GameFinished extends GameEvent {

        public GameFinished(GameSession session) {
            super(session);
        }

    }

    public static class PlayerMakeMove extends GameEvent {

        public int playerId;
        public String jsonData;

        public PlayerMakeMove(GameSession session, int playerId, String jsonData) {
            super(session);
            this.playerId = playerId;
            this.jsonData = jsonData;
        }

    }

}