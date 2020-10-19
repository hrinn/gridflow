package com.company.components;

import com.company.main.Main;
import processing.core.PConstants;

import java.util.ArrayList;

public class Breaker70kV extends Component {

    // TODO:  Link the solar plant to SS70-1 and SS70-2

    public Breaker70kV(Main mainSketch, int id, String name, String type, char orientation, int normalState, int xPos, int yPos, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
    calcDrawingCoords();
    } // END Constructor #0

    public Breaker70kV(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
    } // END Constructor #1

    public Breaker70kV(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedToIn, String inoutIn, Component connectedToOut, String inoutOut, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedToIn, inoutIn, connectedToOut, inoutOut, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
    } // END Constructor #2

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

        // Draw bottom energy line if present
        if (getOutNode().isEnergized()) {
            drawLine(1, 3);
        }

        // Draw energy box if present
        if ((getInNode().isEnergized() && getCurrentState() == 0) ||
                getOutNode().isEnergized() && getCurrentState() == 0) {
            drawBox( 4, 5, 6, 7, 1);
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

        // Draw top black line regardless
        drawLine(0, 2);

        // Draw bottom black line regardless
        drawLine(1, 3);

        // Draw box but fill with green or red depending on state open/closed
        if (getCurrentState() == 0) mainSketch.fill(255, 0, 0); // red
        else if(getCurrentState() == 2) mainSketch.fill(252, 252, 3);  // yellow
        else mainSketch.fill(0, 255, 0); // green
        drawBox(4, 5, 6, 7, 1);

        // Inscribe "LOCK" when locked open
        if(getCurrentState() == 2) {
            mainSketch.fill(0); // black
            drawText(8, "LOCK", "CC", 'H');
        }
        // Otherwise inscribe "N/C" when open and "N/O" when closed
        else if (!(getNormalState() == getCurrentState())) {
            mainSketch.fill(0);
            if (getNormalState() == 0) {
                drawText(8, "N/C", "CC", 'H');
            } else {
                drawText(8, "N/O", "CC", 'H');
            }
        }

        // Place text/label at dS(9)
        mainSketch.fill(0); // black
        if(getLabelPlacement() == 'R') {
            drawText(9, getLabel(), getTextAnchor(), getLabelOrientation());
        } else {
            drawText(10, getLabel(), getTextAnchor(), getLabelOrientation());
        }


    } // END renderLines()

    private void calcDrawingCoords() {
        ArrayList<Coord> coords = new ArrayList<>();
        float x = this.getInNode().getCoord().getxPos();
        float y = this.getInNode().getCoord().getyPos();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        coords.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        coords.add(this.getOutNode().getCoord());

        // These next steps define specific points
        Coord coord;
        coord = new Coord(x, y + 1f); // Element #2 - bottom of top line
        coords.add(coord);
        coord = new Coord(x, y + 2f); // Element #3 - Top of bottom line
        coords.add(coord);
        coord = new Coord(x - 0.5f, y + 1f); // Element #4 - top left corner of box
        coords.add(coord);
        coord = new Coord(x - 0.5f, y + 2f); // Element #5 - bottom left corner of box
        coords.add(coord);
        coord = new Coord(x + 0.5f, y + 1f); // Element #6 - top right corner of box
        coords.add(coord);
        coord = new Coord(x + 0.5f, y + 2f); // Element #7 - bottom right corner of box
        coords.add(coord);
        coord = new Coord(x , y + 1.5f); // Element #8 - anchor for N/C, N/O indicator
        coords.add(coord);
        coord = new Coord(x + 0.75f, y + 1.5f); // Element #9 - Right anchor for text/label
        coords.add(coord);
        coord = new Coord(x - 0.75f, y + 1.5f); // Element #10 - Left anchor for text/label
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
        this.setClickCoords(4, 7);

    } // END calcDrawingCoords

} // END public class Breaker70kV
