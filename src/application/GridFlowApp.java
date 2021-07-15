package application;

import base.BaseUIViewController;
import base.MenuFunctionController;
import construction.buildMenu.BuildMenuViewController;
import construction.properties.PropertiesMenuViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import application.events.*;

import security.Access;
import security.AccountController;
import security.LoginUIViewController;
import security.LoginController;
import simulation.SimulationController;
import visualization.VisualizationController;
import construction.ConstructionController;

public class GridFlowApp extends Application implements GridFlowEventListener {

    private static final String TITLE = "GridFlow";
    public static final String WINDOW_ICON_PATH = "/resources/icon.png";
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private static final int LOGIN_WIDTH = 1000;
    private static final int LOGIN_HEIGHT = 632;

    private GridFlowEventManager gridFlowEventManager;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {

        /* Set event manager and primary stage in the Application private fields.
           This is so that startApplication can access them when it runs. */
        this.gridFlowEventManager = new GridFlowEventManager();
        this.primaryStage = primaryStage;

        /* Add the application as an event listener */
        gridFlowEventManager.addListener(this);

        /* Open login screen */
        startLogin();
    }

    public void startLogin() throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, LOGIN_WIDTH, LOGIN_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        /* Initialize Security Module */
        LoginController loginController = new LoginController(gridFlowEventManager);
        FXMLLoader loginUIViewLoader = new FXMLLoader(getClass().getResource("/security/LoginUIView.fxml"));
        Node loginUIView = loginUIViewLoader.load();
        LoginUIViewController loginUIViewController = loginUIViewLoader.getController();
        loginUIViewController.setController(loginController);

        /* Add login screen to scene root */
        root.getChildren().add(loginUIView);

        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(WINDOW_ICON_PATH));
        primaryStage.setMinHeight(LOGIN_HEIGHT);
        primaryStage.setMinWidth(LOGIN_WIDTH);
        primaryStage.setMaxHeight(LOGIN_HEIGHT);
        primaryStage.setMaxWidth(LOGIN_WIDTH);
        primaryStage.show();
    }

    /* This waits for a successful login before displaying the main application */
    @Override
    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent instanceof LoginEvent) {
            /* User successfully logged in */
            /* Run the application once login is successful */
            Access permissionLevel = ((LoginEvent)gridFlowEvent).getAccess();
            try {
                startApplication(permissionLevel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (gridFlowEvent instanceof ReLoginEvent) {
            /* User wants to login as a different user */
            /* Open the login screen */
            try {
                startLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* This initializes and displays the main application */
    public void startApplication(Access permissionLevel) throws Exception {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        System.out.println("Screen size: " + screenBounds.getWidth() + " x " + screenBounds.getHeight());

        /* Create GUI elements */
        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.DARKGRAY);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        primaryStage.setScene(scene);

        /* Init modules and connect them all together */
        // Base UI Module
        FXMLLoader baseUIViewLoader = new FXMLLoader(getClass().getResource("/base/BaseUIView.fxml"));
        MenuFunctionController menuFunctionController = new MenuFunctionController(gridFlowEventManager);
        Node baseUIView = baseUIViewLoader.load();
        BaseUIViewController baseUIViewController = baseUIViewLoader.getController();
        baseUIViewController.setController(menuFunctionController);
        baseUIViewController.setMainScene(scene);
        baseUIViewController.setServices(getHostServices());
        baseUIViewController.bindMenuBarWidthProperty(primaryStage);

        // Account Controller
        AccountController accountController = new AccountController(scene);
        gridFlowEventManager.addListener(accountController);

        // Construction Module
        ConstructionController constructionController = new ConstructionController(menuFunctionController.getGrid(), gridFlowEventManager, primaryStage);

        FXMLLoader buildMenuViewLoader = new FXMLLoader(getClass().getResource("/construction/buildMenu/BuildMenuView.fxml"));
        Node buildMenuView = buildMenuViewLoader.load();
        FXMLLoader propertiesMenuViewLoader = new FXMLLoader(getClass().getResource("/construction/properties/PropertiesMenuView.fxml"));
        Node propertiesMenuView = propertiesMenuViewLoader.load();

        BuildMenuViewController buildMenuViewController = buildMenuViewLoader.getController();
        constructionController.setBuildMenuViewController(buildMenuViewController);
        PropertiesMenuViewController propertiesMenuViewController = propertiesMenuViewLoader.getController();
        constructionController.setPropertiesMenuViewController(propertiesMenuViewController);

        buildMenuViewController.setBuildMenuFunctions(constructionController);
        buildMenuViewController.bindBuildMenuHeightProperty(primaryStage);
        baseUIViewController.setBaseMenuFunctions(constructionController);

        // Visualization Module
        VisualizationController visualizationController = new VisualizationController(menuFunctionController.getGrid(), constructionController.getCanvasFacade());
        gridFlowEventManager.addListener(visualizationController);

        // Simulation Module
        SimulationController simulationController = new SimulationController(menuFunctionController.getGrid(), gridFlowEventManager);
        gridFlowEventManager.addListener(simulationController);

        // Change accessible functionality based on permission level
//        buildMenuViewController.setPermissions(permissionLevel);
        baseUIViewController.setPermissions(permissionLevel);
        constructionController.setPermissions(permissionLevel);

        /* Add UI elements to Scene */
        BorderPane UI = new BorderPane();
        UI.setLeft(buildMenuView);
        UI.setRight(propertiesMenuView);
        UI.setTop(baseUIView);
        UI.setPickOnBounds(false);

        // Set padding on borderpane elements
        Insets insets = new Insets(10, 0, 0, 0);
        BorderPane.setMargin(buildMenuView, insets);
        BorderPane.setMargin(propertiesMenuView, insets);

        root.getChildren().addAll(constructionController.getCanvasFacade().getCanvas(), UI);

        /* Show the new UI elements */
        primaryStage.setMinHeight(WINDOW_HEIGHT);
        primaryStage.setMaxWidth(scene.getWidth());
        primaryStage.setMaxHeight(screenBounds.getHeight());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}