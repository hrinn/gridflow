package com.company.main;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import com.company.oldcomponents.*;
import processing.core.PApplet;
import processing.event.*;
import controlP5.*;

public class Main extends PApplet {

    // TODO:  fix rendering of closed switch for Canyon Fire damaged area (not depicting slash or X)

    // TODO:  create different fonts for some of the labels
    //        PFont assocLabel = mainSketch.createFont("LucidaBrightItalic.ttf", 48);
    //        mainSketch.textFont(assocLabel);

    // TODO:  Fix the way connected loads are built to divorce them from the 1-Node constructor
    // TODO:  Design upfeed modifier for jumper
    // TODO:  Fix auto-zoom function

    private Viewport viewport = new Viewport();

    public Viewport getViewport() {
        return viewport;
    }

    Click click = new Click();
    UserInterface ui = new UserInterface();
    ControlP5 cp5;
    private Grid grid = new Grid();
    public static ArrayList<String> switchingOrder;


    // Global Constants
    public static final int UNIT = 20;
    public static final int STD_OBJ_LENGTH = 3;
    public static final int STROKE_TRIPLE = 18; // triple stroke weight used for parallel power sources
    public static final int STROKE_FAT = 6;     // fat stroke weight used for energy highlights
    public static final int STROKE_THIN = 2;    // thin stroke weight used for black lines
    public static final int SKETCH_WINDOW_WIDTH = 1300; //
    public static final int SKETCH_WINDOW_HEIGHT = 700; //
    public static int WIDTH = 40, HEIGHT = 20; // used only in the UserInterface class

    // Global Constants for colors
    public static final float RED =      0xFF0000;        // {255, 0, 0};
    public static final float GREEN =    0x00FF00;        // {0, 255, 0};
    public static final float BLUE =     0x0000FF;        // {0, 0, 252};
    public static final float YELLOW =   0xFCFC03;        // {252,252,3}
    public static final float LTGREEN =  0x00FF7D;        // {0, 255, 125};
    public static final float PURPLE =   0x660066;        // {102, 0, 102};
    public static final float ORANGE =   0xFF8000;        // {255, 128, 0};
    public static final float BLACK =    0x000000;        // {0, 0, 0};
    public static final float WHITE =    0xFFFFFF;        // {255, 255, 255};
    public static final float DARKGRAY = 0x464646;        // {70, 70, 70};
    public static final float LTGRAY =   0xE0E0E0;        // {224, 224, 224};
    public static final float NAVY =     0x000080;        // {0, 0, 128};



    public boolean canZoom = true;

    public void setup() {

        background(255);  // 0 = black
        cp5 = new ControlP5(this);

        System.out.println("Begin Switching Order");


    } // END setup()

    public void settings() {

        size(SKETCH_WINDOW_WIDTH, SKETCH_WINDOW_HEIGHT);
//        fullScreen();

        ui.setSize(this);
    } // END settings

    public void draw() {

        // Returns the screen to white 60x per second
        background(0xFFFFFF);

        // Iterate through all Associations and render the boxes and labels
        for(Association a : grid.associations) {
            a.renderAssociation(viewport.scale, viewport.getX(), viewport.getY());
        } // END for each Association

        // Draw yellow highlights first
        for (OldComponent c : grid.components) {
            c.renderEnergy(viewport.scale, viewport.getX(), viewport.getY());
        }

        // Then draw black lines and labels
        for (OldComponent c : grid.components) {
            c.renderLines(viewport.scale, viewport.getX(), viewport.getY());
        }

        // Update all tandem breakers
        for (Breaker12kVTandem bt : grid.tandems) {
            bt.updateTandemStatus();
        } // END for each tandem breaker

        // Draw grey hints for Components with no static labels drawn
        for (OldComponent c : grid.components) {

            if (c instanceof OldBus || c instanceof Cutout || c instanceof OldJumper || c instanceof OldMeter) {
                if (c.isOnComponent(viewport.scale, viewport.getX(), viewport.getY(), mouseX, mouseY)) {
                    c.renderHint(mouseX, mouseY);
                }
            } // END if component is one that we show hints for

        } // END draw hints for each component

        ui.draw();

    } // END draw()

    public static void main(String[] args) {

        String[] processingArgs = {"SwitchBoss"};
        Main mainSketch = new Main();
//        cp5 = new ControlP5(mainSketch);

        switchingOrder = new ArrayList<>();

        try {
            readComponentFile(mainSketch, "lib/MtJediGalaxyGrid.TXT"); // Was "ModelAll_TEST_THIS_FILE.TXT"
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Establish initial energy state of all objects
        setInitialEnergyState(mainSketch);



        // Run PApplet to create sketch
        PApplet.runSketch(processingArgs, mainSketch);
        mainSketch.surface.setTitle("Switch Boss");

    } // END psvm

    public static void readComponentFile(Main mainSketch, String fileName) throws IOException {

        File readThisFile = new File(fileName);
        mainSketch.grid.components.clear(); // empties the arrayList of components
        Scanner scanner;
        int count = 0;
        try {
            scanner = new Scanner(readThisFile);
            String str = scanner.nextLine(); // This throws away the header row in the file
            while (scanner.hasNextLine()) {
                str = scanner.nextLine();
                count++;
                String[] values = str.split(",");
                buildComponent(mainSketch, values);
            } // END while scanner has next line
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    } // END readComponentFile()

    public static void buildComponent(Main mainSketch, String[] values) {

        switch (values[0].toUpperCase()) {

            case "0-NODE":
                buildZeroNode(mainSketch, values);
                break;
            case "1-NODE":
                buildOneNode(mainSketch, values);
                break;
            case "2-NODE":
                buildTwoNode(mainSketch, values);
                break;
            case "TANDEM":
                buildTandemBreakers(mainSketch, values);
                break;
            case "CLONE_12KVB":
                buildClone12kVB(mainSketch, values);
                break;
            case "ASSOC" :
                buildAssociation(mainSketch, values);
                break;
            case "ASSIGN" :
                buildAssignment(mainSketch, values);
                break;
            case "SUBSTATION" :
                buildSubstation(mainSketch, values);
                break;
            case "GRID_POWER_SOURCE" :
                buildGridPowerSource(mainSketch, values);
                break;

            // This allows for text to be typed in as markers
            default:
                break;
        } // END switch (first value in String[] array) indicates build type

    } // END buildComponent

    public static void buildGridPowerSource(Main mainSketch, String[] values) {

        String id = values[1];
        String breakerName = values[2];
        String color = values[3];
        float voltage = Float.parseFloat(values[4]);

        // Search for the breaker
        CloneBreaker12kV breaker = null;
        boolean breakerFound = false;
        for(CloneBreaker12kV c : mainSketch.grid.clones) {
            if(c.getName().equals(breakerName)) {
                breakerFound = true;
                breaker = c;
            }
        }

    } // END buildGridPowerSource()

    public static void buildSubstation(Main mainSketch, String[] values) {

        int id = Integer.parseInt(values[1]);
        String name = values[2];
        int xCoord = Integer.parseInt(values[3]);
        int yCoord = Integer.parseInt(values[4]);
        String label = values[5];
        String assoc = values[6];


    } // END buildSubstation

    public static void buildAssignment(Main mainSketch, String[] values) {

        String name = values[1];
        int currentState = Integer.parseInt(values[2]);

        for(OldComponent c:  mainSketch.grid.components) {
            if(c.getName().equals(name)) {
                c.setCurrentState(currentState);
                break;
            } // END if name match found
        } // END for each component

    } // END buildAssignment()

    public static void buildAssociation(Main mainSketch, String[] values) {

        //ASSOC,111,SUBK,Substation K,L,Fuck Off,M,1,1,1,1,  50,  10
        //  0    1   2        3       4    5     6 7 8 9 10  11   12

        // TODO:  Fix Association build to calculate all this math within the class itself calcCoords().  Pass an ArrayList to the class method to accomplish this.

        int id = Integer.parseInt(values[1]);
        String assoc = values[2];
        String label1 = values[3];
        String label1Size = values[4];
        String label2 = values[5];
        String label2Size = values[6];
        String labelAlt = values[7];
        int bufferLeft = Integer.parseInt(values[8]);
        int bufferRight = Integer.parseInt(values[9]);
        int bufferTop = Integer.parseInt(values[10]);
        int bufferBottom = Integer.parseInt(values[11]);
        int labelHorizPct = Integer.parseInt(values[12]);
        int labelVertPct = Integer.parseInt(values[13]);
        System.out.println("BUilt assoc " + assoc);
        boolean hide = false;
        if(values.length > 14) hide = true;

        // Build the association
        Association association = new Association(mainSketch, assoc, label1, label1Size, label2, label2Size, labelAlt,
                bufferLeft, bufferRight, bufferTop, bufferBottom, labelHorizPct, labelVertPct, hide);
        mainSketch.grid.associations.add(association);

    } // END buildAssociation

    public static void buildClone12kVB(Main mainSketch, String[] values) {

        int id = Integer.parseInt(values[1]);
        String name = values[2].toUpperCase();
        char orientation = values[3].charAt(0);
        int xPos = Integer.parseInt(values[4]);
        int yPos = Integer.parseInt(values[5]);
        String cloneName = values[6].toUpperCase();
        String associatedWith = values[7].toUpperCase();
        int normalState = 0;
        String label = "TBD";
        String compType = values[0].toUpperCase();
        String textAnchor = "LC";
        char labelOrientation = 'H';
        char labelPlacement = 'R';
        OldComponent clone = null;
        String cloneType;
        String color;
        String substationAttach;
        int substationAttachNode;

        if(values.length <= 8) {
            cloneType = "CKT_DIAGRAM";
            color = "BLACK";
            substationAttach = "NONE";
            substationAttachNode = 0;
        } // END if this is a stand-alone circuit diagram, length of input string is 8
        else { // BEGIN else if this is a clone for a 12 kV switching diagram
            cloneType = values[8].toUpperCase();
            color = values[9].toUpperCase();
            substationAttach = values[10].toUpperCase();
            substationAttachNode = Integer.parseInt(values[11]);
        } // END else if this is a clone for a 12 kV switching diagram


        // Look for clone in all components
        for(OldComponent c : mainSketch.grid.components) {
            if(c.getName().equals(cloneName)) {
                normalState = c.getNormalState();
                label = c.getLabel();
                clone = c;
                continue;
            } // END if component found
        } // END for each component

        CloneBreaker12kV clone12KVB = new CloneBreaker12kV(mainSketch, id, name, compType, orientation, normalState,
                xPos, yPos, 4, label, textAnchor, labelOrientation, labelPlacement, associatedWith, clone,
                cloneType, color, substationAttach, substationAttachNode);
        mainSketch.grid.components.add(clone12KVB);
        mainSketch.grid.clones.add(clone12KVB);

    } // END buildClone12kVB

    public static void buildTandemBreakers(Main mainSketch, String[] values) {

        String nameA = values[1];
        String nameB = values[2];

        // Build the tandem breaker and add to ArrayList
        Breaker12kVTandem b12t = new Breaker12kVTandem(mainSketch, nameA, nameB);
        mainSketch.grid.tandems.add(b12t);

    } // END buildTandemBreakers

    public static void buildTwoNode(Main mainSketch, String[] values) {

        int id = Integer.parseInt(values[1]);
        String name = values[2].toUpperCase();
        String type = values[3].toUpperCase();
        char orientation = values[4].charAt(0);
        int normalState = Integer.parseInt(values[5]);
        String inNodeConnectedToComponent = values[6].toUpperCase();
        String inNodeConnectedToNode = values[7].toUpperCase();
        String outNodeConnectedToComponent = values[8].toUpperCase();
        String outNodeConnectedToNode = values[9].toUpperCase();
        int length = Integer.parseInt(values[10]);
        String label = values[11];
        String textAnchor = values[12];
        char labelOrientation = values[13].charAt(0);
        char labelPlacement = values[14].charAt(0);
        String associatedWith = values[15];
        String cutoutDirection;
        if(values.length > 16){
            cutoutDirection = values[16];
        } else {
            cutoutDirection = "NORMAL";
        }

        // Find the component this component's inNode and outNode are connected to
        OldComponent connectedInComp = new OldComponent();
        boolean foundIn = false;
        OldComponent connectedOutComp = new OldComponent();
        boolean foundOut = false;
        for (OldComponent c : mainSketch.grid.components) {
            if (c.getName().equals(inNodeConnectedToComponent)) {
                connectedInComp = c;
                foundIn = true;
            }
            if (c.getName().equals(outNodeConnectedToComponent)) {
                connectedOutComp = c;
                foundOut = true;
            }
        } // For each component already in the ArrayList

        // if both Components were found call Constructor #2
        if (foundIn && foundOut) {
            switch (type) {
                case "POWER":
                    break;
                case "70KVB":
                    Breaker70kV breaker70 = new Breaker70kV(mainSketch, id, name, type, orientation,
                            normalState, connectedInComp, inNodeConnectedToNode,
                            connectedOutComp, outNodeConnectedToNode, label, textAnchor, labelOrientation,
                            labelPlacement, associatedWith);
                    mainSketch.grid.components.add(breaker70);
                    break;
                case "12KVB":
                    Breaker12kV breaker12 = new Breaker12kV(mainSketch, id, name, type, orientation,
                            normalState, connectedInComp, inNodeConnectedToNode,
                            connectedOutComp, outNodeConnectedToNode, label, textAnchor, labelOrientation,
                            labelPlacement, associatedWith);
                    mainSketch.grid.components.add(breaker12);
                    break;
                case "SWITCH":
                    OldSwitch sw = new OldSwitch(mainSketch, id, name, type, orientation,
                            normalState, connectedInComp, inNodeConnectedToNode,
                            connectedOutComp, outNodeConnectedToNode, label, textAnchor, labelOrientation,
                            labelPlacement, associatedWith);
                    mainSketch.grid.components.add(sw);
                    break;
                case "XFMR":
                    break;
                case "BUS":
                    OldBus oldBus = new OldBus(mainSketch, id, name, type, orientation,
                            normalState, connectedInComp, inNodeConnectedToNode,
                            connectedOutComp, outNodeConnectedToNode, label, textAnchor, labelOrientation,
                            labelPlacement, associatedWith);
                    mainSketch.grid.components.add(oldBus);
                    break;
                case "CUTOUT":
                    Cutout cutout = new Cutout(mainSketch, id, name, type, orientation,
                            normalState, connectedInComp, inNodeConnectedToNode,
                            connectedOutComp, outNodeConnectedToNode, label, textAnchor, labelOrientation,
                            labelPlacement, associatedWith, cutoutDirection);
                    mainSketch.grid.components.add(cutout);
                    break;
                case "JUMPER":
                    OldJumper oldJumper = new OldJumper(mainSketch, id, name, type, orientation,
                            normalState, connectedInComp, inNodeConnectedToNode,
                            connectedOutComp, outNodeConnectedToNode, label, textAnchor, labelOrientation,
                            labelPlacement, associatedWith);
                    mainSketch.grid.components.add(oldJumper);
                    break;
                case "METER":
                    break;

            } // END switch(type)
        } // END if(found)

    } // END buildTwoNode

    public static void buildOneNode(Main mainSketch, String[] values) {

        int id = 0;
        String name = "";
        String type = "";
        char orientation = 'D';
        int normalState = 0;
        String inNodeConnectedToComponent = "none";
        String inNodeConnectedToNode = "OUT";
        int length = 1;
        String label = "";
        String textAnchor = "CC";
        char labelOrientation = 'H';
        char labelPlacement = 'R';
        String associatedWith = "none";
        String cutoutDirection = "NORMAL";

        if(values[3].toUpperCase().equals("GRIDLINE")) {
            //buildGridLine(mainSketch, values);
        } else if(values[3].toUpperCase().equals("GRIDBREAKER")) {
            //buildGridLine(mainSketch, values);
        } else if(values[3].toUpperCase().equals("LOAD")) {
            buildConnectedLoad(mainSketch, values);
        } else if(!values[3].toUpperCase().equals("LOAD")) {
            id = Integer.parseInt(values[1]);
            name = values[2].toUpperCase();
            type = values[3].toUpperCase();
            orientation = values[4].charAt(0);
            normalState = Integer.parseInt(values[5]);
            inNodeConnectedToComponent = values[6].toUpperCase();
            inNodeConnectedToNode = values[7].toUpperCase();
            length = Integer.parseInt(values[10]);
            label = values[11];
            textAnchor = values[12];
            labelOrientation = values[13].charAt(0);
            labelPlacement = values[14].charAt(0);
            associatedWith = values[15];
            if (values.length > 16) {
                cutoutDirection = values[16];
            } else {
                cutoutDirection = "NORMAL";
            }
        } // END if not "LOAD"
        else {
            System.out.println("CompType not defined!");
            return;
        }

        // Find the component this component is connected to
        OldComponent connectedComp = new OldComponent();
        boolean found = false;
        for (OldComponent c : mainSketch.grid.components) {
            if (c.getName().equals(inNodeConnectedToComponent)) {
                connectedComp = c;
                found = true;
                break;
            }
        } // For each component already in the ArrayList

        // if(found) Call Constructor #1
        if (found) {
            switch (type) {
                case "POWER":
                    break;
                case "70KVB":
                    Breaker70kV breaker70 = new Breaker70kV(mainSketch, id, name, type, orientation,
                            normalState, connectedComp, inNodeConnectedToNode, STD_OBJ_LENGTH,
                            label, textAnchor, labelOrientation, labelPlacement, associatedWith);
                    mainSketch.grid.components.add(breaker70);
                    break;
                case "12KVB":
                    Breaker12kV breaker12 = new Breaker12kV(mainSketch, id, name, type, orientation,
                            normalState, connectedComp, inNodeConnectedToNode, 4,
                            label, textAnchor, labelOrientation, labelPlacement, associatedWith);
                    mainSketch.grid.components.add(breaker12);
                    break;
                case "SWITCH":
                    OldSwitch sw = new OldSwitch(mainSketch, id, name, type, orientation,
                            normalState, connectedComp, inNodeConnectedToNode, STD_OBJ_LENGTH, label, textAnchor,
                            labelOrientation, labelPlacement, associatedWith);
                    mainSketch.grid.components.add(sw);
                    break;
                case "XFMR":
                    OldTransformer xfmr = new OldTransformer(mainSketch, id, name, type, orientation,
                            normalState, connectedComp, inNodeConnectedToNode, STD_OBJ_LENGTH, label, textAnchor,
                            labelOrientation, labelPlacement, associatedWith);
                    mainSketch.grid.components.add(xfmr);
                    break;
                case "BUS":
                    OldBus oldBus = new OldBus(mainSketch, id, name, type, orientation,
                            normalState, connectedComp, inNodeConnectedToNode, length, label, textAnchor,
                            labelOrientation, labelPlacement, associatedWith);
                    mainSketch.grid.components.add(oldBus);
                    break;
                case "UG_PASS" :
                    OldUGPass oldUgPass = new OldUGPass(mainSketch, id, name, type, orientation,
                            normalState, connectedComp, inNodeConnectedToNode, length, label, textAnchor,
                            labelOrientation, labelPlacement, associatedWith);
                    mainSketch.grid.components.add(oldUgPass);
                    break;
                case "CUTOUT":
                    Cutout cutout = new Cutout(mainSketch, id, name, type, orientation,
                            normalState, connectedComp, inNodeConnectedToNode, STD_OBJ_LENGTH, label, textAnchor,
                            labelOrientation, labelPlacement, associatedWith, cutoutDirection);
                    mainSketch.grid.components.add(cutout);
                    break;
                case "JUMPER":
                    OldJumper oldJumper = new OldJumper(mainSketch, id, name, type, orientation,
                            normalState, connectedComp, inNodeConnectedToNode, STD_OBJ_LENGTH, label, textAnchor,
                            labelOrientation, labelPlacement, associatedWith);
                    mainSketch.grid.components.add(oldJumper);
                    break;
                case "METER":
                    OldMeter oldMeter = new OldMeter(mainSketch, id, name, type, orientation,
                            normalState, connectedComp, inNodeConnectedToNode, STD_OBJ_LENGTH, label, textAnchor,
                            labelOrientation, labelPlacement, associatedWith);
                    mainSketch.grid.components.add(oldMeter);
                    break;
                case "ZIGZAG":
                    OldZigZag zz = new OldZigZag(mainSketch, id, name, type, orientation,
                            normalState, connectedComp, inNodeConnectedToNode, STD_OBJ_LENGTH, label, textAnchor,
                            labelOrientation, labelPlacement, associatedWith);
                    mainSketch.grid.components.add(zz);
                    break;
                case "LOAD" :
                    break;
                case "GRIDLINE" :
                    break;

            } // END switch(type)
        } // END if(found)

    } // END buildOneNode()

    public static void buildZeroNode(Main mainSketch, String[] values) {

        int id = Integer.parseInt(values[1]);
        String name = values[2].toUpperCase();
        String type = values[3].toUpperCase();
        char orientation = values[4].charAt(0);
        int normalState = Integer.parseInt(values[5]);
        int xPos = Integer.parseInt(values[6]);
        int yPos = Integer.parseInt(values[7]);
        int length = Integer.parseInt(values[10]);
        String label = values[11];
        String textAnchor = values[12];
        char labelOrientation = values[13].charAt(0);
        char labelPlacement = values[14].charAt(0);
        String associatedWith = values[15];

        // Call Constructor #0
        switch (type) {
            case "POWER":
                OldPowerSource ps = new OldPowerSource(mainSketch, id, name, type, orientation, normalState, xPos, yPos, STD_OBJ_LENGTH, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
                mainSketch.grid.components.add(ps);
                break;
            case "70KVB":
                Breaker70kV breaker70 = new Breaker70kV(mainSketch, id, name, type, orientation, normalState, xPos, yPos, STD_OBJ_LENGTH,
                        label, textAnchor, labelOrientation, labelPlacement, associatedWith);
                mainSketch.grid.components.add(breaker70);
                break;
            case "12KVB":
                Breaker12kV breaker12 = new Breaker12kV(mainSketch, id, name, type, orientation, normalState, xPos, yPos, 4,
                        label, textAnchor, labelOrientation, labelPlacement, associatedWith);
                mainSketch.grid.components.add(breaker12);
                break;
            case "SWITCH":
                OldSwitch sw = new OldSwitch(mainSketch, id, name, type, orientation, normalState, xPos, yPos, STD_OBJ_LENGTH,
                        label, textAnchor, labelOrientation, labelPlacement, associatedWith);
                mainSketch.grid.components.add(sw);
                break;
            case "XFMR":
                OldTransformer xfmr = new OldTransformer(mainSketch, id, name, type, orientation, normalState, xPos, yPos, STD_OBJ_LENGTH,
                        label, textAnchor, labelOrientation, labelPlacement, associatedWith);
                break;
            case "BUS":
                OldBus oldBus = new OldBus(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length,
                        label, textAnchor, labelOrientation, labelPlacement, associatedWith);
                mainSketch.grid.components.add(oldBus);
                break;
            case "CUTOUT":
                break;
            case "JUMPER":
                break;
            case "METER":
                break;
            case "TURB":
                OldTurbine oldTurbine = new OldTurbine(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length,
                        label, textAnchor, labelOrientation, labelPlacement, associatedWith);
                mainSketch.grid.components.add(oldTurbine);

        } // END switch(type)

    } // END buildZeroNode()

    public static void buildConnectedLoad(Main mainSketch, String[] values) {
        //1-NODE,101014,LOAD_B7000,LOAD,D,0,LINE_A2-9,OUT,750 kVA,Bldg 7000,Bldg 7007,Bldg 7005,Bldg 7015,JSpOC Complex,CKT_A2
        //   0     1       2         3  4 5     6      7    8        9          10        11        12        13          [last]

        int id = Integer.parseInt(values[1]);
        String name = values[2].toUpperCase();
        String type = values[3].toUpperCase();
        char orientation = values[4].charAt(0);
        int normalState = Integer.parseInt(values[5]);
        String inNodeConnectedToComponent = values[6].toUpperCase();
        String inNodeConnectedToNode = values[7].toUpperCase();
        int length = 1;
        String label = name;
        String textAnchor;
        if (orientation == 'D') textAnchor = "CT"; else textAnchor = "LC";
        char labelOrientation = 'H';
        char labelPlacement = 'R';
        String associatedWith = values[values.length-1];
        String[] labels = new String[values.length-9];
        for(int i = 8; i < values.length - 1; i++) {
            labels[i-8] = values[i];
        }

        // Find the component this component is connected to
        OldComponent connectedComp = new OldComponent();
        boolean found = false;
        for (OldComponent c : mainSketch.grid.components) {
            if (c.getName().equals(inNodeConnectedToComponent)) {
                connectedComp = c;
                found = true;
                break;
            }
        } // For each component already in the ArrayList

    } // END build connected load

    public void mousePressed() {
        //char ret = click.mousePress(this, mouseX, mouseY, mouseButton, viewport.getScale(), (int) viewport.getX(), (int) viewport.getY(), ui);
        viewport.mousePress(mouseX, mouseY);
    }

    public void mouseDragged() {
        viewport.mouseDrag(mouseX, mouseY);
    }

    public void mouseReleased() {
        viewport.mouseRelease();
        canZoom = true;
    }

    public void mouseWheel(MouseEvent event) {
        float count = event.getCount();
        viewport.setScale(count);
    }

    public void keyPressed() {

        System.out.println("Inside keyPressed.  Key entered was: " + key);

        String assoc = "NONE";

        switch(key) {
            case 'A' :
                assoc = "SUBA";
                break;
            case 'B' :
                assoc = "SUBB";
                break;
            case 'C' :
                assoc = "SUBC";
                break;
            case 'D' :
                assoc = "SUBD";
                break;
            case 'E' :
                assoc = "SUBE";
                break;
            case 'F' :
                assoc = "SUBF";
                break;
            case 'K' :
                assoc = "SUBK";
                break;
            case 'M' :
                assoc = "SUBM";
                break;
            case 'N' :
                assoc = "SUBN";
                break;
            case 'S' :
                assoc = "SWSTAT";
                break;
            case 'T' :
                assoc = "SUBT";
                break;
            case 'V' :
                assoc = "SVPP";
                break;
            case 'Z' :
                viewport.jumpToPosition(0.199f, 238,-987);
                break;
            case '1' :
                viewport.jumpToPosition(2.2f, -9389, 89);
                break;
            case '2' :
                viewport.jumpToPosition(0.700f, -4222, -3095);
                break;
            default:
                break;
        } // END switch(key)

        for(Association a : grid.associations) {
            if(a.getAssoc().equals(assoc)) {
                viewport.jumpToPosition(a.getZoomScale(), a.getZoomPanX(), a.getZoomPanY());
                break;
            } // END if association found
        } // END for each association stored
    } // END keyPressed

    public static void testRun(Main mainSketch) {


    } // END testRun()


    public void displayButtons() {
        String[] btnBarStrings = {"A", "B", "C", "D", "E", "F", "K", "N", "T", "M", "SVPP", "VAFB"};
        String[] dropDownZoomStrings = {"A", "B", "C", "D", "E", "F", "K", "N", "T", "M", "SVPP", "VAFB"};
        String[] dropDownFileStrings = {"Open", "Save", "Close", "Exit"};
        cp5.setFont(new ControlFont(createFont("Arial",14),14));
        DropdownList fileMenu = new DropdownList(cp5, "File");
        fileMenu.addItems(dropDownFileStrings).setPosition(0, 0).setSize(100,700).setValue(0).setOpen(false).setWidth(75).setItemHeight(30).setBarHeight(30);


        cp5.addDropdownList("Zoom").addItems(dropDownZoomStrings).setPosition(75, 0).setSize(100, 700).setValue(0).setOpen(false).setWidth(75).setItemHeight(30).setBarHeight(30);
    } // END displayButtons()

    public void TEST() {
        System.out.println("Inside TEST() code block");
    }

    public void Save() {
        System.out.println("Inside Save() code block");
    }

    public static void setInitialEnergyState(Main mainSketch) {

        Click clickInit = new Click();
        mainSketch.getGrid().recalcAllGridEnergy();
    }

    public void updateNodeEnergy() {

//        System.out.println("Inside updateNodeEnergy()");

        // All nodes (except PowerSource inNodes) were set to "de-energized" in Click.java
        // Now, iterate through all components and determine if nodes should be energized, repeat until no change found
        boolean changeFound = true;
        while(changeFound) {
            int countChanges = 0;
            for (OldComponent c : this.grid.components) {
                if (c.getInNode().isEnergized() && c.getCurrentState() == 0) {
                    c.getOutNode().setEnergized(true);
                    countChanges++;
                } else if (c.getOutNode().isEnergized() && c.getCurrentState() == 0) {
                    c.getInNode().setEnergized(true);
                    countChanges++;
                }
            }

            // Iterate through all clones and set their inNodes energized according to their clone's inNode
            for (CloneBreaker12kV c : this.grid.clones) {
                c.getInNode().setEnergized(c.getClone().getInNode().isEnergized());
            }

            if(countChanges == 0) changeFound = false;
        } // END while change found

    } // END updateNodeEnergy

    public Grid getGrid() {
        return this.grid;
    }

} // END public class Main
