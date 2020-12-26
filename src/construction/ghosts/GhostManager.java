package construction.ghosts;

import construction.PropertiesData;
import construction.ComponentType;
import construction.canvas.GridCanvasFacade;
import domain.geometry.Point;
import javafx.animation.StrokeTransition;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import visualization.componentIcons.ComponentIcon;
import visualization.componentIcons.ComponentIconCreator;

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
            default -> null;
        };
        if (ghostIcon == null) return;
        ghostIcon.getComponentNode().setOpacity(GHOST_OPACITY);
        ghostIcon.setAngle(properties.getRotation(), origin);
        canvasMaster.addOverlayNode(ghostIcon.getComponentNode());
        canvasMaster.addOverlayNode(ghostIcon.getBoundingRect());
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
        ghostIcon = ComponentIconCreator.getWireIcon(start, end);
        ghostIcon.getComponentNode().setOpacity(GHOST_OPACITY);
        canvasMaster.addOverlayNode(ghostIcon.getComponentNode());
    }

    public void showGhostError() {
        StrokeTransition st = new StrokeTransition(Duration.millis(1000), ghostIcon.getBoundingRect(), Color.RED, ComponentIcon.DEFAULT_BOUNDING_COLOR);
        st.setCycleCount(1);
        st.play();
    }

    public void enableGhostIcon() {
        ghostEnabled = true;
    }

    public void disableGhostIcon() {
        ghostEnabled = false;
        canvasMaster.clearOverlay();
    }

    public boolean isGhostEnabled() {
        return ghostEnabled;
    }
}
