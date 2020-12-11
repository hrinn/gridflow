package domain.geometry;

public class Rectangle {

    private Point topLeft;
    private Point bottomRight;

    public Rectangle(Point topleft, Point bottomRight) {
        this.topLeft = topleft;
        this.bottomRight = bottomRight;
    }

    public boolean intersects(Rectangle other) {
        return false;
    }

    public Point getTopLeft() {
        return topLeft;
    }

    public Point getBottomRight() {
        return bottomRight;
    }
}
