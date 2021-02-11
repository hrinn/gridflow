package application.events;

import construction.ToolType;

public class GridChangedEvent implements GridFlowEvent {
    public ToolType toolCausingChange = null;
}
