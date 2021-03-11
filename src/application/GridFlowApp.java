package application;

import base.BaseUIViewController;
import base.MenuFunctionController;
import construction.BuildMenuViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import application.events.*;

import security.LoginUIViewController;
import security.SecurityController;
import simulation.SimulationController;
import visualization.VisualizationController;
import construction.ConstructionController;

public class GridFlowApp extends Application implements GridFlowEventListener {

    private static final String TITLE = "GridFlow";
    private static final String WINDOW_ICON_PATH = "/resources/icon.png";
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 720;
    private static final int MAX_WIDTH = 3840;
    private static final int MAX_HEIGHT = 2160;

    private GridFlowEventManager gridFlowEventManager;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        /* Create custom event manager for module communication */
        GridFlowEventManager gridFlowEventManager = new GridFlowEventManager();

        /* Set event manager and primary stage in the Application private fields.
           This is so that startApplication can access them when it runs. */
        this.gridFlowEventManager = gridFlowEventManager;
        this.primaryStage = primaryStage;

        /* Open login screen */
        startLogin();
    }

    public void startLogin() throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, 1280, 720);

        /* Initialize Security Module */
        SecurityController securityController = new SecurityController(gridFlowEventManager);
        FXMLLoader loginUIViewLoader = new FXMLLoader(getClass().getResource("/security/LoginUIView.fxml"));
        Node loginUIView = loginUIViewLoader.load();
        LoginUIViewController loginUIViewController = loginUIViewLoader.getController();
        loginUIViewController.setController(securityController);

        /* Add login screen to scene root */
        root.getChildren().add(loginUIView);

        /* Add the application as an event listener */
        gridFlowEventManager.addListener(this);

        primaryStage.setMaxWidth(WINDOW_WIDTH);
        primaryStage.setMaxHeight(WINDOW_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(WINDOW_ICON_PATH));
        primaryStage.setMinHeight(WINDOW_HEIGHT);
        primaryStage.setMinWidth(WINDOW_WIDTH);
        primaryStage.show();

    }

    @Override
    public void handleEvent(GridFlowEvent gridFlowEvent) {
        if (gridFlowEvent instanceof LoginEvent) {
            /* User successfully logged in */
            /* Run the application once login is successful */
            try {
                startApplication();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startApplication() throws Exception {
        /* Create GUI elements */
        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);


        /* Init modules and connect them all together */
        // Base UI Module
        FXMLLoader baseUIViewLoader = new FXMLLoader(getClass().getResource("/base/BaseUIView.fxml"));
        MenuFunctionController menuFunctionController = new MenuFunctionController(gridFlowEventManager);
        Node baseUIView = baseUIViewLoader.load();
        BaseUIViewController baseUIViewController = baseUIViewLoader.getController();
        baseUIViewController.setController(menuFunctionController);

        // Construction Module
        ConstructionController constructionController = new ConstructionController(menuFunctionController.getGrid(), gridFlowEventManager, primaryStage);
        FXMLLoader buildMenuViewLoader = new FXMLLoader(getClass().getResource("/construction/BuildMenuView.fxml"));
        Node buildMenuView = buildMenuViewLoader.load();
        BuildMenuViewController buildMenuViewController = buildMenuViewLoader.getController();
        buildMenuViewController.setConstructionController(constructionController);
        baseUIViewController.setBaseMenuFunctions(constructionController);

        // Visualization Module
        VisualizationController visualizationController = new VisualizationController(menuFunctionController.getGrid(), constructionController.getCanvasFacade());
        gridFlowEventManager.addListener(visualizationController);

        // Simulation Module
        SimulationController simulationController = new SimulationController(menuFunctionController.getGrid(), gridFlowEventManager);
        gridFlowEventManager.addListener(simulationController);

        // Load the default grid JSON file
        menuFunctionController.loadDefaultGrid();

        /* Add UI elements to Scene */
        BorderPane UI = new BorderPane();
        UI.setLeft(buildMenuView);
        UI.setTop(baseUIView);
        UI.setPickOnBounds(false);
        root.getChildren().addAll(constructionController.getCanvasFacade().getCanvas(), UI);

        /* Show the new UI elements */
        primaryStage.setMaxWidth(MAX_WIDTH);
        primaryStage.setMaxHeight(MAX_HEIGHT);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}