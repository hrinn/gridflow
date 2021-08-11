package construction.properties;

import construction.ComponentType;
import construction.properties.objectData.*;
import construction.selector.observable.Observer;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class PropertiesMenuViewController implements Observer<String>, Visitor {

    private List<String> selectedIDs;

    private PropertiesMenuFunctions propertiesMenuFunctions;

    private ObjectData currentObjectData = null;

    // JavaFX Elements
    public AnchorPane PropertiesWindow;

    public HBox nameControl;
    public HBox namePosControl;
    public HBox defaultStateControl;
    public HBox labelControl;
    public HBox subLabelControl;
    public HBox acronymControl;
    public HBox linkBreakersControl;
    public HBox clearLinkedControl;

    public TextField nameField;
    public Label numSelectedLabel;
    public TextField labelField;
    public TextField subLabelField;
    public TextField acronymField;
    public Label numSelectedToClearLabel;
    public Button clearLinkedButton;

    public ToggleButton toggleLeft;
    public ToggleButton toggleRight;
    public ToggleButton toggleOpen;
    public ToggleButton toggleClosed;

    public Button applyButton;

    ToggleGroup namePosTG = new ToggleGroup();
    ToggleGroup defStateTG = new ToggleGroup();

    public void initialize() {
        PropertiesWindow.managedProperty().bind(PropertiesWindow.visibleProperty());
        nameControl.managedProperty().bind(nameControl.visibleProperty());
        namePosControl.managedProperty().bind(namePosControl.visibleProperty());
        defaultStateControl.managedProperty().bind(defaultStateControl.visibleProperty());
        labelControl.managedProperty().bind(labelControl.visibleProperty());
        subLabelControl.managedProperty().bind(subLabelControl.visibleProperty());
        acronymControl.managedProperty().bind(acronymControl.visibleProperty());
        linkBreakersControl.managedProperty().bind(linkBreakersControl.visibleProperty());
        clearLinkedControl.managedProperty().bind(clearLinkedControl.visibleProperty());

        PropertiesWindow.setVisible(false);
        hideAllControls();

        toggleLeft.setToggleGroup(namePosTG);
        toggleRight.setToggleGroup(namePosTG);

        toggleOpen.setToggleGroup(defStateTG);
        toggleClosed.setToggleGroup(defStateTG);
    }

    private void hideAllControls() {
        nameControl.setVisible(false);
        namePosControl.setVisible(false);
        defaultStateControl.setVisible(false);
        labelControl.setVisible(false);
        subLabelControl.setVisible(false);
        acronymControl.setVisible(false);
        linkBreakersControl.setVisible(false);
        clearLinkedControl.setVisible(false);
        applyButton.setVisible(false);
    }

    @Override
    public void onListUpdate(List<String> newList) {
        selectedIDs = newList;
        setupMenu();
    }

    private void setupMenu() {
        hideAllControls();

        if (selectedIDs.size() == 2 && allSelectedItemsAreBreakers()) {
            setLinkBreakersMenu();
            return;
        }
        if (selectedIDs.size() != 1) return;
        if (!PropertiesWindow.isVisible()) PropertiesWindow.setVisible(true);

        // Get the data of the selected object
        ObjectData objectData = propertiesMenuFunctions.getObjectData(selectedIDs.get(0));
        if (objectData == null) {
            System.err.println("Selected ID did not match a component");
            return;
        }
        applyButton.setVisible(true);

        // Determine which menu to display
        // This runs the correct setMenu() function depending on the object
        // This is the Visitor pattern / Double Dispatch
        objectData.accept(this);
    }

    private boolean allSelectedItemsAreBreakers() {
        for (String ID : selectedIDs) {
            ComponentType type = propertiesMenuFunctions.getComponentType(ID);
            if (type != ComponentType.BREAKER_12KV && type != ComponentType.BREAKER_70KV) {
                return false;
            }
        }
        return true;
    }

    private void orientRightLeft(boolean isVertical) {
        if (isVertical) {
            toggleRight.setText("Right");
            toggleLeft.setText("Left");
        } else {
            toggleRight.setText("Top");
            toggleLeft.setText("Bottom");
        }
    }

    public void setComponentMenu(ComponentData data) {
        currentObjectData = data;

        // Show correct controls
        nameControl.setVisible(true);
        namePosControl.setVisible(true);

        // Fill in data
        nameField.setText(data.getName());
        orientRightLeft(data.isVertical());
        namePosTG.selectToggle(data.isNameRight() ? toggleRight : toggleLeft);
    }

    public void setCloseableMenu(CloseableData data) {
        currentObjectData = data;

        // Show correct controls
        nameControl.setVisible(true);
        namePosControl.setVisible(true);
        defaultStateControl.setVisible(true);

        // Fill in data
        nameField.setText(data.getName());
        defStateTG.selectToggle(data.isClosed() ? toggleClosed : toggleOpen);
        orientRightLeft(data.isVertical());
        namePosTG.selectToggle(data.isNameRight() ? toggleRight : toggleLeft);
    }

    public void setBreakerMenu(BreakerData data) {
        currentObjectData = data;

        // Show correct controls
        nameControl.setVisible(true);
        namePosControl.setVisible(true);
        defaultStateControl.setVisible(true);
        clearLinkedControl.setVisible(true);

        // Fill in data
        nameField.setText(data.getName());
        defStateTG.selectToggle(data.isClosed() ? toggleClosed : toggleOpen);
        orientRightLeft(data.isVertical());
        namePosTG.selectToggle(data.isNameRight() ? toggleRight : toggleLeft);

        if (data.getTandemID() == null) {
            numSelectedToClearLabel.setText("0");
            clearLinkedButton.setDisable(true);
        } else {
            numSelectedToClearLabel.setText("1");
            clearLinkedButton.setDisable(false);
        }
    }

    public void setAssociationMenu(AssociationData data) {
        currentObjectData = data;

        // Show correct controls
        labelControl.setVisible(true);
        subLabelControl.setVisible(true);
        acronymControl.setVisible(true);
    }

    public void setSourceMenu(SourceData data) {
        currentObjectData = data;

        // Show correct controls
        nameControl.setVisible(true);

        // Fill in data
        nameField.setText(data.getName());
    }

    private void setLinkBreakersMenu() {
        // Show correct controls
        linkBreakersControl.setVisible(true);

        // Fill in data
        numSelectedLabel.setText("" + selectedIDs.size());
    }

    public void applyProperties() {
        // Gather settings
        String name = nameField.getText();
        boolean nameRight = namePosTG.getSelectedToggle() == toggleRight;
        boolean isClosed = defStateTG.getSelectedToggle() == toggleClosed;
        String label = labelField.getText();
        String subLabel = subLabelField.getText();
        String acronym = acronymField.getText();

        // Modify the object data
        ObjectData newData = currentObjectData.applySettings(name, nameRight, isClosed, label, subLabel, acronym);

        // Send it to construction controller for application
        propertiesMenuFunctions.setObjectData(selectedIDs.get(0), newData);
    }

    public void linkBreakers() {
        propertiesMenuFunctions.linkBreakers(List.of(selectedIDs.get(0), selectedIDs.get(1)));
    }

    public void clearTandem() {
        propertiesMenuFunctions.clearTandem(selectedIDs.get(0));
        clearLinkedButton.setDisable(true);
        numSelectedToClearLabel.setText("0");
    }

    public void setPropertiesWindowVisibility(boolean visible) {
        PropertiesWindow.setVisible(visible);
    }

    public void setPropertiesMenuFunctions(PropertiesMenuFunctions pmf) {
        this.propertiesMenuFunctions = pmf;
    }

}
