package domain;

import domain.geometry.Point;
import javafx.scene.Group;
import visualization.componentIcons.ComponentIconCreator;

public class Association {

    private String acronym = "";
    private String label = "Association";
    private String subLabel = "";

    private Point position; // top left
    private double width;
    private double height;

    private Group associationNode;

    public Association(Point position, double width, double height) {
        this.position = position;
        this.width = width;
        this.height = height;
        createAssociationNode();
    }

    private void createAssociationNode() {
        this.associationNode = ComponentIconCreator.getAssociationNode(position, width, height);
    }

    public Group getAssociationNode() {
        return associationNode;
    }
}
