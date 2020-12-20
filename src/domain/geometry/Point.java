package domain.geometry;

import application.Globals;

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

    public static Point midpoint(Point p1, Point p2) {
        return new Point((p1.x + p2.x)/2, (p1.y + p2.y)/2);
    }

    public static Point nearestCoordinate(double x, double y) {
        double rx = Math.round(x / Globals.UNIT) * Globals.UNIT;
        double ry = Math.round(y / Globals.UNIT) * Globals.UNIT;
        return new Point(rx, ry);
    }

    public Point clampPerpendicular(Point clampPoint) {
        if (clampPoint.differenceX(this) > clampPoint.differenceY(this)) {
            return new Point(x, clampPoint.getY());
        } else {
            return new Point(clampPoint.getX(), y);
        }
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
        return new Point(this.x + x, this.y + y);
    }

    public Point rotate(double angle, Point pivot) {
        double theta = Math.toRadians(angle);

        double rx = pivot.getX() + Math.cos(theta) * (x - pivot.getX()) - Math.sin(theta) * (y - pivot.getY());
        double ry = pivot.getY() + Math.sin(theta) * (x - pivot.getX()) + Math.cos(theta) * (y - pivot.getY());

        return new Point(rx, ry);
    }

    public double differenceY(Point other) {
        return Math.abs(this.y - other.y);
    }

    public double differenceX(Point other) {
        return Math.abs(this.x - other.x);
    }

    public boolean equals(Point point) {
        return x == point.x && y == point.y;
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }
}
