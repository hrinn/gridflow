package domain.components;

import application.Globals;

public class Dimensions {

    private static final double DEFAULT_UNIT_PADDING = -0.5;

    public double unitWidth;
    public double unitHeight;
    public double unitWidthPadding = DEFAULT_UNIT_PADDING;
    public double unitHeightPadding = DEFAULT_UNIT_PADDING;

    public double getAdjustedWidth() {
        return (unitWidth + unitWidthPadding) * Globals.UNIT;
    }

    public double getAdjustedHeight() {
        return (unitHeight + unitHeightPadding) * Globals.UNIT;
    }

    public double getHeight() {
        return unitHeight * Globals.UNIT;
    }

    public double getHeightPadding() {
        return unitHeightPadding * Globals.UNIT;
    }
}
