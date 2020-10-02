package com.company;

import processing.core.PConstants;

import java.util.ArrayList;

public class Substation {

    protected Main mainSketch;

    private String name;
    private int id;
    private String label;
    private float xCoord;
    private float yCoord;
    private ArrayList<Node> substationNodes;
    private String associatedWith;

    public Substation(Main mainSketch, int id, String name, float xCoord, float yCoord, String label, String associatedWith) {
        this.mainSketch = mainSketch;
        this.name = name;
        this.id = id;
        this.label = label;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.associatedWith = associatedWith;
        calcSubstationNodes();
    } // END constructor (note: this is NOT a Component!)

    private int calcPos(float coord, float scale, float pan) {
        return (int) ((Main.UNIT * coord * scale) + (pan * scale));
    }

    public void renderSubstation() {

        float xPos = mainSketch.viewport.getX();
        float yPos = mainSketch.viewport.getY();
        float scale = mainSketch.viewport.getScale();
        float unit = mainSketch.UNIT;

        int drawX = calcPos(xCoord, scale, xPos);
        int drawY = calcPos(yCoord, scale, yPos);
        float drawWidth = 5 * unit * scale;
        float drawHeight = 3 * unit * scale;
        float drawRadius = 0.5f * unit * scale;
        int drawLabelX = calcPos(substationNodes.get(17).getCoord().getxPos(), scale, xPos);
        int drawLabelY = calcPos(substationNodes.get(17).getCoord().getyPos(), scale, yPos);
        float texSize = 25 * scale;

        // Draw box w/ rounded corners
        mainSketch.stroke(0);
        mainSketch.strokeWeight(mainSketch.STROKE_THIN * scale);
        mainSketch.noFill();
        mainSketch.rect(drawX, drawY, drawWidth, drawHeight, drawRadius);

        // Place text
        mainSketch.textSize(texSize);
        mainSketch.textAlign(PConstants.CENTER, PConstants.CENTER);
        mainSketch.fill(224, 224, 224); // light grey
        mainSketch.text(this.label, drawLabelX + 0.05f * scale * unit, drawLabelY + 0.05f * scale * unit);
        mainSketch.fill(0, 0, 102); // navy blue
        mainSketch.text(this.label, drawLabelX, drawLabelY);

    } // END renderSubstation()

    private void calcSubstationNodes() {

        substationNodes = new ArrayList<>();

        Coord coord = new Coord(xCoord, yCoord);                      // #0 - Substation anchor point
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 0.5f, yCoord);               // #1 - Breaker attach point #1
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 1.5f, yCoord);               // #2 - Breaker attach point #2
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 2.5f, yCoord);               // #3 - Breaker attach point #3
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 3.5f, yCoord);               // #4 - Breaker attach point #4
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 4.5f, yCoord);               // #5 - Breaker attach point #5
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 5.0f, yCoord + 0.5f);  // #6 - Breaker attach point #6
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 5.0f, yCoord + 1.5f);  // #7 - Breaker attach point #7
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 5.0f, yCoord + 2.5f);  // #8 - Breaker attach point #8
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 4.5f, yCoord + 3.0f);  // #9 - Breaker attach point #9
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 3.5f, yCoord + 3.0f);  // #10 - Breaker attach point #10
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 2.5f, yCoord + 3.0f);  // #11 - Breaker attach point #11
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 1.5f, yCoord + 3.0f);  // #12 - Breaker attach point #12
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 0.5f, yCoord + 3.0f);  // #13 - Breaker attach point #13
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 0.0f, yCoord + 2.5f);  // #14 - Breaker attach point #14
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 0.0f, yCoord + 1.5f);  // #15 - Breaker attach point #15
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 0.0f, yCoord + 0.5f);  // #16 - Breaker attach point #16
        substationNodes.add(new Node(coord, false));
        coord = new Coord(xCoord + 2.5f, yCoord + 1.5f);  // #17 - Substation label anchor point
        substationNodes.add(new Node(coord, false));

    } // END calcSubstationNodes

    public String getName() {
        return name;
    }

    public ArrayList<Node> getSubstationNodes() {
        return substationNodes;
    }
} // END public class Substation



