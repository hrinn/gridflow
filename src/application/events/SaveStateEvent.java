package application.events;

import construction.history.GridMemento;

public class SaveStateEvent implements GridFlowEvent {
    private final GridMemento memento;

    public SaveStateEvent(GridMemento memento) {
        this.memento = memento;
    }

    public GridMemento getMemento() {
        return memento;
    }
}
