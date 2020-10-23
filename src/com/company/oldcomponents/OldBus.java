package com.company.oldcomponents;

import com.company.geometry.Point;
import com.company.main.Main;
import processing.core.PConstants;

import java.util.ArrayList;

// TODO:  renderLines() needs a routine to determine if it's crossing another line and draw an underpass

public class OldBus extends OldComponent {
    public OldBus(Main mainSketch, int id, String name, String type, char orientation, int normalState, int xPos, int yPos, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setNormalState(0);  // ensures "closed"
        setCurrentState(0); // ensures "closed"
        setCanOpen(false);  // ensures logic will not allow this component to operate
    } // END Constructor #0

    public OldBus(Main mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setNormalState(0);  // ensures "closed"
        setCurrentState(0); // ensures "closed"
        setCanOpen(false);  // ensures logic will not allow this component to operate
    } // END Constructor #1

    public OldBus(Main mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedToIn, String inoutIn, OldComponent connectedToOut, String inoutOut, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedToIn, inoutIn, connectedToOut, inoutOut, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setNormalState(0);  // ensures "closed"
        setCurrentState(0); // ensures "closed"
        setCanOpen(false);  // ensures logic will not allow this component to operate
    } // END Constructor #2

    public void renderEnergy(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_FAT * scale;

        // Set drawing parameters
        mainSketch.stroke(252, 252, 3);  // yellow
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw energy line if present
        if (getInNode().isEnergized() || getOutNode().isEnergized()) {
            drawLine(0, 1);
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

        // Draw line regardless
        drawLine(0, 1);

        // Draw underpass if this line crosses another line already existing in the sketch
        for (OldComponent c : mainSketch.oldComponents) {
            if (c instanceof OldBus &&                   // If component is a bus
                    this.getId() > c.getId() &&       // and component is already on the screen
                    calcOrtho(this, c) &&     // and component is orthogonal with THIS
                    calcCross(this, c))       // and component crosses THIS
            {                                         // THEN
                drawBusUnderpass(findCrossingPoint(c), c.getOrientation(), c.getInNode().isEnergized());
            } // END if component is a bus that crosses THIS
        } // END for each component

    } // END renderLines()

    private boolean calcCross(OldComponent second, OldComponent first) {

        float horzXMin, horzXMax, horzY;
        float vertX, vertYMin, vertYMax;

        if (first.getOrientation() == 'L' || first.getOrientation() == 'R') {

            horzXMin = Math.min(first.getInNode().getCoord().getX(), first.getOutNode().getCoord().getX());
            horzXMax = Math.max(first.getInNode().getCoord().getX(), first.getOutNode().getCoord().getX());
            horzY = first.getInNode().getCoord().getY();

            vertX = second.getInNode().getCoord().getX();
            vertYMin = Math.min(second.getInNode().getCoord().getY(), second.getOutNode().getCoord().getY());
            vertYMax = Math.max(second.getInNode().getCoord().getY(), second.getOutNode().getCoord().getY());

        } else {
            horzXMin = Math.min(second.getInNode().getCoord().getX(), second.getOutNode().getCoord().getX());
            horzXMax = Math.max(second.getInNode().getCoord().getX(), second.getOutNode().getCoord().getX());
            horzY = second.getInNode().getCoord().getY();

            vertX = first.getInNode().getCoord().getX();
            vertYMin = Math.min(first.getInNode().getCoord().getY(), first.getOutNode().getCoord().getY());
            vertYMax = Math.max(first.getInNode().getCoord().getY(), first.getOutNode().getCoord().getY());

        }

        if (horzY > vertYMin && horzY < vertYMax && vertX > horzXMin && vertX < horzXMax) return true;

        return false;

    }  // END calcOrtho()

    private boolean calcOrtho(OldComponent second, OldComponent first) {

        char firstOrient = 'V';
        char secondOrient = 'V';

        if (first.getOrientation() == 'L' || first.getOrientation() == 'R') firstOrient = 'H';
        if (second.getOrientation() == 'L' || second.getOrientation() == 'R') secondOrient = 'H';

        if (firstOrient == secondOrient) return false;

        return true;

    }  // END calcOrtho()

    private Point findCrossingPoint(OldComponent c) {

        float thisInX = this.getInNode().getCoord().getX();
        float thisInY = this.getInNode().getCoord().getY();
        float thisOutX = this.getOutNode().getCoord().getX();
        float thisOutY = this.getOutNode().getCoord().getY();
        float compInX = c.getInNode().getCoord().getX();
        float compInY = c.getInNode().getCoord().getY();
        float compOutX = c.getOutNode().getCoord().getX();
        float compOutY = c.getOutNode().getCoord().getY();

        // Find intersection coordinate x,y
        float crossPtX = ((thisInX * thisOutY - thisInY * thisOutX) * (compInX - compOutX)
                - (thisInX - thisOutX) * (compInX * compOutY - compInY * compOutX)) /
                ((thisInX - thisOutX) * (compInY - compOutY) - (thisInY - thisOutY) * (compInX - compOutX));
        float crossPtY = ((thisInX * thisOutY - thisInY * thisOutX) * (compInY - compOutY)
                - (thisInY - thisOutY) * (compInX * compOutY - compInY * compOutX)) /
                ((thisInX - thisOutX) * (compInY - compOutY) - (thisInY - thisOutY) * (compInX - compOutX));

        return new Point(crossPtX, crossPtY);

    } // END findCrossingPoint

    private void calcDrawingCoords() {

        ArrayList<Point> points = new ArrayList<>();
        float x = this.getInNode().getCoord().getX();
        float y = this.getInNode().getCoord().getY();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        points.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        points.add(this.getOutNode().getCoord());

        // These next steps put coordinates into the array for clicking (mouse-hovering)
        Point point = new Point(x - 0.25f, y); // Element #2 - top left clickable area
        points.add(point);
        point = new Point(x + 0.25f, y + this.getLength()); // Element #3 - bottom right clickable area
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
        this.setClickCoords(2, 3);

    } // END calcDrawingCoords

} // END public class Bus
