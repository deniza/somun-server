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

        if (e instanceof GameEvent.GameCreated) {
            GameEvent.GameCreated event = (GameEvent.GameCreated) e;
            for (GameEventHandler handler : handlers) {
                handler.onGameCreated(event.session);
            }
        }
        else if (e instanceof GameEvent.GameFinished) {
            GameEvent.GameFinished event = (GameEvent.GameFinished) e;
            for (GameEventHandler handler : handlers) {
                handler.onGameFinished(event.session);
            }
        }
        else if (e instanceof GameEvent.PlayerMakeMove) {
            GameEvent.PlayerMakeMove event = (GameEvent.PlayerMakeMove) e;
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

}
