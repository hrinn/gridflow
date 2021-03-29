package visualization.componentIcons;

import javafx.scene.Group;
import javafx.scene.shape.Shape;

public class BreakerIcon extends DeviceIcon {
    // shapes that are energized when both nodes are energized and the device is closed
    private final Group backFedOffNodeEnergyOutline = new Group();

    public BreakerIcon() {
        addEnergyOutlineNode(backFedOffNodeEnergyOutline);
    }

    public void addBackfedOffNodeShapes(Shape... shapes) {
        addNodesToIconNode(shapes);
        addShapesToEnergyOutlineNode(backFedOffNodeEnergyOutline, shapes);
    }

    public void setBreakerEnergyStates(boolean inNodeEnergized, boolean outNodeEnergized, boolean isClosed) {
        setInNodeEnergyState(inNodeEnergized);
        setOutNodeEnergyState(outNodeEnergized);

        if (!getMidNodeEnergyOutline().getChildren().isEmpty()) {
            setMidNodeEnergyState(inNodeEnergized && outNodeEnergized);
        }

        if (!backFedOffNodeEnergyOutline.getChildren().isEmpty()) {
            setBackfedOffNodeEnergyState(isClosed && inNodeEnergized && outNodeEnergized);
        }
    }

    private void setBackfedOffNodeEnergyState(boolean energized) {
        backFedOffNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }
}
