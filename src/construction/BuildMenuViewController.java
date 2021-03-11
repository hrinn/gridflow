package construction;

import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.transform.Translate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

enum MenuState {
    MenuCollapsed, MenuExpanded
}

enum PropertyState {
    Component, Selection, Association
}

public class BuildMenuViewController implements PropertiesObserver{

    private static final String ARROW_RIGHT_PATH = "src/resources/ArrowRight.png";
    private static final String ARROW_LEFT_PATH = "src/resources/ArrowLeft.png";
    private static final int MENU_EXPANDED_HEIGHT = 600;
    private static final int MENU_EXPANDED_WIDTH = 225;
    private static final int MENU_COLLAPSED_HEIGHT = 300;
    private static final int MENU_COLLAPSED_WIDTH = 25;
    private static final int SHIFT_MENU_UP = -150;
    private static final int SHIFT_MENU_DOWN = 150;

    private MenuState state = MenuState.MenuExpanded;

    private ConstructionController constructionController;
    private PropertiesData Properties;
    private boolean defaultStateChanged;

    // variables for the different property windows
    private PropertyState propertyWindow;
    private VBox ComponentFieldNames;
    private VBox ComponentFields;
    private VBox AssociationFieldNames;
    private VBox AssociationFields;
    private VBox SelectedFieldNames;
    private VBox SelectedFields;


    @FXML
    private Button ComponentMenuButton;

    @FXML
    private HBox ComponentBuilderMenu;

    @FXML
    private ImageView ArrowImage;

    @FXML
    private HBox ComponentMenu;

    @FXML
    private BorderPane PropertiesWindow;

    @FXML
    private void initialize() {
        this.Properties = new PropertiesData();
        this.defaultStateChanged = false;

        // Bind the managed and visible properties for the component menu and properties window.
        // Allows the windows to be hidden through node visibility.
        ComponentMenu.managedProperty().bind(ComponentMenu.visibleProperty());
        PropertiesWindow.managedProperty().bind(PropertiesWindow.visibleProperty());

        // Setup observer for properties data
        PropertiesManager.attach(this);

        // Pre-generate the different windows
        generatePropertyElements();
    }

    public void setConstructionController(ConstructionController constructionController) {
        this.constructionController = constructionController;
    }

    @Override
    public void updateProperties(PropertiesData properties) {
        // State and rotation may change if the key handlers were pressed, update component window

        // received data from somewhere else, update the properties variable and set the window
        this.Properties = new PropertiesData(properties.getType(), properties.getID(), properties.getName(),
                properties.getDefaultState(), properties.getRotation(), properties.getNumSelected(), properties.getAssociation(),
                properties.getAssocLabel(), properties.getAssocSubLabel(), properties.getAssocAcronym());

        setPropertiesWindow();
    }

    private void setPropertiesAndUpdate (ComponentType type) {
        Properties.setDefaultProperties(type);
        PropertiesManager.notifyObservers(this.Properties);
        // Notify ghostmanager of changes and update the window
        constructionController.notifyGhostController(true, true);
        setComponentPropertiesWindow();
    }

    @FXML
    public void ConstructionViewController(){ }


    // Tool Selection

    @FXML
    private void pickInteractTool() {
        // At this point, the tool is selected. Update the properties window to show default now
        constructionController.setBuildMenuData(ToolType.INTERACT, null);

        //setPropertiesAndUpdate(this.Properties.getType());
        // Instead of supply/changing the toggled properties, set the properties window with default properties
        setDefaultPropertiesWindow();
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
        Translate menuButtonTrans = new Translate();

        if (state == MenuState.MenuExpanded) {
            CollapseMenu();
            ComponentBuilderMenu.setPrefHeight(MENU_COLLAPSED_HEIGHT);
            ComponentBuilderMenu.setPrefWidth(MENU_COLLAPSED_WIDTH);

            //menuButtonTrans.setY(SHIFT_MENU_UP);

            state = MenuState.MenuCollapsed;

        } else {
            //menuButtonTrans.setY(SHIFT_MENU_DOWN);

            ComponentBuilderMenu.setPrefHeight(MENU_EXPANDED_HEIGHT);
            ComponentBuilderMenu.setPrefWidth(MENU_EXPANDED_WIDTH);
            ExpandMenu();

            state = MenuState.MenuExpanded;
        }

        SetMenuButtonImage(ArrowImage);
        ComponentBuilderMenu.getTransforms().add(menuButtonTrans);
    }

    private void setDefaultPropertiesWindow(){
        Properties.setDefaultProperties(null);
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
    }

    private void setPropertiesWindow(){
        // if selected < 2 and association not selected, set compoenent properties window
        if (Properties.getNumSelected() < 2) {
            if (!Properties.getAssociation()) {
                setComponentPropertiesWindow();

            } else {
                // set association window
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

        // Show window
        PropertiesWindow.setLeft(SelectedFieldNames);
        PropertiesWindow.setCenter(SelectedFields);
    }

    private void setComponentPropertiesWindow() {
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
                    ((RadioButton)node).setSelected(!Properties.getDefaultState());
                }
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
        namePosButtonLabelTrans.setY(4);

        Label type = new Label("Type");
        type.getStyleClass().add("field-label");

        Label name = new Label("Name");
        name.getStyleClass().addAll("field-label-top");

        Label state = new Label("State");
        state.getTransforms().add(stateButtonLabelTrans);
        state.getStyleClass().addAll("field-label-top");

        Label namePos = new Label("NamePos");
        namePos.getTransforms().add(namePosButtonLabelTrans);
        namePos.getStyleClass().addAll("field-label-top");

        ComponentFieldNames = new VBox(10, type, name, state, namePos);
        ComponentFieldNames.getStyleClass().add("field-name-container");

        //TextField typeField = new TextField();
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
        stateField.getStyleClass().addAll("field");
        stateField.selectedProperty().addListener((observableValue, aBoolean, isSelected) -> {
            if (isSelected) {
                // change/update properties
                Properties.setDefaultState(false);
            } else {
                Properties.setDefaultState(true);
            }

            defaultStateChanged = true;
        });

        RadioButton namePosField = new RadioButton();
        namePosField.setId("namePosField");
        namePosField.getStyleClass().addAll("field");
        namePosField.selectedProperty().addListener((observableValue, aBoolean, isSelected) -> {
            if (isSelected) {
                // change/update properties
                //Properties.setDefaultState(false);
                // Properties.setNamePos(?midleft???)
            } else {
                //Properties.setDefaultState(true);
                // Properties.setNamePos(?midright???)
            }
            //namePosChanged??
            //defaultStateChanged = true;
        });

        Button applyButton = new Button("Apply");
        applyButton.setId("applyComponentProperties");
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
                defaultStateChanged = false;
            }

//            PropertiesManager.notifyObservers(this.Properties);
        });

        ComponentFields = new VBox(10, typeField, nameField, stateField, namePosField, applyButton);
        ComponentFields.getStyleClass().add("field-container");

    }


    private void initSelectionProperties() {
        Label numSelected = new Label("Number Selected:");
        numSelected.getStyleClass().add("selected_field_label");

        Label selectedField = new Label("");
        selectedField.setId("selectedField");
        selectedField.getStyleClass().addAll("selected_field_text");

        SelectedFieldNames = new VBox(numSelected);
        //SelectedFieldNames.getStyleClass().add("field-name-container");

        SelectedFields = new VBox(selectedField);

    }

    private void initAssociationProperties() {
        Translate subLabelTrans = new Translate();
        subLabelTrans.setY(8);
        Translate acronymLabelTrans = new Translate();
        acronymLabelTrans.setY(16);

        Label label = new Label("Label");
        label.getStyleClass().addAll("field-label-top");

        Label subLabel = new Label("SubLabel");
        subLabel.getTransforms().add(subLabelTrans);
        subLabel.getStyleClass().addAll("field-label-top");

        Label acronym = new Label("Acronym");
        acronym.getTransforms().add(acronymLabelTrans);
        acronym.getStyleClass().addAll( "field-label-top");

        AssociationFieldNames = new VBox(10, label, subLabel, acronym);
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

        TextArea subLabelField = new TextArea();
        subLabelField.setId("subLabelField");
        subLabelField.getStyleClass().addAll( "field-text-area");
        subLabelField.focusedProperty().addListener((observableValue, aBoolean, textFieldActive) -> {
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
        applyButton.getStyleClass().addAll("field", "apply-button");
        applyButton.setOnAction(actionEvent -> {
            // && !nameField.getText().isEmpty()
            if (labelField.getText() != null) {
                Properties.setAssocLabel(labelField.getText());
                PropertiesManager.notifyObservers(this.Properties);
            }

            if (subLabelField.getText() != null) {
                Properties.setAssocSubLabel(subLabelField.getText());
                PropertiesManager.notifyObservers(this.Properties);
            }

            if (acronymField.getText() != null) {
                Properties.setAssocAcronym(acronymField.getText());
                PropertiesManager.notifyObservers(this.Properties);
            }


        });

        AssociationFields = new VBox(10, labelField, subLabelField, acronymField, applyButton);
        AssociationFields.getStyleClass().add("field-container");

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

    private void SetMenuButtonImage(ImageView arrow){
        File img = state == MenuState.MenuCollapsed ? new File(ARROW_RIGHT_PATH) : new File(ARROW_LEFT_PATH);

        InputStream isImage;

        try {
            isImage = new FileInputStream(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        arrow.setImage(new Image(isImage));
    }
}

