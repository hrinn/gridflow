package old.oldcomponents;

import model.components.geometry.Point;

public class OldNode {

    private Point point;
    private boolean isEnergized;
    private int powerSourceCount;

    public OldNode(Point point, boolean isEnergized) {
        this.point = point;
        this.isEnergized = isEnergized;
        this.powerSourceCount = 0;
    }  // END constructor for a component where you know the node is energized (isEnergized = true)

    public OldNode(Point inPoint) {
        this.point = inPoint;
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

    public Point getCoord() {
        return point;
    }

    public void setCoord(Point point) {
        this.point = point;
    }

    public boolean isEnergized() {
        return isEnergized;
    }

    public void setEnergized(boolean energized) {
        isEnergized = energized;
    }
}
