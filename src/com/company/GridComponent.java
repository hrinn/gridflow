package com.company;

import java.util.ArrayList;

public class GridComponent extends Component {

    // This class is the same as Component except the objects have ArrayLists of GridPowerSources to identify which
    // colors should be rendered around energized lines.  This class is used only for the 12 kV switching diagrams
    // There will be no 0-Node constructors for this class because there are only lines, switches and ties



    private ArrayList<GridPowerSource> inNodePowerSources;
    private ArrayList<GridPowerSource> outNodePowerSources;

    public GridComponent(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        inNodePowerSources = new ArrayList<>();
        outNodePowerSources = new ArrayList<>();
    }

    public GridComponent(Main mainSketch, int id, String name, String type, char orientation, int normalState, Component connectedToIn, String inoutIn, Component connectedToOut, String inoutOut, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedToIn, inoutIn, connectedToOut, inoutOut, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        inNodePowerSources = new ArrayList<>();
        outNodePowerSources = new ArrayList<>();
    }

    public void refreshPowerSources() {



    } // END refreshPowerSources

    public ArrayList<GridPowerSource> getInNodePowerSources() {
        return inNodePowerSources;
    }

    public ArrayList<GridPowerSource> getOutNodePowerSources() {
        return outNodePowerSources;
    }
} // END public class GridComponent
