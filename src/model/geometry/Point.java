package model.geometry;

public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point origin() {
        return new Point(0, 0);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point scale(int scalar) {
        return new Point(this.x * scalar, this.y * scalar);
    }

    public Point translate(double x, double y) {
        return new Point(this.x + (int)x, this.y + (int)y);
    }

    public double differenceY(Point other) {
        return Math.abs(this.y - other.y);
    }

    public double differenceX(Point other) {
        return Math.abs(this.x - other.x);
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }
}
