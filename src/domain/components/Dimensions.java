package domain.components;

import application.Globals;

public class Dimensions {

    private static final double DEFAULT_UNIT_PADDING = -0.25;

    private double width;
    private double height;

    private double topPadding = DEFAULT_UNIT_PADDING * Globals.UNIT;
    private double bottomPadding = DEFAULT_UNIT_PADDING * Globals.UNIT;
    private double rightPadding = DEFAULT_UNIT_PADDING * Globals.UNIT;
    private double leftPadding = DEFAULT_UNIT_PADDING * Globals.UNIT;

    public double getAdjustedWidth() {
        return width + leftPadding + rightPadding;
    }

    public double getAdjustedHeight() {
        return height + topPadding + bottomPadding;
    }

    public double getTopPadding() {
        return topPadding;
    }

    public double getHeight() {
        return height;
    }

    public void setWidth(double unitWidth) {
        this.width = unitWidth * Globals.UNIT;
    }

    public void setHeight(double unitHeight) {
        this.height = unitHeight * Globals.UNIT;
    }

    public void setPadding(double unitPadding) {
        this.topPadding = unitPadding * Globals.UNIT;
        this.bottomPadding = unitPadding * Globals.UNIT;
        this.rightPadding = unitPadding * Globals.UNIT;
        this.leftPadding = unitPadding * Globals.UNIT;
    }

    public void setTopPadding(double unitTopPadding) {
        this.topPadding = unitTopPadding * Globals.UNIT;
    }

    public void setBottomPadding(double unitBottomPadding) {
        this.bottomPadding = unitBottomPadding * Globals.UNIT;
    }

    public void setRightPadding(double unitRightPadding) {
        this.rightPadding = unitRightPadding * Globals.UNIT;
    }

    public void setLeftPadding(double unitLeftPadding) {
        this.leftPadding = unitLeftPadding * Globals.UNIT;
    }
}
