package construction;

import construction.canvas.GridCanvas;
import construction.canvas.GridCanvasMaster;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;

public class GhostManager {

    private final static double GHOST_OPACITY = 0.5;

    private GridCanvasMaster canvasMaster;
    private ComponentIcon ghostIcon;
    private ComponentProperties properties;
    private boolean ghostEnabled;

    public GhostManager(GridCanvasMaster canvasMaster, ComponentProperties properties) {
        this.canvasMaster = canvasMaster;
        this.properties = properties;
    }

    public void setGhostIcon(ComponentType componentType) {
        ghostEnabled = true;
        canvasMaster.removeGhostIcon();
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
        canvasMaster.addGhostIcon(ghostIcon);
    }

    public void updateGhostPosition(Point pos) {
        ghostIcon.getComponentNode().setTranslateX(pos.getX());
        ghostIcon.getComponentNode().setTranslateY(pos.getY());
    }

    public void extendGhostWire(Point start, Point end) {
        ghostEnabled = true;
        canvasMaster.removeGhostIcon();
        ghostIcon = ComponentIconCreator.getWireIcon(start, end);
        ghostIcon.getComponentNode().setOpacity(GHOST_OPACITY);
        canvasMaster.addGhostIcon(ghostIcon);
    }

    public void enableGhostIcon() {
        ghostEnabled = true;
    }

    public void disableGhostIcon() {
        ghostEnabled = false;
        canvasMaster.removeGhostIcon();
    }

    public void hideGhostIcon() {
        ghostIcon.getComponentNode().setOpacity(0);
    }

    public void revealGhostIcon() {
        ghostIcon.getComponentNode().setOpacity(GHOST_OPACITY);
    }

    public boolean isGhostEnabled() {
        return ghostEnabled;
    }
}
