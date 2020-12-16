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

    public void sendEvent(GridFlowEvent gridFlowEvent) {
        listeners.forEach(listener -> listener.handleEvent(gridFlowEvent));
    }
}
