package main.events;

import main.events.Event;

public interface IEventListener {
    void handleEvent(Event event);
}
