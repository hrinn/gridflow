package com.company.oldcomponents;

import com.company.geometry.Point;
import com.company.main.Main;
import processing.core.PConstants;

import java.util.ArrayList;

public class Turbine extends OldComponent {

    public Turbine(Main mainSketch, int id, String name, String type, char orientation, int normalState, int xPos, int yPos, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
    }  // END Constructor #0

    public void renderEnergy(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_FAT * scale;

        // Set drawing parameters
        mainSketch.stroke(252, 252, 3);  // yellow
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw top and bottom energy highlight lines if present
        if (getInNode().isEnergized()) {
            drawLine(0, 2);
            drawLine(1, 3);
        }

        // Draw energy circle if present
        if (getOutNode().isEnergized()) {
            drawTeardropDot(4, 2);
        }

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

        // Draw top and bottom lines regardless
        drawLine(0, 2);
        drawLine(1, 3);

        // Draw circle regardless, but fill with Green or Red depending
        if(getCurrentState() == 0) {
            mainSketch.fill(255, 0, 0); // Red
        } else {
            mainSketch.fill(0, 255, 0); // Green
        }
        drawTeardropDot(4, 2);

        // Place text/label at dS(7) regardless
        mainSketch.fill(0);
        drawText(7, getLabel(), getTextAnchor(), getLabelOrientation());

    } // END renderLines()

    // Establishes all of the grid coordinates needed to draw this object at the correct orientation
    public void calcDrawingCoords() {

        ArrayList<Point> points = new ArrayList<>();
        float x = this.getInNode().getCoord().getX();
        float y = this.getInNode().getCoord().getY();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        points.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        points.add(this.getOutNode().getCoord());

        // These next steps define specific points
        Point point;
        point = new Point(x, y + 1f); // Element #2 - bottom of top vertical line
        points.add(point);
        point = new Point(x, y + 3f); // Element #3 - top of bottom vertical line
        points.add(point);
        point = new Point(x, y + 2f); // Element #4 - circle centerpoint
        points.add(point);
        point = new Point(x - 0.707f, y + 1.293f); // Element #5 - TL of click coordinates
        points.add(point);
        point = new Point(x + 0.707f, y + 2.707f); // Element #6 - BR of click coordinates
        points.add(point);
        point = new Point(x, y + 1.75f); // Element #7 - Text anchor
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

        // Establish the click coordinates
        this.setClickCoords(5, 6);

    } // END calcDrawingCoords

} // END public class Turbine