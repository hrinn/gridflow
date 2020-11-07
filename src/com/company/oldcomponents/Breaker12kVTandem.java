package com.company.oldcomponents;

import com.company.main.OldMain;

public class Breaker12kVTandem {

    protected OldMain mainSketch;

    private String nameA;
    private String nameB;

    public Breaker12kVTandem(OldMain mainSketch, String nameA, String nameB) {
        this.mainSketch = mainSketch;
        this.nameA = nameA;
        this.nameB = nameB;
    }

    public void updateTandemStatus() {

        // Update all tandem breakers
        OldComponent compA = new OldComponent();
        OldComponent compB = new OldComponent();

        for (OldComponent c : mainSketch.getGrid().components) {
            if (this.getNameA().equals(c.getName())) compA = c;
            if (this.getNameB().equals(c.getName())) compB = c;
        } // END for each component

        if (compA.getCurrentState() == 0) {          // if Component A is closed
            compB.setCanOpen(false);                 // prevent user from clicking Component B
            compB.setCurrentState(2);                // lockout (state = 2) Component B
        } else if (compB.getCurrentState() == 0) {   // Otherwise, if Component B is closed
            compA.setCanOpen(false);                 // prevent user from clicking Component A
            compA.setCurrentState(2);                // lockout (state = 2) Component A
        } else {                                     // Otherwise, if neither component is closed
            compA.setCanOpen(true);                  // Allow user to click Component A
            compA.setCurrentState(1);                // Remove lockout state from Component A
            compB.setCanOpen(true);                  // Allow user to click Component B
            compB.setCurrentState(1);                // Remove lockout state from Component B
        }
    } // END updateTandemStatus

    public String getNameA() {
        return nameA;
    }

    public void setNameA(String nameA) {
        this.nameA = nameA;
    }

    public String getNameB() {
        return nameB;
    }

    public void setNameB(String nameB) {
        this.nameB = nameB;
    }
} // END public class Breaker12kVTandem
