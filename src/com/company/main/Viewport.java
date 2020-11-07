package com.company.main;

public class Viewport {

    // TODO:  study whether unit is even needed in this class

    // Global Variables
    int unit;


    // panning variables
    float x;  // current grid position of the top left corner of the screen
    float y;  // current grid position of the top left corner of the screen
    float startX;
    float startY;
    float changeX;
    float changeY;

    // width and height of screen in pixels
    float width;
    float height;

    // viewport scale
    float scale;

    // viewport midpoint
    float midX;
    float midY;

    public Viewport() {
        this.x = 0;
        this.y = 0;
        this.startX = 0;
        this.startY = 0;
        this.changeX = 0;
        this.changeY = 0;
        this.width = OldMain.SKETCH_WINDOW_WIDTH;
        this.height = OldMain.SKETCH_WINDOW_HEIGHT;
        this.scale = 1.0f;
        this.unit = OldMain.UNIT;

        setSize(this.width, this.height);
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        this.midX = width / 2;
        this.midY = height / 2;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return (this.x + this.changeX) + (1 / this.scale) * (this.midX) - this.midX;
    }

    public float getY() {
        return (this.y + this.changeY) + (1 / this.scale) * (this.midY) - this.midY;
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float count) {

        float zoomPctPerClick = 0.075f; // increases/decreases zoom by 7.5% per click

        if(count == -1 || count == 1) {

            if (count < 0) {
                this.scale = (1 + zoomPctPerClick) * this.scale;
            } else {
                this.scale = (1 - zoomPctPerClick) * this.scale;
            }
        } else {
            this.scale = count;
        }
        if (this.scale < 0.1) {
            this.scale = 0.1f;
        }

        // TODO delete this
        System.out.println("scale: " + this.scale + "  XPOS:  " + this.x + "  YPOS:  " + this.y + "  Screen Size in Grid Units: Width, " +
                OldMain.SKETCH_WINDOW_WIDTH / this.unit / this.scale + "   Height, " + OldMain.SKETCH_WINDOW_HEIGHT / this.unit / this.scale);

    } // END setScale()

    public void mousePress(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
        this.changeX = 0;
        this.changeY = 0;
    }

    public void mouseDrag(float x, float y) {
        this.changeX = (1 / this.scale) * (x - this.startX);
        this.changeY = (1 / this.scale) * (y - this.startY);
    }

    public void mouseRelease() {
//        System.out.println("old: " + this.x + "," + this.y);
        this.x += this.changeX;
        this.y += this.changeY;
//        System.out.println("new: " + this.x + "," + this.y);
        this.startX = 0;
        this.startY = 0;
        this.changeX = 0;
        this.changeY = 0;
    }

    public void jumpToPosition(float scale, float panX, float panY) {
        System.out.println("JumpToPosition: " + scale + "," + panX + "," + panY);
        this.scale = scale;
        this.x = panX;
        this.y = panY;
    }
}