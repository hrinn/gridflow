package construction;

import construction.builder.GridBuilderController;
import construction.properties.PropertiesData;
import construction.properties.PropertiesManager;
import construction.properties.PropertiesObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Translate;
import security.Access;

import java.io.*;

enum MenuState {
    MenuCollapsed, MenuExpanded
}

public class BuildMenuViewController implements PropertiesObserver {

    private static final String ARROW_RIGHT_PATH = "resources/ArrowRight.png";
    private static final String ARROW_LEFT_PATH = "resources/ArrowLeft.png";
    private static final int MENU_EXPANDED_HEIGHT = 600;
    private static final int MENU_EXPANDED_WIDTH = 225;
    private static final int MENU_COLLAPSED_HEIGHT = 300;
    private static final int MENU_COLLAPSED_WIDTH = 25;


    private MenuState state = MenuState.MenuCollapsed;

    private ConstructionController constructionController;
    private GridBuilderController gridBuilderController;
    private PropertiesData Properties;
    private boolean namePosChanged;
    private boolean defaultStateChanged;
    private ChangeListener<Boolean> defStateListener;
    private ChangeListener<Boolean> namePosListener;

    // variables for the different property windows
    private VBox ComponentFieldNames;
    private VBox ComponentFields;
    private VBox AssociationFieldNames;
    private VBox AssociationFields;
    private VBox SelectedFieldNames;
    private VBox SelectedFields;
    private Label clearTandemLabel;
    private Button clearTandemField;
    private Label TandemLabel;
    private Button TandemField;

    @FXML
    private Button InteractToolButton;

    @FXML
    private Button SelectionToolButton;

    @FXML
    private Button AssociationToolButton;

    @FXML
    private Button ComponentMenuButton;

    @FXML
    private HBox ComponentBuilderMenu;

    @FXML
    private VBox VerticalMenuContainer;

    @FXML
    private ImageView ArrowImage;

    @FXML
    private HBox ComponentMenu;

    @FXML
    private BorderPane PropertiesWindow;

    @FXML
    private void initialize() {
        this.Properties = new PropertiesData();
        this.namePosChanged = false;
        this.defaultStateChanged = false;

        // Bind the managed and visible properties for the component menu and properties window.
        // Allows the windows to be hidden through node visibility.
        ComponentMenu.managedProperty().bind(ComponentMenu.visibleProperty());
        PropertiesWindow.managedProperty().bind(PropertiesWindow.visibleProperty());
        ComponentMenu.setVisible(state != MenuState.MenuCollapsed);
        PropertiesWindow.setVisible(state != MenuState.MenuCollapsed);
        // Setup observer for properties data
        PropertiesManager.attach(this);
        // Pre-generate the different windows
        generatePropertyElements();
    }

    public void setConstructionAndBuildController(ConstructionController constructionController) {
        this.constructionController = constructionController;
        this.gridBuilderController = constructionController.getGridBuilderController();
    }

    /* Permanently hides the build menu based on permission level */
    public void setPermissions(Access access) {
        if (access == Access.VIEWER) {
            VerticalMenuContainer.setVisible(false);
        }
    }

    @Override
    public void updateProperties(PropertiesData properties) {
        // received data from somewhere else, update the properties variable and set the window
        this.Properties = new PropertiesData(properties.getType(), properties.getID(), properties.getName(),
                properties.getDefaultState(), properties.getRotation(), properties.getNumSelected(),
                properties.getNamePos(), properties.getAssociation(), properties.getAssocLabel(),
                properties.getAssocSubLabel(), properties.getAssocAcronym(),
                properties.getTandemComponents());

        setPropertiesWindow();
    }

    private void setPropertiesAndUpdate (ComponentType type) {
        Properties.setDefaultProperties(type);
        PropertiesManager.notifyObservers(this.Properties);
        // Notify ghostmanager of changes and update the window
        constructionController.notifyGhostController(true, true);
        setComponentPropertiesWindow();
    }

    public void highlightInteractTool() { InteractToolButton.requestFocus(); }

    public void highlightSelectTool() { SelectionToolButton.requestFocus(); }

    public void highlightAssociationTool() { AssociationToolButton.requestFocus(); }

    // Tool Selection

    @FXML
    private void pickInteractTool() {
        constructionController.setBuildMenuData(ToolType.INTERACT, null);
        hideProperties();
    }

    @FXML
    private void pickSelectTool() {
        constructionController.setBuildMenuData(ToolType.SELECT, null);
    }

    @FXML
    private void pickWireTool() {
        constructionController.setBuildMenuData(ToolType.WIRE, ComponentType.WIRE);
        setPropertiesAndUpdate(ComponentType.WIRE);
    }

    @FXML
    private void pickPlacePowerSourceTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.POWER_SOURCE);
        setPropertiesAndUpdate(ComponentType.POWER_SOURCE);
    }

    @FXML
    private void pickPlaceTurbineTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.TURBINE);
        setPropertiesAndUpdate(ComponentType.TURBINE);
    }

    @FXML
    private void pickPlaceSwitchTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.SWITCH);
        setPropertiesAndUpdate(ComponentType.SWITCH);
    }

    @FXML
    private void pickPlaceBreaker70KVTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.BREAKER_70KV);
        setPropertiesAndUpdate(ComponentType.BREAKER_70KV);
    }

    @FXML
    private void pickPlaceBreaker12KVTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.BREAKER_12KV);
        setPropertiesAndUpdate(ComponentType.BREAKER_12KV);
    }

    @FXML
    private void pickPlaceTransformerTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.TRANSFORMER);
        setPropertiesAndUpdate(ComponentType.TRANSFORMER);
    }

    @FXML
    private void pickPlaceJumperTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.JUMPER);
        setPropertiesAndUpdate(ComponentType.JUMPER);
    }

    @FXML
    private void pickPlaceCutoutTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.CUTOUT);
        setPropertiesAndUpdate(ComponentType.CUTOUT);
    }

    @FXML
    private void pickAssociationTool() {
        constructionController.setBuildMenuData(ToolType.ASSOCIATION, null);
    }

    // UI Control

    @FXML
    private void darkenMenuButtonOnHover() {
        ComponentMenuButton.setStyle("-fx-background-color: #737373");
    }

    @FXML
    private void lightenMenuButtonOnHover() {
        ComponentMenuButton.setStyle("-fx-background-color: #b0b0b0");
    }


    @FXML
    private void toggleMenu() {

        if (state == MenuState.MenuExpanded) {
            CollapseMenu();
            ComponentBuilderMenu.setPrefHeight(MENU_COLLAPSED_HEIGHT);
            ComponentBuilderMenu.setPrefWidth(MENU_COLLAPSED_WIDTH);

            state = MenuState.MenuCollapsed;

        } else {

            ComponentBuilderMenu.setPrefHeight(MENU_EXPANDED_HEIGHT);
            ComponentBuilderMenu.setPrefWidth(MENU_EXPANDED_WIDTH);
            ExpandMenu();

            state = MenuState.MenuExpanded;
        }

        SetMenuButtonImage();
    }

    private void CollapseMenu(){
        ComponentMenu.setVisible(false);
        PropertiesWindow.setVisible(false);
        constructionController.setBuildMenuData(ToolType.INTERACT, null);
        constructionController.getCanvasFacade().showBackgroundGrid(false);
    }

    private void ExpandMenu(){
        ComponentMenu.setVisible(true);
        PropertiesWindow.setVisible(true);
        constructionController.getCanvasFacade().showBackgroundGrid(true);
    }

    private void SetMenuButtonImage(){
        String img = state == MenuState.MenuCollapsed ? ARROW_RIGHT_PATH : ARROW_LEFT_PATH;
        ArrowImage.setImage(new Image(img));
    }

    // Properties Window Functions

    private void hideProperties() {
        PropertiesWindow.setLeft(null);
        PropertiesWindow.setCenter(null);
    }

    public void setPropertiesWindow() {
        if (Properties.getNumSelected() == 0) {
            hideProperties();

        } else if (Properties.getNumSelected() == 1) {
            if (!Properties.getAssociation()) {
                setComponentPropertiesWindow();

            } else {
                setAssociationPropertiesWindow();
            }
        } else {
            setSelectionPropertiesWindow();
        }
    }

    private void setAssociationPropertiesWindow() {
        // set the window fields before adding to view
        for (Node node : AssociationFields.getChildren()) {
            if (node != null){
                if (node.getId().equals("labelField")){
                    ((TextArea)node).setText(Properties.getAssocLabel());
                }
                else if (node.getId().equals("subLabelField")) {
                    ((TextArea)node).setText(Properties.getAssocSubLabel());
                }
                else if (node.getId().equals("acronymField")) {
                    ((TextArea)node).setText(Properties.getAssocAcronym());
                }
            }
        }

        // Show window
        PropertiesWindow.setLeft(AssociationFieldNames);
        PropertiesWindow.setCenter(AssociationFields);
    }

    private void setSelectionPropertiesWindow() {
        for (Node node : SelectedFields.getChildren()) {
            if (node != null) {
                if (node.getId().equals("selectedField")){
                    ((Label)node).setText(Integer.toString(Properties.getNumSelected()));

                }
            }
        }

        SelectedFields.getChildren().remove(TandemField);
        SelectedFieldNames.getChildren().remove(TandemLabel);

        // if numselected == 2 and both selectables are the same breakers, set the two new fields for setting them tandem-able
        if (Properties.getNumSelected() == 2 && Properties.getTandemComponents().size() == 2) {
            // Assuming all the necessary checking has been performed in Selection Manager,
            // set the new nodes into the selection window
            SelectedFieldNames.getChildren().add(TandemLabel);
            SelectedFields.getChildren().add(TandemField);
        }

        // Show window
        PropertiesWindow.setLeft(SelectedFieldNames);
        PropertiesWindow.setCenter(SelectedFields);
    }

    private void setComponentPropertiesWindow() {
        Node applyButton = null;
        // set the window fields before adding to view
        for (Node node : ComponentFields.getChildren()) {
            if (node != null){
                if (node.getId().equals("typeField")){
                    if (Properties.getType() == null) {
                        ((Label)node).setText("");
                    } else {
                        ((Label)node).setText(Properties.getType().toString());
                    }
                }
                else if (node.getId().equals("nameField")) {
                    ((TextArea)node).setText(Properties.getName());
                }
                else if (node.getId().equals("stateField")) {
                    ((RadioButton)node).selectedProperty().removeListener(defStateListener);
                    ((RadioButton)node).setSelected(!Properties.getDefaultState());
                    ((RadioButton)node).selectedProperty().addListener(defStateListener);
                }
                else if (node.getId().equals("namePosField")) {
                    ((RadioButton)node).selectedProperty().removeListener(namePosListener);
                    ((RadioButton)node).setSelected(Properties.getNamePos());
                    ((RadioButton)node).selectedProperty().addListener(namePosListener);
                } else if (node.getId().equals("applyComponentProperties")) {
                    applyButton = node;
                }
            }
        }

        ComponentFieldNames.getChildren().remove(clearTandemLabel);
        ComponentFields.getChildren().remove(clearTandemField);

        // if the single comp is a breaker, add the field and button to unlink its tandem
        if (gridBuilderController.isBreaker(Properties.getID())) {
            if (applyButton != null) {
                ComponentFields.getChildren().remove(applyButton);

                // remove apply button, add fields/labels, add apply button back
                ComponentFieldNames.getChildren().add(clearTandemLabel);
                ComponentFields.getChildren().add(clearTandemField);

                // always add the apply button back
                ComponentFields.getChildren().add(applyButton);
            }
        }

        // Show window
        PropertiesWindow.setLeft(ComponentFieldNames);
        PropertiesWindow.setCenter(ComponentFields);
    }


    private void generatePropertyElements() {
        initComponentProperties();
        initSelectionProperties();
        initAssociationProperties();
    }

    private void initComponentProperties() {
        Translate stateButtonLabelTrans = new Translate();
        stateButtonLabelTrans.setY(2);
        Translate namePosButtonLabelTrans = new Translate();
        namePosButtonLabelTrans.setY(2);
        Translate clearTandemButtonLabelTrans = new Translate();
        clearTandemButtonLabelTrans.setY(2);

        Label type = new Label("Type");
        type.getStyleClass().add("field-label");

        Label name = new Label("Name");
        name.getStyleClass().addAll("field-label-top");

        Label state = new Label("Def State");
        state.getTransforms().add(stateButtonLabelTrans);
        state.getStyleClass().addAll("field-label-top");

        Label namePos = new Label("NamePos");
        namePos.getTransforms().add(namePosButtonLabelTrans);
        namePos.getStyleClass().addAll("field-label-top");

        // Set this up to be added later if a breaker is selected
        clearTandemLabel = new Label("ClearTand");
        clearTandemLabel.getTransforms().add(clearTandemButtonLabelTrans);
        clearTandemLabel.getStyleClass().addAll("field-label-top");

        ComponentFieldNames = new VBox(10, type, name, state, namePos);
        ComponentFieldNames.getStyleClass().add("field-name-container");

        Label typeField = new Label();
        typeField.setId("typeField");
        typeField.getStyleClass().addAll("field", "field_text");

        TextArea nameField = new TextArea();
        nameField.setId("nameField");
        nameField.getStyleClass().addAll("field");
        nameField.focusedProperty().addListener((observableValue, aBoolean, textFieldActive) -> {
            if (textFieldActive){
                constructionController.setUserTyping(true);
            } else {
                constructionController.setUserTyping(false);
            }
        });

        RadioButton stateField = new RadioButton();
        stateField.setId("stateField");
        stateField.setTooltip(new Tooltip("Change Default State"));
        stateField.getStyleClass().addAll("field");
        defStateListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean isSelected) {
                if (isSelected) {
                    // change/update properties
                    Properties.setDefaultState(false);
                } else {
                    Properties.setDefaultState(true);
                }

                defaultStateChanged = true;
            }
        };
        stateField.selectedProperty().addListener(defStateListener);


        RadioButton namePosField = new RadioButton();
        namePosField.setId("namePosField");
        namePosField.setTooltip(new Tooltip("Invert Label Pos"));
        namePosField.getStyleClass().addAll("field");
        namePosListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean isSelected) {
                if (isSelected) {
                    // change/update properties
                    Properties.setNamePos(true);
                } else {
                    Properties.setNamePos(false);
                }

                namePosChanged = true;
            }
        };
        namePosField.selectedProperty().addListener(namePosListener);

//        namePosField.selectedProperty().addListener((observableValue, aBoolean, isSelected) -> {
//            if (isSelected) {
//                // change/update properties
//                Properties.setNamePos(true);
//            } else {
//                Properties.setNamePos(false);
//            }
//
//            namePosChanged = true;
//        });

        Button applyButton = new Button("Apply");
        applyButton.setId("applyComponentProperties");
        applyButton.setTooltip(new Tooltip("Apply Name/State/Pos"));
        applyButton.getStyleClass().addAll("field", "apply-button");
        applyButton.setOnAction(actionEvent -> {
            if (nameField.getText() != null) {
                // set component name in properties and update
                Properties.setName(nameField.getText());
                PropertiesManager.notifyObservers(this.Properties);
            }

            if (defaultStateChanged) {
                PropertiesManager.notifyObservers(this.Properties);
                constructionController.notifyGhostController(false, true);
                gridBuilderController.applyProperties(this.Properties);
                defaultStateChanged = false;
            }

            if (namePosChanged) {
                PropertiesManager.notifyObservers(this.Properties);
                namePosChanged = false;
            }

        });

        // set up clear tandem button for later
        clearTandemField = new Button("Clear");
        clearTandemField.setId("clearTandemField");
        clearTandemField.setTooltip(new Tooltip("Remove Tandem Link"));
        clearTandemField.getStyleClass().addAll("field", "apply-button");
        clearTandemField.setOnAction(actionEvent -> {
            // get the component via properties, call unlink on that single breaker
            gridBuilderController.unlinkTandemByID(Properties.getID());
        });

        ComponentFields = new VBox(10, typeField, nameField, stateField, namePosField, applyButton);
        ComponentFields.getStyleClass().add("field-container");
    }

    private void initSelectionProperties() {
        // Declare the tandem fields for later
        TandemLabel = new Label("Set Tandem");
        TandemLabel.setId("tandemLabel");
        TandemLabel.getStyleClass().add("tandem_label");
        TandemLabel.setAlignment(Pos.CENTER_RIGHT);

        TandemField = new Button("Link");
        TandemField.setId("tandemField");
        TandemField.setTooltip(new Tooltip("Set Breakers Tandem"));
        TandemField.getStyleClass().addAll("field");
        TandemField.setOnAction(actionEvent -> {
            gridBuilderController.linkTandems(Properties.getTandemComponents());
        });

        Label numSelected = new Label("Number Selected:");
        numSelected.getStyleClass().add("selected_field_label");

        Label selectedField = new Label("");
        selectedField.setId("selectedField");
        selectedField.getStyleClass().addAll("selected_field_text");

        SelectedFieldNames = new VBox(numSelected);

        SelectedFields = new VBox(selectedField);
    }

    private void initAssociationProperties() {
        Translate acronymLabelTrans = new Translate();
        acronymLabelTrans.setY(6);

        Label label = new Label("Label");
        label.getStyleClass().addAll("field-label-top");

        Label acronym = new Label("Acronym");
        acronym.getTransforms().add(acronymLabelTrans);
        acronym.getStyleClass().addAll( "field-label-top");

        AssociationFieldNames = new VBox(10, label, acronym);
        AssociationFieldNames.getStyleClass().add("field-name-container");

        TextArea labelField = new TextArea();
        labelField.setId("labelField");
        labelField.getStyleClass().addAll("field-text-area");
        labelField.focusedProperty().addListener((observableValue, aBoolean, textFieldActive) -> {
            if (textFieldActive){
                constructionController.setUserTyping(true);
            } else {
                constructionController.setUserTyping(false);
            }
        });

        TextArea acronymField = new TextArea();
        acronymField.setId("acronymField");
        acronymField.getStyleClass().addAll( "field-text-area");
        acronymField.focusedProperty().addListener((observableValue, aBoolean, textFieldActive) -> {
            if (textFieldActive){
                constructionController.setUserTyping(true);
            } else {
                constructionController.setUserTyping(false);
            }
        });

        Button applyButton = new Button("Apply");
        applyButton.setId("applyAssociationProperties");
        applyButton.setTooltip(new Tooltip("Apply Label/Acronym"));
        applyButton.getStyleClass().addAll("field", "apply-button");
        applyButton.setOnAction(actionEvent -> {
            if (labelField.getText() != null) {
                Properties.setAssocLabel(labelField.getText());
            }

            if (acronymField.getText() != null) {
                Properties.setAssocAcronym(acronymField.getText());
            }

            PropertiesManager.notifyObservers(this.Properties);
        });

        AssociationFields = new VBox(10, labelField, acronymField, applyButton);
        AssociationFields.getStyleClass().add("field-container");
    }
}

