package com.company;

import processing.core.PConstants;

import java.util.ArrayList;

// TODO:  renderLines() needs a routine to determine if it's crossing another line and draw an underpass

public class Bus extends Component {
    public Bus(Main mainSketch, int id, String name, String type, char orientation, int normalState, int xPos, int yPos, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setNormalState(0);  // ensures "closed"
        setCurrentState(0); // ensures "closed"
        setCanOpen(false);  // ensures logic will not allow this component to operate
    } // END Constructor #0

    public Bus(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setNormalState(0);  // ensures "closed"
        setCurrentState(0); // ensures "closed"
        setCanOpen(false);  // ensures logic will not allow this component to operate
    } // END Constructor #1

    public Bus(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedToIn, String inoutIn, Component connectedToOut, String inoutOut, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
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

        int x = calcPos((int) (getInNode().getCoord().getxPos()), scale, panX);
        int y = calcPos((int) (getInNode().getCoord().getyPos()), scale, panY);

        // Set drawing parameters
        mainSketch.stroke(0);  // black
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw line regardless
        drawLine(0, 1);

        // Draw underpass if this line crosses another line already existing in the sketch
        for (Component c : mainSketch.components) {
            if (c instanceof Bus &&                   // If component is a bus
                    this.getId() > c.getId() &&       // and component is already on the screen
                    calcOrtho(this, c) &&     // and component is orthogonal with THIS
                    calcCross(this, c))       // and component crosses THIS
            {                                         // THEN
                drawBusUnderpass(findCrossingPoint(c), c.getOrientation(), c.getInNode().isEnergized());
            } // END if component is a bus that crosses THIS
        } // END for each component

    } // END renderLines()

    private boolean calcCross(Component second, Component first) {

        float horzXMin, horzXMax, horzY;
        float vertX, vertYMin, vertYMax;

        if (first.getOrientation() == 'L' || first.getOrientation() == 'R') {

            horzXMin = Math.min(first.getInNode().getCoord().getxPos(), first.getOutNode().getCoord().getxPos());
            horzXMax = Math.max(first.getInNode().getCoord().getxPos(), first.getOutNode().getCoord().getxPos());
            horzY = first.getInNode().getCoord().getyPos();

            vertX = second.getInNode().getCoord().getxPos();
            vertYMin = Math.min(second.getInNode().getCoord().getyPos(), second.getOutNode().getCoord().getyPos());
            vertYMax = Math.max(second.getInNode().getCoord().getyPos(), second.getOutNode().getCoord().getyPos());

        } else {
            horzXMin = Math.min(second.getInNode().getCoord().getxPos(), second.getOutNode().getCoord().getxPos());
            horzXMax = Math.max(second.getInNode().getCoord().getxPos(), second.getOutNode().getCoord().getxPos());
            horzY = second.getInNode().getCoord().getyPos();

            vertX = first.getInNode().getCoord().getxPos();
            vertYMin = Math.min(first.getInNode().getCoord().getyPos(), first.getOutNode().getCoord().getyPos());
            vertYMax = Math.max(first.getInNode().getCoord().getyPos(), first.getOutNode().getCoord().getyPos());

        }

        if (horzY > vertYMin && horzY < vertYMax && vertX > horzXMin && vertX < horzXMax) return true;

        return false;

    }  // END calcOrtho()

    private boolean calcOrtho(Component second, Component first) {

        char firstOrient = 'V';
        char secondOrient = 'V';

        if (first.getOrientation() == 'L' || first.getOrientation() == 'R') firstOrient = 'H';
        if (second.getOrientation() == 'L' || second.getOrientation() == 'R') secondOrient = 'H';

        if (firstOrient == secondOrient) return false;

        return true;

    }  // END calcOrtho()

    private Coord findCrossingPoint(Component c) {

        float thisInX = this.getInNode().getCoord().getxPos();
        float thisInY = this.getInNode().getCoord().getyPos();
        float thisOutX = this.getOutNode().getCoord().getxPos();
        float thisOutY = this.getOutNode().getCoord().getyPos();
        float compInX = c.getInNode().getCoord().getxPos();
        float compInY = c.getInNode().getCoord().getyPos();
        float compOutX = c.getOutNode().getCoord().getxPos();
        float compOutY = c.getOutNode().getCoord().getyPos();

        // Find intersection coordinate x,y
        float crossPtX = ((thisInX * thisOutY - thisInY * thisOutX) * (compInX - compOutX)
                - (thisInX - thisOutX) * (compInX * compOutY - compInY * compOutX)) /
                ((thisInX - thisOutX) * (compInY - compOutY) - (thisInY - thisOutY) * (compInX - compOutX));
        float crossPtY = ((thisInX * thisOutY - thisInY * thisOutX) * (compInY - compOutY)
                - (thisInY - thisOutY) * (compInX * compOutY - compInY * compOutX)) /
                ((thisInX - thisOutX) * (compInY - compOutY) - (thisInY - thisOutY) * (compInX - compOutX));

        return new Coord(crossPtX, crossPtY);

    } // END findCrossingPoint

    private void calcDrawingCoords() {

        ArrayList<Coord> coords = new ArrayList<>();
        float x = this.getInNode().getCoord().getxPos();
        float y = this.getInNode().getCoord().getyPos();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        coords.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        coords.add(this.getOutNode().getCoord());

        // These next steps put coordinates into the array for clicking (mouse-hovering)
        Coord coord = new Coord(x - 0.25f, y); // Element #2 - top left clickable area
        coords.add(coord);
        coord = new Coord(x + 0.25f, y + this.getLength()); // Element #3 - bottom right clickable area
        coords.add(coord);

        // This next step determines if the coordinates have to be rotated, rotates them, and
        // then executes the setDs() method for this component.
        switch (this.getOrientation()) {
            case 'U':
                setDs(rotateDwgCoords(coords, 180));
                break;
            case 'L':
                setDs(rotateDwgCoords(coords, 90));
                break;
            case 'R':
                setDs(rotateDwgCoords(coords, 270));
                break;
            default:
                setDs(coords);
                break;
        } // END switch (orientation)

        // Establish the click coordinates
        this.setClickCoords(2, 3);

    } // END calcDrawingCoords

} // END public class Bus
