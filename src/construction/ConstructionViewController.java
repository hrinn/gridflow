package construction;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Translate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ConstructionViewController {

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

    @FXML
    private Button ComponentMenuButton;

    @FXML
    private HBox ComponentBuilderMenu;

    @FXML
    private ImageView ArrowImage;

    @FXML
    private HBox ComponentMenu;

    @FXML
    private void initialize() {
        // Bind the managed and visible properties for the component menus
        ComponentMenu.managedProperty().bind(ComponentMenu.visibleProperty());
    }

    public void setConstructionController(ConstructionController constructionController) {
        this.constructionController = constructionController;
    }

    @FXML
    public void ConstructionViewController(){ }

    @FXML
    private void pickSelectTool() {
        constructionController.setCurrentToolType(ToolType.SELECT);
    }

    @FXML
    private void pickPlaceSwitchTool() {
        constructionController.setCurrentToolType(ToolType.PLACE);
        constructionController.setCurrentComponentType(ComponentType.SWITCH);
    }

    @FXML
    private void pickPlaceBreakerTool() {
        constructionController.setCurrentToolType(ToolType.PLACE);
        constructionController.setCurrentComponentType(ComponentType.BREAKER_12KV);
    }

    @FXML
    private void pickWireTool() {
        constructionController.setCurrentToolType(ToolType.WIRE);
        constructionController.setCurrentComponentType(ComponentType.WIRE);
    }

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
            CollapseMenu(ComponentMenu);
            ComponentBuilderMenu.setPrefHeight(MENU_COLLAPSED_HEIGHT);
            ComponentBuilderMenu.setPrefWidth(MENU_COLLAPSED_WIDTH);

            menuButtonTrans.setY(SHIFT_MENU_UP);

            state = MenuState.MenuCollapsed;

        } else {
            menuButtonTrans.setY(SHIFT_MENU_DOWN);

            ComponentBuilderMenu.setPrefHeight(MENU_EXPANDED_HEIGHT);
            ComponentBuilderMenu.setPrefWidth(MENU_EXPANDED_WIDTH);
            ExpandMenu(ComponentMenu);

            state = MenuState.MenuExpanded;
        }

        SetMenuButtonImage(ArrowImage);
        ComponentBuilderMenu.getTransforms().add(menuButtonTrans);
    }

    private void CollapseMenu(HBox menu){
        menu.setVisible(false);
        constructionController.setCurrentToolType(ToolType.SELECT);
    }

    private void ExpandMenu(HBox menu){
        menu.setVisible(true);
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

enum MenuState {
    MenuCollapsed, MenuExpanded
}
