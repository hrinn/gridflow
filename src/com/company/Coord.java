package com.company;

public class Coord {

    private float xPos;
    private float yPos;


    public Coord(float xPos, float yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    @Override
    public String toString() {
        return this.xPos + ", " + this.yPos;
    }
}
