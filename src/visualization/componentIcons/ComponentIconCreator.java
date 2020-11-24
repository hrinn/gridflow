package visualization.componentIcons;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import model.geometry.Point;
import visualization.GridScene;

public class ComponentIconCreator {

    public static DeviceIcon getSwitchIcon(Point position) {
        DeviceIcon switchIcon = new DeviceIcon();

        Line inLine = createLine(position, position.translate(0, 1.25 * GridScene.UNIT));
        Line inBar = createLine(position.translate(-0.5 * GridScene.UNIT, 1.25 * GridScene.UNIT),
                position.translate(0.5 * GridScene.UNIT, 1.25 * GridScene.UNIT));
        switchIcon.addInNodeShapes(inLine, inBar);

        Line outLine = createLine(position.translate(0, 1.75 * GridScene.UNIT),
                position.translate(0, 3 * GridScene.UNIT));
        Line outBar = createLine(position.translate(-0.5 * GridScene.UNIT, 1.75 * GridScene.UNIT),
                position.translate(0.5 * GridScene.UNIT, 1.75 * GridScene.UNIT));
        switchIcon.addOutNodeShapes(outLine, outBar);

        // TODO: Add indicator, cases for normal state and state. n/c closed has to be a mid node

        return switchIcon;
    }

    public static DeviceIcon get70KVBreakerIcon(Point position) {
        DeviceIcon breakerIcon = new DeviceIcon();

        Line inLine = createLine(position, position.translate(0, 1 * GridScene.UNIT));
        breakerIcon.addInNodeShapes(inLine);

        Line outLine = createLine(position.translate(0, 2 * GridScene.UNIT), position.translate(0, 3 * GridScene.UNIT));
        breakerIcon.addOutNodeShapes(outLine);

        Rectangle box = createRectangle(position.translate(-0.5 * GridScene.UNIT, 1 * GridScene.UNIT),
                position.translate(0.5 * GridScene.UNIT, 2 * GridScene.UNIT), Color.RED);
        breakerIcon.addMidNodeShapes(box);

        return breakerIcon;
    }

    public static DeviceIcon get12KVBreakerIcon(Point p) {
        DeviceIcon breakerIcon = new DeviceIcon();

        Line inLine1 = createLine(p, p.translate(0, 0.75 * GridScene.UNIT));
        Line inLine2 = createRoundedLine(p.translate(0, GridScene.UNIT), p.translate(0, 1.5 * GridScene.UNIT));
        Line inChevron1L = createRoundedLine(p.translate(-0.5 * GridScene.UNIT, GridScene.UNIT), p.translate(0, 0.75 * GridScene.UNIT));
        Line inChevron1R = createRoundedLine(p.translate(0.5 * GridScene.UNIT, GridScene.UNIT), p.translate(0, 0.75 * GridScene.UNIT));
        Line inChevron2L = createRoundedLine(p.translate(-0.5 * GridScene.UNIT, 1.25 * GridScene.UNIT), p.translate(0, GridScene.UNIT));
        Line inChevron2R = createRoundedLine(p.translate(0.5 * GridScene.UNIT, 1.25 * GridScene.UNIT), p.translate(0, GridScene.UNIT));
        breakerIcon.addInNodeShapes(inLine1, inLine2, inChevron1L, inChevron1R, inChevron2L, inChevron2R);

        Line outLine1 = createRoundedLine(p.translate(0, 2.5 * GridScene.UNIT), p.translate(0, 3 * GridScene.UNIT));
        Line outLine2 = createLine(p.translate(0, 3.25 * GridScene.UNIT), p.translate(0, 4 * GridScene.UNIT));
        Line outChevron1L = createRoundedLine(p.translate(-0.5 * GridScene.UNIT, 2.75 * GridScene.UNIT),
                p.translate(0, 3 * GridScene.UNIT));
        Line outChevron1R = createRoundedLine(p.translate(0.5 * GridScene.UNIT, 2.75 * GridScene.UNIT),
                p.translate(0, 3 * GridScene.UNIT));
        Line outChevron2L = createRoundedLine(p.translate(-0.5 * GridScene.UNIT, 3 * GridScene.UNIT),
                p.translate(0, 3.25 * GridScene.UNIT));
        Line outChevron2R = createRoundedLine(p.translate(0.5 * GridScene.UNIT, 3 * GridScene.UNIT),
                p.translate(0, 3.25 * GridScene.UNIT));
        breakerIcon.addOutNodeShapes(outLine1, outLine2, outChevron1L, outChevron1R, outChevron2L, outChevron2R);

        Rectangle box = createRectangle(p.translate(-0.5 * GridScene.UNIT, 1.5 * GridScene.UNIT),
                p.translate(0.5 * GridScene.UNIT, 2.5 * GridScene.UNIT), Color.RED);
        breakerIcon.addMidNodeShapes(box);

        return breakerIcon;
    }

    public static DeviceIcon getTransformerIcon(Point p) {

        // change to new icon that can't be split energy maybe
        DeviceIcon transformerIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, 1.1 * GridScene.UNIT));
        Line inEdgeL = createLine(p.translate(-1 * GridScene.UNIT, 0.9 * GridScene.UNIT),
                p.translate(-1 * GridScene.UNIT, 1.1 * GridScene.UNIT));
        Line inEdgeR = createLine(p.translate(1 * GridScene.UNIT, 0.9 * GridScene.UNIT),
                p.translate(1 * GridScene.UNIT, 1.1 * GridScene.UNIT));
        Arc arcIn1 = createHalfArc(p.translate(-0.75 * GridScene.UNIT, 1.1 * GridScene.UNIT), 0.25 * GridScene.UNIT, Orientation.DOWN);
        Arc arcIn2 = createHalfArc(p.translate(-0.25 * GridScene.UNIT, 1.1 * GridScene.UNIT), 0.25 * GridScene.UNIT, Orientation.DOWN);
        Arc arcIn3 = createHalfArc(p.translate(0.25 * GridScene.UNIT, 1.1 * GridScene.UNIT), 0.25 * GridScene.UNIT, Orientation.DOWN);
        Arc arcIn4 = createHalfArc(p.translate(0.75 * GridScene.UNIT, 1.1 * GridScene.UNIT), 0.25 * GridScene.UNIT, Orientation.DOWN);
        transformerIcon.addInNodeShapes(inLine, inEdgeL, inEdgeR, arcIn1, arcIn2, arcIn3, arcIn4);

        Line outLine = createLine(p.translate(0, 1.9 * GridScene.UNIT), p.translate(0, 3 * GridScene.UNIT));
        Line outEdgeL = createLine(p.translate(-1 * GridScene.UNIT, 1.9 * GridScene.UNIT),
                p.translate(-1 * GridScene.UNIT, 2.1 * GridScene.UNIT));
        Line outEdgeR = createLine(p.translate(1 * GridScene.UNIT, 1.9 * GridScene.UNIT),
                p.translate(1 * GridScene.UNIT, 2.1 * GridScene.UNIT));
        Arc arcOut1 = createHalfArc(p.translate(-0.75 * GridScene.UNIT, 1.9 * GridScene.UNIT), 0.25 * GridScene.UNIT, Orientation.UP);
        Arc arcOut2 = createHalfArc(p.translate(-0.25 * GridScene.UNIT, 1.9 * GridScene.UNIT), 0.25 * GridScene.UNIT, Orientation.UP);
        Arc arcOut3 = createHalfArc(p.translate(0.25 * GridScene.UNIT, 1.9 * GridScene.UNIT), 0.25 * GridScene.UNIT, Orientation.UP);
        Arc arcOut4 = createHalfArc(p.translate(0.75 * GridScene.UNIT, 1.9 * GridScene.UNIT), 0.25 * GridScene.UNIT, Orientation.UP);
        transformerIcon.addOutNodeShapes(outLine, outEdgeL, outEdgeR, arcOut1, arcOut2, arcOut3, arcOut4);

        return transformerIcon;
    }

    public static DeviceIcon getJumperIcon(Point p, boolean closed) {
        DeviceIcon jumperIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, GridScene.UNIT));
        jumperIcon.addInNodeShapes(inLine);

        Line outLine = createLine(p.translate(0, 2 * GridScene.UNIT), p.translate(0, 3 * GridScene.UNIT));
        Arc jumper = createHalfArc(p.translate(0, 1.5 * GridScene.UNIT), 0.5 * GridScene.UNIT, Orientation.RIGHT);
        // transforms must be applied prior to adding the node
        if (!closed) rotateNode(jumper, p.translate(0, 2 * GridScene.UNIT), 45);

        jumperIcon.addOutNodeShapes(outLine, jumper);

        return jumperIcon;
    }

    public static DeviceIcon getCutoutIcon(Point p, boolean closed) {
        DeviceIcon cutoutIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, .95 * GridScene.UNIT));
        cutoutIcon.addInNodeShapes(inLine);

        Line outLine = createLine(p.translate(0, 2 * GridScene.UNIT), p.translate(0, 3 * GridScene.UNIT));
        // these shapes get rotated together
        Arc cutoutArc = createHalfArc(p.translate(0, 1.125 * GridScene.UNIT), 0.15 * GridScene.UNIT, Orientation.UP);
        Circle cutoutDot = createCircle(p.translate(0, 1.125 * GridScene.UNIT), 0.5, Color.TRANSPARENT);
        Line cutoutLineL = createRoundedLine(p.translate(0, 2 * GridScene.UNIT), p.translate(-0.15 * GridScene.UNIT, 1.125 * GridScene.UNIT));
        Line cutoutLineR = createRoundedLine(p.translate(0, 2 * GridScene.UNIT), p.translate(0.15 * GridScene.UNIT, 1.125 * GridScene.UNIT));

        // rotate shapes
        if (!closed) {
            Point pivot = p.translate(0, 2 * GridScene.UNIT);
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

        Rectangle sourceBox = createRectangle(p.translate(-GridScene.UNIT, 0), p.translate(GridScene.UNIT, 2 * GridScene.UNIT), Color.LIME);
        powerSourceIcon.addSourceNodeShapes(sourceBox);

        Line outLine = createLine(p.translate(0, 2 * GridScene.UNIT), p.translate(0, 3 * GridScene.UNIT));
        powerSourceIcon.addOutputLine(outLine);

        return powerSourceIcon;
    }

    public static SourceIcon getTurbineIcon(Point p) {
        SourceIcon turbineIcon = new SourceIcon();

        Circle turbineCircle = createCircle(p.translate(0, 2 * GridScene.UNIT), GridScene.UNIT, Color.RED);
        turbineIcon.addSourceNodeShapes(turbineCircle);

        Line outLine1 = createLine(p, p.translate(0, GridScene.UNIT));
        turbineIcon.addOutputLine(outLine1);

        Line outLine2 = createLine(p.translate(0, 3 * GridScene.UNIT), p.translate(0, 4 * GridScene.UNIT));
        turbineIcon.addOutputLine(outLine2);

        return turbineIcon;
    }

    public static WireIcon getWireIcon(Point p1, Point p2) {
        WireIcon wireIcon = new WireIcon();

        if (p1.equals(p2)) {
            Circle wireDot = createCircle(p1, 1, Color.BLACK);
            wireIcon.addWireShape(wireDot);
        } else {
            Line wireLine = createLine(p1, p2);
            wireIcon.addWireShape(wireLine);
        }

        return wireIcon;
    }

    private static Line createLine(Point p1, Point p2) {
        Line line = new Line();
        line.setStrokeWidth(GridScene.STROKE_WIDTH);

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
        rectangle.setStrokeWidth(GridScene.STROKE_WIDTH);
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
        arc.setStrokeWidth(GridScene.STROKE_WIDTH);
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
        circle.setStrokeWidth(GridScene.STROKE_WIDTH);
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
