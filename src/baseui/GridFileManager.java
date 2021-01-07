package baseui;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Grid;
import domain.components.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;

public class GridFileManager {
    private Grid grid;
    private ObjectMapper mapper;

    public GridFileManager() {
        newGrid();
        mapper = new ObjectMapper();
    }

    public void saveGrid(String path) throws IOException {
        ObjectNode gridNode = mapper.createObjectNode();
        ArrayNode components = mapper.createArrayNode();

        grid.getComponents().forEach(c -> components.add(c.getObjectNode(mapper)));
        gridNode.put("components", components);

        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(path), gridNode);
    }

    public void loadGrid(String path) throws IOException {
        JsonParser parser = mapper.getFactory().createParser(new File(path));
        ObjectNode gridNode = mapper.readTree(parser);
        parser.close();

        grid = new Grid();
        ArrayNode components = (ArrayNode) gridNode.get("components");
        for (JsonNode componentJSON : components) {
            Component component = switch (componentJSON.get("type").asText()) {
                case "Breaker" -> new Breaker(componentJSON);
                case "Cutout" -> new Cutout(componentJSON);
                case "Jumper" -> new Jumper(componentJSON);
                case "PowerSource" -> new PowerSource(componentJSON);
                case "Switch" -> new Switch(componentJSON);
                case "Transformer" -> new Transformer(componentJSON);
                case "Turbine" -> new Turbine(componentJSON);
                case "Wire" -> new Wire(componentJSON);
                default -> throw new UnsupportedOperationException();
            };
            grid.addComponent(component);
        }
        System.out.println(grid.getComponents().size());
    }

    public void newGrid() { //newgrid, returns new grid
        this.grid = new Grid();
    }

    public Grid getGrid() {
        return grid;
    }

}
