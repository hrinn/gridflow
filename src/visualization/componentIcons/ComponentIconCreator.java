package visualization.componentIcons;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.transform.Rotate;
import domain.geometry.Point;
import visualization.GridVisualizer;

public class ComponentIconCreator {

    public static DeviceIcon getSwitchIcon(Point p, boolean isClosed, boolean isClosedByDefault) {
        DeviceIcon switchIcon = new DeviceIcon();

        // base shape
        Line inLine = createLine(p, p.translate(0, 1.25 * GridVisualizer.UNIT));
        Line inBar = createLine(p.translate(-0.5 * GridVisualizer.UNIT, 1.25 * GridVisualizer.UNIT),
                p.translate(0.5 * GridVisualizer.UNIT, 1.25 * GridVisualizer.UNIT));
        switchIcon.addInNodeShapes(inLine, inBar);

        Line outLine = createLine(p.translate(0, 1.75 * GridVisualizer.UNIT),
                p.translate(0, 3 * GridVisualizer.UNIT));
        Line outBar = createLine(p.translate(-0.5 * GridVisualizer.UNIT, 1.75 * GridVisualizer.UNIT),
                p.translate(0.5 * GridVisualizer.UNIT, 1.75 * GridVisualizer.UNIT));
        switchIcon.addOutNodeShapes(outLine, outBar);

        // State indicators
        if (isClosedByDefault) {
            if (isClosed){
                Line closedBar = createLine(p.translate(0.5 * GridVisualizer.UNIT, GridVisualizer.UNIT),
                        p.translate(-0.5 * GridVisualizer.UNIT, 2 * GridVisualizer.UNIT));
                switchIcon.addMidNodeShapes(closedBar);
            }
            else {
                Circle openCircle = createCircle(p.translate(0, 1.5 * GridVisualizer.UNIT), 0.25 * GridVisualizer.UNIT,
                        Color.TRANSPARENT, Color.LIMEGREEN);
                switchIcon.addStaticNodes(openCircle);
            }
        }
        else {
            if (isClosed) {
                Line closedBar1 = createLine(p.translate(0.5 * GridVisualizer.UNIT, GridVisualizer.UNIT),
                        p.translate(-0.5 * GridVisualizer.UNIT, 2 * GridVisualizer.UNIT));
                closedBar1.setStroke(Color.RED);
                Line closedBar2 = createLine(p.translate(-0.5 * GridVisualizer.UNIT, GridVisualizer.UNIT),
                        p.translate(0.5 * GridVisualizer.UNIT, 2 * GridVisualizer.UNIT));
                closedBar2.setStroke(Color.RED);
                switchIcon.addStaticNodes(closedBar1, closedBar2);
            }
        }

        return switchIcon;
    }

    public static DeviceIcon get70KVBreakerIcon(Point p, boolean isClosed, boolean isClosedByDefault) {
        DeviceIcon breakerIcon = new DeviceIcon();
        Line inLine = createLine(p, p.translate(0, 1 * GridVisualizer.UNIT));
        breakerIcon.addInNodeShapes(inLine);

        Line outLine = createLine(p.translate(0, 2 * GridVisualizer.UNIT), p.translate(0, 3 * GridVisualizer.UNIT));
        breakerIcon.addOutNodeShapes(outLine);

        Rectangle box = createRectangle(p.translate(-0.5 * GridVisualizer.UNIT, 1 * GridVisualizer.UNIT),
                p.translate(0.5 * GridVisualizer.UNIT, 2 * GridVisualizer.UNIT), Color.RED, Color.BLACK);
        breakerIcon.addMidNodeShapes(box);

        Point center = p.translate(0, 1.5 * GridVisualizer.UNIT);

        if (isClosedByDefault) {
            if (!isClosed) {
                box.setFill(Color.LIME);
                Text text = createText(center, "N/C", Color.WHITE, 10);
                breakerIcon.addStaticNodes(text);
            }
        }
        else {
            if (isClosed) {
                Text text = createText(center, "N/O", Color.WHITE, 10);
                breakerIcon.addStaticNodes(text);
            }
            else {
                box.setFill(Color.LIME);
            }
        }

        return breakerIcon;
    }

    public static DeviceIcon get12KVBreakerIcon(Point p, boolean isClosed, boolean isClosedByDefault) {
        DeviceIcon breakerIcon = new DeviceIcon();

        Line inLine1 = createLine(p, p.translate(0, 0.75 * GridVisualizer.UNIT));
        Line inLine2 = createRoundedLine(p.translate(0, GridVisualizer.UNIT), p.translate(0, 1.5 * GridVisualizer.UNIT));
        Line inChevron1L = createRoundedLine(p.translate(-0.5 * GridVisualizer.UNIT, GridVisualizer.UNIT), p.translate(0, 0.75 * GridVisualizer.UNIT));
        Line inChevron1R = createRoundedLine(p.translate(0.5 * GridVisualizer.UNIT, GridVisualizer.UNIT), p.translate(0, 0.75 * GridVisualizer.UNIT));
        Line inChevron2L = createRoundedLine(p.translate(-0.5 * GridVisualizer.UNIT, 1.25 * GridVisualizer.UNIT), p.translate(0, GridVisualizer.UNIT));
        Line inChevron2R = createRoundedLine(p.translate(0.5 * GridVisualizer.UNIT, 1.25 * GridVisualizer.UNIT), p.translate(0, GridVisualizer.UNIT));
        breakerIcon.addInNodeShapes(inLine1, inLine2, inChevron1L, inChevron1R, inChevron2L, inChevron2R);

        Line outLine1 = createRoundedLine(p.translate(0, 2.5 * GridVisualizer.UNIT), p.translate(0, 3 * GridVisualizer.UNIT));
        Line outLine2 = createLine(p.translate(0, 3.25 * GridVisualizer.UNIT), p.translate(0, 4 * GridVisualizer.UNIT));
        Line outChevron1L = createRoundedLine(p.translate(-0.5 * GridVisualizer.UNIT, 2.75 * GridVisualizer.UNIT),
                p.translate(0, 3 * GridVisualizer.UNIT));
        Line outChevron1R = createRoundedLine(p.translate(0.5 * GridVisualizer.UNIT, 2.75 * GridVisualizer.UNIT),
                p.translate(0, 3 * GridVisualizer.UNIT));
        Line outChevron2L = createRoundedLine(p.translate(-0.5 * GridVisualizer.UNIT, 3 * GridVisualizer.UNIT),
                p.translate(0, 3.25 * GridVisualizer.UNIT));
        Line outChevron2R = createRoundedLine(p.translate(0.5 * GridVisualizer.UNIT, 3 * GridVisualizer.UNIT),
                p.translate(0, 3.25 * GridVisualizer.UNIT));
        breakerIcon.addOutNodeShapes(outLine1, outLine2, outChevron1L, outChevron1R, outChevron2L, outChevron2R);

        Rectangle box = createRectangle(p.translate(-0.5 * GridVisualizer.UNIT, 1.5 * GridVisualizer.UNIT),
                p.translate(0.5 * GridVisualizer.UNIT, 2.5 * GridVisualizer.UNIT), Color.RED, Color.BLACK);
        breakerIcon.addMidNodeShapes(box);

        Point center = p.translate(0, 2 * GridVisualizer.UNIT);

        if (isClosedByDefault) {
            if (!isClosed) {
                box.setFill(Color.LIME);
                Text text = createText(center, "N/C", Color.WHITE, 10);
                breakerIcon.addStaticNodes(text);
            }
        }
        else {
            if (isClosed) {
                Text text = createText(center, "N/O", Color.WHITE, 10);
                breakerIcon.addStaticNodes(text);
            }
            else {
                box.setFill(Color.LIME);
            }
        }

        return breakerIcon;
    }

    public static DeviceIcon getTransformerIcon(Point p) {

        // change to new icon that can't be split energy maybe
        DeviceIcon transformerIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, 1.1 * GridVisualizer.UNIT));
        Line inEdgeL = createLine(p.translate(-1 * GridVisualizer.UNIT, 0.9 * GridVisualizer.UNIT),
                p.translate(-1 * GridVisualizer.UNIT, 1.1 * GridVisualizer.UNIT));
        Line inEdgeR = createLine(p.translate(1 * GridVisualizer.UNIT, 0.9 * GridVisualizer.UNIT),
                p.translate(1 * GridVisualizer.UNIT, 1.1 * GridVisualizer.UNIT));
        Arc arcIn1 = createHalfArc(p.translate(-0.75 * GridVisualizer.UNIT, 1.1 * GridVisualizer.UNIT), 0.25 * GridVisualizer.UNIT, Orientation.DOWN);
        Arc arcIn2 = createHalfArc(p.translate(-0.25 * GridVisualizer.UNIT, 1.1 * GridVisualizer.UNIT), 0.25 * GridVisualizer.UNIT, Orientation.DOWN);
        Arc arcIn3 = createHalfArc(p.translate(0.25 * GridVisualizer.UNIT, 1.1 * GridVisualizer.UNIT), 0.25 * GridVisualizer.UNIT, Orientation.DOWN);
        Arc arcIn4 = createHalfArc(p.translate(0.75 * GridVisualizer.UNIT, 1.1 * GridVisualizer.UNIT), 0.25 * GridVisualizer.UNIT, Orientation.DOWN);
        transformerIcon.addInNodeShapes(inLine, inEdgeL, inEdgeR, arcIn1, arcIn2, arcIn3, arcIn4);

        Line outLine = createLine(p.translate(0, 1.9 * GridVisualizer.UNIT), p.translate(0, 3 * GridVisualizer.UNIT));
        Line outEdgeL = createLine(p.translate(-1 * GridVisualizer.UNIT, 1.9 * GridVisualizer.UNIT),
                p.translate(-1 * GridVisualizer.UNIT, 2.1 * GridVisualizer.UNIT));
        Line outEdgeR = createLine(p.translate(1 * GridVisualizer.UNIT, 1.9 * GridVisualizer.UNIT),
                p.translate(1 * GridVisualizer.UNIT, 2.1 * GridVisualizer.UNIT));
        Arc arcOut1 = createHalfArc(p.translate(-0.75 * GridVisualizer.UNIT, 1.9 * GridVisualizer.UNIT), 0.25 * GridVisualizer.UNIT, Orientation.UP);
        Arc arcOut2 = createHalfArc(p.translate(-0.25 * GridVisualizer.UNIT, 1.9 * GridVisualizer.UNIT), 0.25 * GridVisualizer.UNIT, Orientation.UP);
        Arc arcOut3 = createHalfArc(p.translate(0.25 * GridVisualizer.UNIT, 1.9 * GridVisualizer.UNIT), 0.25 * GridVisualizer.UNIT, Orientation.UP);
        Arc arcOut4 = createHalfArc(p.translate(0.75 * GridVisualizer.UNIT, 1.9 * GridVisualizer.UNIT), 0.25 * GridVisualizer.UNIT, Orientation.UP);
        transformerIcon.addOutNodeShapes(outLine, outEdgeL, outEdgeR, arcOut1, arcOut2, arcOut3, arcOut4);

        return transformerIcon;
    }

    public static DeviceIcon getJumperIcon(Point p, boolean closed) {
        DeviceIcon jumperIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, GridVisualizer.UNIT));
        jumperIcon.addInNodeShapes(inLine);

        Line outLine = createLine(p.translate(0, 2 * GridVisualizer.UNIT), p.translate(0, 3 * GridVisualizer.UNIT));
        Arc jumper = createHalfArc(p.translate(0, 1.5 * GridVisualizer.UNIT), 0.5 * GridVisualizer.UNIT, Orientation.RIGHT);
        // transforms must be applied prior to adding the node
        if (!closed) rotateNode(jumper, p.translate(0, 2 * GridVisualizer.UNIT), 45);

        jumperIcon.addOutNodeShapes(outLine, jumper);

        return jumperIcon;
    }

    public static DeviceIcon getCutoutIcon(Point p, boolean closed) {
        DeviceIcon cutoutIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, .95 * GridVisualizer.UNIT));
        cutoutIcon.addInNodeShapes(inLine);

        Line outLine = createLine(p.translate(0, 2 * GridVisualizer.UNIT), p.translate(0, 3 * GridVisualizer.UNIT));
        // these shapes get rotated together
        Arc cutoutArc = createHalfArc(p.translate(0, 1.125 * GridVisualizer.UNIT), 0.15 * GridVisualizer.UNIT, Orientation.UP);
        Circle cutoutDot = createCircle(p.translate(0, 1.125 * GridVisualizer.UNIT), 0.5, Color.TRANSPARENT, Color.BLACK);
        Line cutoutLineL = createRoundedLine(p.translate(0, 2 * GridVisualizer.UNIT), p.translate(-0.15 * GridVisualizer.UNIT, 1.125 * GridVisualizer.UNIT));
        Line cutoutLineR = createRoundedLine(p.translate(0, 2 * GridVisualizer.UNIT), p.translate(0.15 * GridVisualizer.UNIT, 1.125 * GridVisualizer.UNIT));

        // rotate shapes
        if (!closed) {
            Point pivot = p.translate(0, 2 * GridVisualizer.UNIT);
            double angle = 135;

            rotateNode(cutoutArc, pivot, angle);
            rotateNode(cutoutLineL, pivot, angle);
            rotateNode(cutoutLineR, pivot, angle);
            rotateNode(cutoutDot, pivot, angle);
        }
        cutoutIcon.addOutNodeShapes(outLine, cutoutArc, cutoutDot, cutoutLineL, cutoutLineR);

        return cutoutIcon;
    }

    public static SourceIcon getPowerSourceIcon(Point p, String name, boolean isOn) {
        SourceIcon powerSourceIcon = new SourceIcon();

        Rectangle sourceBox = createRectangle(p.translate(-GridVisualizer.UNIT, 0),
                p.translate(GridVisualizer.UNIT, 2 * GridVisualizer.UNIT), Color.RED, Color.BLACK);
        powerSourceIcon.addSourceNodeShapes(sourceBox);

        Line outLine = createLine(p.translate(0, 2 * GridVisualizer.UNIT), p.translate(0, 3 * GridVisualizer.UNIT));
        powerSourceIcon.addOutputLine(outLine);

        if (!isOn) sourceBox.setFill(Color.LIME);
        Point center = p.translate(0, GridVisualizer.UNIT);
        Text text = createText(center, name, Color.BLACK, 12);
        powerSourceIcon.addStaticNodeShapes(text);

        return powerSourceIcon;
    }

    public static SourceIcon getTurbineIcon(Point p) {
        SourceIcon turbineIcon = new SourceIcon();

        Circle turbineCircle = createCircle(p.translate(0, 2 * GridVisualizer.UNIT), GridVisualizer.UNIT, Color.RED, Color.BLACK);
        turbineIcon.addSourceNodeShapes(turbineCircle);

        Line outLine1 = createLine(p, p.translate(0, GridVisualizer.UNIT));
        turbineIcon.addOutputLine(outLine1);

        Line outLine2 = createLine(p.translate(0, 3 * GridVisualizer.UNIT), p.translate(0, 4 * GridVisualizer.UNIT));
        turbineIcon.addOutputLine(outLine2);

        return turbineIcon;
    }

    public static WireIcon getWireIcon(Point p1, Point p2) {
        WireIcon wireIcon = new WireIcon();

        if (p1.equals(p2)) {
            Circle wireDot = createCircle(p1, 1, Color.BLACK, Color.BLACK);
            wireIcon.addWireShape(wireDot);
        } else {
            Line wireLine = createLine(p1, p2);
            wireIcon.addWireShape(wireLine);
        }

        return wireIcon;
    }

    private static Line createLine(Point p1, Point p2) {
        Line line = new Line();
        line.setStrokeWidth(GridVisualizer.STROKE_WIDTH);

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

    private static Rectangle createRectangle(Point p1, Point p2, Color fill, Color stroke) {
        Rectangle rectangle = new Rectangle();
        rectangle.setStrokeWidth(GridVisualizer.STROKE_WIDTH);
        rectangle.setStrokeType(StrokeType.CENTERED);
        rectangle.setStroke(stroke);
        rectangle.setFill(fill);

        rectangle.setX(p1.getX());
        rectangle.setY(p1.getY());

        rectangle.setWidth(p1.differenceX(p2));
        rectangle.setHeight(p1.differenceY(p2));

        return rectangle;
    }

    private static Arc createHalfArc(Point center, double radius, Orientation orientation) {
        Arc arc = new Arc();
        arc.setStrokeWidth(GridVisualizer.STROKE_WIDTH);
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

    private static Circle createCircle(Point center, double radius, Color fill, Color stroke) {
        Circle circle = new Circle();
        circle.setStroke(stroke);
        circle.setStrokeWidth(GridVisualizer.STROKE_WIDTH);
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

    public static Text createText(Point center, String string, Color fill, double size) {
        Text text = new Text(string);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setFont(Font.font(null, size));
        text.setFill(fill);
        text.setLayoutX(center.getX() - text.prefWidth(-1)/2);
        text.setLayoutY(center.getY() + text.prefHeight(-1)/4);
        return text;
    }
}

enum Orientation {
    UP, DOWN, LEFT, RIGHT
}
