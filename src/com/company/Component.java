package com.company;

import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

public class Component {

//    public static final int UNIT = 20;

    protected Main mainSketch;

    private int id;  // unique ID #
    private String name; // SS70-603, B4, SW_53

    private String compType; // POWER, 70KVB, 12KVB, XFMR, SWITCH, CUTOUT, BUS, JUMPER, SUBSTATION
    private char orientation; // down (D), left (L), right (R), up (U)
    private int normalState; // 0 = closed
    private int currentState; // 0 = closed
    private boolean canOpen; // true = object can be clicked on to open or close; false = object is static
    private Node inNode;
    private Node outNode;
    private int length;
    private int width, height; // dimensions of click box area in grid units
    private Coord clickMin, clickMax;  // Coordinates of clickable area in grid units
    private String label; // the text to be displayed on the screen
    private String textAnchor; // how the text is aligned with its anchor point (LC, CC, RC, LT, CT, RT, LB, CB, RB)
    private char labelOrientation; // H = horizontal, V = vertical
    private char labelPlacement; // L = left, R = right
    private String associatedWith; // Indicates assemblies of components in a substation

    private ArrayList<Coord> ds;    // Grid xy positions of all the drawing lines, arcs, circles etc.
    private ArrayList<Component> inComps;  // all components connected to the inNode
    private ArrayList<Component> outComps; // all components connected to the outNode

    public Component() {

    } // NULL constructor

    // Constructor #0 - When no existing Nodes are already defined
    public Component(Main mainSketch, int id, String name, String type, char orientation,
                     int normalState, int xPos, int yPos, int length, String label, String textAnchor,
                     char labelOrientation, char labelPlacement, String associatedWith) {
        this.mainSketch = mainSketch;
        this.id = id;
        this.name = name;
        this.compType = type;
        this.orientation = orientation;
        this.normalState = normalState;
        this.currentState = normalState;
        this.canOpen = calcCanOpen(type);

        this.inNode = (new Node(new Coord(xPos, yPos), true));
        mainSketch.addNode(this.inNode);
        if(this.compType.equals("BUS") || this.compType.equals("UG_PASS")) {
            setOutNode(calcOutNode(this.inNode, this.orientation, length));
            this.length = length;
        } else if(this.compType.equals("12KVB") || this.compType.equals("TURB") || this.compType.equals("CLONE_12KVB")) {
            setOutNode(calcOutNode(this.inNode, this.orientation, 4));
        } else {
            setOutNode(calcOutNode(this.inNode, this.orientation, mainSketch.STD_OBJ_LENGTH));
        }
        mainSketch.addNode(this.outNode);
        this.inComps = new ArrayList<>();
        this.outComps = new ArrayList<>();
        this.ds = new ArrayList<>();
        this.label = label;
        this.textAnchor = textAnchor;
        this.labelOrientation = labelOrientation;
        this.labelPlacement = labelPlacement;
        this.associatedWith = associatedWith;

        System.out.println("Constructor #0 built " + name + " at " + getInNode().getCoord().toString());

    } // END Constructor #0

    // Constructor #1 - When one Node already exists
    public Component(Main mainSketch, int id, String name, String type, char orientation,
                     int normalState, Component connectedTo, String inout, int length,
                     String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        this.mainSketch = mainSketch;
        this.id = id;
        this.name = name;
        this.compType = type;
        this.orientation = orientation;
        this.normalState = normalState;
        this.currentState = normalState;
        this.canOpen = calcCanOpen(type);
        if(inout.equals("OUT")) {
            this.inNode = connectedTo.getOutNode();
        } else {
            this.inNode = connectedTo.getInNode();
        }

        if(this.compType.equals("BUS") || this.compType.equals("UG_PASS")) {
            setOutNode(calcOutNode(this.inNode, this.orientation, length));
            this.length = length;
        } else if(this.compType.equals("12KVB") || this.compType.equals("CLONE_12KVB")) {
            setOutNode(calcOutNode(this.inNode, this.orientation, 4));
        } else if(this.compType.equals("ZIGZAG")) {
            setOutNode(calcOutNode(this.inNode, this.orientation, 2));
        } else if(this.compType.equals("LOAD")) {
            this.setLength(1);
            setOutNode(calcOutNode(this.inNode, this.orientation, this.getLength()));
        } else if(this.compType.equals("GRIDLINE")){
            this.setLength(length);
            setOutNode(calcOutNode(this.inNode, this.orientation, this.getLength()));
        } else {
            setOutNode(calcOutNode(this.inNode, this.orientation, mainSketch.STD_OBJ_LENGTH));
        }
        mainSketch.addNode(this.outNode);
        this.inComps = new ArrayList<>();
        this.outComps = new ArrayList<>();
        if(inout.equals("OUT")) {
            connectedTo.addOutComp(this);
        } else {
            connectedTo.addInComp(this);
        }
        this.ds = new ArrayList<>();
        this.label = label;
        this.textAnchor = textAnchor;
        this.labelOrientation = labelOrientation;
        this.labelPlacement = labelPlacement;
        this.associatedWith = associatedWith;

        System.out.println("Constructor #1 built " + name + " at " + getInNode().getCoord().toString());


    }  // END Constructor #1

    // Constructor #2 - When both Nodes already exist
    public Component(Main mainSketch, int id, String name, String type, char orientation,
                     int normalState, Component connectedToIn, String inoutIn,
                     Component connectedToOut, String inoutOut,
                     String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        this.mainSketch = mainSketch;
        this.id = id;
        this.name = name;
        this.compType = type;
        this.orientation = orientation;
        this.normalState = normalState;
        this.currentState = normalState;
        this.canOpen = calcCanOpen(type);

        if(inoutIn.equals("OUT")) {
            this.inNode = connectedToIn.getOutNode();
            connectedToIn.addOutComp(this);
        } else {
            this.inNode = connectedToIn.getInNode();
            connectedToIn.addInComp(this);
        }
        if(inoutOut.equals("OUT")) {
            this.outNode = connectedToOut.getOutNode();
            connectedToOut.addOutComp(this);
        } else {
            this.outNode = connectedToOut.getInNode();
            connectedToOut.addInComp(this);
        }
        if(this.compType.equals("BUS")) this.length = calcBusLength();
        this.inComps = new ArrayList<>();
        this.outComps = new ArrayList<>();
        this.ds = new ArrayList<>();
        this.label = label;
        this.textAnchor = textAnchor;
        this.labelOrientation = labelOrientation;
        this.labelPlacement = labelPlacement;
        this.associatedWith = associatedWith;

        System.out.println("Constructor #2 built " + name + " at " + getInNode().getCoord().toString());


    }  // END Constructor #2

    private int calcBusLength() {
        int length;

        if(this.getInNode().getCoord().getxPos() == this.getOutNode().getCoord().getxPos()) {
            length = (int) Math.abs(this.getInNode().getCoord().getyPos() - this.getOutNode().getCoord().getyPos());
        } else {
            length = (int) Math.abs(this.getInNode().getCoord().getxPos() - this.getOutNode().getCoord().getxPos());
        }

        return length;
    }

    private boolean calcCanOpen (String type) {

        // returns true unless type is XFMR or BUS or METER
        if(type.equals("XFMR") || type.equals("BUS") || type.equals("METER")) return false;
        return true;

    } // END calcCanOpen

    private Node calcOutNode(Node inNode, char orientation, int length) {

        switch (orientation) {
            case 'U' :
                return new Node(new Coord(inNode.getCoord().getxPos(), inNode.getCoord().getyPos() - length));
            case 'L' :
                return new Node(new Coord(inNode.getCoord().getxPos() - length, inNode.getCoord().getyPos()));
            case 'R' :
                return new Node(new Coord(inNode.getCoord().getxPos() + length, inNode.getCoord().getyPos()));
            default :
                return new Node(new Coord(inNode.getCoord().getxPos(), inNode.getCoord().getyPos() + length));
        } // END switch

    } // END calcOutNode

    public int calcPos(float coord, float scale, float pan) {
        return (int) ((Main.UNIT * coord * scale) + (pan * scale));
    }

    public void render(float scale, float panX, float panY) {

    } // END render()

    public void renderEnergy(float scale, float panX, float panY) {

    } // END renderEnergy()

    public void renderLines(float scale, float panX, float panY) {

    } // END renderLines()

    public void renderHint(int mouseX, int mouseY) {

        mainSketch.fill(70, 70, 70); // grey  70, 70, 70
        mainSketch.textAlign(PConstants.RIGHT, PConstants.CENTER);
        mainSketch.text(this.getName(), mouseX-10, mouseY-10);

    } // END renderHint

    // Returns true if given mouse (x,y) coord is within component's clickable boundaries, false otherwise
    public boolean isOnComponent(float scale, float panX, float panY, int mouseX, int mouseY) {

        int xMin = calcPos((getClickMin().getxPos()), scale, panX);
        int yMin = calcPos((getClickMin().getyPos()), scale, panY);
        int xMax = calcPos((getClickMax().getxPos()), scale, panX);
        int yMax = calcPos((getClickMax().getyPos()), scale, panY);

        return mouseX >= xMin && mouseX <= xMax && mouseY >= yMin && mouseY <= yMax;
    } // END isOnComponent

    public void addOutComp(Component c) {
        this.outComps.add(c);
    }

    public void addInComp(Component c) {
        this.inComps.add(c);
    }

    public String getName() {
        return name;
    }

    public void setNormalState(int normalState) {
        this.normalState = normalState;
    }

    public int getNormalState() {
        return normalState;
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }

    public char getOrientation() {
        return orientation;
    }

    public void setCanOpen(boolean canOpen) {
        this.canOpen = canOpen;
    }


    public void setLength(int length) {
        this.length = length;
    }

    public void setInNode(Node inNode) {
        this.inNode = inNode;
    }

    public void setOutNode(Node outNode) {

        this.outNode = outNode;
    }

    public Node getInNode() {
        return inNode;
    }

    public Node getOutNode() {
        return outNode;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTextAnchor() {
        return textAnchor;
    }

    public void setTextAnchor(String textAnchor) {
        this.textAnchor = textAnchor;
    }

    public char getLabelOrientation() {
        return labelOrientation;
    }

    public void setLabelOrientation(char labelOrientation) {
        this.labelOrientation = labelOrientation;
    }

    public char getLabelPlacement() {
        return labelPlacement;
    }

    public void setLabelPlacement(char labelPlacement) {
        this.labelPlacement = labelPlacement;
    }

    public String getAssociatedWith() {
        return associatedWith;
    }

    public void setAssociatedWith(String associatedWith) {
        this.associatedWith = associatedWith;
    }

    public void setClickCoords(int topLeft, int botRight) {

        float tlX = this.getDs().get(topLeft).getxPos();
        float tlY = this.getDs().get(topLeft).getyPos();
        float brX = this.getDs().get(botRight).getxPos();
        float brY = this.getDs().get(botRight).getyPos();

        float minX = Math.min(tlX, brX);
        float minY = Math.min(tlY, brY);
        float maxX = Math.max(tlX, brX);
        float maxY = Math.max(tlY, brY);

        this.setClickMin(new Coord(minX, minY));
        this.setClickMax(new Coord(maxX, maxY));

    } // END setClickCoords

    public int getId() {
        return id;
    }

    public Coord getClickMin() {
        return clickMin;
    }

    public void setClickMin(Coord clickMin) {
        this.clickMin = clickMin;
    }

    public Coord getClickMax() {
        return clickMax;
    }

    public void setClickMax(Coord clickMax) {
        this.clickMax = clickMax;
    }

    public boolean isCanOpen() {
        return canOpen;
    }

    public ArrayList<Coord> getDs() {
        return ds;
    }

    public int getLength() {
        return length;
    }

    public void setDs(ArrayList<Coord> ds) {
        this.ds = ds;
    }

    // Returns an ArrayList of coordinates which have been rotated
    // by an angle around the very first coordinate in the ArrayList
    public ArrayList<Coord> rotateDwgCoords(ArrayList<Coord> oldCoords, float angle) {

//        System.out.println("rotateDwgCoords called by " + this.getName());
//        for(Coord coord : oldCoords) {
//            System.out.println(coord.toString());
//        }

        ArrayList<Coord> newCoords = new ArrayList<>();
        // This step puts the inNode coordinates into the first position of the new ArrayList
        Coord anchorPt = oldCoords.get(0);
        newCoords.add(anchorPt);
        // This step puts the outNode coordinates into the second position of the ArrayList
        Coord outNode = oldCoords.get(1);
        newCoords.add(outNode);

        // for each coordinate in dS, EXCEPT element(0) and element(1)
        for(int i = 2; i < oldCoords.size(); i++) {
            newCoords.add(rotatePoint(anchorPt, oldCoords.get(i), angle));
        } // END for each additional element in oldCoords

//        System.out.println("rotateDwgCoords rotated " + this.getName());
//        for(Coord coord : newCoords) {
//            System.out.println(coord.toString());
//        }

        return newCoords;
    } // END rotateDwgCoords

    public Coord rotatePoint(Coord anchorPt, Coord startPoint, float angle) {

        angle = (float) ((angle) * (Math.PI/180)); // Convert to radians

        float newX = (float) (Math.cos(angle) * (startPoint.getxPos() - anchorPt.getxPos()) -
                Math.sin(angle) * (startPoint.getyPos() - anchorPt.getyPos()) + anchorPt.getxPos());

        float newY = (float) (Math.sin(angle) * (startPoint.getxPos() - anchorPt.getxPos()) +
                Math.cos(angle) * (startPoint.getyPos() - anchorPt.getyPos()) + anchorPt.getyPos());

        return new Coord(newX, newY);

    } // END rotatePoint

    public void drawLine(int from, int to) {

        float scale = mainSketch.viewport.scale;
        float panX = mainSketch.viewport.getX();
        float panY = mainSketch.viewport.getY();

        float fromX = calcPos(getDs().get(from).getxPos(), scale, panX);
        float fromY = calcPos(getDs().get(from).getyPos(), scale, panY);
        float toX   = calcPos(getDs().get(to).getxPos(), scale, panX);
        float toY   = calcPos(getDs().get(to).getyPos(), scale, panY);

        mainSketch.line(fromX, fromY, toX, toY);
    } // END drawLine()

    public void drawDashedLine(int from, int to) {

        float scale = mainSketch.viewport.scale;
        float panX = mainSketch.viewport.getX();
        float panY = mainSketch.viewport.getY();
        float unit = mainSketch.UNIT;

        ArrayList<Coord> dashDs = new ArrayList<>();
        float length;
        float dashInterval = 0.25f;
        char dir;

        // Find start and end points in grid coordinates
        float fromX = Math.min(getDs().get(from).getxPos(), getDs().get(to).getxPos());
        float fromY = Math.min(getDs().get(from).getyPos(), getDs().get(to).getyPos());
        float toX   = Math.max(getDs().get(from).getxPos(), getDs().get(to).getxPos());
        float toY   = Math.max(getDs().get(from).getyPos(), getDs().get(to).getyPos());

        if(fromX == toX) {
            length = Math.abs(toY - fromY);
            dir = 'V';
        } else {
            length = Math.abs(toX - fromX);
            dir = 'H';
        }

        // Fill ArrayList with grid coords for each dash-mark
        for(float stop = 0; stop < length; stop += dashInterval) {
            float xStop, yStop;
            if (dir == 'H') {
                xStop = fromX + stop;
                yStop = toY;
            } else {
                xStop = toX;
                yStop = fromY + stop;
            }
            Coord dashCoord = new Coord(xStop, yStop);
            dashDs.add(dashCoord);

        }

        for(int stop = 0; stop < dashDs.size() - 1; stop++) {
            if(stop%2 == 0) {
                float dashFromX = calcPos(dashDs.get(stop).getxPos(), scale, panX);
                float dashFromY = calcPos(dashDs.get(stop).getyPos(), scale, panY);
                float dashToX = calcPos(dashDs.get(stop+1).getxPos(), scale, panX);
                float dashToY = calcPos(dashDs.get(stop+1).getyPos(), scale, panY);
                mainSketch.line(dashFromX, dashFromY, dashToX,dashToY);
            } // END if stop is an even number
        } // END for all stops in the Ds

    } // END drawDashedLine()

    public void drawBox(int vertex1, int vertex2, int vertex3, int vertex4, int size) {

        float scale = mainSketch.viewport.scale;
        float panX = mainSketch.viewport.getX();
        float panY = mainSketch.viewport.getY();
        float unit = mainSketch.UNIT * scale;

        // Find the x-coordinate of the top left vertex of the box, as the minimum of all four vertices
        float fromX = Math.min(getDs().get(vertex1).getxPos(), getDs().get(vertex2).getxPos());
        fromX = Math.min(fromX, getDs().get(vertex3).getxPos());
        fromX = Math.min(fromX, getDs().get(vertex4).getxPos());

        // Find the y-coordinate of the top left vertex of the box, as the minimum of all four vertices
        float fromY = Math.min(getDs().get(vertex1).getyPos(), getDs().get(vertex2).getyPos());
        fromY = Math.min(fromY, getDs().get(vertex3).getyPos());
        fromY = Math.min(fromY, getDs().get(vertex4).getyPos());

        // Translate into drawing coords
        fromX = calcPos(fromX, scale, panX);
        fromY = calcPos(fromY, scale, panY);

        mainSketch.rect(fromX, fromY, unit*size, unit*size);

    } // END drawBox()

    public void drawLock(int anchorPt) {

        float xPos = getDs().get(anchorPt).getxPos();
        float yPos = getDs().get(anchorPt).getyPos();
        float unit = mainSketch.UNIT * mainSketch.viewport.getScale();

// TODO: draw lock



    }

    public void drawText(int anchorPt, String text, String textAnchor, char labelOrientation) {

        float scale = mainSketch.viewport.scale;
        float panX = mainSketch.viewport.getX();
        float panY = mainSketch.viewport.getY();
        float unit = mainSketch.UNIT * scale;

        // Set the text size
        if(this.compType.equals("TURB")) {
            mainSketch.textSize(unit * 1.5f);
        } else if(text.equals("xfmr off-line")) {
            mainSketch.textSize(unit * 0.3f);
        }
         else {
             mainSketch.textSize(unit * 0.5f);
        }
        if(text.equals("LOCK")) mainSketch.textSize(unit * 0.3f);
        if(text.equals("N/O") || text.equals("N/C")) mainSketch.textSize(unit * 0.4f);
        if(text.equals("N/O")) mainSketch.fill(255);

        // Find the grid coordinates of the text anchor point
        float anchorX = getDs().get(anchorPt).getxPos();
        float anchorY = getDs().get(anchorPt).getyPos();

        // Translate into drawing coords
        anchorX = calcPos(anchorX, scale, panX);
        anchorY = calcPos(anchorY, scale, panY);

        // Determine text anchor placement and set PConstants
        switch(textAnchor) {
            case "LT" :
                mainSketch.textAlign(PConstants.LEFT, PConstants.TOP);
                break;
            case "CT" :
                mainSketch.textAlign(PConstants.CENTER, PConstants.TOP);
                break;
            case "RT" :
                mainSketch.textAlign(PConstants.RIGHT, PConstants.TOP);
                break;
            case "LC" :
                mainSketch.textAlign(PConstants.LEFT, PConstants.CENTER);
                break;
            case "RC" :
                mainSketch.textAlign(PConstants.RIGHT, PConstants.CENTER);
                break;
            case "LB" :
                mainSketch.textAlign(PConstants.LEFT, PConstants.BOTTOM);
                break;
            case "CB" :
                mainSketch.textAlign(PConstants.CENTER, PConstants.BOTTOM);
                break;
            case "RB" :
                mainSketch.textAlign(PConstants.RIGHT, PConstants.BOTTOM);
                break;
            default :
                mainSketch.textAlign(PConstants.CENTER, PConstants.CENTER);
                break;
        } // END switch(textAnchor)

        // Determine if the text needs to be rotated vertically
        if(labelOrientation == 'V') {
            mainSketch.pushMatrix();
            mainSketch.translate(anchorX, anchorY);
            mainSketch.rotate(PApplet.radians(270));
//            mainSketch.fill(0); // Set text to black
            mainSketch.text(text, 0, 0);
            mainSketch.popMatrix();
        } else {
//            mainSketch.fill(0); // Set text to black
            mainSketch.text(text, anchorX, anchorY);
        }

    } // END drawText()

    public void drawArc(int centerpoint, float startArcAngle, float size) {

        float scale = mainSketch.viewport.scale;
        float panX = mainSketch.viewport.getX();
        float panY = mainSketch.viewport.getY();
        float unit = mainSketch.UNIT * scale;

        // Find the grid coordinates of the arc centerpoint
        float cpX = getDs().get(centerpoint).getxPos();
        float cpY = getDs().get(centerpoint).getyPos();

        // Translate into drawing coords
        cpX = calcPos(cpX, scale, panX);
        cpY = calcPos(cpY, scale, panY);

        // Calc the start and end angles and translate to radians
        startArcAngle = startArcAngle * PConstants.PI;
        float endArcAngle = startArcAngle + PConstants.PI;

        // Render the arc
        mainSketch.noFill();
        mainSketch.arc(cpX, cpY, unit*size, unit*size, startArcAngle, endArcAngle);

    } // END drawTeardropArc()

    public void drawTeardropDot(int centerpoint, float size) {

        float scale = mainSketch.viewport.scale;
        float panX = mainSketch.viewport.getX();
        float panY = mainSketch.viewport.getY();
        float unit = mainSketch.UNIT * scale;

        // Find the grid coordinates of the dot centerpoint
        float cpX = getDs().get(centerpoint).getxPos();
        float cpY = getDs().get(centerpoint).getyPos();

        // Translate into drawing coords
        cpX = calcPos(cpX, scale, panX);
        cpY = calcPos(cpY, scale, panY);

        // Render the dot
        mainSketch.ellipse(cpX, cpY, unit*size, unit*size);

    } // END drawTeardropDot()

    public void drawBezier(int start, int control, int end) {

        float scale = mainSketch.viewport.scale;
        float panX = mainSketch.viewport.getX();
        float panY = mainSketch.viewport.getY();
        float unit = mainSketch.UNIT * scale;

        // Find the grid coordinates of the dot start, control and end
        float startX = getDs().get(start).getxPos();
        float startY = getDs().get(start).getyPos();
        float controlX = getDs().get(control).getxPos();
        float controlY = getDs().get(control).getyPos();
        float endX =  getDs().get(end).getxPos();
        float endY = getDs().get(end).getyPos();

        // Translate into drawing coords
        startX = calcPos(startX, scale, panX);
        startY = calcPos(startY, scale, panY);
        controlX = calcPos(controlX, scale, panX);
        controlY = calcPos(controlY, scale, panY);
        endX = calcPos(endX, scale, panX);
        endY = calcPos(endY, scale, panY);

        // Render the curve
        mainSketch.bezier(startX, startY, controlX, controlY, controlX, controlY, endX, endY);

    } // END drawBezier()

    public void drawBusUnderpass(Coord c, char orientation, boolean energized) {

        float scale = mainSketch.viewport.scale;
        float panX = mainSketch.viewport.getX();
        float panY = mainSketch.viewport.getY();
        float unit = mainSketch.UNIT * scale;

        // Calculate the x,y coords of upper left corner of erasure box
        float x = calcPos(c.getxPos() - 0.25f, scale, panX);
        float y = calcPos(c.getyPos() - 0.25f, scale, panY);

        // Draw white box 0.5 units by 0.5 units
        mainSketch.strokeWeight(1);
        mainSketch.stroke(255); // white
        mainSketch.fill(255); // white
        mainSketch.rect(x, y, unit*0.5f, unit*0.5f);

        // Calculate x,y coordinates of lines needed
        float xIn, yIn, xOut, yOut;

        if(orientation == 'L' || orientation == 'R') {
            xIn = calcPos(c.getxPos() - 0.3f, scale, panX);
            yIn = calcPos(c.getyPos(), scale, panY);
            xOut = calcPos(c.getxPos() + 0.3f, scale, panX);
            yOut = calcPos(c.getyPos(), scale, panY);
        } else {
            xIn = calcPos(c.getxPos(), scale, panX);
            yIn = calcPos(c.getyPos() - 0.3f, scale, panY);
            xOut = calcPos(c.getxPos(), scale, panX);
            yOut = calcPos(c.getyPos() + 0.3f, scale, panY);
        }

        // If energized, draw yellow line
        if(energized) {
            mainSketch.strokeWeight(mainSketch.STROKE_FAT * scale);
            mainSketch.stroke(252,252,3); // yellow
            mainSketch.line(xIn, yIn, xOut, yOut);
        }

        // Draw black line regardless
        mainSketch.strokeWeight(mainSketch.STROKE_THIN * scale);
        mainSketch.stroke(0);
        mainSketch.line(xIn, yIn, xOut, yOut);

    } // END drawBusUnderpass

} // END public class Component
