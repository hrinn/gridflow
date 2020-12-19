package baseui;

import application.DevUtils;
import domain.Grid;

public class GridFileManager {
    // actual logic
    private Grid grid;

    public GridFileManager() {
        newGrid();
    }

    public void saveGrid(String path) { //savegrid, takes in json file/path and grid object as arguments
        //get path, hardcode for now
        //grid.getcomponents gives list of components
        //export component list to json
    }

    public void loadGrid(String path) { //loadgrid, takes in filename, load components from json file
        //parse json, build list of components, call grid.loadcomponents
    }

    public void newGrid() { //newgrid, returns new grid
        this.grid = new Grid();
    }

    public Grid getGrid() {
        return grid;
    }

}
