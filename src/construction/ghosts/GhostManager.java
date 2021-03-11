package construction.ghosts;

import construction.PropertiesData;
import construction.ComponentType;
import construction.PropertiesManager;
import construction.PropertiesObserver;
import construction.canvas.GridCanvasFacade;
import domain.geometry.Point;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;

import java.util.List;

public class GhostManager implements PropertiesObserver {

    private final static double GHOST_OPACITY = 0.5;

    private GridCanvasFacade canvasMaster;
    private ComponentIcon ghostIcon;
    private PropertiesData properties;
    private boolean ghostEnabled;

    public GhostManager(GridCanvasFacade canvasMaster) {
        this.canvasMaster = canvasMaster;
        this.properties = new PropertiesData();
        PropertiesManager.attach(this);
    }

    @Override
    public void updateProperties(PropertiesData PD) {
        this.properties = new PropertiesData(PD.getType(), PD.getID(), PD.getName(),
                PD.getDefaultState(), PD.getRotation(), PD.getNumSelected(), PD.getAssociation(),
                PD.getAssocLabel(), PD.getAssocSubLabel(), PD.getAssocAcronym());
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
            case BREAKER_12KV -> ComponentIconCreator.get12KVBreakerIcon(pos, properties.getDefaultState(), properties.getDefaultState(), false);
            case BREAKER_70KV -> ComponentIconCreator.get70KVBreakerIcon(pos, properties.getDefaultState(), properties.getDefaultState(), false);
            case CUTOUT -> ComponentIconCreator.getCutoutIcon(pos, properties.getDefaultState(), false);
            case JUMPER -> ComponentIconCreator.getJumperIcon(pos, properties.getDefaultState(), false);
            case POWER_SOURCE -> ComponentIconCreator.getPowerSourceIcon(pos, properties.getName(), true, false);
            case SWITCH -> ComponentIconCreator.getSwitchIcon(pos, properties.getDefaultState(), properties.getDefaultState(), false);
            case TRANSFORMER -> ComponentIconCreator.getTransformerIcon(pos);
            case TURBINE -> ComponentIconCreator.getTurbineIcon(pos, true, false);
            case WIRE -> ComponentIconCreator.getWireIcon(pos, pos, List.of());
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
        ghostIcon = ComponentIconCreator.getWireIcon(start, end, List.of());
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
