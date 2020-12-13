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

    private double getWidth() {
        return Math.abs(topLeft.getX()) - bottomRight.getX();
    }

    private double getHeight() {
        return Math.abs(topLeft.getY()) - bottomRight.getY();
    }

    public Point getMidRight() {
        return bottomRight.translate(0, getHeight()/2);
    }
}
