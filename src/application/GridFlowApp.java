package application;

import construction.BuildMenuViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import domain.Grid;
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

        // Custom event manager for our events
        GridFlowEventManager gridFlowEventManager = new GridFlowEventManager();

        // Create empty grid
        Grid grid = new Grid();

        // Init modules
        ConstructionController constructionController = new ConstructionController(grid, gridFlowEventManager, primaryStage);
        FXMLLoader buildMenuViewLoader = new FXMLLoader(getClass().getResource("/construction/BuildMenuView.fxml"));
        Node buildMenuView = buildMenuViewLoader.load();
        BuildMenuViewController buildMenuViewController = buildMenuViewLoader.getController();
        buildMenuViewController.setConstructionController(constructionController);

        VisualizationController visualizationController = new VisualizationController(grid, constructionController.getCanvasFacade());
        gridFlowEventManager.addListener(visualizationController);

        SimulationController simulationController = new SimulationController(grid, gridFlowEventManager);
        gridFlowEventManager.addListener(simulationController);

        FXMLLoader baseUIViewLoader = new FXMLLoader(getClass().getResource("/baseui/BaseUIView.fxml"));

        // Load components into grid
        grid.loadComponents(DevUtils.createTestComponents());
        gridFlowEventManager.sendEvent(new GridChangedEvent()); // build would do this later

        // Init GUI
        initGui(primaryStage, constructionController.getCanvasFacade().getCanvas(), buildMenuView, baseUIViewLoader.load());
    }

    private void initGui(Stage primaryStage, GridCanvas canvas, Node buildMenuView, Node baseUIView) {
        Group root = new Group();

        BorderPane ui = new BorderPane();
        ui.setLeft(buildMenuView);
        ui.setTop(baseUIView);
        ui.setPickOnBounds(false);

        root.getChildren().addAll(canvas, ui);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(WINDOW_ICON_PATH));
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}