package visualization;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import model.geometry.Point;

public class ComponentIconCreator {

    private static double UNIT = 20;
    private static double STROKE_WIDTH = 1.5;

    public static DeviceIcon getSwitchIcon(Point position) {
        DeviceIcon switchIcon = new DeviceIcon();

        Line inLine = createLine(position, position.translate(0, 1.25 * UNIT));
        Line inBar = createLine(position.translate(-0.5 * UNIT, 1.25 * UNIT),
                position.translate(0.5 * UNIT, 1.25 * UNIT));
        switchIcon.addInNodeShapes(inLine, inBar);

        Line outLine = createLine(position.translate(0, 1.75 * UNIT),
                position.translate(0, 3 * UNIT));
        Line outBar = createLine(position.translate(-0.5 * UNIT, 1.75 * UNIT),
                position.translate(0.5 * UNIT, 1.75 * UNIT));
        switchIcon.addOutNodeShapes(outLine, outBar);

        // TODO: Add indicator, cases for normal state and state. n/c closed has to be a mid node

        return switchIcon;
    }

    public static DeviceIcon get70KVBreakerIcon(Point position) {
        DeviceIcon breakerIcon = new DeviceIcon();

        Line inLine = createLine(position, position.translate(0, 1 * UNIT));
        breakerIcon.addInNodeShapes(inLine);

        Line outLine = createLine(position.translate(0, 2 * UNIT), position.translate(0, 3 * UNIT));
        breakerIcon.addOutNodeShapes(outLine);

        Rectangle box = createRectangle(position.translate(-0.5 * UNIT, 1 * UNIT),
                position.translate(0.5 * UNIT, 2 * UNIT), Color.RED);
        breakerIcon.addMidNodeShapes(box);

        return breakerIcon;
    }

    public static DeviceIcon get12KVBreakerIcon(Point p) {
        DeviceIcon breakerIcon = new DeviceIcon();

        Line inLine1 = createLine(p, p.translate(0, 0.75 * UNIT));
        Line inLine2 = createLine(p.translate(0, UNIT), p.translate(0, 1.5 * UNIT));
        Line inChevron1L = createLine(p.translate(-0.5 * UNIT, UNIT), p.translate(0, 0.75 * UNIT));
        Line inChevron1R = createLine(p.translate(0.5 * UNIT, UNIT), p.translate(0, 0.75 * UNIT));
        Line inChevron2L = createLine(p.translate(-0.5 * UNIT, 1.25 * UNIT), p.translate(0, UNIT));
        Line inChevron2R = createLine(p.translate(0.5 * UNIT, 1.25 * UNIT), p.translate(0, UNIT));
        breakerIcon.addInNodeShapes(inLine1, inLine2, inChevron1L, inChevron1R, inChevron2L, inChevron2R);

        Line outLine1 = createLine(p.translate(0, 2.5 * UNIT), p.translate(0, 3 * UNIT));
        Line outLine2 = createLine(p.translate(0, 3.25 * UNIT), p.translate(0, 4 * UNIT));
        Line outChevron1L = createLine(p.translate(-0.5 * UNIT, 2.75 * UNIT),
                p.translate(0, 3 * UNIT));
        Line outChevron1R = createLine(p.translate(0.5 * UNIT, 2.75 * UNIT),
                p.translate(0, 3 * UNIT));
        Line outChevron2L = createLine(p.translate(-0.5 * UNIT, 3 * UNIT),
                p.translate(0, 3.25 * UNIT));
        Line outChevron2R = createLine(p.translate(0.5 * UNIT, 3 * UNIT),
                p.translate(0, 3.25 * UNIT));
        breakerIcon.addOutNodeShapes(outLine1, outLine2, outChevron1L, outChevron1R, outChevron2L, outChevron2R);

        Rectangle box = createRectangle(p.translate(-0.5 * UNIT, 1.5 * UNIT),
                p.translate(0.5 * UNIT, 2.5 * UNIT), Color.RED);
        breakerIcon.addMidNodeShapes(box);

        return breakerIcon;
    }

    public static DeviceIcon getTransformerIcon(Point p) {
        DeviceIcon transformerIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, 1.1 * UNIT));
        Line inEdgeL = createLine(p.translate(-1 * UNIT, 0.9 * UNIT),
                p.translate(-1 * UNIT, 1.1 * UNIT));
        Line inEdgeR = createLine(p.translate(1 * UNIT, 0.9 * UNIT),
                p.translate(1 * UNIT, 1.1 * UNIT));
        Arc arcIn1 = createHalfArc(p.translate(-0.75 * UNIT, 1.1 * UNIT), 0.25 * UNIT, true);
        Arc arcIn2 = createHalfArc(p.translate(-0.25 * UNIT, 1.1 * UNIT), 0.25 * UNIT, true);
        Arc arcIn3 = createHalfArc(p.translate(0.25 * UNIT, 1.1 * UNIT), 0.25 * UNIT, true);
        Arc arcIn4 = createHalfArc(p.translate(0.75 * UNIT, 1.1 * UNIT), 0.25 * UNIT, true);
        transformerIcon.addInNodeShapes(inLine, inEdgeL, inEdgeR, arcIn1, arcIn2, arcIn3, arcIn4);

        Line outLine = createLine(p.translate(0, 1.9 * UNIT), p.translate(0, 3 * UNIT));
        Line outEdgeL = createLine(p.translate(-1 * UNIT, 1.9 * UNIT),
                p.translate(-1 * UNIT, 2.1 * UNIT));
        Line outEdgeR = createLine(p.translate(1 * UNIT, 1.9 * UNIT),
                p.translate(1 * UNIT, 2.1 * UNIT));
        Arc arcOut1 = createHalfArc(p.translate(-0.75 * UNIT, 1.9 * UNIT), 0.25 * UNIT, false);
        Arc arcOut2 = createHalfArc(p.translate(-0.25 * UNIT, 1.9 * UNIT), 0.25 * UNIT, false);
        Arc arcOut3 = createHalfArc(p.translate(0.25 * UNIT, 1.9 * UNIT), 0.25 * UNIT, false);
        Arc arcOut4 = createHalfArc(p.translate(0.75 * UNIT, 1.9 * UNIT), 0.25 * UNIT, false);
        transformerIcon.addInNodeShapes(outLine, outEdgeL, outEdgeR, arcOut1, arcOut2, arcOut3, arcOut4);

        return transformerIcon;
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

    private static Arc createHalfArc(Point center, double radius, boolean up) {
        Arc arc = new Arc();
        arc.setStrokeWidth(STROKE_WIDTH);
        arc.setStroke(Color.BLACK);
        arc.setFill(Color.TRANSPARENT);
        arc.setType(ArcType.OPEN);

        arc.setCenterX(center.getX());
        arc.setCenterY(center.getY());
        arc.setRadiusX(radius);
        arc.setRadiusY(radius);
        arc.setStartAngle(up ? 180 : 0);
        arc.setLength(180);

        return arc;
    }
}
