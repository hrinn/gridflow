package visualization.componentIcons;

import application.Globals;

public class Dimensions {

    private static final double DEFAULT_UNIT_PADDING = -0.25;

    private double width;
    private double height;

    private double topPadding = DEFAULT_UNIT_PADDING * Globals.UNIT;
    private double bottomPadding = DEFAULT_UNIT_PADDING * Globals.UNIT;
    private double rightPadding = DEFAULT_UNIT_PADDING * Globals.UNIT;
    private double leftPadding = DEFAULT_UNIT_PADDING * Globals.UNIT;
    private boolean flip = false;

    // most common dimensions
    public Dimensions() {
        this.width = 2 * Globals.UNIT;
        this.height = 3 * Globals.UNIT;
    }

    public Dimensions(double unitWidth, double unitHeight) {
        this.width = unitWidth * Globals.UNIT;
        this.height = unitHeight * Globals.UNIT;
    }

    public Dimensions(double unitWidth, double unitHeight, double unitPadding) {
        this.width = unitWidth * Globals.UNIT;
        this.height = unitHeight * Globals.UNIT;
        this.topPadding = unitPadding * Globals.UNIT;
        this.bottomPadding = unitPadding * Globals.UNIT;
        this.rightPadding = unitPadding * Globals.UNIT;
        this.leftPadding = unitPadding * Globals.UNIT;
    }

    public Dimensions(double unitWidth, double unitHeight, double unitTopPadding, double unitBottomPadding, double unitLeftPadding, double unitRightPadding) {
        this.width = unitWidth * Globals.UNIT;
        this.height = unitHeight * Globals.UNIT;
        this.topPadding = unitTopPadding * Globals.UNIT;
        this.bottomPadding = unitBottomPadding * Globals.UNIT;
        this.rightPadding = unitRightPadding * Globals.UNIT;
        this.leftPadding = unitLeftPadding * Globals.UNIT;
    }

    public Dimensions(double unitWidth, double unitHeight, double unitTopPadding, double unitBottomPadding, double unitLeftPadding, double unitRightPadding, boolean flip) {
        this.width = unitWidth * Globals.UNIT;
        this.height = unitHeight * Globals.UNIT;
        this.topPadding = unitTopPadding * Globals.UNIT;
        this.bottomPadding = unitBottomPadding * Globals.UNIT;
        this.rightPadding = unitRightPadding * Globals.UNIT;
        this.leftPadding = unitLeftPadding * Globals.UNIT;
        this.flip = flip;
    }

    public boolean isFlipped() {
        return flip;
    }

    public double getAdjustedWidth() {
        return width + leftPadding + rightPadding;
    }

    public double getAdjustedHeight() {
        return height + topPadding + bottomPadding;
    }

    public double getTopPadding() {
        return topPadding;
    }

    public double getBottomPadding() {
        return bottomPadding;
    }

    public double getLeftPadding() {
        return leftPadding;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }
}
