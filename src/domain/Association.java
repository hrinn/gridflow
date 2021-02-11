package domain;

import domain.geometry.Point;
import javafx.scene.Group;
import visualization.componentIcons.AssociationIcon;
import visualization.componentIcons.IconCreator;

import java.util.UUID;

public class Association implements Selectable {

    private UUID id;

    // strings displayed inside the association
    private String acronym = "ASC";
    private String label = "Association";
    private String subLabel = "";

    // the javaFX node displayed on screen
    private AssociationIcon associationIcon;

    public Association(Point position, double width, double height) {
        this.id = UUID.randomUUID();
        createAssociationIcon(position, width, height);
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
}
