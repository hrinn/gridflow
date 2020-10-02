package com.company;

public class Node {

    private Coord coord;
    private boolean isEnergized;
    private int powerSourceCount;

    public Node(Coord inCoord, boolean isEnergized) {
        this.coord = inCoord;
        this.isEnergized = isEnergized;
        this.powerSourceCount = 0;
    }  // END constructor for a component where you know the node is energized (isEnergized = true)

    public Node(Coord inCoord) {
        this.coord = inCoord;
        this.isEnergized = false;
        this.powerSourceCount = 0;
    } // END generic constructor drives isEnergized = false

    // updates energy state
    public void update() {


    }

    public void setPowerSourceCountToZero() {
        this.powerSourceCount = 0;
    }

    public void addToPowerSourceCount() {
        this.powerSourceCount++;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public boolean isEnergized() {
        return isEnergized;
    }

    public void setEnergized(boolean energized) {
        isEnergized = energized;
    }
}
