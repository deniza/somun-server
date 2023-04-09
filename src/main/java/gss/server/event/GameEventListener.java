package gss.server.event;

import java.util.ArrayList;

import gss.server.event.EventManager.SomunEvent;
import gss.server.event.EventManager.SomunEventListener;
import gss.server.model.GameSession;

public class GameEventListener implements SomunEventListener {

    private static final GameEventListener instance = new GameEventListener();

    private ArrayList<GameEventHandler> handlers = new ArrayList<>();

    public static GameEventListener get() {
        return instance;
    }

    @Override
    public void onEvent(SomunEvent e) {

        if (e instanceof GameCreated) {
            GameCreated event = (GameCreated) e;
            for (GameEventHandler handler : handlers) {
                handler.onGameCreated(event.session);
            }
        }
        else if (e instanceof GameFinished) {
            GameFinished event = (GameFinished) e;
            for (GameEventHandler handler : handlers) {
                handler.onGameFinished(event.session);
            }
        }
        else if (e instanceof PlayerMakeMove) {
            PlayerMakeMove event = (PlayerMakeMove) e;
            for (GameEventHandler handler : handlers) {
                handler.onPlayerMakeMove(event.session, event.jsonData);
            }
        }

    }

    public void addHandler(GameEventHandler handler) {
        handlers.add(handler);
    }

    public void removeHandler(GameEventHandler handler) {
        handlers.remove(handler);
    }

    public static class GameEvent implements SomunEvent {

        public GameSession session;

        public GameEvent(GameSession session) {
            this.session = session;
        }

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
