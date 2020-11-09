package model.geometry;

public class Point {

    private float x;
    private float y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Point add(Point other) {
        return new Point(this.x + other.x, this.y + other.y);
    }

    public Point multiply(float multiplicand) {
        return new Point(this.x * multiplicand, this.y * multiplicand);
    }

    public Point negative() {
        return new Point(this.x * -1, this.y * -1);
    }

    @Override
    public String toString() {
        return this.x + ", " + this.y;
    }
}
