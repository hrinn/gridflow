package construction;

import construction.builder.GridBuilderController;
import construction.properties.PropertiesData;
import construction.properties.PropertiesManager;
import construction.properties.PropertiesObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.transform.Translate;
import security.Access;

import java.io.*;

public class BuildMenuViewController {

    private static final String ARROW_RIGHT_PATH = "resources/ArrowRight.png";
    private static final String ARROW_LEFT_PATH = "resources/ArrowLeft.png";
    private static final int MENU_EXPANDED_WIDTH = 75;
    private static final int MENU_COLLAPSED_WIDTH = 25;
    private BuildMenuFunctions buildMenuFunctions;

    private boolean menuOpen = false;

    // JavaFX Elements
    public HBox MenuContainer;
    public VBox componentMenu;

    public Button InteractToolButton;
    public Button SelectToolButton;
    public Button AssociationToolButton;
    public Button WireButton;
    public Button PowerSourceButton;
    public Button TurbineButton;
    public Button SwitchButton;
    public Button Breaker12Button;
    public Button Breaker70Button;
    public Button TransformerButton;
    public Button JumperButton;
    public Button CutoutButton;

    public ImageView ShowMenuImage;
    public Button ShowMenuButton;

    public void setBuildMenuFunctions(BuildMenuFunctions buildMenuFunctions) {
        this.buildMenuFunctions = buildMenuFunctions;
    }

    // Run when this controller is initialized
    public void initialize() {
        componentMenu.setVisible(false);
        MenuContainer.getChildren().setAll(ShowMenuButton);
    }

    // These are the button press handlers
    // They can also be accessed outside of the class to handle key shortcuts
    public void selectInteractTool() {
        buildMenuFunctions.setBuildMenuData(ToolType.INTERACT, null);
        InteractToolButton.requestFocus();
    }

    public void selectSelectTool() {
        buildMenuFunctions.setBuildMenuData(ToolType.SELECT, null);
        SelectToolButton.requestFocus();
    }

    public void selectAssociationTool() {
        buildMenuFunctions.setBuildMenuData(ToolType.ASSOCIATION, null);
        AssociationToolButton.requestFocus();
    }

    public void selectWireTool() {
        buildMenuFunctions.setBuildMenuData(ToolType.WIRE, ComponentType.WIRE);
        WireButton.requestFocus();
    }

    public void selectPowerSourceTool() {
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.POWER_SOURCE);
        PowerSourceButton.requestFocus();
    }

    public void selectTurbineTool() {
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.TURBINE);
        TurbineButton.requestFocus();
    }

    public void selectSwitchTool() {
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.SWITCH);
        SwitchButton.requestFocus();
    }

    public void selectBreaker12Tool() {
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.BREAKER_12KV);
        Breaker12Button.requestFocus();
    }

    public void selectBreaker70Tool() {
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.BREAKER_70KV);
        Breaker70Button.requestFocus();
    }

    public void selectTransformerTool() {
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.TRANSFORMER);
        TransformerButton.requestFocus();
    }

    public void selectCutoutTool() {
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.CUTOUT);
        CutoutButton.requestFocus();
    }

    public void selectJumperTool() {
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.JUMPER);
        JumperButton.requestFocus();
    }

    @FXML
    private void toggleMenu() {
        componentMenu.setVisible(!menuOpen);
        buildMenuFunctions.setBackgroundGridVisible(!menuOpen);

        if (menuOpen) {
            ShowMenuImage.setImage(new Image(ARROW_RIGHT_PATH));
            MenuContainer.getChildren().setAll(ShowMenuButton);

        } else {
            ShowMenuImage.setImage(new Image(ARROW_LEFT_PATH));
            MenuContainer.getChildren().setAll(componentMenu, ShowMenuButton);
        }

        menuOpen = !menuOpen;
    }
}

