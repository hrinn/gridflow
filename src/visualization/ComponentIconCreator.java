package visualization;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import model.geometry.Point;

public class ComponentIconCreator {

    private static final double UNIT = 20;
    private static final double STROKE_WIDTH = 1.5;
    public static final double ENERGY_STROKE_WIDTH = 4;

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
        Line inLine2 = createRoundedLine(p.translate(0, UNIT), p.translate(0, 1.5 * UNIT));
        Line inChevron1L = createRoundedLine(p.translate(-0.5 * UNIT, UNIT), p.translate(0, 0.75 * UNIT));
        Line inChevron1R = createRoundedLine(p.translate(0.5 * UNIT, UNIT), p.translate(0, 0.75 * UNIT));
        Line inChevron2L = createRoundedLine(p.translate(-0.5 * UNIT, 1.25 * UNIT), p.translate(0, UNIT));
        Line inChevron2R = createRoundedLine(p.translate(0.5 * UNIT, 1.25 * UNIT), p.translate(0, UNIT));
        breakerIcon.addInNodeShapes(inLine1, inLine2, inChevron1L, inChevron1R, inChevron2L, inChevron2R);

        Line outLine1 = createRoundedLine(p.translate(0, 2.5 * UNIT), p.translate(0, 3 * UNIT));
        Line outLine2 = createLine(p.translate(0, 3.25 * UNIT), p.translate(0, 4 * UNIT));
        Line outChevron1L = createRoundedLine(p.translate(-0.5 * UNIT, 2.75 * UNIT),
                p.translate(0, 3 * UNIT));
        Line outChevron1R = createRoundedLine(p.translate(0.5 * UNIT, 2.75 * UNIT),
                p.translate(0, 3 * UNIT));
        Line outChevron2L = createRoundedLine(p.translate(-0.5 * UNIT, 3 * UNIT),
                p.translate(0, 3.25 * UNIT));
        Line outChevron2R = createRoundedLine(p.translate(0.5 * UNIT, 3 * UNIT),
                p.translate(0, 3.25 * UNIT));
        breakerIcon.addOutNodeShapes(outLine1, outLine2, outChevron1L, outChevron1R, outChevron2L, outChevron2R);

        Rectangle box = createRectangle(p.translate(-0.5 * UNIT, 1.5 * UNIT),
                p.translate(0.5 * UNIT, 2.5 * UNIT), Color.RED);
        breakerIcon.addMidNodeShapes(box);

        return breakerIcon;
    }

    public static DeviceIcon getTransformerIcon(Point p) {

        // change to new icon that can't be split energy maybe
        DeviceIcon transformerIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, 1.1 * UNIT));
        Line inEdgeL = createLine(p.translate(-1 * UNIT, 0.9 * UNIT),
                p.translate(-1 * UNIT, 1.1 * UNIT));
        Line inEdgeR = createLine(p.translate(1 * UNIT, 0.9 * UNIT),
                p.translate(1 * UNIT, 1.1 * UNIT));
        Arc arcIn1 = createHalfArc(p.translate(-0.75 * UNIT, 1.1 * UNIT), 0.25 * UNIT, Orientation.DOWN);
        Arc arcIn2 = createHalfArc(p.translate(-0.25 * UNIT, 1.1 * UNIT), 0.25 * UNIT, Orientation.DOWN);
        Arc arcIn3 = createHalfArc(p.translate(0.25 * UNIT, 1.1 * UNIT), 0.25 * UNIT, Orientation.DOWN);
        Arc arcIn4 = createHalfArc(p.translate(0.75 * UNIT, 1.1 * UNIT), 0.25 * UNIT, Orientation.DOWN);
        transformerIcon.addInNodeShapes(inLine, inEdgeL, inEdgeR, arcIn1, arcIn2, arcIn3, arcIn4);

        Line outLine = createLine(p.translate(0, 1.9 * UNIT), p.translate(0, 3 * UNIT));
        Line outEdgeL = createLine(p.translate(-1 * UNIT, 1.9 * UNIT),
                p.translate(-1 * UNIT, 2.1 * UNIT));
        Line outEdgeR = createLine(p.translate(1 * UNIT, 1.9 * UNIT),
                p.translate(1 * UNIT, 2.1 * UNIT));
        Arc arcOut1 = createHalfArc(p.translate(-0.75 * UNIT, 1.9 * UNIT), 0.25 * UNIT, Orientation.UP);
        Arc arcOut2 = createHalfArc(p.translate(-0.25 * UNIT, 1.9 * UNIT), 0.25 * UNIT, Orientation.UP);
        Arc arcOut3 = createHalfArc(p.translate(0.25 * UNIT, 1.9 * UNIT), 0.25 * UNIT, Orientation.UP);
        Arc arcOut4 = createHalfArc(p.translate(0.75 * UNIT, 1.9 * UNIT), 0.25 * UNIT, Orientation.UP);
        transformerIcon.addOutNodeShapes(outLine, outEdgeL, outEdgeR, arcOut1, arcOut2, arcOut3, arcOut4);

        return transformerIcon;
    }

    public static DeviceIcon getJumperIcon(Point p, boolean closed) {
        DeviceIcon jumperIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, UNIT));
        jumperIcon.addInNodeShapes(inLine);

        Line outLine = createLine(p.translate(0, 2 * UNIT), p.translate(0, 3 * UNIT));
        Arc jumper = createHalfArc(p.translate(0, 1.5 * UNIT), 0.5 * UNIT, Orientation.RIGHT);
        // transforms must be applied prior to adding the node
        if (!closed) rotateNode(jumper, p.translate(0, 2 * UNIT), 45);

        jumperIcon.addOutNodeShapes(outLine, jumper);

        return jumperIcon;
    }

    public static DeviceIcon getCutoutIcon(Point p, boolean closed) {
        DeviceIcon cutoutIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, .95 * UNIT));
        cutoutIcon.addInNodeShapes(inLine);

        Line outLine = createLine(p.translate(0, 2 * UNIT), p.translate(0, 3 * UNIT));
        // these shapes get rotated together
        Arc cutoutArc = createHalfArc(p.translate(0, 1.125 * UNIT), 0.15 * UNIT, Orientation.UP);
        Circle cutoutDot = createCircle(p.translate(0, 1.125 * UNIT), 0.5, Color.TRANSPARENT);
        Line cutoutLineL = createRoundedLine(p.translate(0, 2 * UNIT), p.translate(-0.15 * UNIT, 1.125 * UNIT));
        Line cutoutLineR = createRoundedLine(p.translate(0, 2 * UNIT), p.translate(0.15 * UNIT, 1.125 * UNIT));

        // rotate shapes
        if (!closed) {
            Point pivot = p.translate(0, 2 * UNIT);
            double angle = 135;

            rotateNode(cutoutArc, pivot, angle);
            rotateNode(cutoutLineL, pivot, angle);
            rotateNode(cutoutLineR, pivot, angle);
            rotateNode(cutoutDot, pivot, angle);
        }
        cutoutIcon.addOutNodeShapes(outLine, cutoutArc, cutoutDot, cutoutLineL, cutoutLineR);

        return cutoutIcon;
    }

    public static SourceIcon getPowerSourceIcon(Point p) {
        SourceIcon powerSourceIcon = new SourceIcon();

        Rectangle sourceBox = createRectangle(p.translate(-UNIT, 0), p.translate(UNIT, 2 * UNIT), Color.GREEN);
        powerSourceIcon.addSourceNodeShapes(sourceBox);

        Line outLine = createLine(p.translate(0, 2 * UNIT), p.translate(0, 3 * UNIT));
        powerSourceIcon.addOutputLine(outLine);

        return powerSourceIcon;
    }

    public static SourceIcon getTurbineIcon(Point p) {
        SourceIcon turbineIcon = new SourceIcon();

        Circle turbineCircle = createCircle(p.translate(0, 2 * UNIT), UNIT, Color.RED);
        turbineIcon.addSourceNodeShapes(turbineCircle);

        Line outLine1 = createLine(p, p.translate(0, UNIT));
        turbineIcon.addOutputLine(outLine1);

        Line outLine2 = createLine(p.translate(0, 3 * UNIT), p.translate(0, 4 * UNIT));
        turbineIcon.addOutputLine(outLine2);

        return turbineIcon;
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

    private static Line createRoundedLine(Point p1, Point p2) {
        Line line = createLine(p1, p2);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        return line;
    }

    private static Rectangle createRectangle(Point p1, Point p2, Color fill) {
        Rectangle rectangle = new Rectangle();
        rectangle.setStrokeWidth(STROKE_WIDTH);
        rectangle.setStrokeType(StrokeType.CENTERED);
        rectangle.setStroke(Color.BLACK);
        rectangle.setFill(fill);

        rectangle.setX(p1.getX());
        rectangle.setY(p1.getY());

        rectangle.setWidth(p1.differenceX(p2));
        rectangle.setHeight(p1.differenceY(p2));

        return rectangle;
    }

    private static Arc createHalfArc(Point center, double radius, Orientation orientation) {
        Arc arc = new Arc();
        arc.setStrokeWidth(STROKE_WIDTH);
        arc.setStroke(Color.BLACK);
        arc.setFill(Color.TRANSPARENT);
        arc.setType(ArcType.OPEN);
        arc.setStrokeType(StrokeType.CENTERED);

        arc.setCenterX(center.getX());
        arc.setCenterY(center.getY());
        arc.setRadiusX(radius);
        arc.setRadiusY(radius);
        arc.setLength(180);
        arc.setStartAngle(getArcStartAngle(orientation));

        return arc;
    }

    private static Circle createCircle(Point center, double radius, Color fill) {
        Circle circle = new Circle();
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(STROKE_WIDTH);
        circle.setFill(fill);

        circle.setCenterX(center.getX());
        circle.setCenterY(center.getY());
        circle.setRadius(radius);

        return circle;
    }

    private static void rotateNode(Node node, Point pivot, double angle) {
        Rotate rotateTransform = new Rotate();
        rotateTransform.setPivotX(pivot.getX());
        rotateTransform.setPivotY(pivot.getY());
        rotateTransform.setAngle(angle);

        node.getTransforms().add(rotateTransform);
    }

    private static double getArcStartAngle(Orientation orientation) {
        switch (orientation) {
            case UP:
                return 0;
            case DOWN:
                return 180;
            case LEFT:
                return 90;
            case RIGHT:
                return 270;
        }
        return 0;
    }
}

enum Orientation {
    UP, DOWN, LEFT, RIGHT
}
