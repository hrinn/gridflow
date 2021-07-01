package construction;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class PropertiesMenuViewController implements ListChangeListener<String> {

    private ObservableList<String> selectedIDs = FXCollections.observableArrayList();

    private boolean menuVisible = false;

    public void initialize() {
    }

    @Override
    public void onChanged(Change<? extends String> change) {
        selectedIDs = (ObservableList<String>) change.getList();
        displayMenu();
    }

    private void displayMenu() {
        if (selectedIDs.size() != 1) return;
    }
}
