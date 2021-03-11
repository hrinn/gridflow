package application.events;

import java.util.ArrayList;
import java.util.List;

public class GridFlowEventManager {

    private final List<GridFlowEventListener> listeners;

    public GridFlowEventManager() {
        listeners = new ArrayList<>();
    }

    public void addListener(GridFlowEventListener listener) {
        listeners.add(listener);
    }

    /* The login event is special because it only goes to the GridFlowApp class at program start.
     * At this time, the only listener will be the GridFlowApp
     * This function exists because login boots the app, which adds a bunch of listeners, and so the listeners
     * list is bigger because the loop is completed in sendEvent.
     */
    public void sendLoginEvent(LoginEvent loginEvent) {
        listeners.get(0).handleEvent(loginEvent);
    }

    public void sendEvent(GridFlowEvent gridFlowEvent) {
        for (GridFlowEventListener listener : listeners) {
            listener.handleEvent(gridFlowEvent);
        }
    }
}
