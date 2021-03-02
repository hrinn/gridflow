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

import simulation.SimulationController;
import visualization.VisualizationController;
import construction.ConstructionController;
import construction.canvas.GridCanvas;

public class GridFlowApp extends Application {

    private static final String TITLE = "GridFlow";
    private static final String WINDOW_ICON_PATH = "/resources/icon.png";
    private static final int WINDOW_WIDTH = 1300;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception {

        /* Create GUI elements */
        Group root = new Group();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);

        /* Create custom event manager for module communication */
        GridFlowEventManager gridFlowEventManager = new GridFlowEventManager();

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

        /* Init GUI */
        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(WINDOW_ICON_PATH));
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(500);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}