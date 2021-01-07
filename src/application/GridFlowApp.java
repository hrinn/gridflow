package application;

import baseui.BaseUIViewController;
import baseui.MenuFunctionController;
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

import java.awt.*;

public class GridFlowApp extends Application {

    private static final String TITLE = "GridFlow";
    private static final String WINDOW_ICON_PATH = "/resources/icon.png";
    private static final int WINDOW_WIDTH = 1300;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Custom event manager for our events
        GridFlowEventManager gridFlowEventManager = new GridFlowEventManager();

        // Init modules
        FXMLLoader baseUIViewLoader = new FXMLLoader(getClass().getResource("/baseui/BaseUIView.fxml"));
        MenuFunctionController menuFunctionController = new MenuFunctionController(gridFlowEventManager);

        Node baseUIView = baseUIViewLoader.load();
        BaseUIViewController baseUIViewController = baseUIViewLoader.getController();
        baseUIViewController.setController(menuFunctionController);

        ConstructionController constructionController = new ConstructionController(menuFunctionController.getGrid(), gridFlowEventManager, primaryStage);
        FXMLLoader buildMenuViewLoader = new FXMLLoader(getClass().getResource("/construction/BuildMenuView.fxml"));
        Node buildMenuView = buildMenuViewLoader.load();
        BuildMenuViewController buildMenuViewController = buildMenuViewLoader.getController();
        buildMenuViewController.setConstructionController(constructionController);

        VisualizationController visualizationController = new VisualizationController(menuFunctionController.getGrid(), constructionController.getCanvasFacade());
        gridFlowEventManager.addListener(visualizationController);

        SimulationController simulationController = new SimulationController(menuFunctionController.getGrid(), gridFlowEventManager);
        gridFlowEventManager.addListener(simulationController);

        menuFunctionController.loadDefaultGrid();

        // Init GUI
        initGui(primaryStage, constructionController.getCanvasFacade().getCanvas(), buildMenuView, baseUIView);
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