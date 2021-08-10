package construction.buildMenu;

import construction.ComponentType;
import construction.ToolType;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import security.Access;

public class BuildMenuViewController {

    private static final String ARROW_RIGHT_PATH = "resources/ArrowRight.png";
    private static final String ARROW_LEFT_PATH = "resources/ArrowLeft.png";
    private static final int MENU_EXPANDED_WIDTH = 75;
    private static final int MENU_COLLAPSED_WIDTH = 25;
    private BuildMenuFunctions buildMenuFunctions;

    private boolean menuOpen = false;

    // JavaFX Elements
    public AnchorPane rootNode;
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

    private Button currentButton;

    public ImageView ShowMenuImage;
    public Button ShowMenuButton;

    public void setBuildMenuFunctions(BuildMenuFunctions buildMenuFunctions) {
        this.buildMenuFunctions = buildMenuFunctions;
    }

    // Run when this controller is initialized
    public void initialize() {
        componentMenu.setVisible(false);
        componentMenu.managedProperty().bind(componentMenu.visibleProperty());
    }

    public void bindBuildMenuHeightProperty(Stage primaryStage) {
        componentMenu.maxHeightProperty().bind(primaryStage.heightProperty());
    }

    // These are the button press handlers
    // They can also be accessed outside of the class to handle key shortcuts
    public void selectInteractTool() {
        if (currentButton != null) {
            currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        }
        buildMenuFunctions.setBuildMenuData(ToolType.INTERACT, null);
        InteractToolButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = InteractToolButton;
    }

    public void selectSelectTool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.SELECT, null);
        SelectToolButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = SelectToolButton;
    }

    public void selectAssociationTool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.ASSOCIATION, null);
        AssociationToolButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = AssociationToolButton;
    }

    public void selectWireTool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.WIRE, ComponentType.WIRE);
        WireButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = WireButton;
    }

    public void selectPowerSourceTool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.POWER_SOURCE);
        PowerSourceButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = PowerSourceButton;
    }

    public void selectTurbineTool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.TURBINE);
        TurbineButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = TurbineButton;
    }

    public void selectSwitchTool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.SWITCH);
        SwitchButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = SwitchButton;
    }

    public void selectBreaker12Tool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.BREAKER_12KV);
        Breaker12Button.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = Breaker12Button;
    }

    public void selectBreaker70Tool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.BREAKER_70KV);
        Breaker70Button.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = Breaker70Button;
    }

    public void selectTransformerTool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.TRANSFORMER);
        TransformerButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = TransformerButton;
    }

    public void selectCutoutTool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.CUTOUT);
        CutoutButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = CutoutButton;
    }

    public void selectJumperTool() {
        currentButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), false);
        buildMenuFunctions.setBuildMenuData(ToolType.PLACE, ComponentType.JUMPER);
        JumperButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("current"), true);
        currentButton = JumperButton;
    }

    @FXML
    private void toggleMenu() {
        componentMenu.setVisible(!menuOpen);
        buildMenuFunctions.setBackgroundGridVisible(!menuOpen);

        if (menuOpen) {
            // Close the menu
            ShowMenuImage.setImage(new Image(ARROW_RIGHT_PATH));
            buildMenuFunctions.setPropertiesWindowVisible(false);
        } else {
            // Open the menu
            ShowMenuImage.setImage(new Image(ARROW_LEFT_PATH));
        }
        selectInteractTool();

        menuOpen = !menuOpen;
    }

    public void setPermissions(Access permissionLevel) {
        if (permissionLevel == Access.VIEWER) {
            rootNode.setVisible(false);
            rootNode.setManaged(false);
        }
    }
}

