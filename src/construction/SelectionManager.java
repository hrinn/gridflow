package construction;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class SelectionManager {

    private List<String> selectedComponentIDs;
    private Rectangle selectionBox;

    public SelectionManager() {
        selectedComponentIDs = new ArrayList<>();
        selectionBox = new Rectangle();
    }

    public void select(String... IDs) {
        for (String ID : IDs) {

        }
    }
}
