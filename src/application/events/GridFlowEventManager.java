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

    /* This send event is special because it only goes to the GridFlowApp class.
     * This avoids a concurrent modification error when the GridFlowApp class creates or removes listeners
     * in response to the sent event.
     * GridFlowApp was added first and should always be the first listener.
     */
    public void sendApplicationOnlyEvent(GridFlowEvent event) {
        listeners.get(0).handleEvent(event);
    }

    public void sendEvent(GridFlowEvent gridFlowEvent) {
        for (GridFlowEventListener listener : listeners) {
            listener.handleEvent(gridFlowEvent);
        }
    }
}
