package construction.ghosts;

import construction.canvas.GridCanvasFacade;
import domain.geometry.Point;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

// The association ghost has distinct logic from regular ghosts so it has its own model
public class AssociationGhostManager {
    private Node ghost;
    private GridCanvasFacade canvasFacade;
    private boolean ghostEnabled = false;
    private boolean secondCrosshair = false;

    private final double crosshair_width = 5;

    public AssociationGhostManager(GridCanvasFacade canvasFacade) {
        this.canvasFacade = canvasFacade;
    }

    // before the rectangle has been created, the ghost is just a crosshair
    public void setAssociationGhost() {
        canvasFacade.clearOverlay();
        secondCrosshair = false;

        ghost = createCrosshair();
        canvasFacade.addOverlayNode(ghost);
    }

    private Group createCrosshair() {
        Group crosshair = new Group();
        Line hline = new Line(-crosshair_width, 0, crosshair_width, 0);
        Line vline = new Line(0, -crosshair_width, 0, crosshair_width);

        crosshair.getChildren().addAll(hline, vline);
        crosshair.setOpacity(0.5);
        return crosshair;
    }

    public void setSecondCrosshair() {
        secondCrosshair = true;
        Group crosshair2 = createCrosshair();

        ghost = crosshair2;
        canvasFacade.addOverlayNode(ghost);
    }

    public void updateGhostPosition(Point pos) {
        ghost.setTranslateX(pos.getX());
        ghost.setTranslateY(pos.getY());
    }

    public void setGhostEnabled(boolean enabled) {
        if (ghost != null) {
            if (enabled) {
                ghost.setOpacity(0.5);
            } else {
                ghost.setOpacity(0);
            }
        }
        ghostEnabled = enabled;
    }

    public void setSecondCrosshairEnabled(boolean enabled) {
        secondCrosshair = enabled;
    }

    public boolean isGhostEnabled() {
        return ghostEnabled;
    }

    public boolean isSecondCrosshairEnabled() {
        return secondCrosshair;
    }


}
