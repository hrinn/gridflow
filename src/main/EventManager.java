package main;

import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private final List<IEventListener> listeners;

    public EventManager() {
        listeners = new ArrayList<>();
    }

    public void addListener(IEventListener listener) {
        listeners.add(listener);
    }

    public void sendEvent(Event event) {
        listeners.forEach(listener -> listener.handleEvent(event));
    }
}
