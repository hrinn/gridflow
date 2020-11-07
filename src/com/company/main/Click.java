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

    public char mousePress(OldMain mainSketch, int mouseX, int mouseY, int mouseButton, float scale, int panX, int panY, UserInterface ui) {
        // check if clicked in tools box
        // TODO:  WRITE CODE FOR MENU ITEMS!!!
        // If not on a menu item, check if mouse is on a clickable component

        for (OldComponent c : mainSketch.getGrid().components) {

            // Is this even a clickable object?
            if (c.isCanOpen()) {
                // If so, is the mouse on this particular component?
                if (c.isOnComponent(scale, panX, panY, mouseX, mouseY)) {

                    resetEnergy(mainSketch.getGrid().components);
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
                    mainSketch.getGrid().recalcAllGridEnergy();
                } // END if mouse is on the component
            } // END if the object can be opened
        } // FOR each component

        return '\0';
    } // END mousePress

    public void updateClones(OldMain mainSketch, OldComponent clickedOldComponent) {

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
        for(OldComponent c : mainSketch.getGrid().components) {
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

} // END public class Click
