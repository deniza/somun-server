package gss.server.event;

import java.util.ArrayList;
import java.util.HashMap;

import gss.server.event.GameEventListener.GameEvent;

public class EventManager {
    
    private HashMap<Class<? extends SomunEvent>, ArrayList<SomunEventListener>> listeners = new HashMap<>();

    private static final EventManager instance = new EventManager();

    private EventManager() {

        addListener(GameEvent.class, new GameEventListener());

    }

    public static EventManager get() {
        return instance;
    }

    private void addListener(Class<? extends SomunEvent> eventType, SomunEventListener listener) {
        ArrayList<SomunEventListener> eventListeners = listeners.get(eventType);
        if (eventListeners == null) {
            eventListeners = new ArrayList<>();
            listeners.put(eventType, eventListeners);
        }
        eventListeners.add(listener);
    }

    private void removeListener(Class<? extends SomunEvent> eventType, SomunEventListener listener) {
        ArrayList<SomunEventListener> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    public void dispatch(SomunEvent event) {
        ArrayList<SomunEventListener> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (SomunEventListener listener : eventListeners) {
                listener.onEvent(event);
            }
        }
    }

    public interface SomunEventListener {
        void onEvent(SomunEvent event);
    }
    
    public interface SomunEvent {
        // Define any methods or properties that all events should have.
    }
    
}



