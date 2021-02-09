package construction.ghosts;

import construction.PropertiesData;
import construction.ComponentType;
import construction.canvas.GridCanvasFacade;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.IconCreator;

import java.util.List;

// The ghost manager creates the ghost icons that display when components are being placed
public class GhostManager {

    private final static double GHOST_OPACITY = 0.5;

    private GridCanvasFacade canvasMaster;
    private ComponentIcon ghostIcon;
    private PropertiesData properties;
    private boolean ghostEnabled;

    public GhostManager(GridCanvasFacade canvasMaster, PropertiesData properties) {
        this.canvasMaster = canvasMaster;
        this.properties = properties;
    }

    public void setGhostIcon(ComponentType componentType) {
        ghostEnabled = true;
        canvasMaster.clearOverlay();

        this.ghostIcon = ghostIconCreator(componentType, Point.origin());
        if (ghostIcon == null) return;

        ghostIcon.getComponentNode().setOpacity(GHOST_OPACITY);
        ghostIcon.setAngle(properties.getRotation(), Point.origin());
        canvasMaster.addOverlayNode(ghostIcon.getComponentNode());
        canvasMaster.addOverlayNode(ghostIcon.getBoundingRect());
    }

    public void updateGhostIcon(ComponentType componentType) {
        canvasMaster.clearOverlay();
        Point pos = ghostIcon.getTranslate();
        this.ghostIcon = ghostIconCreator(componentType, Point.origin());
        if (ghostIcon == null) return;

        ghostIcon.setTranslate(pos.getX(), pos.getY());
        ghostIcon.getComponentNode().setOpacity(GHOST_OPACITY);
        ghostIcon.setAngle(properties.getRotation(), Point.origin());

        canvasMaster.addOverlayNode(ghostIcon.getComponentNode());
        canvasMaster.addOverlayNode(ghostIcon.getBoundingRect());
    }

    private ComponentIcon ghostIconCreator(ComponentType componentType, Point pos) {
        return switch (componentType) {
            case BREAKER_12KV -> IconCreator.create12KVBreakerIcon(pos, properties.getDefaultState(), properties.getDefaultState());
            case BREAKER_70KV -> IconCreator.create70KVBreakerIcon(pos, properties.getDefaultState(), properties.getDefaultState());
            case CUTOUT -> IconCreator.createCutoutIcon(pos, properties.getDefaultState());
            case JUMPER -> IconCreator.createJumperIcon(pos, properties.getDefaultState());
            case POWER_SOURCE -> IconCreator.createPowerSourceIcon(pos, properties.getName(), false);
            case SWITCH -> IconCreator.createSwitchIcon(pos, properties.getDefaultState(), properties.getDefaultState());
            case TRANSFORMER -> IconCreator.createTransformerIcon(pos);
            case TURBINE -> IconCreator.createTurbineIcon(pos, false);
            case WIRE -> IconCreator.createWireIcon(pos, pos, List.of());
        };
    }

    public void rotateGhostIcon() {
        ghostIcon.resetAngle();
        ghostIcon.setAngle(properties.getRotation(), Point.origin());
    }

    public void updateGhostPosition(Point pos) {
        ghostIcon.setTranslate(pos.getX(), pos.getY());
    }

    public void extendGhostWire(Point start, Point end) {
        ghostEnabled = true;
        canvasMaster.clearOverlay();
        ghostIcon = IconCreator.createWireIcon(start, end, List.of());
        ghostIcon.getComponentNode().setOpacity(GHOST_OPACITY);
        canvasMaster.addOverlayNode(ghostIcon.getComponentNode());
    }

    public void showGhostError() {
        ghostIcon.showError();
    }

    public void setGhostEnabled(boolean enabled) {
        if (ghostIcon != null) {
            if (enabled) {
                ghostIcon.getComponentNode().setOpacity(GHOST_OPACITY);
            } else {
                ghostIcon.getComponentNode().setOpacity(0);
            }
        }
        ghostEnabled = enabled;
    }

    public boolean isGhostEnabled() {
        return ghostEnabled;
    }
}
