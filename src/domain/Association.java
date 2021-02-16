package domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import construction.history.AssociationMemento;
import construction.history.ComponentMemento;
import domain.components.Component;
import domain.geometry.Point;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import visualization.componentIcons.AssociationIcon;
import visualization.componentIcons.ComponentIconCreator;

import java.util.UUID;

public class Association implements Selectable {

    private UUID id;

    // strings displayed inside the association
    private String label;
    private String subLabel = "";
    private String acronym = "ASC";

    // the javaFX node displayed on screen
    private AssociationIcon associationIcon;

    // first time constructor
    public Association(Point position, double width, double height, int n) {
        this.id = UUID.randomUUID();
        this.label = "Association " + n;
        createAssociationIcon(position, width, height);
    }

    // memento constructor (restore from snapshot)
    public Association(String id, String label, String subLabel, String acronym,
                       Point pos, double width, double height, Point labelPos) {
        this.id = UUID.fromString(id);
        this.label = label;
        this.subLabel = subLabel;
        this.acronym = acronym;
        createAssociationIcon(pos, width, height);
        associationIcon.setTextPosition(labelPos);
        associationIcon.showHandles(false);
    }

    // JSON Node Constructor (for restoring from file)
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
        this.associationIcon = ComponentIconCreator.getAssociationNode(position, width, height, label, id);
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

    public AssociationMemento makeSnapshot() {
        Rectangle rect = getAssociationIcon().getRect();
        Text text = getAssociationIcon().getText();
        return new AssociationSnapshot(id.toString(), label, subLabel, acronym, new Point(rect.getX(), rect.getY()),
                rect.getWidth(), rect.getHeight(), new Point(text.getTranslateX(), text.getTranslateY()));
    }
}

class AssociationSnapshot implements AssociationMemento {
    private String id;
    private String label;
    private String subLabel;
    private String acronym;
    private Point position;
    private double width;
    private double height;
    private Point labelPos;

    public AssociationSnapshot(String id, String label, String sublabel, String acronym,
                               Point pos, double width, double height, Point labelPos) {
        this.id = id;
        this.label = label;
        this.subLabel = sublabel;
        this.acronym = acronym;
        this.position = pos;
        this.width = width;
        this.height = height;
        this.labelPos = labelPos;
    }

    public Association getAssociation() {
        return new Association(id, label, subLabel, acronym, position, width, height, labelPos);
    }
}
