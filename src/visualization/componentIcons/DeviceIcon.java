package visualization.componentIcons;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

public class DeviceIcon extends ComponentIcon {

    // TODO: add clickable bounding box

    // shapes that are energized when the in node is energized
    private final Group inNodeEnergyOutline = new Group();

    // shapes that are energized when the out node is energized
    private final Group outNodeEnergyOutline = new Group();

    // shapes that are energized when both nodes are energized
    private final Group midNodeEnergyOutline = new Group();

    // static shapes do not have an energy outline

    public DeviceIcon() {
        addEnergyOutlineNode(inNodeEnergyOutline, outNodeEnergyOutline, midNodeEnergyOutline);
    }

    public void addStaticNodes(Node... nodes) {
        addNodesToIconNode(nodes);
    }

    public void addInNodeShapes(Shape... shapes) {
        addNodesToIconNode(shapes);
        addShapesToEnergyOutlineNode(inNodeEnergyOutline, shapes);
    }

    public void addOutNodeShapes(Shape... shapes) {
        addNodesToIconNode(shapes);
        addShapesToEnergyOutlineNode(outNodeEnergyOutline, shapes);
    }

    public void addMidNodeShapes(Shape... shapes) {
        addNodesToIconNode(shapes);
        addShapesToEnergyOutlineNode(midNodeEnergyOutline, shapes);
    }

    public void setDeviceEnergyStates(boolean inNodeEnergized, boolean outNodeEnergized) {
        setInNodeEnergyState(inNodeEnergized);
        setOutNodeEnergyState(outNodeEnergized);

        if (!midNodeEnergyOutline.getChildren().isEmpty()) {
            setMidNodeEnergyState(inNodeEnergized && outNodeEnergized);
        }
    }

    protected void setInNodeEnergyState(boolean energized) {
        inNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    protected void setOutNodeEnergyState(boolean energized) {
        outNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    protected void setMidNodeEnergyState(boolean energized) {
        midNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    protected Group getMidNodeEnergyOutline() {
        return midNodeEnergyOutline;
    }
}
