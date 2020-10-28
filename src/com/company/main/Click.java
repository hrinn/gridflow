package com.company.main;

import com.company.oldcomponents.*;
import processing.core.PConstants;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Click {

    // TODO:  Do we need this psfi variable?
    public static final int UNIT = 20;

    float x;
    float y;

    public Click() {
        this.x = 0;
        this.y = 0;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public int calcPos(float coord, float scale, int pan) {
        return (int) ((UNIT * coord * scale) + (pan * scale));
    }

    // writeFile writes to positions.txt every time a component is clicked
    // it looks for the target line by looking at the component number and changes its current state
    public static void writeFile(OldComponent c, String fName, int changedstate, int newstate) {
        File file = new File(fName);
//        String target = c.getId() + " " + c.getType() + " " + c.getX() + " " + c.getY() + " " + c.getOrientation() + " " + c.getName() + " " + c.getNormalState() + " " + changedstate;
//        String replacement = c.getId() + " " + c.getType() + " " + c.getX() + " " + c.getY() + " " + c.getOrientation() + " " + c.getName() + " " + c.getNormalState() + " " + newstate;
//
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            StringBuffer inputBuffer = new StringBuffer();
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                inputBuffer.append(line);
//                inputBuffer.append('\n');
//            }
//            br.close();
//            String inputStr = inputBuffer.toString();
//
//            inputStr = inputStr.replace(target, replacement);
//
//            FileOutputStream fileOut = new FileOutputStream(fName);
//            fileOut.write(inputStr.getBytes());
//            fileOut.close();
//        } catch (Exception e) {
//            System.out.println("Problem reading file.");
//        }
    }

    public char mousePress(Main mainSketch, int mouseX, int mouseY, int mouseButton, float scale, int panX, int panY, UserInterface ui) {
        // check if clicked in tools box
        // TODO:  WRITE CODE FOR MENU ITEMS!!!
        // If not on a menu item, check if mouse is on a clickable component

        for (OldComponent c : mainSketch.oldComponents) {

            // Is this even a clickable object?
            if (c.isCanOpen()) {
                // If so, is the mouse on this particular component?
                if (c.isOnComponent(scale, panX, panY, mouseX, mouseY)) {

                    resetEnergy(mainSketch.oldComponents);
                    // If so, is the mouse button the LEFT mouse?
                    if (mouseButton == PConstants.LEFT) {
                        if (c.getCurrentState() == 0) {
                            c.setCurrentState(1);
//                            System.out.println("OPEN " + c.getLabel() + " at " + LocalDateTime.now().toString());
//                        writeFile(c, "positions.txt", 0, 1);
                        } else if (c.getCurrentState() == 1) {
                            c.setCurrentState(0);
//                            System.out.println("CLOSE " + c.getLabel() + " at " + LocalDateTime.now().toString());
//                        writeFile(c, "positions.txt", 1, 0);
                        }
                    } // END if LEFT mouse button
                    // otherwise if RIGHT mouse button
                    else if (mouseButton == PConstants.RIGHT) {
                        if (c.getCurrentState() == 2) {
                            c.setCurrentState(1);  // if Component is already locked out, then set it to OPEN
//                            System.out.println("UNLOCK " + c.getLabel() + " at " + LocalDateTime.now().toString());
                        } else if (c.getCurrentState() == 1) {
                            c.setCurrentState(2);  // Otherwise, set the component to LOCKOUT
//                            System.out.println("LOCK " + c.getLabel());
                        } else {
                            c.setCurrentState(2);  // Otherwise, set the component to LOCKOUT
//                            System.out.println("OPEN AND LOCK " + c.getLabel() + " at " + LocalDateTime.now().toString());
                        }
                    } // END if RIGHT mouse button

                    System.out.print("Clicked " + c.getName());
                    switch (c.getCurrentState()) {
                        case 0:
                            System.out.println(" CLOSED at " + LocalDateTime.now().toString());
                            break;
                        case 1:
                            System.out.println(" OPEN   at " + LocalDateTime.now().toString());
                            break;
                        default:
                            System.out.println(" LOCKED at " + LocalDateTime.now().toString());
                            break;
                    }

                    // ***********************************************
                    // Now determine if any clones need to be impacted
                    updateClones(mainSketch, c);

//                    // Look for clones
//                    Component clone = null;
//                    boolean cloneFound = false;
//                    // First consider if clone is this component anyhow
//                    if (c instanceof CloneBreaker12kV) {
//                        String cloneName = ((CloneBreaker12kV) c).getClone().getName();
//                        cloneFound = true;
//
//                        for (Component cloneCandidate : mainSketch.components) {
//                            if (cloneCandidate.getName().equals(cloneName)) {
//                                clone = cloneCandidate;
////                                if(cloneFound) System.out.println("Component " + c.getName() + "  Clone " + cloneCandidate.getName());
//                                break;
//                            }
//                        } // END for every component (search)
//
//                    } // END if c is an instance of a clone
//                    // else if this is not already a clone, look for a clone
//                    // TODO: need to improve method for searching for all clones
//                    else {
//                        String cloneName1 = "CLONE_" + c.getName();
//                        String cloneName2 = "CLONE_" + c.getName() + "_12KV";
//                        for (Component cloneCandidate : mainSketch.components) {
//                            if (cloneCandidate.getName().equals(cloneName1) ||
//                                cloneCandidate.getName().equals(cloneName2)) {
//                                clone = cloneCandidate;
//                                cloneFound = true;
//                                break;
//                            } // END if clone name found
//
//                        } // END for each component
//                    } // END else if not already a clone
//
//                    if (cloneFound) {
//
//                        clone.setCurrentState(c.getCurrentState());
//                        // Now reset the energy state of the clone's InNode so it can have power
//                        if (c instanceof CloneBreaker12kV) {
//                            c.getInNode().setEnergized(clone.getInNode().isEnergized());
//                        } else {
//                            clone.getInNode().setEnergized(c.getInNode().isEnergized());
//                        }
//                    } // END if clone was found
                    recalcAllGridEnergy(mainSketch);
                } // END if mouse is on the component
            } // END if the object can be opened
        } // FOR each component

        return '\0';
    } // END mousePress

    public void updateClones(Main mainSketch, OldComponent clickedOldComponent) {

        boolean cloneUpdated = false;

        ArrayList<OldComponent> clonesFound = new ArrayList<>();
        String masterComponentName = "";

        // Establish the master component name
        if(clickedOldComponent instanceof CloneBreaker12kV) {
            OldComponent master = ((CloneBreaker12kV) clickedOldComponent).getClone();
            masterComponentName = master.getName();
        }
        else if(clickedOldComponent instanceof Breaker12kV) {
            masterComponentName = clickedOldComponent.getName();
        }

        // Search through all components to find any clones or masters and add them to the arrayList
        for(OldComponent c : mainSketch.oldComponents) {
            if(c.getName().equals(masterComponentName)) {
                clonesFound.add(c);
                continue;
            }
            if(c instanceof CloneBreaker12kV) {
                if(((CloneBreaker12kV) c).getClone().getName().equals(masterComponentName)) {
                    clonesFound.add(c);
                }
            }
        } // END for each Component

        // Assign currentState to all masters/clones in the ArrayList
        for(OldComponent c: clonesFound) {
            c.setCurrentState(clickedOldComponent.getCurrentState());
            cloneUpdated = true;
        }

    } // END updateClones

    // Resets the energy state for all Nodes except PowerSource inNodes
    public void resetEnergy(ArrayList<OldComponent> oldComponents) {


        // Set all nodes to denergized except Power Sources
        for (OldComponent c : oldComponents) {
            if (!(c instanceof OldPowerSource)) {
                c.getInNode().setEnergized(false);
                c.getOutNode().setEnergized(false);
            } else {
                c.getInNode().setEnergized(true);
                c.getOutNode().setEnergized(false);
            }
        }

    } // END reset energy

    public void recalcAllGridEnergy(Main mainSketch) {

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
            for (OldComponent c : mainSketch.oldComponents) {
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
            for (CloneBreaker12kV c : mainSketch.clones) {
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

} // END public class Click
