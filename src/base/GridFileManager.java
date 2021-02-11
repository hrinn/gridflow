package base;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.Association;
import domain.Grid;
import domain.components.*;
import domain.geometry.Point;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GridFileManager {
    private Grid grid;
    private ObjectMapper mapper;

    public GridFileManager() {
        grid = new Grid();
        mapper = new ObjectMapper();
    }

    public void saveGrid(String path) throws IOException {
        ObjectNode gridNode = mapper.createObjectNode();

        // save components
        ArrayNode components = mapper.createArrayNode();
        grid.getComponents().forEach(c -> components.add(c.getObjectNode(mapper)));
        gridNode.put("components", components);

        // save associations
        ArrayNode associations = mapper.createArrayNode();
        grid.getAssociations().forEach(a -> associations.add(a.getObjectNode(mapper)));
        gridNode.put("associations", associations);

        // write all the data to the file
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File(path), gridNode);
    }

    public void loadGrid(String path) {
        ObjectNode gridNode;
        try {
            JsonParser parser = mapper.getFactory().createParser(new File(path));
            gridNode = mapper.readTree(parser);
            parser.close();
        } catch (IOException e) {
            System.err.println("Cannot read file");
            return;
        }

        grid.clearGrid();
        ArrayNode JSONComponents = (ArrayNode) gridNode.get("components");

        // create components
        for (JsonNode componentJSON : JSONComponents) {
            Component component = switch (componentJSON.get("type").asText()) {
                case "Breaker" -> new Breaker(componentJSON);
                case "Cutout" -> new Cutout(componentJSON);
                case "Jumper" -> new Jumper(componentJSON);
                case "PowerSource" -> new PowerSource(componentJSON);
                case "Switch" -> new Switch(componentJSON);
                case "Transformer" -> new Transformer(componentJSON);
                case "Turbine" -> new Turbine(componentJSON);
                case "Wire" -> createWireFromJson(componentJSON);
                default -> throw new UnsupportedOperationException();
            };
            grid.addComponent(component);
        }

        // connect components
        for (int i = 0; i < grid.getComponents().size(); i++) {
            Component component = grid.getComponents().get(i);
            JsonNode componentJSON = JSONComponents.get(i);
            component.setConnections(getConnectionsList(componentJSON));
        }

        // create associations from JSON
        ArrayNode JSONAssociations = (ArrayNode) gridNode.get("associations");
        if (JSONAssociations == null) return;
        for (JsonNode associationJSON : JSONAssociations) {
            Association association = new Association(associationJSON);
            grid.addAssociation(association);
        }
    }

    private Wire createWireFromJson(JsonNode node) {
        Point start = Point.fromString(node.get("start").asText());
        Point end = Point.fromString(node.get("end").asText());
        return new Wire(node, start, end);
    }

    private List<Component> getConnectionsList(JsonNode node) {
        List<Component> connections = new ArrayList<>();
        switch (node.get("type").asText()) {
            case "Breaker", "Cutout", "Jumper", "Switch", "Transformer" -> {
                connections.add(grid.getComponent(node.get("inWire").asText()));
                connections.add(grid.getComponent(node.get("outWire").asText()));
            }
            case "PowerSource" -> {
                connections.add(grid.getComponent(node.get("outWire").asText()));
            }
            case "Turbine" -> {
                connections.add(grid.getComponent(node.get("outWire1").asText()));
                connections.add(grid.getComponent(node.get("outWire2").asText()));
            }
            case "Wire" -> {
                ArrayNode jsonConnections = (ArrayNode)node.get("connections");
                jsonConnections.forEach(jsonConnection ->
                    connections.add(grid.getComponent(jsonConnection.asText()))
                );
            }
            default -> throw new UnsupportedOperationException();
        }
        return connections;
    }

    public Grid getGrid() {
        return grid;
    }

}
