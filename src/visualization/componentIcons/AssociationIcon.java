package visualization.componentIcons;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AssociationIcon {

    private Rectangle rect;

    private Circle resizeHandleNW;
    private Circle resizeHandleSE;
    private Circle resizeHandleNE;
    private Circle resizeHandleSW;

    private List<Line> borderLines = new ArrayList<>();

    private Text label;

    // the group that holds everything
    private final Group association;

    public AssociationIcon() {
        association = new Group();
    }

    // must be run after the lines are created
    public void setID(UUID ID) {
        association.setId(ID.toString());
        getHandles().forEach(handle -> handle.setId(ID.toString()));
        borderLines.forEach(bl -> bl.setId(ID.toString()));
        label.setId(ID.toString());
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
        /* set the borders, this is used to detect when the association is highlighted */
        Line top = new Line();
        top.startXProperty().bind(rect.xProperty());
        top.startYProperty().bind(rect.yProperty());
        top.endXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        top.endYProperty().bind(rect.yProperty());
        borderLines.add(top);

        Line right = new Line();
        right.startXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        right.startYProperty().bind(rect.yProperty());
        right.endXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        right.endYProperty().bind(rect.yProperty().add(rect.heightProperty()));
        borderLines.add(right);

        Line bottom = new Line();
        bottom.startXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        bottom.startYProperty().bind(rect.yProperty().add(rect.heightProperty()));
        bottom.endXProperty().bind(rect.xProperty());
        bottom.endYProperty().bind(rect.yProperty().add(rect.heightProperty()));
        borderLines.add(bottom);

        Line left = new Line();
        left.startXProperty().bind(rect.xProperty());
        left.startYProperty().bind(rect.yProperty().add(rect.heightProperty()));
        left.endXProperty().bind(rect.xProperty());
        left.endYProperty().bind(rect.yProperty());
        borderLines.add(left);

        borderLines.forEach(line -> {
            line.setOpacity(0);
            line.setStrokeWidth(5);
        });
        association.getChildren().addAll(rect, top, right, bottom, left);
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setHandles(Circle NW, Circle SE, Circle NE, Circle SW) {
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

        resizeHandleNE = NE;
        NE.centerXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        NE.centerYProperty().bind(rect.yProperty());

        resizeHandleSW = SW;
        SW.centerXProperty().bind(rect.xProperty());
        SW.centerYProperty().bind(rect.yProperty().add(rect.heightProperty()));

        association.getChildren().addAll(NW, SE, NE, SW);
    }

    public List<Node> getSelectableNodes() {
        List<Node> nodes = new ArrayList<>();
        nodes.addAll(borderLines);
        nodes.add(label);
        return nodes;
    }

    public void setSelect(boolean select) {
        if (select) {
            rect.setStroke(Color.BLUE);
            label.setStroke(Color.BLUE);
        } else {
            rect.setStroke(Color.GRAY);
            label.setStroke(Color.GRAY);
        }
    }

    public void showHandles(boolean show) {
        double opacity;
        if (show) {
            opacity = 1;
        } else {
            opacity = 0;
        }
        getHandles().forEach(handle -> handle.setOpacity(opacity));
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
        return List.of(resizeHandleNW, resizeHandleSE, resizeHandleNE, resizeHandleSW);
    }

    public Circle getResizeHandleNW() {
        return resizeHandleNW;
    }

    public Circle getResizeHandleSE() {
        return resizeHandleSE;
    }

    public Circle getResizeHandleNE() {
        return resizeHandleNE;
    }

    public Circle getResizeHandleSW() {
        return resizeHandleSW;
    }
}
