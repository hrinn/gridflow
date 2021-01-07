package baseui;

import application.DevUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Grid;
import domain.components.Component;

import java.io.File;
import java.io.IOException;

public class GridFileManager {
    // actual logic
    private Grid grid;
    private ObjectMapper mapper;

    public GridFileManager() {
        newGrid();
        mapper = new ObjectMapper();
    }

    public void saveGrid(String path) throws IOException { //savegrid, takes in json file/path and grid object as arguments
        ObjectNode gridNode = mapper.createObjectNode();
        ArrayNode components = mapper.createArrayNode();

        grid.getComponents().forEach(c -> components.add(c.getJSONObject(mapper)));
        gridNode.put("components", components);

        //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(gridNode));

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(path), gridNode);
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
