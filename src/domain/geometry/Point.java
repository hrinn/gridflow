package domain.geometry;

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
