package construction;

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
import javafx.scene.transform.Translate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
    private boolean defaultState;

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
        this.defaultState = true;

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
        //this.Properties = constructionController.propertiesData;
    }

    @Override
    public void updateProperties(PropertiesData properties) {
        // received data from somewhere else, update the properties variable and set the window
        this.Properties = new PropertiesData(properties.getType(), properties.getID(), properties.getName(),
                properties.getDefaultState(), properties.getRotation());

        setPropertiesWindow();
    }

    @FXML
    public void ConstructionViewController(){ }

    // Tool Selection

    @FXML
    private void pickInteractTool() {
        // At this point, the tool is selected. Update the properties window to show default now
        constructionController.setBuildMenuData(ToolType.INTERACT, null);
        //Properties.setDefaultProperties(this.Properties.getType());
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickSelectTool() {
        constructionController.setBuildMenuData(ToolType.SELECT, null);
    }

    @FXML
    private void pickWireTool() {

        constructionController.setBuildMenuData(ToolType.WIRE, ComponentType.WIRE);
        Properties.setDefaultComponentProperties(ComponentType.WIRE);
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlacePowerSourceTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.POWER_SOURCE);
        Properties.setDefaultComponentProperties(ComponentType.POWER_SOURCE);
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceTurbineTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.TURBINE);
        Properties.setDefaultComponentProperties(ComponentType.TURBINE);
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceSwitchTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.SWITCH);
        Properties.setDefaultComponentProperties(ComponentType.SWITCH);
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceBreaker70KVTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.BREAKER_70KV);
        Properties.setDefaultComponentProperties(ComponentType.BREAKER_70KV);
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceBreaker12KVTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.BREAKER_12KV);
        Properties.setDefaultComponentProperties(ComponentType.BREAKER_12KV);
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceTransformerTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.TRANSFORMER);
        Properties.setDefaultComponentProperties(ComponentType.TRANSFORMER);
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceJumperTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.JUMPER);
        Properties.setDefaultComponentProperties(ComponentType.JUMPER);
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceCutoutTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.CUTOUT);
        Properties.setDefaultComponentProperties(ComponentType.CUTOUT);
        PropertiesManager.notifyObservers(this.Properties);
        setComponentPropertiesWindow();
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

            menuButtonTrans.setY(SHIFT_MENU_UP);

            state = MenuState.MenuCollapsed;

        } else {
            menuButtonTrans.setY(SHIFT_MENU_DOWN);

            ComponentBuilderMenu.setPrefHeight(MENU_EXPANDED_HEIGHT);
            ComponentBuilderMenu.setPrefWidth(MENU_EXPANDED_WIDTH);
            ExpandMenu();

            state = MenuState.MenuExpanded;
        }

        SetMenuButtonImage(ArrowImage);
        ComponentBuilderMenu.getTransforms().add(menuButtonTrans);
    }

    private void setDefaultPropertiesWindow(){

    }

    private void setPropertiesWindow(){
        // If ID is not default, component is active
        if (!Properties.getID().equals(new UUID(0, 0))) {
            // Component properties, update window
            setComponentPropertiesWindow();
        }

    }

    private void setComponentPropertiesWindow() {
        // set the window fields before adding to view
        for (Node node : ComponentFields.getChildren()) {
            if (node != null){
                if (node.getId().equals("typeField") && Properties.getType() != null){
                    ((TextField)node).setText(Properties.getType().toString());
                }
                else if (node.getId().equals("nameField")) {
                    ((TextField)node).setText(Properties.getName());
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
    }

    private void initComponentProperties() {
        boolean defaultState = true;

        Label type = new Label("Type");
        type.getStyleClass().add("field-label");

        Label name = new Label("Name");
        name.getStyleClass().add("field-label");

        Label state = new Label("State");
        state.getStyleClass().add("field-label");

        ComponentFieldNames = new VBox(10, type, name, state);
        ComponentFieldNames.getStyleClass().add("field-name-container");

        TextField typeField = new TextField();
        typeField.setId("typeField");
        typeField.setEditable(false);
        typeField.setFocusTraversable(false);
        typeField.getStyleClass().add("field");

        TextField nameField = new TextField();
        nameField.setId("nameField");
        nameField.getStyleClass().add("field");
        nameField.focusedProperty().addListener((observableValue, aBoolean, textFieldActive) -> {
            if (textFieldActive){
                System.out.println("User is typing!");
                constructionController.setUserTyping(true);
            } else {
                System.out.println("User has stopped typing!");
                constructionController.setUserTyping(false);
            }
        });

        RadioButton stateField = new RadioButton();
        stateField.setId("stateField");
        stateField.getStyleClass().add("field");
        stateField.selectedProperty().addListener((observableValue, aBoolean, isSelected) -> {
            if (isSelected) {
                // change/update properties
                Properties.setDefaultState(false);
            } else {
                Properties.setDefaultState(true);
            }
            PropertiesManager.notifyObservers(Properties);
        });

        Button applyButton = new Button("Apply");
        applyButton.setId("applyComponentProperties");
        applyButton.getStyleClass().addAll("field", "apply-button");
        applyButton.setOnAction(actionEvent -> {
            // && !nameField.getText().isEmpty()
            if (nameField.getText() != null) {
                // set component name in properties and update
                Properties.setName(nameField.getText());
                PropertiesManager.notifyObservers(Properties);
            }
        });

        ComponentFields = new VBox(10, typeField, nameField, stateField, applyButton);
        ComponentFields.getStyleClass().add("field-container");

    }


    private void initSelectionProperties() {
        Label numSelected = new Label("Selected");
        numSelected.getStyleClass().add("field-label");

        TextField selectedField = new TextField();
        selectedField.setEditable(false);
        selectedField.setFocusTraversable(false);
        selectedField.getStyleClass().add("field");

        SelectedFieldNames = new VBox(numSelected);
        SelectedFieldNames.getStyleClass().add("field-name-container");

        SelectedFields = new VBox(selectedField);

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

