package construction.properties.objectData;

import construction.properties.Visitor;

// This class holds Component Data that the properties window displays and can set
public class ComponentData extends ObjectData {

    private String name;
    private double angle;

    // Name Pos is relative to angle
    // 0 - false is left, true is right
    // 90 - false is top, true is bottom
    // 180 - true is left, false is right
    // 270 - true is top, false is bottom
    private boolean namePos;

    public ComponentData(String name, boolean namePos, double angle) {
        this.name = name;
        this.namePos = namePos;
        this.angle = angle;
    }

    public boolean isVertical() {
        return angle % 180 == 0;
    }

    public boolean isNameRight() {
        return (angle == 0 && namePos) || (angle == 180 && !namePos);
    }

    public boolean isNameTop() {
        return (angle == 90 && !namePos) || (angle == 270 && namePos);
    }

    public boolean isNamePos() {
        return namePos;
    }

    @Override
    public void accept(Visitor v) {
        v.setComponentMenu(this);
    }

    public String getName() {
        return name;
    }

    protected boolean getNamePos(boolean nameRightOrTop) {
        if (isVertical()) {
            // nameRight
            if (angle == 0) {
                return nameRightOrTop;
            } else { // angle == 180
                return !nameRightOrTop;
            }
        } else {
            // nameTop
            if (angle == 90) {
                return !nameRightOrTop;
            } else { // angle == 270
                return nameRightOrTop;
            }
        }
    }

    @Override
    public ObjectData applySettings(String name, boolean nameRightOrTop, boolean isClosed, String label, String subLabel, String acronym) {
        return new ComponentData(name, getNamePos(nameRightOrTop), angle);
    }

    public double getAngle() {
        return angle;
    }
}
