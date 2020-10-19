package com.company.components;

import com.company.main.Main;
import processing.core.PConstants;

import java.util.ArrayList;

public class ConnectedLoad extends Component {

    private String[] labels;

    public ConnectedLoad(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith, String[] labels) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        this.labels = labels;
        calcDrawingCoords();
        System.out.println("built conn load with " + labels.length + " labels. ");
        for(String l : labels) System.out.println(l);

    } // END Constructor #1

    public void renderEnergy(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_FAT * scale;

        // Set drawing parameters
        mainSketch.stroke(252, 252, 3);  // yellow
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw energy line if present
        if (getInNode().isEnergized() && getOutNode().isEnergized()) {
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

        // Draw line or "off-line" warning depending on currentState
        if(getCurrentState() == 0) {
            mainSketch.stroke(0); // black
            drawLine(0, 1);
        } else {
            mainSketch.fill(255, 0, 0); // red
            drawText(4, "xfmr off-line", "CC", 'H');
        }

        // Place labels as needed
        if(getOutNode().isEnergized()) {
            mainSketch.fill(0);
            for (int i = 0; i < labels.length; i++) {
                drawText(i + 5, labels[i], this.getTextAnchor(), this.getLabelOrientation());
            }
        } else {
            mainSketch.fill(255, 0,0); // RED
            for (int i = 0; i < labels.length; i++) {
                drawText(i + 5, labels[i], this.getTextAnchor(), this.getLabelOrientation());
            }
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

        // These next steps put coordinates into the array for clicking (mouse-hovering)
        Coord coord = new Coord(x - 0.25f, y); // Element #2 - top left clickable area
        coords.add(coord);
        coord = new Coord(x + 0.25f, y + this.getLength()); // Element #3 - bottom right clickable area
        coords.add(coord);

        // Add text anchor for "xfmr offline" warning
        coord = new Coord(x, y + 0.5f); // Element #4 - text anchor for off-line warning
        coords.add(coord);

        // Add text anchors for every label
        for(int i = 0; i < labels.length; i++) {
            if (this.getOrientation() == 'D') {
                coord = new Coord(x, y + 1.25f * this.getLength() + 0.625f * i);
                coords.add(coord);
            } else {
                coord = new Coord(x -  0.625f * i, y + this.getLength() + 0.25f);
                coords.add(coord);
            }
        }

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

} // END public class ConnectedLoad
