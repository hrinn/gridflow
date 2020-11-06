package com.company.oldcomponents;

import com.company.geometry.Point;
import com.company.main.Main;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

public class OldComponent {

//    public static final int UNIT = 20;

    protected Main mainSketch;

    private int id;  // unique ID #
    private String name; // SS70-603, B4, SW_53

    private String compType; // POWER, 70KVB, 12KVB, XFMR, SWITCH, CUTOUT, BUS, JUMPER, SUBSTATION
    private char orientation; // down (D), left (L), right (R), up (U)
    private int normalState; // 0 = closed
    private int currentState; // 0 = closed
    private boolean canOpen; // true = object can be clicked on to open or close; false = object is static
    private OldNode inOldNode;
    private OldNode outOldNode;
    private int length;
    private int width, height; // dimensions of click box area in grid units
    private Point clickMin, clickMax;  // Coordinates of clickable area in grid units
    private String label; // the text to be displayed on the screen
    private String textAnchor; // how the text is aligned with its anchor point (LC, CC, RC, LT, CT, RT, LB, CB, RB)
    private char labelOrientation; // H = horizontal, V = vertical
    private char labelPlacement; // L = left, R = right
    private String associatedWith; // Indicates assemblies of components in a substation

    private ArrayList<Point> ds;    // Grid xy positions of all the drawing lines, arcs, circles etc.
    private ArrayList<OldComponent> inComps;  // all components connected to the inNode
    private ArrayList<OldComponent> outComps; // all components connected to the outNode

    public OldComponent() {

    } // NULL constructor

    // Constructor #0 - When no existing Nodes are already defined
    public OldComponent(Main mainSketch, int id, String name, String type, char orientation,
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

        this.inOldNode = (new OldNode(new Point(xPos, yPos), true));
        mainSketch.getGrid().addNode(this.inOldNode);
        if(this.compType.equals("BUS") || this.compType.equals("UG_PASS")) {
            setOutNode(calcOutNode(this.inOldNode, this.orientation, length));
            this.length = length;
        } else if(this.compType.equals("12KVB") || this.compType.equals("TURB") || this.compType.equals("CLONE_12KVB")) {
            setOutNode(calcOutNode(this.inOldNode, this.orientation, 4));
        } else {
            setOutNode(calcOutNode(this.inOldNode, this.orientation, mainSketch.STD_OBJ_LENGTH));
        }
        mainSketch.getGrid().addNode(this.outOldNode);
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
    public OldComponent(Main mainSketch, int id, String name, String type, char orientation,
                        int normalState, OldComponent connectedTo, String inout, int length,
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
            this.inOldNode = connectedTo.getOutNode();
        } else {
            this.inOldNode = connectedTo.getInNode();
        }

        if(this.compType.equals("BUS") || this.compType.equals("UG_PASS")) {
            setOutNode(calcOutNode(this.inOldNode, this.orientation, length));
            this.length = length;
        } else if(this.compType.equals("12KVB") || this.compType.equals("CLONE_12KVB")) {
            setOutNode(calcOutNode(this.inOldNode, this.orientation, 4));
        } else if(this.compType.equals("ZIGZAG")) {
            setOutNode(calcOutNode(this.inOldNode, this.orientation, 2));
        } else if(this.compType.equals("LOAD")) {
            this.setLength(1);
            setOutNode(calcOutNode(this.inOldNode, this.orientation, this.getLength()));
        } else if(this.compType.equals("GRIDLINE")){
            this.setLength(length);
            setOutNode(calcOutNode(this.inOldNode, this.orientation, this.getLength()));
        } else {
            setOutNode(calcOutNode(this.inOldNode, this.orientation, mainSketch.STD_OBJ_LENGTH));
        }
        mainSketch.getGrid().addNode(this.outOldNode);
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
    public OldComponent(Main mainSketch, int id, String name, String type, char orientation,
                        int normalState, OldComponent connectedToIn, String inoutIn,
                        OldComponent connectedToOut, String inoutOut,
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
            this.inOldNode = connectedToIn.getOutNode();
            connectedToIn.addOutComp(this);
        } else {
            this.inOldNode = connectedToIn.getInNode();
            connectedToIn.addInComp(this);
        }
        if(inoutOut.equals("OUT")) {
            this.outOldNode = connectedToOut.getOutNode();
            connectedToOut.addOutComp(this);
        } else {
            this.outOldNode = connectedToOut.getInNode();
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

        if(this.getInNode().getCoord().getX() == this.getOutNode().getCoord().getX()) {
            length = (int) Math.abs(this.getInNode().getCoord().getY() - this.getOutNode().getCoord().getY());
        } else {
            length = (int) Math.abs(this.getInNode().getCoord().getX() - this.getOutNode().getCoord().getX());
        }

        return length;
    }

    private boolean calcCanOpen (String type) {

        // returns true unless type is XFMR or BUS or METER
        if(type.equals("XFMR") || type.equals("BUS") || type.equals("METER")) return false;
        return true;

    } // END calcCanOpen

    private OldNode calcOutNode(OldNode inOldNode, char orientation, int length) {

        switch (orientation) {
            case 'U' :
                return new OldNode(new Point(inOldNode.getCoord().getX(), inOldNode.getCoord().getY() - length));
            case 'L' :
                return new OldNode(new Point(inOldNode.getCoord().getX() - length, inOldNode.getCoord().getY()));
            case 'R' :
                return new OldNode(new Point(inOldNode.getCoord().getX() + length, inOldNode.getCoord().getY()));
            default :
                return new OldNode(new Point(inOldNode.getCoord().getX(), inOldNode.getCoord().getY() + length));
        } // END switch

    } // END calcOutNode

    public int calcPos(double point, float scale, float pan) {
        return (int) ((Main.UNIT * point * scale) + (pan * scale));
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

        int xMin = calcPos((getClickMin().getX()), scale, panX);
        int yMin = calcPos((getClickMin().getY()), scale, panY);
        int xMax = calcPos((getClickMax().getX()), scale, panX);
        int yMax = calcPos((getClickMax().getY()), scale, panY);

        return mouseX >= xMin && mouseX <= xMax && mouseY >= yMin && mouseY <= yMax;
    } // END isOnComponent

    public void addOutComp(OldComponent c) {
        this.outComps.add(c);
    }

    public void addInComp(OldComponent c) {
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

    public void setInNode(OldNode inOldNode) {
        this.inOldNode = inOldNode;
    }

    public void setOutNode(OldNode outOldNode) {

        this.outOldNode = outOldNode;
    }

    public OldNode getInNode() {
        return inOldNode;
    }

    public OldNode getOutNode() {
        return outOldNode;
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

        float tlX = this.getDs().get(topLeft).getX();
        float tlY = this.getDs().get(topLeft).getY();
        float brX = this.getDs().get(botRight).getX();
        float brY = this.getDs().get(botRight).getY();

        float minX = Math.min(tlX, brX);
        float minY = Math.min(tlY, brY);
        float maxX = Math.max(tlX, brX);
        float maxY = Math.max(tlY, brY);

        this.setClickMin(new Point(minX, minY));
        this.setClickMax(new Point(maxX, maxY));

    } // END setClickCoords

    public int getId() {
        return id;
    }

    public Point getClickMin() {
        return clickMin;
    }

    public void setClickMin(Point clickMin) {
        this.clickMin = clickMin;
    }

    public Point getClickMax() {
        return clickMax;
    }

    public void setClickMax(Point clickMax) {
        this.clickMax = clickMax;
    }

    public boolean isCanOpen() {
        return canOpen;
    }

    public ArrayList<Point> getDs() {
        return ds;
    }

    public int getLength() {
        return length;
    }

    public void setDs(ArrayList<Point> ds) {
        this.ds = ds;
    }

    // Returns an ArrayList of coordinates which have been rotated
    // by an angle around the very first coordinate in the ArrayList
    public ArrayList<Point> rotateDwgCoords(ArrayList<Point> oldPoints, float angle) {

//        System.out.println("rotateDwgCoords called by " + this.getName());
//        for(Coord coord : oldCoords) {
//            System.out.println(coord.toString());
//        }

        ArrayList<Point> newPoints = new ArrayList<>();
        // This step puts the inNode coordinates into the first position of the new ArrayList
        Point anchorPt = oldPoints.get(0);
        newPoints.add(anchorPt);
        // This step puts the outNode coordinates into the second position of the ArrayList
        Point outNode = oldPoints.get(1);
        newPoints.add(outNode);

        // for each coordinate in dS, EXCEPT element(0) and element(1)
        for(int i = 2; i < oldPoints.size(); i++) {
            newPoints.add(rotatePoint(anchorPt, oldPoints.get(i), angle));
        } // END for each additional element in oldCoords

//        System.out.println("rotateDwgCoords rotated " + this.getName());
//        for(Coord coord : newCoords) {
//            System.out.println(coord.toString());
//        }

        return newPoints;
    } // END rotateDwgCoords

    public Point rotatePoint(Point anchorPt, Point startPoint, float angle) {

        angle = (float) ((angle) * (Math.PI/180)); // Convert to radians

        float newX = (float) (Math.cos(angle) * (startPoint.getX() - anchorPt.getX()) -
                Math.sin(angle) * (startPoint.getY() - anchorPt.getY()) + anchorPt.getX());

        float newY = (float) (Math.sin(angle) * (startPoint.getX() - anchorPt.getX()) +
                Math.cos(angle) * (startPoint.getY() - anchorPt.getY()) + anchorPt.getY());

        return new Point(newX, newY);

    } // END rotatePoint

    public void drawLine(int from, int to) {

        float scale = mainSketch.getViewport().getScale();
        float panX = mainSketch.getViewport().getX();
        float panY = mainSketch.getViewport().getY();

        float fromX = calcPos(getDs().get(from).getX(), scale, panX);
        float fromY = calcPos(getDs().get(from).getY(), scale, panY);
        float toX   = calcPos(getDs().get(to).getX(), scale, panX);
        float toY   = calcPos(getDs().get(to).getY(), scale, panY);

        mainSketch.line(fromX, fromY, toX, toY);
    } // END drawLine()

    public void drawDashedLine(int from, int to) {

        float scale = mainSketch.getViewport().getScale();
        float panX = mainSketch.getViewport().getX();
        float panY = mainSketch.getViewport().getY();
        float unit = mainSketch.UNIT;

        ArrayList<Point> dashDs = new ArrayList<>();
        float length;
        float dashInterval = 0.25f;
        char dir;

        // Find start and end points in grid coordinates
        float fromX = Math.min(getDs().get(from).getX(), getDs().get(to).getX());
        float fromY = Math.min(getDs().get(from).getY(), getDs().get(to).getY());
        float toX   = Math.max(getDs().get(from).getX(), getDs().get(to).getX());
        float toY   = Math.max(getDs().get(from).getY(), getDs().get(to).getY());

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
            Point dashPoint = new Point(xStop, yStop);
            dashDs.add(dashPoint);

        }

        for(int stop = 0; stop < dashDs.size() - 1; stop++) {
            if(stop%2 == 0) {
                float dashFromX = calcPos(dashDs.get(stop).getX(), scale, panX);
                float dashFromY = calcPos(dashDs.get(stop).getY(), scale, panY);
                float dashToX = calcPos(dashDs.get(stop+1).getX(), scale, panX);
                float dashToY = calcPos(dashDs.get(stop+1).getY(), scale, panY);
                mainSketch.line(dashFromX, dashFromY, dashToX,dashToY);
            } // END if stop is an even number
        } // END for all stops in the Ds

    } // END drawDashedLine()

    public void drawBox(int vertex1, int vertex2, int vertex3, int vertex4, int size) {

        float scale = mainSketch.getViewport().getScale();
        float panX = mainSketch.getViewport().getX();
        float panY = mainSketch.getViewport().getY();
        float unit = mainSketch.UNIT * scale;

        // Find the x-coordinate of the top left vertex of the box, as the minimum of all four vertices
        float fromX = Math.min(getDs().get(vertex1).getX(), getDs().get(vertex2).getX());
        fromX = Math.min(fromX, getDs().get(vertex3).getX());
        fromX = Math.min(fromX, getDs().get(vertex4).getX());

        // Find the y-coordinate of the top left vertex of the box, as the minimum of all four vertices
        float fromY = Math.min(getDs().get(vertex1).getY(), getDs().get(vertex2).getY());
        fromY = Math.min(fromY, getDs().get(vertex3).getY());
        fromY = Math.min(fromY, getDs().get(vertex4).getY());

        // Translate into drawing coords
        fromX = calcPos(fromX, scale, panX);
        fromY = calcPos(fromY, scale, panY);

        mainSketch.rect(fromX, fromY, unit*size, unit*size);

    } // END drawBox()

    public void drawLock(int anchorPt) {

        float xPos = getDs().get(anchorPt).getX();
        float yPos = getDs().get(anchorPt).getY();
        float unit = mainSketch.UNIT * mainSketch.getViewport().getScale();

// TODO: draw lock



    }

    public void drawText(int anchorPt, String text, String textAnchor, char labelOrientation) {

        float scale = mainSketch.getViewport().getScale();
        float panX = mainSketch.getViewport().getX();
        float panY = mainSketch.getViewport().getY();
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
        float anchorX = getDs().get(anchorPt).getX();
        float anchorY = getDs().get(anchorPt).getY();

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

        float scale = mainSketch.getViewport().getScale();
        float panX = mainSketch.getViewport().getX();
        float panY = mainSketch.getViewport().getY();
        float unit = mainSketch.UNIT * scale;

        // Find the grid coordinates of the arc centerpoint
        float cpX = getDs().get(centerpoint).getX();
        float cpY = getDs().get(centerpoint).getY();

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

        float scale = mainSketch.getViewport().getScale();
        float panX = mainSketch.getViewport().getX();
        float panY = mainSketch.getViewport().getY();
        float unit = mainSketch.UNIT * scale;

        // Find the grid coordinates of the dot centerpoint
        float cpX = getDs().get(centerpoint).getX();
        float cpY = getDs().get(centerpoint).getY();

        // Translate into drawing coords
        cpX = calcPos(cpX, scale, panX);
        cpY = calcPos(cpY, scale, panY);

        // Render the dot
        mainSketch.ellipse(cpX, cpY, unit*size, unit*size);

    } // END drawTeardropDot()

    public void drawBezier(int start, int control, int end) {

        float scale = mainSketch.getViewport().getScale();
        float panX = mainSketch.getViewport().getX();
        float panY = mainSketch.getViewport().getY();
        float unit = mainSketch.UNIT * scale;

        // Find the grid coordinates of the dot start, control and end
        float startX = getDs().get(start).getX();
        float startY = getDs().get(start).getY();
        float controlX = getDs().get(control).getX();
        float controlY = getDs().get(control).getY();
        float endX =  getDs().get(end).getX();
        float endY = getDs().get(end).getY();

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

    public void drawBusUnderpass(Point c, char orientation, boolean energized) {

        float scale = mainSketch.getViewport().getScale();
        float panX = mainSketch.getViewport().getX();
        float panY = mainSketch.getViewport().getY();
        float unit = mainSketch.UNIT * scale;

        // Calculate the x,y coords of upper left corner of erasure box
        float x = calcPos(c.getX() - 0.25f, scale, panX);
        float y = calcPos(c.getY() - 0.25f, scale, panY);

        // Draw white box 0.5 units by 0.5 units
        mainSketch.strokeWeight(1);
        mainSketch.stroke(255); // white
        mainSketch.fill(255); // white
        mainSketch.rect(x, y, unit*0.5f, unit*0.5f);

        // Calculate x,y coordinates of lines needed
        float xIn, yIn, xOut, yOut;

        if(orientation == 'L' || orientation == 'R') {
            xIn = calcPos(c.getX() - 0.3f, scale, panX);
            yIn = calcPos(c.getY(), scale, panY);
            xOut = calcPos(c.getX() + 0.3f, scale, panX);
            yOut = calcPos(c.getY(), scale, panY);
        } else {
            xIn = calcPos(c.getX(), scale, panX);
            yIn = calcPos(c.getY() - 0.3f, scale, panY);
            xOut = calcPos(c.getX(), scale, panX);
            yOut = calcPos(c.getY() + 0.3f, scale, panY);
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
