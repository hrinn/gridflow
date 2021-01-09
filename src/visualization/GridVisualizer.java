package visualization;

import construction.canvas.GridCanvasFacade;
import domain.Association;
import domain.Grid;
import domain.components.Component;

public class GridVisualizer {

    private final Grid grid;
    private final GridCanvasFacade canvasFacade;

    public GridVisualizer(Grid grid, GridCanvasFacade canvasFacade) {
        this.grid = grid;
        this.canvasFacade = canvasFacade;
    }

    public void displayGrid() {
        canvasFacade.clearComponentGroups();
        for (Component component : grid.getComponents()) {
            canvasFacade.addComponentIcon(component.getUpdatedComponentIcon());
        }
    }

    public void displayAssociations() {
        canvasFacade.clearAssociationGroup();
        for (Association association : grid.getAssociations()) {
            canvasFacade.addAssociationNode(association.getAssociationNode());
        }
    }

}
