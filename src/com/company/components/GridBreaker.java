package com.company.components;

import com.company.main.Main;

import java.util.ArrayList;

public class GridBreaker extends GridComponent {

    private String substationAttach;
    private int substationAttachNode;

    public GridBreaker(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
    } // END Constructor #1

    public GridBreaker(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedToIn, String inoutIn, Component connectedToOut, String inoutOut, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedToIn, inoutIn, connectedToOut, inoutOut, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
    } // END Constructor #2



    private void calcDrawingCoords() {
        ArrayList<Coord> coords = new ArrayList<>();

        // Set the inNode, by first looking for the substation it is attached to
        boolean foundSub = false;
        Coord inNodeCoord = null;

        for (Substation sub : mainSketch.substations) {
            if (this.substationAttach.equals(sub.getName())) {
                foundSub = true;
                inNodeCoord = sub.getSubstationNodes().get(this.substationAttachNode).getCoord();
                break;
            } // END if substation is found
        } // END for each substation

        // Set the inNode

        // Set the outNode coordinate
        int length = 2;
        Coord newOutNode = new Coord(inNodeCoord.getxPos(), inNodeCoord.getyPos() + length);
        switch (getOrientation()) {
            case 'U':
                newOutNode = new Coord(inNodeCoord.getxPos(), inNodeCoord.getyPos() - length);
                break;
            case 'L':
                newOutNode = new Coord(inNodeCoord.getxPos() - length, inNodeCoord.getyPos());
                break;
            case 'R':
                newOutNode = new Coord(inNodeCoord.getxPos() + length, inNodeCoord.getyPos());
                break;
            default:
                break;
        } // END switch(orientation)


        float x = this.getInNode().getCoord().getxPos();
        float y = this.getInNode().getCoord().getyPos();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        coords.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        coords.add(this.getOutNode().getCoord());

        // These next steps define specific points
        Coord coord;
        coord = new Coord(x + 0.75f, y + 1.0f);     // Element #2 - Right text label anchor point
        coords.add(coord);
        coord = new Coord(x - 0.75f, y + 1.0f);     // Element #3 - Left text label anchor
        coords.add(coord);
        coord = new Coord(x - 0.25f, y);                  // Element #4 - Top left click area
        coords.add(coord);
        coord = new Coord(x + 0.25f, y + 2.0f);     // Element #5 - Bottom right click area
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
        this.setClickCoords(4, 5);
    } // END calcDrawingCoods


} // END public class GridBreaker
