package com.company.components;

import com.company.main.Main;
import processing.core.PConstants;

import java.util.ArrayList;

public class Association {

    // Values provided by constructor
    protected Main mainSketch;
    private String assoc;           // The name of the association eg. "SUBA", "SVPP", "PGE" etc.
    private String label1;          // Text for primary label
    private String label1Size;      // Size of label 1 in pixels at 1.0f scale
    private String label2;          // Text for sub-label
    private String label2Size;      // Size of label 2 in pixels at 1.0f scale
    private String labelAlt;        // 1-letter or 2-letter value to render when scale is zoomed far out
    private int bufferLeft;         // Grid unit buffer
    private int bufferRight;        // Grid unit buffer
    private int bufferTop;          // Grid unit buffer
    private int bufferBottom;       // Grid unit buffer
    private int labelHorizPct;      // How far across the assocation to place the labels in percentage
    private int labelVertPct;       // How far down the association to place the labels in percentage
    private boolean hide;           // true = do NOT render the rectangle

    // Values calculated by private method
    private Coord topLeft;        // Coord object for top left extreme point of all objects in the association
    private int width;           // Grid unit distance between extreme nodes in association (horiz)
    private int height;          // Grid unit distance between extreme nodes in association (vert)
    private Coord boxTopLeft;            // Coord object for top left corner of box to be drawn
    private Coord overallTopLeft;  // Coord object for overall dimensions
    private ArrayList<Coord> allCoords;
    private int boxWidth;        // Grid unit dimension of box
    private int boxHeight;       // Grid unit dimension of box
    private Coord label1Anchor;  // Coord object for Label 1 and altLabel
    private Coord label2Anchor;  // Coord object for Label 2
    private int overallWidth;    // Grid unit dimension of box plus label (for zooming)
    private int overallHeight;   // Grid unit dimension of box plus label (for zooming)
    private float zoomPanX;      // Provides zooming value to shortcut key
    private float zoomPanY;      // Provides zooming value to shortcut key
    private float zoomScale;     // Provides zooming value to shortcut key

    public Association(Main mainSketch, String assoc, String label1, String label1Size, String label2, String label2Size, String labelAlt, int bufferLeft, int bufferRight, int bufferTop, int bufferBottom, int labelHorizPct, int labelVertPct, boolean hide) {
        this.mainSketch = mainSketch;
        this.assoc = assoc;
        this.label1 = label1;
        this.label1Size = label1Size;
        this.label2 = label2;
        this.label2Size = label2Size;
        this.labelAlt = labelAlt;
        this.bufferLeft = bufferLeft;
        this.bufferRight = bufferRight;
        this.bufferTop = bufferTop;
        this.bufferBottom = bufferBottom;
        this.labelHorizPct = labelHorizPct;
        this.labelVertPct = labelVertPct;
        this.hide = hide;
        this.allCoords = new ArrayList<>();
        calcDrawingCoords();
        calcZoomCoords();
    } // END constructor

    public String getAssoc() {
        return assoc;
    }

    private int calcPos(float coord, float scale, float pan) {
        return (int) ((Main.UNIT * coord * scale) + (pan * scale));
    }

    public void renderAssociation(float scale, float panX, float panY) {

        // TODO:  add italics, fonts, bold and shadow

        // Draw box around association
        mainSketch.stroke(224,224,224); // light grey
        mainSketch.strokeWeight(1);  // thin line
        mainSketch.noFill();
        if(!hide) {
            mainSketch.rect(calcPos(this.boxTopLeft.getxPos(), scale, panX), calcPos(this.boxTopLeft.getyPos(), scale, panY),
                    this.boxWidth * scale * Main.UNIT, this.boxHeight * scale * Main.UNIT);
        }

        if(mainSketch.getViewport().getScale() > 0.33f) {
            // Place Label 1
            float xLabel1Anchor = calcPos(label1Anchor.getxPos(), scale, panX); // Dwg coords
            float yLabel1Anchor = calcPos(label1Anchor.getyPos(), scale, panY); // Dwg coords
            float label2Offset = 0;

            switch (label1Size) {
                case "L":
                    mainSketch.textSize(24 * scale);
                    label2Offset = 24;
                    break;
                case "M":
                    mainSketch.textSize(18 * scale);
                    label2Offset = 18;
                    break;
                case "S":
                    mainSketch.textSize(10 * scale);
                    label2Offset = 10;
                    break;
                default:
                    mainSketch.textSize(50 * scale);
                    label2Offset = 50;
                    break;
            } // END switch Label1size

            // Draw Label 1 with shadow
            mainSketch.textAlign(PConstants.CENTER, PConstants.CENTER);
            mainSketch.fill(224, 224, 224); // light grey
            mainSketch.text(label1, xLabel1Anchor + 0.05f * scale * Main.UNIT, yLabel1Anchor + 0.05f * scale * Main.UNIT);
            mainSketch.fill(0, 0, 102); // navy blue
            mainSketch.text(label1, xLabel1Anchor, yLabel1Anchor);

            // Place Label 2
            float xLabel2Anchor = xLabel1Anchor;
            float yLabel2Anchor = yLabel1Anchor + label2Offset * scale;
            switch (label2Size) {
                case "L":
                    mainSketch.textSize(24 * scale);
                    break;
                case "M":
                    mainSketch.textSize(18 * scale);
                    break;
                case "S":
                    mainSketch.textSize(10 * scale);
                    break;
                default:
                    mainSketch.textSize(50 * scale);
                    break;
            } // END switch Label2size

            // Draw Label 2 with shadow
            mainSketch.textAlign(PConstants.CENTER, PConstants.TOP);
            mainSketch.fill(224, 224, 224); // light grey
            mainSketch.text(label2, xLabel2Anchor + 0.05f * scale * Main.UNIT, yLabel2Anchor + 0.05f * scale * Main.UNIT);
            mainSketch.fill(0, 0, 102); // navy blue
            mainSketch.text(label2, xLabel2Anchor, yLabel2Anchor);
        } else {
            // Draw alt label with shadow
            float xLabel1Anchor = calcPos(label1Anchor.getxPos(), scale, panX); // Dwg coords
            float yLabel1Anchor = calcPos(label1Anchor.getyPos(), scale, panY); // Dwg coords
            mainSketch.textAlign(PConstants.CENTER, PConstants.CENTER);
            mainSketch.textSize(80 * scale);
            mainSketch.fill(224, 224, 224); // light grey
            mainSketch.text(labelAlt, xLabel1Anchor + 0.05f * scale * Main.UNIT, yLabel1Anchor + 0.05f * scale * Main.UNIT);
            mainSketch.fill(0, 0, 102); // navy blue
            mainSketch.text(labelAlt, xLabel1Anchor, yLabel1Anchor);
        }

    } // END renderAssociation

    private void calcDrawingCoords() {

        float xMin =  1000000;
        float xMax = -1000000;
        float yMin =  1000000;
        float yMax = -1000000;

        // Determine min and max x,y grid coordinates for all components
        for(Component c : mainSketch.components) {
            if (c.getAssociatedWith().equals(this.getAssoc())) {
                xMin = Math.min(c.getInNode().getCoord().getxPos(), xMin);
                xMin = Math.min(c.getOutNode().getCoord().getxPos(), xMin);
                xMax = Math.max(c.getInNode().getCoord().getxPos(), xMax);
                xMax = Math.max(c.getOutNode().getCoord().getxPos(), xMax);

                yMin = Math.min(c.getInNode().getCoord().getyPos(), yMin);
                yMin = Math.min(c.getOutNode().getCoord().getyPos(), yMin);
                yMax = Math.max(c.getInNode().getCoord().getyPos(), yMax);
                yMax = Math.max(c.getOutNode().getCoord().getyPos(), yMax);
            } // END if association matches AssociatedWith
        } // END for each component

        // Set grid unit coordinates for top left corner of area encompassing all drawn objects
        float topLeftX = (int) xMin;
        float topLeftY = (int) yMin;
        this.topLeft = new Coord(topLeftX, topLeftY);
        this.allCoords.add(topLeft);

        // Set width and height (grid units) for area encompasssing all drawn objects
        this.width = (int) (xMax - xMin);
        this.height = (int) (yMax - yMin);

        // Set values for box to be drawn around association
        float boxX = (int) topLeft.getxPos() - bufferLeft;                        // Grid units
        float boxY = (int) topLeft.getyPos() - bufferTop;                         // Grid units
        this.boxTopLeft = new Coord(boxX, boxY);
        this.allCoords.add(boxTopLeft);
        this.boxWidth = this.width + bufferLeft + bufferRight;       // Grid units
        this.boxHeight = this.height + bufferTop + bufferBottom;     // Grid units
        this.allCoords.add(new Coord(this.boxTopLeft.getxPos() + this.boxWidth, this.boxTopLeft.getyPos()+ this.boxHeight));

        // Set coordinates for labels
        float xLabel = this.boxTopLeft.getxPos() + this.boxWidth * labelHorizPct/100;           // Grid units
        float yLabel = this.boxTopLeft.getyPos() + this.boxHeight * labelVertPct/100;           // Grid units
        this.label1Anchor = new Coord(xLabel, yLabel);
        this.allCoords.add(label1Anchor);
        yLabel = yLabel + 0.5f;
        this.label2Anchor = new Coord(xLabel, yLabel);
        this.allCoords.add(label2Anchor);

    } // END calcDrawingCoords

    private void calcZoomCoords() {

        // Determine min and max x,y grid coordinates for all components
        float xMin =  1000000;
        float xMax = -1000000;
        float yMin =  1000000;
        float yMax = -1000000;
        for(Coord coord : this.allCoords) {
                if(coord.getxPos() < xMin) xMin = coord.getxPos();
                if(coord.getxPos() > xMax) xMax = coord.getxPos();
                if(coord.getyPos() < yMin) yMin = coord.getyPos();
                if(coord.getyPos() > yMax) yMax = coord.getyPos();
        } // END for each coordinate in the ArrayList

        // Set Coord object for overall
        this.overallTopLeft = new Coord(xMin, yMin);
//        this.allCoords.add(overallTopLeft);

        // Calculate overall height and width in grid units (including labels) for purpose of zooming
        this.overallWidth = (int) (xMax - xMin);
        this.overallHeight = (int) (yMax - yMin);

        // Calculate midpoint of overall in grid units
        float midX = overallTopLeft.getxPos() + this.overallWidth / 2;
        float midY = overallTopLeft.getyPos() + this.overallHeight / 2;

        // Figure the controlling scale (vertical or horiz)
        // how many grid units fit on the screen horizontally and vertically at 1.0f scale?
        float screenWidthInGridUnits = mainSketch.SKETCH_WINDOW_WIDTH / mainSketch.UNIT;
        float screenHeightInGridUnits = mainSketch.SKETCH_WINDOW_HEIGHT / mainSketch.UNIT;
        // What scale is needed to fit all components of this association on the screen?
        float horizScaleNeeded = screenWidthInGridUnits / this.overallWidth;
        float vertScaleNeeded = (screenHeightInGridUnits / this.overallHeight);
        this.zoomScale = 0.9f * Math.min(horizScaleNeeded, vertScaleNeeded);

        // Set the panning values
        this.zoomPanX = 0 - (midX * mainSketch.UNIT - mainSketch.SKETCH_WINDOW_WIDTH/2);
        this.zoomPanY = 0 - (midY * mainSketch.UNIT - mainSketch.SKETCH_WINDOW_HEIGHT/2);

    } // END calcZoomCoords

    public float getZoomPanX() {
        return zoomPanX;
    }

    public float getZoomPanY() {
        return zoomPanY;
    }

    public float getZoomScale() {
        return zoomScale;
    }

} // END public class Association