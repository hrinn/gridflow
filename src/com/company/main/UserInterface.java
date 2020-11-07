package com.company.main;

import controlP5.ControlP5;

public class UserInterface {



    // title
    private String title = "Vandenberg AFB Grid";  // title of grid
    private int titleSize = 20;                             // text size of title

    private String verifyInfo = "";

    private float buffer;

    // pixel location of center of title
    private float titleX = 100;
    private float titleY = 10;

    // pixel location of upper left corner of tools
    public float toolsX;
    public float toolsY;
    private float toolsBuffer;

    // dividing line 1
    public float divPoint1Y;
    // dividing line 2
    public float divPoint2Y;
    // dividing line 3
    public float divPoint3Y;

    private String zoomIn = "+";
    private String zoomOut = "-";
    private String refresh = "r";
    private String validate = "v";

    private int buttonTextSize;
    private int verifySize;

    private float titleWidth;
    private float titleHeight;
    public float toolsWidth;
    public float toolsHeight;

    protected OldMain mainSketch;
    // Controls GUI
    protected ControlP5 cp5;

    private int screenWidth;
    private int screenHeight;


    public UserInterface() {
//        this.cp5 = new ControlP5(mainSketch);
    }

    public void setSize(OldMain mainSketch) {
        this.mainSketch = mainSketch;

        this.screenWidth = mainSketch.SKETCH_WINDOW_WIDTH;
        this.screenHeight = mainSketch.SKETCH_WINDOW_HEIGHT;

        this.titleWidth = screenWidth / 4;
        this.titleHeight = screenHeight / 20;
        this.titleSize = mainSketch.HEIGHT / 2;

        this.toolsWidth = screenWidth / 30;
        this.toolsHeight = screenHeight / 4;
        this.buffer = mainSketch.HEIGHT / 10;
        this.toolsBuffer = screenWidth / 60;

        this.titleX = toolsBuffer;
        this.titleY = toolsBuffer;
        this.toolsX = screenWidth - toolsWidth - toolsBuffer;
        this.toolsY = screenHeight - toolsHeight - toolsBuffer;
        this.divPoint1Y = toolsY + toolsHeight / 4;
        this.divPoint2Y = divPoint1Y + toolsHeight / 4;
        this.divPoint3Y = divPoint2Y + toolsHeight / 4;

        this.buttonTextSize = titleSize;
        this.verifySize = (int)(titleSize / 1.5);

        // TODO delete test code
//        System.out.println("*********************");
//        System.out.println("Inside UI.setSize()");
//        System.out.println("MS display width  = " + mainSketch.displayWidth);
//        System.out.println("MS display height = " + mainSketch.displayHeight);
//        System.out.println("screenwidth       = " + screenWidth);
//        System.out.println("screenheight      = " + screenHeight);
//        System.out.println("TitleWidth        = " + titleWidth );
//        System.out.println("TitleHeight       = " + titleHeight);
//        System.out.println("TitleSize         = " + titleSize);
//        System.out.println("ToolsWidth        = " + toolsWidth);
//        System.out.println("ToolsHeight       = " + toolsHeight);
//        System.out.println("Buffer            = " + buffer);
//        System.out.println("ToolsBuffer       = " + toolsBuffer);
//        System.out.println("TitleX            = " + titleX);
//        System.out.println("TitleY            = " + titleY);
//        System.out.println("ToolsX            = " + toolsX);
//        System.out.println("TOolsY            = " + toolsY);


    } // END setSize()

    public void draw() {
        // title box
//        mainSketch.stroke(0); // black
//        mainSketch.fill(255); // white
//        mainSketch.strokeWeight(1);
//        mainSketch.rect(titleX, titleY, titleWidth, titleHeight);
//        mainSketch.fill(0); // black
//        mainSketch.textSize(titleSize);
//        mainSketch.textAlign(PConstants.CENTER, PConstants.CENTER);
//        mainSketch.text(title, titleX + (4 * buffer), titleY + (2 * buffer)); //, titleX + titleWidth, titleY + titleHeight);

        // output
//        System.out.println("Inside UI.draw() " + toolsX + "," + toolsY + "," + toolsWidth + "," + toolsHeight);

        // tools window

        mainSketch.stroke(0);  // black
        mainSketch.fill(255);  // white
        mainSketch.strokeWeight(1);
        mainSketch.rect(toolsX, toolsY, toolsWidth, toolsHeight);
        mainSketch.fill(0);

        // dividing lines
        mainSketch.line(toolsX, divPoint1Y, toolsX + toolsWidth, divPoint1Y);
        mainSketch.line(toolsX, divPoint2Y, toolsX + toolsWidth, divPoint2Y);
        mainSketch.line(toolsX, divPoint3Y, toolsX + toolsWidth, divPoint3Y);

        // button text
        mainSketch.text(zoomIn, toolsX + (4 * buffer), toolsY + (3 * buffer), toolsX + toolsWidth, toolsY + toolsHeight);
        mainSketch.text(zoomOut, toolsX + (4 * buffer), divPoint1Y + (3 * buffer), toolsX + toolsWidth, toolsY + toolsHeight);
        mainSketch.text(refresh, toolsX + (4 * buffer), divPoint2Y + (3 * buffer), toolsX + toolsWidth, toolsY + toolsHeight);
        mainSketch.text(validate, toolsX + (4 * buffer), divPoint3Y + (3 * buffer), toolsX + toolsWidth, toolsY + toolsHeight);

        // verify info
        mainSketch.textSize(verifySize);
        mainSketch.text(verifyInfo, (4 * buffer), toolsY + toolsHeight - (3 * buffer), toolsX + toolsWidth, toolsY + toolsHeight);




    } // END draw()

    public void setVerifyInfo(String verifyInfo) {
        this.verifyInfo = verifyInfo;
    } // END setVerifyInfo


} // END public class UserInterface
