package com.company.components;

import com.company.main.Main;
import processing.core.PConstants;

import java.util.ArrayList;

public class Meter extends Component {

    public Meter(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
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

        int x = calcPos((int) (getInNode().getCoord().getxPos()), scale, panX);
        int y = calcPos((int) (getInNode().getCoord().getyPos()), scale, panY);

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

        ArrayList<Coord> coords = new ArrayList<>();
        float x = this.getInNode().getCoord().getxPos();
        float y = this.getInNode().getCoord().getyPos();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        coords.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        coords.add(this.getOutNode().getCoord());

        // These next steps define specific points
        Coord coord;
        coord = new Coord(x, y + 2f); // Element #2 - bottom of top vertical line
        coords.add(coord);
        coord = new Coord(x, y + 2.5f); // Element #3 - CP of circle and "M" text point
        coords.add(coord);
        coord = new Coord(x + 1f, y + 2.5f); // Element #4 - anchor point text/label
        coords.add(coord);
        coord = new Coord(x - 0.5f, y + 2f); // Element #5 - top left mouse click area
        coords.add(coord);
        coord = new Coord(x + 0.5f, y + 3f); // Element #6 - bot right mouse click area
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

        // Update the location of the outNode
        this.getOutNode().setCoord(getDs().get(1));

        // Establish the click coordinates
        this.setClickCoords(5, 6);

    } // END calcDrawingCoords

} // END public class Meter
