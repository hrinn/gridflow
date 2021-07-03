package construction.properties;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;

public class PropertiesMenuViewController implements ListChangeListener<String> {

    private ObservableList<String> selectedIDs = FXCollections.observableArrayList();

    public AnchorPane PropertiesWindow;

    public void initialize() {
        PropertiesWindow.setVisible(false);
    }

    @Override
    public void onChanged(Change<? extends String> change) {
        selectedIDs = (ObservableList<String>) change.getList();
        determineMenuVisibility();
    }

    private void determineMenuVisibility() {
        if (selectedIDs.size() != 1) return;
        if (!PropertiesWindow.isVisible()) PropertiesWindow.setVisible(true);
    }

    public void setPropertiesWindowVisibility(boolean visible) {
        PropertiesWindow.setVisible(visible);
    }
}
