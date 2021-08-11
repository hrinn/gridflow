package visualization.componentIcons;

import application.Globals;
import construction.ComponentType;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.transform.Rotate;
import domain.geometry.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ComponentIconCreator {

    private static final double BRIDGE_GAP = 10;
    private static final double LOCK_TEXT_SIZE = 6;
    private static final String LOCK_TEXT = "LOCK";

    public static DeviceIcon getSwitchIcon(Point p, boolean isClosed, boolean isClosedByDefault, boolean isLocked) {
        DeviceIcon switchIcon = new DeviceIcon();

        // base shape
        Line inLine = createLine(p, p.translate(0, 1.25 * Globals.UNIT));
        Line outLine = createLine(p.translate(0, 1.75 * Globals.UNIT),
                p.translate(0, 3 * Globals.UNIT));
        switchIcon.addInNodeShapes(inLine);
        switchIcon.addOutNodeShapes(outLine);

        if (isLocked) {
            Circle lockCircle = createCircle(p.translate(0, 1.5 * Globals.UNIT), 0.5 * Globals.UNIT,
                    Color.YELLOW, Color.BLACK);
            Text lockText = createText(p.translate(0, 1.5*Globals.UNIT), LOCK_TEXT, Color.RED, LOCK_TEXT_SIZE);
            switchIcon.addStaticNodes(lockCircle);
            switchIcon.addTextElement(lockText);
        } else {
            Line inBar = createLine(p.translate(-0.5 * Globals.UNIT, 1.25 * Globals.UNIT),
                    p.translate(0.5 * Globals.UNIT, 1.25 * Globals.UNIT));
            switchIcon.addInNodeShapes(inBar);


            Line outBar = createLine(p.translate(-0.5 * Globals.UNIT, 1.75 * Globals.UNIT),
                    p.translate(0.5 * Globals.UNIT, 1.75 * Globals.UNIT));
            switchIcon.addOutNodeShapes(outBar);

            // State indicators

            if (isClosedByDefault) {
                if (isClosed){
                    Line closedBar = createLine(p.translate(0.5 * Globals.UNIT, Globals.UNIT),
                            p.translate(-0.5 * Globals.UNIT, 2 * Globals.UNIT));
                    switchIcon.addMidNodeShapes(closedBar);
                }
                else {
                    Circle openCircle = createCircle(p.translate(0, 1.5 * Globals.UNIT), 0.25 * Globals.UNIT,
                            Color.TRANSPARENT, Color.LIMEGREEN);
                    switchIcon.addStaticNodes(openCircle);
                }
            }
            else {
                if (isClosed) {
                    Line closedBar1 = createLine(p.translate(0.5 * Globals.UNIT, Globals.UNIT),
                            p.translate(-0.5 * Globals.UNIT, 2 * Globals.UNIT));
                    closedBar1.setStroke(Color.RED);
                    Line closedBar2 = createLine(p.translate(-0.5 * Globals.UNIT, Globals.UNIT),
                            p.translate(0.5 * Globals.UNIT, 2 * Globals.UNIT));
                    closedBar2.setStroke(Color.RED);
                    switchIcon.addStaticNodes(closedBar1, closedBar2);
                }
            }
        }

        switchIcon.setBoundingRect(new Dimensions(), p);
        switchIcon.setFittingRect(new Dimensions(2, 3, -1, -1, -0.5, -0.5), p);

        return switchIcon;
    }

    public static BreakerIcon get70KVBreakerIcon(Point p, boolean isClosed, boolean isClosedByDefault, boolean isLocked) {
        BreakerIcon breakerIcon = new BreakerIcon();

        //base shape
        Line inLine = createLine(p, p.translate(0, 1 * Globals.UNIT));
        breakerIcon.addInNodeShapes(inLine);

        Line outLine = createLine(p.translate(0, 2 * Globals.UNIT), p.translate(0, 3 * Globals.UNIT));
        breakerIcon.addOutNodeShapes(outLine);

        if(isLocked) {
            Rectangle box = createRectangle(p.translate(-0.5 * Globals.UNIT, 1 * Globals.UNIT),
                    p.translate(0.5 * Globals.UNIT, 2 * Globals.UNIT), Color.YELLOW, Color.BLACK);
            breakerIcon.addBackfedOffNodeShapes(box);
            Text lockText = createText(p.translate(0, 1.5*Globals.UNIT), LOCK_TEXT, Color.RED, LOCK_TEXT_SIZE);
            breakerIcon.addTextElement(lockText);
        } else {
            Rectangle box = createRectangle(p.translate(-0.5 * Globals.UNIT, 1 * Globals.UNIT),
                    p.translate(0.5 * Globals.UNIT, 2 * Globals.UNIT), Color.RED, Color.BLACK);
            breakerIcon.addBackfedOffNodeShapes(box);
            Point center = p.translate(0, 1.5 * Globals.UNIT);

            if (isClosedByDefault) {
                if (!isClosed) {
                    box.setFill(Color.LIME);
                    Text text = createText(center, "N/C", Color.WHITE, 10);
                    breakerIcon.addTextElement(text);
                }
            }
            else {
                if (isClosed) {
                    Text text = createText(center, "N/O", Color.WHITE, 10);
                    breakerIcon.addTextElement(text);
                }
                else {
                    box.setFill(Color.LIME);
                }
            }
        }

        breakerIcon.setBoundingRect(new Dimensions(), p);
        breakerIcon.setFittingRect(new Dimensions(2, 3, -1, -1, -0.5, -0.5), p);
        return breakerIcon;
    }

    public static BreakerIcon get12KVBreakerIcon(Point p, boolean isClosed, boolean isClosedByDefault, boolean isLocked) {
        BreakerIcon breakerIcon = new BreakerIcon();

        //base shape
        Line inLine1 = createLine(p, p.translate(0, 0.75 * Globals.UNIT));
        Line inLine2 = createRoundedLine(p.translate(0, Globals.UNIT), p.translate(0, 1.5 * Globals.UNIT));
        Line inChevron1L = createRoundedLine(p.translate(-0.5 * Globals.UNIT, Globals.UNIT), p.translate(0, 0.75 * Globals.UNIT));
        Line inChevron1R = createRoundedLine(p.translate(0.5 * Globals.UNIT, Globals.UNIT), p.translate(0, 0.75 * Globals.UNIT));
        Line inChevron2L = createRoundedLine(p.translate(-0.5 * Globals.UNIT, 1.25 * Globals.UNIT), p.translate(0, Globals.UNIT));
        Line inChevron2R = createRoundedLine(p.translate(0.5 * Globals.UNIT, 1.25 * Globals.UNIT), p.translate(0, Globals.UNIT));
        breakerIcon.addInNodeShapes(inLine1, inLine2, inChevron1L, inChevron1R, inChevron2L, inChevron2R);

        Line outLine1 = createRoundedLine(p.translate(0, 2.5 * Globals.UNIT), p.translate(0, 3 * Globals.UNIT));
        Line outLine2 = createLine(p.translate(0, 3.25 * Globals.UNIT), p.translate(0, 4 * Globals.UNIT));
        Line outChevron1L = createRoundedLine(p.translate(-0.5 * Globals.UNIT, 2.75 * Globals.UNIT),
                p.translate(0, 3 * Globals.UNIT));
        Line outChevron1R = createRoundedLine(p.translate(0.5 * Globals.UNIT, 2.75 * Globals.UNIT),
                p.translate(0, 3 * Globals.UNIT));
        Line outChevron2L = createRoundedLine(p.translate(-0.5 * Globals.UNIT, 3 * Globals.UNIT),
                p.translate(0, 3.25 * Globals.UNIT));
        Line outChevron2R = createRoundedLine(p.translate(0.5 * Globals.UNIT, 3 * Globals.UNIT),
                p.translate(0, 3.25 * Globals.UNIT));
        breakerIcon.addOutNodeShapes(outLine1, outLine2, outChevron1L, outChevron1R, outChevron2L, outChevron2R);


        if(isLocked) {
            Rectangle box = createRectangle(p.translate(-0.5 * Globals.UNIT, 1.5 * Globals.UNIT),
                    p.translate(0.5 * Globals.UNIT, 2.5 * Globals.UNIT), Color.YELLOW, Color.BLACK);
            Text lockText = createText(p.translate(0, 2*Globals.UNIT), LOCK_TEXT, Color.RED, LOCK_TEXT_SIZE);
            breakerIcon.addBackfedOffNodeShapes(box);
            breakerIcon.addTextElement(lockText);
        } else {
            Rectangle box = createRectangle(p.translate(-0.5 * Globals.UNIT, 1.5 * Globals.UNIT),
                    p.translate(0.5 * Globals.UNIT, 2.5 * Globals.UNIT), Color.RED, Color.BLACK);
            breakerIcon.addBackfedOffNodeShapes(box);

            Point center = p.translate(0, 2 * Globals.UNIT);

            if (isClosedByDefault) {
                if (!isClosed) {
                    box.setFill(Color.LIME);
                    Text text = createText(center, "N/C", Color.WHITE, 10);
                    breakerIcon.addTextElement(text);
                }
            }
            else {
                if (isClosed) {
                    Text text = createText(center, "N/O", Color.WHITE, 10);
                    breakerIcon.addTextElement(text);
                }
                else {
                    box.setFill(Color.LIME);
                }
            }
        }

        breakerIcon.setBoundingRect(new Dimensions(2, 4), p);
        breakerIcon.setFittingRect(new Dimensions(2, 4, -0.75, -0.75, -0.5, -0.5), p);
        return breakerIcon;
    }

    public static DeviceIcon getTransformerIcon(Point p) {

        // change to new icon that can't be split energy maybe
        DeviceIcon transformerIcon = new DeviceIcon();

        Line inLine = createLine(p, p.translate(0, 1.1 * Globals.UNIT));
        Line inEdgeL = createLine(p.translate(-1 * Globals.UNIT, 0.9 * Globals.UNIT),
                p.translate(-1 * Globals.UNIT, 1.1 * Globals.UNIT));
        Line inEdgeR = createLine(p.translate(1 * Globals.UNIT, 0.9 * Globals.UNIT),
                p.translate(1 * Globals.UNIT, 1.1 * Globals.UNIT));
        Arc arcIn1 = createHalfArc(p.translate(-0.75 * Globals.UNIT, 1.1 * Globals.UNIT), 0.25 * Globals.UNIT, ArcOrientation.DOWN);
        Arc arcIn2 = createHalfArc(p.translate(-0.25 * Globals.UNIT, 1.1 * Globals.UNIT), 0.25 * Globals.UNIT, ArcOrientation.DOWN);
        Arc arcIn3 = createHalfArc(p.translate(0.25 * Globals.UNIT, 1.1 * Globals.UNIT), 0.25 * Globals.UNIT, ArcOrientation.DOWN);
        Arc arcIn4 = createHalfArc(p.translate(0.75 * Globals.UNIT, 1.1 * Globals.UNIT), 0.25 * Globals.UNIT, ArcOrientation.DOWN);
        transformerIcon.addInNodeShapes(inLine, inEdgeL, inEdgeR, arcIn1, arcIn2, arcIn3, arcIn4);

        Line outLine = createLine(p.translate(0, 1.9 * Globals.UNIT), p.translate(0, 3 * Globals.UNIT));
        Line outEdgeL = createLine(p.translate(-1 * Globals.UNIT, 1.9 * Globals.UNIT),
                p.translate(-1 * Globals.UNIT, 2.1 * Globals.UNIT));
        Line outEdgeR = createLine(p.translate(1 * Globals.UNIT, 1.9 * Globals.UNIT),
                p.translate(1 * Globals.UNIT, 2.1 * Globals.UNIT));
        Arc arcOut1 = createHalfArc(p.translate(-0.75 * Globals.UNIT, 1.9 * Globals.UNIT), 0.25 * Globals.UNIT, ArcOrientation.UP);
        Arc arcOut2 = createHalfArc(p.translate(-0.25 * Globals.UNIT, 1.9 * Globals.UNIT), 0.25 * Globals.UNIT, ArcOrientation.UP);
        Arc arcOut3 = createHalfArc(p.translate(0.25 * Globals.UNIT, 1.9 * Globals.UNIT), 0.25 * Globals.UNIT, ArcOrientation.UP);
        Arc arcOut4 = createHalfArc(p.translate(0.75 * Globals.UNIT, 1.9 * Globals.UNIT), 0.25 * Globals.UNIT, ArcOrientation.UP);
        transformerIcon.addOutNodeShapes(outLine, outEdgeL, outEdgeR, arcOut1, arcOut2, arcOut3, arcOut4);

        transformerIcon.setBoundingRect(new Dimensions(3, 3), p);
        transformerIcon.setFittingRect(new Dimensions(3, 3, -0.75, -0.75, -0.5, -0.5), p);
        return transformerIcon;
    }

    public static DeviceIcon getJumperIcon(Point p, boolean closed, boolean isLocked) {
        DeviceIcon jumperIcon = new DeviceIcon();

        //base shape
        Line inLine = createLine(p, p.translate(0, Globals.UNIT));
        jumperIcon.addInNodeShapes(inLine);

        Line outLine = createLine(p.translate(0, 2 * Globals.UNIT), p.translate(0, 3 * Globals.UNIT));
        jumperIcon.addOutNodeShapes(outLine);


        if(isLocked) {
            Circle lockCircle = createCircle(p.translate(0, 1.5 * Globals.UNIT), 0.5 * Globals.UNIT,
                    Color.YELLOW, Color.BLACK);
            Text lockText = createText(p.translate(0, 1.5*Globals.UNIT), LOCK_TEXT, Color.RED, LOCK_TEXT_SIZE);
            jumperIcon.addStaticNodes(lockCircle);
            jumperIcon.addTextElement(lockText);
        } else {
            Arc jumper = createHalfArc(p.translate(0, 1.5 * Globals.UNIT), 0.5 * Globals.UNIT, ArcOrientation.RIGHT);
            // transforms must be applied prior to adding the node
            if (!closed) rotateNode(jumper, p.translate(0, 2 * Globals.UNIT), 45);

            jumperIcon.addOutNodeShapes(jumper);
        }



        jumperIcon.setBoundingRect(new Dimensions(2, 3, -0.25, -0.25, -0.75, 0), p);
        jumperIcon.setFittingRect(new Dimensions(2, 3, -1, -0.75, -1, 0), p);
        return jumperIcon;
    }

    public static DeviceIcon getCutoutIcon(Point p, boolean closed, boolean isLocked) {
        DeviceIcon cutoutIcon = new DeviceIcon();

        //base shape
        Line inLine = createLine(p, p.translate(0, .95 * Globals.UNIT));
        cutoutIcon.addInNodeShapes(inLine);

        Line outLine = createLine(p.translate(0, 2 * Globals.UNIT), p.translate(0, 3 * Globals.UNIT));
        cutoutIcon.addOutNodeShapes(outLine);


        if(isLocked) {
            Circle lockCircle = createCircle(p.translate(0, 1.5 * Globals.UNIT), 0.5 * Globals.UNIT,
                    Color.YELLOW, Color.BLACK);
            Text lockText = createText(p.translate(0, 1.5*Globals.UNIT), LOCK_TEXT, Color.RED, LOCK_TEXT_SIZE);
            cutoutIcon.addStaticNodes(lockCircle);
            cutoutIcon.addTextElement(lockText);
        } else {
            // these shapes get rotated together
            Arc cutoutArc = createHalfArc(p.translate(0, 1.125 * Globals.UNIT), 0.15 * Globals.UNIT, ArcOrientation.UP);
            Circle cutoutDot = createCircle(p.translate(0, 1.125 * Globals.UNIT), 0.5, Color.TRANSPARENT, Color.BLACK);
            Line cutoutLineL = createRoundedLine(p.translate(0, 2 * Globals.UNIT), p.translate(-0.15 * Globals.UNIT, 1.125 * Globals.UNIT));
            Line cutoutLineR = createRoundedLine(p.translate(0, 2 * Globals.UNIT), p.translate(0.15 * Globals.UNIT, 1.125 * Globals.UNIT));

            // rotate shapes
            if (!closed) {
                Point pivot = p.translate(0, 2 * Globals.UNIT);
                double angle = 135;

                rotateNode(cutoutArc, pivot, angle);
                rotateNode(cutoutLineL, pivot, angle);
                rotateNode(cutoutLineR, pivot, angle);
                rotateNode(cutoutDot, pivot, angle);
            }
            cutoutIcon.addOutNodeShapes(cutoutArc, cutoutDot, cutoutLineL, cutoutLineR);
        }



        cutoutIcon.setBoundingRect(new Dimensions(), p);
        cutoutIcon.setFittingRect(new Dimensions(2, 3, -1, -0.25, -0.75, -0.25), p);

        return cutoutIcon;
    }

    public static SourceIcon getPowerSourceIcon(Point p, String name, boolean isOn, boolean isLocked) {
        SourceIcon powerSourceIcon = new SourceIcon(ComponentType.POWER_SOURCE);
        Point center = p.translate(0, -2 * Globals.UNIT);

        //base shape
        Line outLine = createLine(p.translate(0, 0), p.translate(0, -Globals.UNIT));
        powerSourceIcon.addOutputLine(outLine);

        if(isLocked) {
            Rectangle sourceBox = createRectangle(p.translate(-Globals.UNIT, -3 * Globals.UNIT),
                    p.translate(Globals.UNIT, -Globals.UNIT), Color.YELLOW, Color.BLACK);
            powerSourceIcon.addSourceNodeShapes(sourceBox);
            Text lockText = createText(center, LOCK_TEXT, Color.RED, LOCK_TEXT_SIZE);
            powerSourceIcon.addTextElement(lockText);
        } else {
            Rectangle sourceBox = createRectangle(p.translate(-Globals.UNIT, -3 * Globals.UNIT),
                    p.translate(Globals.UNIT, -Globals.UNIT), Color.RED, Color.BLACK);
            powerSourceIcon.addSourceNodeShapes(sourceBox);

            if (!isOn) sourceBox.setFill(Color.LIME);
            Text text = createText(center, name, Color.BLACK, 12);
            powerSourceIcon.addTextElement(text);
        }

        powerSourceIcon.setBoundingRect(new Dimensions(2, 3, 0, -0.25, 0, 0, true), p);
        powerSourceIcon.setFittingRect(new Dimensions(2, 3, 0, -1, 0, 0, true), p);
        return powerSourceIcon;
    }

    public static SourceIcon getTurbineIcon(Point p, boolean isOn, boolean isLocked) {
        SourceIcon turbineIcon = new SourceIcon(ComponentType.TURBINE);

        //base shape
        Line outLine1 = createLine(p, p.translate(0, Globals.UNIT));
        turbineIcon.addOutputLine(outLine1);

        Line outLine2 = createLine(p.translate(0, 3 * Globals.UNIT), p.translate(0, 4 * Globals.UNIT));
        turbineIcon.addOutputLine(outLine2);



        if(isLocked) {
            Circle turbineCircle = createCircle(p.translate(0, 2 * Globals.UNIT), Globals.UNIT, Color.YELLOW, Color.BLACK);
            turbineIcon.addSourceNodeShapes(turbineCircle);
            Text lockText = createText(p.translate(0, 2*Globals.UNIT), LOCK_TEXT, Color.RED, LOCK_TEXT_SIZE);
            turbineIcon.addTextElement(lockText);
        } else {
            Circle turbineCircle = createCircle(p.translate(0, 2 * Globals.UNIT), Globals.UNIT, Color.RED, Color.BLACK);
            turbineIcon.addSourceNodeShapes(turbineCircle);

            if (!isOn) turbineCircle.setFill(Color.LIME);
        }
        turbineIcon.setBoundingRect(new Dimensions(3, 4), p);
        turbineIcon.setFittingRect(new Dimensions(3, 4, -1, -1, -0.5, -0.5), p);
        return turbineIcon;
    }

    public static WireIcon getWireIcon(Point p1, Point p2, List<Point> bridgePoints) {
        WireIcon wireIcon = new WireIcon();

        if (p1.equals(p2)) {
            Circle wireDot = createCircle(p1, 1, Color.BLACK, Color.BLACK);
            wireIcon.addWireShape(wireDot);
        } else if (bridgePoints.isEmpty()) {
            Line wireLine = createLine(p1, p2);
            wireIcon.addWireShape(wireLine);
        } else { // create a line with gaps
            createBridgeWire(p1, p2, bridgePoints).forEach(wireIcon::addWireShape);
        }

        Dimensions dim = new Dimensions(p1.differenceX(p2)/Globals.UNIT, p1.differenceY(p2)/Globals.UNIT, 0.25);
        Dimensions dim2 = new Dimensions(p1.differenceX(p2)/Globals.UNIT, p1.differenceY(p2)/Globals.UNIT, 0.1);

        Point mid = Point.midpoint(p1, p2);
        wireIcon.setBoundingRect(dim, mid);
        wireIcon.setFittingRect(dim2, mid);

        return wireIcon;
    }

    private static List<Shape> createBridgeWire(Point p1, Point p2, List<Point> bridgePoints) {
        List<Shape> wires = new ArrayList<>();
        boolean vertical = p1.differenceX(p2) == 0;

        // ensure the bridgePoints are sorted left to right if horizontal, or top to bottom if vertical
        Collections.sort(bridgePoints, (bp1, bp2) -> {
            if (vertical) {
                return (int)bp1.getY() - (int)bp2.getY();
            } else {
                return (int)bp1.getX() - (int)bp2.getX();
            }
        });

        List<Point> wirePoints = new ArrayList<>();
        wirePoints.add(p1); // first wire point
        // split the bridgePoints and add them to wire points
        for (Point bp : bridgePoints) {
            Point bp1, bp2;
            if (vertical) {
                bp1 = bp.translate(0, -BRIDGE_GAP/2);
                bp2 = bp.translate(0, BRIDGE_GAP/2);
            } else {
                bp1 = bp.translate(-BRIDGE_GAP/2, 0);
                bp2 = bp.translate(BRIDGE_GAP/2, 0);
            }
            wirePoints.add(bp1);
            wirePoints.add(bp2);
        }
        wirePoints.add(p2); // last wire point

        // create lines out of the pairs of wire points
        for (int i = 0; i < wirePoints.size(); i += 2) {
            Line segment = createLine(wirePoints.get(i),  wirePoints.get(i+1));
            wires.add(segment);
        }

        return wires;
    }

    public static WireIcon getBlankWireIcon(Point p1, Point p2) {

        WireIcon wireIcon = new WireIcon();

        Dimensions dim = new Dimensions(p1.differenceX(p2)/Globals.UNIT, p1.differenceY(p2)/Globals.UNIT, 0.25);
        Dimensions dim2 = new Dimensions(p1.differenceX(p2)/Globals.UNIT, p1.differenceY(p2)/Globals.UNIT, 0.1);

        Point mid = Point.midpoint(p1, p2);
        wireIcon.setBoundingRect(dim, mid);
        wireIcon.setFittingRect(dim2, mid);

        return wireIcon;
    }

    public static AssociationIcon getAssociationNode(Point topLeft, double width, double height, String label, UUID id) {
        AssociationIcon icon = new AssociationIcon();

        // this is the association rectangle displayed on screen
        Rectangle border = new Rectangle(topLeft.getX(), topLeft.getY(), width, height);
        border.setFill(Color.TRANSPARENT);
        border.setStroke(Color.GRAY);
        border.setStrokeWidth(2);
        icon.setRect(border);

        // resize handles, these are the circles that appear in the 4 corners and allow the users to drag the association
        Circle NW = new Circle();
        Circle SE = new Circle();
        Circle NE = new Circle();
        Circle SW = new Circle();
        List<Circle> handles = List.of(NW, SE, NE, SW);
        handles.forEach(handle -> {
            handle.setRadius(Globals.ASSOC_HANDLE_RADIUS);
            handle.setFill(Color.TRANSPARENT);
            handle.setStroke(Color.GRAY);
            handle.setStrokeWidth(2);
        });
        // handle positions are set in this function
        icon.setHandles(NW, SE, NE, SW);

        // create the text that displays inside the associations
        Point labelPos = getAssociationLabelPosition(topLeft, width, height);
        Text labelText = createText(labelPos, label, Color.BLACK, 32);
        icon.setText(labelText);
        icon.setID(id);

        return icon;
    }

    // gets the top left part of a 3x3 grid to place the text
    private static Point getAssociationLabelPosition(Point topLeft, double width, double height) {
        return new Point(topLeft.getX() + width/2, topLeft.getY() + height/2);
    }

    private static Line createLine(Point p1, Point p2) {
        Line line = new Line();
        line.setStrokeWidth(Globals.STROKE_WIDTH);

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
        rectangle.setStrokeWidth(Globals.STROKE_WIDTH);
        rectangle.setStrokeType(StrokeType.CENTERED);
        rectangle.setStroke(stroke);
        rectangle.setFill(fill);

        rectangle.setX(p1.getX());
        rectangle.setY(p1.getY());

        rectangle.setWidth(p1.differenceX(p2));
        rectangle.setHeight(p1.differenceY(p2));

        return rectangle;
    }

    private static Arc createHalfArc(Point center, double radius, ArcOrientation arcOrientation) {
        Arc arc = new Arc();
        arc.setStrokeWidth(Globals.STROKE_WIDTH);
        arc.setStroke(Color.BLACK);
        arc.setFill(Color.TRANSPARENT);
        arc.setType(ArcType.OPEN);
        arc.setStrokeType(StrokeType.CENTERED);

        arc.setCenterX(center.getX());
        arc.setCenterY(center.getY());
        arc.setRadiusX(radius);
        arc.setRadiusY(radius);
        arc.setLength(180);
        arc.setStartAngle(getArcStartAngle(arcOrientation));

        return arc;
    }

    private static double getArcStartAngle(ArcOrientation arcOrientation) {
        switch (arcOrientation) {
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
        circle.setStrokeWidth(Globals.STROKE_WIDTH);
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

enum ArcOrientation {
    UP, DOWN, LEFT, RIGHT
}
