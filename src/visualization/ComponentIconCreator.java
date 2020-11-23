package visualization;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import model.geometry.Point;

public class ComponentIconCreator {

    private static double UNIT = 20;
    private static double STROKE_WIDTH = 1.5;

    // Might need 2Node Icon or 1Node Icon

    public static DeviceIcon getSwitchIcon(Point position) {
        DeviceIcon switchIcon = new DeviceIcon();

        Line inLine = createLine(position, position.translate(0, 1.25 * UNIT));
        Line inBar = createLine(position.translate(-0.5 * UNIT, 1.25 * UNIT), position.translate(0.5 * UNIT, 1.25 * UNIT));
        switchIcon.addInNodeShapes(inLine, inBar);

        Line outLine = createLine(position.translate(0, 1.75 * UNIT), position.translate(0, 3 * UNIT));
        Line outBar = createLine(position.translate(-0.5 * UNIT, 1.75 * UNIT), position.translate(0.5 * UNIT, 1.75 * UNIT));
        switchIcon.addOutNodeShapes(outLine, outBar);

        return switchIcon;
    }

    public static DeviceIcon get70KVBreakerIcon(Point position) {
        DeviceIcon breakerIcon = new DeviceIcon();

        Line inLine = createLine(position, position.translate(0, 1 * UNIT));
        breakerIcon.addInNodeShapes(inLine);

        Line outLine = createLine(position.translate(0, 2 * UNIT), position.translate(0, 3 * UNIT));
        breakerIcon.addOutNodeShapes(outLine);

        Rectangle box = createRectangle(position.translate(-0.5 * UNIT, 1 * UNIT), position.translate(0.5 * UNIT, 2 * UNIT), Color.RED);
        breakerIcon.addMidNodeShapes(box);

        return breakerIcon;
    }

    private static Line createLine(Point p1, Point p2) {
        Line line = new Line();
        line.setStrokeWidth(STROKE_WIDTH);

        line.setStartX(p1.getX());
        line.setStartY(p1.getY());

        line.setEndX(p2.getX());
        line.setEndY(p2.getY());

        return line;
    }

    private static Rectangle createRectangle(Point p1, Point p2, Color fill) {
        Rectangle rectangle = new Rectangle();
        rectangle.setStrokeWidth(STROKE_WIDTH);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(fill);

        rectangle.setX(p1.getX());
        rectangle.setY(p1.getY());

        rectangle.setWidth(p1.differenceX(p2));
        rectangle.setHeight(p1.differenceY(p2));

        return rectangle;
    }
}
