package domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import domain.geometry.Point;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import visualization.componentIcons.AssociationIcon;
import visualization.componentIcons.IconCreator;

import java.util.UUID;

public class Association implements Selectable {

    private UUID id;

    // strings displayed inside the association
    private String label;
    private String subLabel = "";
    private String acronym = "ASC";

    // the javaFX node displayed on screen
    private AssociationIcon associationIcon;

    public Association(Point position, double width, double height, int n) {
        this.id = UUID.randomUUID();
        this.label = "Association " + n;
        createAssociationIcon(position, width, height);
    }

    public Association(JsonNode node) {
        this.id = UUID.fromString(node.get("id").asText());
        this.label = node.get("label").asText();
        this.subLabel = node.get("subLabel").asText();
        this.acronym = node.get("acronym").asText();
        Point pos = Point.fromString(node.get("pos").asText());
        double width = node.get("width").asDouble();
        double height = node.get("height").asDouble();
        createAssociationIcon(pos, width, height);
        Point labelPos = Point.fromString(node.get("labelPos").asText());
        associationIcon.setTextPosition(labelPos);
        associationIcon.showHandles(false);
    }

    private void createAssociationIcon(Point position, double width, double height) {
        this.associationIcon = IconCreator.createAssociationNode(position, width, height, label, id);
    }

    public AssociationIcon getAssociationIcon() {
        return associationIcon;
    }

    public UUID getID() {
        return id;
    }

    public void setSelect(boolean select) {
        associationIcon.setSelect(select);
    }

    public ObjectNode getObjectNode(ObjectMapper mapper) {
        ObjectNode association = mapper.createObjectNode();
        association.put("id", id.toString());
        association.put("label", label);
        association.put("subLabel", subLabel);
        association.put("acronym", acronym);
        Rectangle rect = associationIcon.getRect();
        Point pos = new Point(rect.getX(), rect.getY());
        association.put("pos", pos.toString());
        association.put("width", rect.getWidth());
        association.put("height", rect.getHeight());
        Text text = associationIcon.getText();
        Point labelPos = new Point(text.getTranslateX(), text.getTranslateY());
        association.put("labelPos", labelPos.toString());
        return association;
    }
}
