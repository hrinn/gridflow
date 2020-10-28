package com.company.oldcomponents;

import com.company.geometry.Point;
import com.company.main.Main;
import processing.core.PConstants;

import java.util.ArrayList;

public class OldTransformer extends OldComponent {

    public OldTransformer(Main mainSketch, int id, String name, String type, char orientation, int normalState, int xPos, int yPos, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setCanOpen(false); // ensure not operable
    } // END Constructor #0

    public OldTransformer(Main mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setCanOpen(false); // ensure not operable
    } // END Constructor #1

    public OldTransformer(Main mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedToIn, String inoutIn, OldComponent connectedToOut, String inoutOut, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedToIn, inoutIn, connectedToOut, inoutOut, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setCanOpen(false); // ensure not operable
    } // END Constructor #2

    public void renderEnergy(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_FAT * scale;

        // Set drawing parameters
        mainSketch.stroke(252, 252, 3);  // yellow
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw transformer shape in highlight color if either node is energized
        if (getInNode().isEnergized() || getOutNode().isEnergized()) {
            // Draw top and bottom lines
            drawLine(0, 2);
            drawLine(1, 3);
            // Draw semicircles based on orientation
            float startArcAngleTop = 0;
            float startArcAngleBot = 0;
            switch (getOrientation()) {
                case 'U':
                    startArcAngleTop = 1f;
                    startArcAngleBot = 0f;
                    break;
                case 'R':
                    startArcAngleTop = 1.5f;
                    startArcAngleBot = 0.5f;
                    break;
                case 'L':
                    startArcAngleTop = 0.5f;
                    startArcAngleBot = 1.5f;
                    break;
                default:
                    startArcAngleTop = 0;
                    startArcAngleBot = 1;
                    break;
            } // END switch (orienation)
            // Top (inNode) arcs
            drawArc(8, startArcAngleTop, 0.5f);
            drawArc(10, startArcAngleTop, 0.5f);
            drawArc(12, startArcAngleTop, 0.5f);
            drawArc(14, startArcAngleTop, 0.5f);
            // Bottom (outNode) arcs
            drawArc(9, startArcAngleBot, 0.5f);
            drawArc(11, startArcAngleBot, 0.5f);
            drawArc(13, startArcAngleBot, 0.5f);
            drawArc(15, startArcAngleBot, 0.5f);
            // Draw tails
//            drawLine(4, 6);
//            drawLine(5, 7);
//            drawLine(16, 18);
//            drawLine(17, 19);
        } // END if Energized
    } // END renderEnergy()

    public void renderLines(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_THIN * scale;
        float unit = mainSketch.UNIT * scale;

        int x = calcPos((int) (getInNode().getCoord().getX()), scale, panX);
        int y = calcPos((int) (getInNode().getCoord().getY()), scale, panY);

        // Set drawing parameters
        mainSketch.stroke(0);  // black
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw transformer shape in highlight color if either node is energized
        // Draw top and bottom lines
        drawLine(0, 2);
        drawLine(1, 3);
        // Draw semicircles based on orientation
        float startArcAngleTop = 0;
        float startArcAngleBot = 0;
        switch (getOrientation()) {
            case 'U':
                startArcAngleTop = 1f;
                startArcAngleBot = 0f;
                break;
            case 'R':
                startArcAngleTop = 1.5f;
                startArcAngleBot = 0.5f;
                break;
            case 'L':
                startArcAngleTop = 0.5f;
                startArcAngleBot = 1.5f;
                break;
            default:
                startArcAngleTop = 0;
                startArcAngleBot = 1;
                break;
        } // END switch (orienation)
        // Top (inNode) arcs
        drawArc(8, startArcAngleTop, 0.5f);
        drawArc(10, startArcAngleTop, 0.5f);
        drawArc(12, startArcAngleTop, 0.5f);
        drawArc(14, startArcAngleTop, 0.5f);
        // Bottom (outNode) arcs
        drawArc(9, startArcAngleBot, 0.5f);
        drawArc(11, startArcAngleBot, 0.5f);
        drawArc(13, startArcAngleBot, 0.5f);
        drawArc(15, startArcAngleBot, 0.5f);
        // Draw tails
//        drawLine(4, 6);
//        drawLine(5, 7);
//        drawLine(16, 18);
//        drawLine(17, 19);

        // Place text/label at dS(20) if Right, ds(21) if Left placement
        if(getLabelPlacement() == 'R') {
            drawText(20, getLabel(), getTextAnchor(), getLabelOrientation());
        } else {
            drawText(21, getLabel(), getTextAnchor(), getLabelOrientation());
        }

    } // END renderLines()

    private void calcDrawingCoords() {

        ArrayList<Point> points = new ArrayList<>();
        float x = this.getInNode().getCoord().getX();
        float y = this.getInNode().getCoord().getY();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        points.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        points.add(this.getOutNode().getCoord());

        // These next steps define specific points
        Point point;
        point = new Point(x, y + 1.1f); // Element #2 - bottom of top vertical line
        points.add(point);
        point = new Point(x, y + 1.9f); // Element #3 - top of bottom vertical line
        points.add(point);
        point = new Point(x - 1f, y + 0.9f); // Element #4 - extreme endpoint top left tail
        points.add(point);
        point = new Point(x - 1f, y + 2.1f); // Element #5 - extreme endpoint bot left tail
        points.add(point);
        point = new Point(x - 1f, y + 1.1f); // Element #6 - near endpoint top left tail
        points.add(point);
        point = new Point(x - 1f, y + 1.9f); // Element #7 - near endpoint bot left tail
        points.add(point);
        point = new Point(x - 0.75f, y + 1.1f); // Element #8 - CP top arc #1
        points.add(point);
        point = new Point(x - 0.75f, y + 1.9f); // Element #9 - CP bot arc #1
        points.add(point);
        point = new Point(x - 0.25f, y + 1.1f); // Element #10 - CP top arc #2
        points.add(point);
        point = new Point(x - 0.25f, y + 1.9f); // Element #11 - CP bot arc #2
        points.add(point);
        point = new Point(x + 0.25f, y + 1.1f); // Element #12 - CP top arc #3
        points.add(point);
        point = new Point(x + 0.25f, y + 1.9f); // Element #13 - CP bot arc #3
        points.add(point);
        point = new Point(x + 0.75f, y + 1.1f); // Element #14 - CP top arc #4
        points.add(point);
        point = new Point(x + 0.75f, y + 1.9f); // Element #15 - CP bot arc #4
        points.add(point);
        point = new Point(x + 1f, y + 1.1f); // Element #16 - near endpoint top right tail
        points.add(point);
        point = new Point(x + 1f, y + 1.9f); // Element #17 - near endpoint bot right tail
        points.add(point);
        point = new Point(x + 1f, y + 0.9f); // Element #18 - extreme endpoint top right tail
        points.add(point);
        point = new Point(x + 1f, y + 2.1f); // Element #19 - extreme endpoint bot right tail
        points.add(point);
        point = new Point(x + 1.5f, y + 1.5f); // Element #20 - Right anchor point text/name label
        points.add(point);
        point = new Point(x - 1.5f, y + 1.5f); // Element #21 - Left anchor point text/name label
        points.add(point);

        // This next step determines if the coordinates have to be rotated, rotates them, and
        // then executes the setDs() method for this component.
        switch (this.getOrientation()) {
            case 'U':
                setDs(rotateDwgCoords(points, 180));
                break;
            case 'L':
                setDs(rotateDwgCoords(points, 90));
                break;
            case 'R':
                setDs(rotateDwgCoords(points, 270));
                break;
            default:
                setDs(points);
                break;
        } // END switch (orientation)

        // Update the location of the outNode
        this.getOutNode().setCoord(getDs().get(1));

        // Establish the click coordinates
        this.setClickCoords(4, 19);

    } // END calcDrawingCoords

} // END public class Transformer()
