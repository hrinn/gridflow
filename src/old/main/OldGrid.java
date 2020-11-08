package old.main;

import old.oldcomponents.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class OldGrid {

    public ArrayList<OldComponent> components;
    public ArrayList<CloneBreaker12kV> clones;
    public ArrayList<Breaker12kVTandem> tandems;
    public ArrayList<OldNode> nodes;
    public ArrayList<Association> associations;

    public OldGrid() {
        components = new ArrayList<>();
        clones = new ArrayList<>();
        tandems = new ArrayList<>();
        nodes = new ArrayList<>();
        associations = new ArrayList<>();
    }

    public void addNode(OldNode oldNode) {
        nodes.add(oldNode);
    }

    public void recalcAllGridEnergy() {

        // Set up time constants
        String startTime = LocalDateTime.now().toString();

        // Now iterate through all components and determine if nodes should be energized
        // Repeat until no change is made
        boolean changeFound = true;
        int iterationCounter = 0;

        // TODO:  get rid of this iterationCounter stop gate (hard-coded to eliminate infinite loop)
        while (changeFound && iterationCounter < 1000) {
            int changeCount = 0;
            iterationCounter++;
            // First address primary grid components
            for (OldComponent c : components) {
                boolean previousInNode = c.getInNode().isEnergized();
                boolean previousOutNode = c.getOutNode().isEnergized();

                // These steps are necessary ONLY for Turbine components
                if(c instanceof OldTurbine) {
                    if (c.getCurrentState() == 0) {
                        c.getInNode().setEnergized(true);
                        c.getOutNode().setEnergized(true);
                    } else {
                        c.getInNode().setEnergized(false);
                        c.getOutNode().setEnergized(false);
                    }
                } // END for turbines only

                // Now update the nodes of each component EXCEPT Turbines
                if(!(c instanceof OldTurbine)) {
                    if (c.getInNode().isEnergized() && c.getCurrentState() == 0) {
                        c.getOutNode().setEnergized(true);
                    } else if (c.getOutNode().isEnergized() && c.getCurrentState() == 0) {
                        c.getInNode().setEnergized(true);
                    } // END else
                    if (!(c.getInNode().isEnergized() == previousInNode) || !(c.getOutNode().isEnergized() == previousOutNode)) {
                        changeCount++;
                    } // END if either node changed energy state
                }
            } // END for each component

            // Second address cloned objects
            // Iterate through all clones and set their inNodes energized according to their clone's inNode
            for (CloneBreaker12kV c : clones) {
                boolean previousInNode = c.getInNode().isEnergized();
                c.getInNode().setEnergized(c.getClone().getInNode().isEnergized());
                if(!(previousInNode == c.getInNode().isEnergized())) changeCount++;
            }

            if (changeCount == 0) changeFound = false;
        } // END while(changeFound)

        System.out.println("Iterations taken " + iterationCounter);
//        System.out.println("Started:  " + startTime);
//        System.out.println("Ended:    " + LocalDateTime.now().toString());
        System.out.println("******** END ************");

    } // END recalcAllGridEnergy

}
