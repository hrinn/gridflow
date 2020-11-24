package visualization.componentIcons;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

import java.util.List;

public class DeviceIcon extends ComponentIcon {

    // TODO: add endpoint circles that don't get energized, can be clicked to add a component
    // TODO: add clickable bounding box

    // shapes that are energized when the in node is energized
    private final Group inNode = new Group();
    private final Group inNodeEnergyOutline = new Group();

    // shapes that are energized when the out node is energized
    private final Group outNode = new Group();
    private final Group outNodeEnergyOutline = new Group();

    // shapes that are energized when both nodes are energized
    private final Group midNode = new Group();
    private final Group midNodeEnergyOutline = new Group();

    // shapes used to indicate state
    private final Group indicatorNode = new Group();

    public DeviceIcon() {
    }

    @Override
    public List<Node> getNodes() {
        return List.of(inNode, outNode, midNode, indicatorNode);
    }

    @Override
    public List<Node> getEnergyOutlineNodes() {
        return List.of(inNodeEnergyOutline, outNodeEnergyOutline, midNodeEnergyOutline);
    }

    public void addInNodeShapes(Shape... shapes) {
        addShapesToNodeAndEnergyOutline(inNode, inNodeEnergyOutline, shapes);
    }

    public void addOutNodeShapes(Shape... shapes) {
        addShapesToNodeAndEnergyOutline(outNode, outNodeEnergyOutline, shapes);
    }

    public void addMidNodeShapes(Shape... shapes) {
        addShapesToNodeAndEnergyOutline(midNode, midNodeEnergyOutline, shapes);
    }

    public void setDeviceEnergyStates(boolean inNodeEnergized, boolean outNodeEnergized) {
        setInNodeEnergyState(inNodeEnergized);
        setOutNodeEnergyState(outNodeEnergized);

        if (!midNodeEnergyOutline.getChildren().isEmpty()) {
            setMidNodeEnergyState(inNodeEnergized && outNodeEnergized);
        }
    }

    private void setInNodeEnergyState(boolean energized) {
        inNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    private void setOutNodeEnergyState(boolean energized) {
        outNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }

    private void setMidNodeEnergyState(boolean energized) {
        midNodeEnergyOutline.getChildren().forEach(element -> element.setOpacity(energized ? 1 : 0));
    }
}
