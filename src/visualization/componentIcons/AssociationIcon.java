package visualization.componentIcons;

import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.List;
import java.util.UUID;

public class AssociationIcon {

    private Rectangle rect;

    private Circle resizeHandleNW;
    private Circle resizeHandleSE;

    private Text label;

    // the group that holds everything
    private final Group association;

    public AssociationIcon() {
        association = new Group();
    }

    // must be run after the lines are created
    public void setID(UUID ID) {
        association.setId(ID.toString());
        resizeHandleNW.setId(ID.toString());
        resizeHandleSE.setId(ID.toString());
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
        association.getChildren().add(rect);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setHandles(Circle NW, Circle SE) {
        if (rect == null) {
            System.err.println("Association handles set before rectangle.");
            return;
        }
        resizeHandleNW = NW;
        NW.centerXProperty().bind(rect.xProperty());
        NW.centerYProperty().bind(rect.yProperty());

        resizeHandleSE = SE;
        SE.centerXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        SE.centerYProperty().bind(rect.yProperty().add(rect.heightProperty()));

        association.getChildren().addAll(NW, SE);
    }

    public void setText(Text text) {
        label = text;
        association.getChildren().add(text);
    }

    public Text getText() {
        return label;
    }

    public Group getAssociationGroup() {
        return association;
    }

    public List<Circle> getHandles() {
        return List.of(resizeHandleNW, resizeHandleSE);
    }

    public Circle getResizeHandleNW() {
        return resizeHandleNW;
    }

    public Circle getResizeHandleSE() {
        return resizeHandleSE;
    }
}
