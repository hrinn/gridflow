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

    // label can exist at 9 different positions, 0 being top left and 8 being bottom right
    private int labelPosition = 0;

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
        this.associationNode = ComponentIconCreator.createAssociationNode(position, width, height, label, labelPosition);
    }

    public Group getAssociationNode() {
        return associationNode;
    }
}
