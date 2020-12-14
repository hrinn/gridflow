package construction;

import application.Globals;
import construction.canvas.GridCanvas;
import domain.geometry.Point;
import javafx.scene.Cursor;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;

public class GhostManager {

    private final static double GHOST_OPACITY = 0.5;

    private GridCanvas gridCanvas;
    private ComponentIcon ghostIcon;
    private ComponentProperties properties;
    private boolean ghostEnabled;

    public GhostManager(GridCanvas gridCanvas, ComponentProperties properties) {
        this.gridCanvas = gridCanvas;
        this.properties = properties;
    }

    public void setGhostIcon(ComponentType componentType) {
        ghostEnabled = true;
        gridCanvas.removeGhostIcon();
        Point origin = Point.origin();
        this.ghostIcon = switch (componentType) {
            case BREAKER_12KV -> ComponentIconCreator.get12KVBreakerIcon(origin, properties.getDefaultState(), properties.getDefaultState());
            case BREAKER_70KV -> ComponentIconCreator.get70KVBreakerIcon(origin, properties.getDefaultState(), properties.getDefaultState());
            case CUTOUT -> ComponentIconCreator.getCutoutIcon(origin, properties.getDefaultState());
            case JUMPER -> ComponentIconCreator.getJumperIcon(origin, properties.getDefaultState());
            case POWER_SOURCE -> ComponentIconCreator.getPowerSourceIcon(origin, properties.getName(), false);
            case SWITCH -> ComponentIconCreator.getSwitchIcon(origin, properties.getDefaultState(), properties.getDefaultState());
            case TRANSFORMER -> ComponentIconCreator.getTransformerIcon(origin);
            case TURBINE -> ComponentIconCreator.getTurbineIcon(origin);
            case WIRE -> ComponentIconCreator.getWireIcon(origin, origin);
        };
        ghostIcon.getComponentNode().setOpacity(GHOST_OPACITY);
        gridCanvas.addGhostIcon(ghostIcon.getComponentNode());
    }

    public void updateGhostPosition(double x, double y) {
        double coordX = roundToUnit(x);
        double coordY = roundToUnit(y);
        ghostIcon.getComponentNode().setTranslateX(coordX);
        ghostIcon.getComponentNode().setTranslateY(coordY);
    }

    private double roundToUnit(double num) {
        return Math.round(num/Globals.UNIT) * Globals.UNIT;
    }

    public void enableGhostIcon() {
        ghostEnabled = true;
    }

    public void disableGhostIcon() {
        ghostIcon = null;
        ghostEnabled = false;
        gridCanvas.removeGhostIcon();
    }

    public boolean isGhostEnabled() {
        return ghostEnabled;
    }
}
