package construction;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Translate;
import security.Access;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

enum MenuState {
    MenuCollapsed, MenuExpanded
}

public class BuildMenuViewController {

    private static final String ARROW_RIGHT_PATH = "src/resources/ArrowRight.png";
    private static final String ARROW_LEFT_PATH = "src/resources/ArrowLeft.png";
    private static final int MENU_EXPANDED_HEIGHT = 600;
    private static final int MENU_EXPANDED_WIDTH = 225;
    private static final int MENU_COLLAPSED_HEIGHT = 300;
    private static final int MENU_COLLAPSED_WIDTH = 25;
    private static final int SHIFT_MENU_UP = -150;
    private static final int SHIFT_MENU_DOWN = 150;

    public Button interactToolButton;

    private MenuState state = MenuState.MenuCollapsed;

    private ConstructionController constructionController;
    private PropertiesData sharedProperties;

    // variables for the different property windows
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
        // Bind the managed and visible properties for the component menu and properties window
        ComponentMenu.managedProperty().bind(ComponentMenu.visibleProperty());
        PropertiesWindow.managedProperty().bind(PropertiesWindow.visibleProperty());
        ComponentMenu.setVisible(state != MenuState.MenuCollapsed);
        // Pre-generate the different windows
        generatePropertyElements();
    }

    public void setConstructionController(ConstructionController constructionController) {
        this.constructionController = constructionController;
        this.sharedProperties = constructionController.propertiesData;
    }

    // TODO: Change this to the root node on Connor's branch
    /* Permanently hides the build menu based on permission level */
    public void setPermissions(Access access) {
        if (access == Access.VIEWER) {
            ComponentBuilderMenu.setVisible(false);
        }
    }

    @FXML
    public void ConstructionViewController(){ }

    public void highlightInteractTool() {
        
    }

    // Tool Selection

    @FXML
    private void pickInteractTool() {
        // At this point, the tool is selected. Update the properties window to show default now
        constructionController.setBuildMenuData(ToolType.INTERACT, null);
    }

    @FXML
    private void pickSelectTool() {
        constructionController.setBuildMenuData(ToolType.SELECT, null);
    }

    @FXML
    private void pickWireTool() {
        constructionController.setBuildMenuData(ToolType.WIRE, ComponentType.WIRE);
    }

    @FXML
    private void pickPlacePowerSourceTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.POWER_SOURCE);
        // Whenever a component is selected, first display the default properties for that component
        sharedProperties.setDefaultProperties(ComponentType.POWER_SOURCE);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceTurbineTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.TURBINE);
        sharedProperties.setDefaultProperties(ComponentType.TURBINE);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceSwitchTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.SWITCH);
        sharedProperties.setDefaultProperties(ComponentType.SWITCH);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceBreaker70KVTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.BREAKER_70KV);
        sharedProperties.setDefaultProperties(ComponentType.BREAKER_70KV);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceBreaker12KVTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.BREAKER_12KV);
        sharedProperties.setDefaultProperties(ComponentType.BREAKER_12KV);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceTransformerTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.TRANSFORMER);
        sharedProperties.setDefaultProperties(ComponentType.TRANSFORMER);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceJumperTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.JUMPER);
        sharedProperties.setDefaultProperties(ComponentType.JUMPER);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickPlaceCutoutTool() {
        constructionController.setBuildMenuData(ToolType.PLACE, ComponentType.CUTOUT);
        sharedProperties.setDefaultProperties(ComponentType.CUTOUT);
        setComponentPropertiesWindow();
    }

    @FXML
    private void pickAssociationTool() {
        constructionController.setBuildMenuData(ToolType.ASSOCIATION, null);
    }

    // UI Control

    @FXML
    private void darkenMenuButtonOnHover(){
        ComponentMenuButton.setStyle("-fx-background-color: #737373");
    }

    @FXML
    private void lightenMenuButtonOnHover(){
        ComponentMenuButton.setStyle("-fx-background-color: #b0b0b0");
    }


    @FXML
    private void toggleMenu() {
        Translate menuButtonTrans = new Translate();

        if (state == MenuState.MenuExpanded){
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

        SetMenuButtonImage(ArrowImage);
        ComponentBuilderMenu.getTransforms().add(menuButtonTrans);
    }

//    private void setPropertiesWindow(ToolType type, ComponentType compType, String name, double rotation, boolean state){
//
//
//    }

    private void setComponentPropertiesWindow(){
        // set the window fields before adding to view
        for (Node node : ComponentFields.getChildren()) {
            if (node.getId().equals("typeField")){
                ((TextField)node).setText(sharedProperties.getType().toString());
            } else if (node.getId().equals("nameField")) {
                ((TextField)node).setText(sharedProperties.getName());
            } else if (node.getId().equals("stateField")) {
                if (!sharedProperties.getDefaultState()) {
                    ((RadioButton)node).setSelected(true);
                }
            }
        }

        // Show window
        PropertiesWindow.setLeft(ComponentFieldNames);
        PropertiesWindow.setCenter(ComponentFields);
    }


    private void generatePropertyElements(){

        initComponentProperties();
        initSelectionProperties();
    }

    private void initComponentProperties(){
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
        typeField.getStyleClass().add("component-field");

        TextField nameField = new TextField();
        nameField.setId("nameField");
        nameField.getStyleClass().add("component-field");

        RadioButton stateField = new RadioButton();
        stateField.setId("stateField");
        stateField.getStyleClass().add("component-field");

        ComponentFields = new VBox(10, typeField, nameField, stateField);
        ComponentFields.getStyleClass().add("field-container");

    }


    private void initSelectionProperties(){
        Label numSelected = new Label("Selected");
        numSelected.getStyleClass().add("field-label");

        TextField selectedField = new TextField();
        selectedField.setEditable(false);
        selectedField.setFocusTraversable(false);
        selectedField.getStyleClass().add("component-field");

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

