package domain;

import domain.geometry.Point;
import javafx.scene.Group;
import visualization.componentIcons.ComponentIconCreator;

import java.util.UUID;

public class Association {

    private UUID id;

    // strings displayed inside the association
    private String acronym = "ASC";
    private String label = "Association";
    private String subLabel = "";

    // association dimensions
    private Point position; // top left
    private double width;
    private double height;

    // the javaFX node displayed on screen
    private Group associationNode;

    public Association(Point position, double width, double height) {
        this.id = UUID.randomUUID();
        this.position = position;
        this.width = width;
        this.height = height;
        createAssociationNode();
    }

    private void createAssociationNode() {
        this.associationNode = ComponentIconCreator.createAssociationNode(position, width, height, label, id);
    }

    public Group getAssociationNode() {
        return associationNode;
    }

    public UUID getID() {
        return id;
    }

    public Point getTopleft() {
        return position;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
