package com.company;

public class GridPowerSource {

    private String id;
    private CloneBreaker12kV breaker;
    private String colorName;
    private float color;
    private float voltage;

    public GridPowerSource(String id, CloneBreaker12kV breaker, String colorName, float voltage) {
        this.id = id;
        this.breaker = breaker;
        this.colorName = colorName;
        this.voltage = voltage;
        setColorByName();
    }

    public CloneBreaker12kV getBreaker() {
        return breaker;
    }

    public void setBreaker(CloneBreaker12kV breaker) {
        this.breaker = breaker;
    }

    public float getColor() {
        return color;
    }

    public void setColor(float color) {
        this.color = color;
    }

    public float getVoltage() {
        return voltage;
    }

    public void setVoltage(float voltage) {
        this.voltage = voltage;
    }

    private void setColorByName() {

        switch(colorName) {

            case "RED" :
                color = Main.RED;
                break;
            case "GREEN" :
                color = Main.GREEN;
                break;
            case "BLUE" :
                color = Main.BLUE;
                break;
            case "YELLOW" :
                color = Main.YELLOW;
                break;
            case "LTGREEN" :
                color = Main.LTGREEN;
                break;
            case "PURPLE" :
                color = Main.PURPLE;
                break;
            case "ORANGE" :
                color = Main.ORANGE;
                break;
            case "BLACK" :
                color = Main.BLACK;
                break;
            case "WHITE" :
                color = Main.WHITE;
                break;
            case "DARKGRAY" :
                color = Main.DARKGRAY;
                break;
            case "LTGRAY" :
                color = Main.LTGRAY;
                break;
            case "NAVY" :
                color = Main.NAVY;
                break;
            default:
                color = Main.BLACK;
                break;
        } // END switch(colorName)

    } // END setColorByName()


} // END public class GridPowerSource
