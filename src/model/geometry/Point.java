package model.geometry;

public class Point {

    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point origin() {
        return new Point(0, 0);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    public Point scale(int scalar) {
        return new Point(this.x * scalar, this.y * scalar);
    }

    public Point translate(double x, double y) {
        return new Point(this.x + (int)x, this.y + (int)y);
    }

    public Point negative() {
        return new Point(this.x * -1, this.y * -1);
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }
}
