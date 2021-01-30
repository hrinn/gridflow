package domain;

import domain.geometry.Point;
import javafx.scene.Group;
import visualization.componentIcons.ComponentIconCreator;

public class Association {

    // strings displayed inside the association
    private String acronym = "";
    private String label = "Association";
    private String subLabel = "";

    // association dimensions
    private Point position; // top left
    private double width;
    private double height;

    // the javaFX node displayed on screen
    private Group associationNode;

    public Association(Point position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
        createAssociationNode();
    }

    private void createAssociationNode() {
        this.associationNode = ComponentIconCreator.createAssociationNode(position, width, height);
    }

    public Group getAssociationNode() {
        return associationNode;
    }
}
