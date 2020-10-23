package com.company.oldcomponents;

import com.company.geometry.Point;
import com.company.main.Main;
import processing.core.PConstants;

import java.util.ArrayList;

public class Meter extends OldComponent {

    public Meter(Main mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setCanOpen(false); // ensure not operable
    } // END Constructor #1
    // DO NOT use with constructor #0 o #2!!

    public void renderEnergy(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_FAT * scale;

        // Set drawing parameters
        mainSketch.stroke(252, 252, 3);  // yellow
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw top energy line if present
        if (getInNode().isEnergized()) {
            drawLine(0, 2);
        }

        // Draw energy circle if present
        if (getInNode().isEnergized()) {
            mainSketch.noFill();
            drawTeardropDot(3, 1f);
        } // END if energized

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

        // Draw top line regardless
        drawLine(0, 2);

        // Draw circle regardless
        mainSketch.noFill();
        drawTeardropDot(3, 1f);

        // Place "M" in circle
        mainSketch.textAlign(PConstants.CENTER, PConstants.CENTER);
        drawText(3, "M", "CC", 'H');

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
        point = new Point(x, y + 2f); // Element #2 - bottom of top vertical line
        points.add(point);
        point = new Point(x, y + 2.5f); // Element #3 - CP of circle and "M" text point
        points.add(point);
        point = new Point(x + 1f, y + 2.5f); // Element #4 - anchor point text/label
        points.add(point);
        point = new Point(x - 0.5f, y + 2f); // Element #5 - top left mouse click area
        points.add(point);
        point = new Point(x + 0.5f, y + 3f); // Element #6 - bot right mouse click area
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
        this.setClickCoords(5, 6);

    } // END calcDrawingCoords

} // END public class Meter
