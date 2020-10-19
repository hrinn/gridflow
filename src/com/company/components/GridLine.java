package com.company.components;

import com.company.main.Main;
import processing.core.PConstants;

import java.util.ArrayList;

public class GridLine extends GridComponent {

    public GridLine(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setNormalState(0);  // ensures "closed"
        setCurrentState(0); // ensures "closed"
        setCanOpen(false);  // ensures logic will not allow this component to operate
    } // END Constructor #1

    public GridLine(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedToIn, String inoutIn, Component connectedToOut, String inoutOut, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedToIn, inoutIn, connectedToOut, inoutOut, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setNormalState(0);  // ensures "closed"
        setCurrentState(0); // ensures "closed"
        setCanOpen(false);  // ensures logic will not allow this component to operate

    } // END Constructor #2

    public void renderEnergy(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_FAT * scale;

        // Determine how many power sources exist for this GridComponent
        int nbrOfSources = this.getInNodePowerSources().size();

        // Determine if the segment is energized
        boolean energyState = this.getInNode().isEnergized();

        // Iterate backward through the power sources and render the colors appropriately
        if(energyState) {
            for(int gpsCt = this.getInNodePowerSources().size() - 1; gpsCt > 0; gpsCt--) {
                mainSketch.strokeWeight((mainSketch.STROKE_THIN + 4 * gpsCt) * scale);
                float color = this.getInNodePowerSources().get(gpsCt).getColor();
                mainSketch.stroke(color);
                mainSketch.strokeCap(PConstants.SQUARE);
                drawLine(0, 1);
            } // END for each GridPowerSource
        } // END if energized

    } // END renderEnergy()

    public void renderLines(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_THIN * scale;
        float unit = mainSketch.UNIT * scale;

        // Set drawing parameters
        mainSketch.stroke(0);  // black
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw line regardless
        drawLine(0, 1);

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

    } // END calcDrawingCoords()

    private int[] getColor(String color) {

        int[] colorRGB = {0, 0, 0};
        switch (color) {
            case "RED" :
                colorRGB[0] = 255;   // red component
                colorRGB[1] = 0;     // green component
                colorRGB[2] = 0;     // blue component
                break;
            case "GREEN" :
                colorRGB[0] = 0;     // red component
                colorRGB[1] = 255;   // green component
                colorRGB[2] = 0;     // blue component
                break;
            case "LTGREEN" :
                colorRGB[0] = 0;     // red component
                colorRGB[1] = 255;   // green component
                colorRGB[2] = 128;     // blue component
                break;
            case "BLUE" :
                colorRGB[0] = 0;     // red component
                colorRGB[1] = 0;     // green component
                colorRGB[2] = 255;   // blue component
                break;
            case "ORANGE" :
                colorRGB[0] = 255;   // red component
                colorRGB[1] = 128;   // green component
                colorRGB[2] = 0;     // blue component
                break;
            case "PURPLE" :
                colorRGB[0] = 102;   // red component
                colorRGB[1] = 0;   // green component
                colorRGB[2] = 102;   // blue component
                break;
            case "YELLOW" :
                colorRGB[0] = 252;   // red component
                colorRGB[1] = 252;   // green component
                colorRGB[2] = 3;   // blue component
                break;
            default:
                colorRGB[0] = 0;     // red component
                colorRGB[1] = 0;     // green component
                colorRGB[2] = 0;     // blue component
                break;
        } // END switch(color)

        return colorRGB;

    } // END getColor

} // END public class GridLine
