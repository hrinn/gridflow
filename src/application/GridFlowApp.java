package application;

import construction.GridBuilder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import application.events.Event;
import application.events.EventManager;
import model.Grid;
import simulation.EnergySimulator;
import visualization.GridScene;

import java.io.IOException;

public class GridFlowApp extends Application {

    private static final String TITLE = "GridFlow";
    private static final String WINDOW_ICON_PATH = "resources/iconLarge.png";
    private static final int WINDOW_WIDTH = 1300;
    private static final int WINDOW_HEIGHT = 700;
    private static final String GRIDFLOW_BLUE = "#008EB0";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Node canvas = initModules();
        initGui(primaryStage, canvas);
    }

    private Node initModules() {

        // Create instance of Grid & Event manager
        Grid grid = new Grid();
        EventManager eventManager = new EventManager();

        // Init Visualization Module
        GridScene gridScene = new GridScene(grid, eventManager);

        // Init Simulation Module
        new EnergySimulator(grid, eventManager);

        // Init Construction Module
       new GridBuilder(gridScene.getCanvas());

        // Load components into grid
        grid.loadComponents(DevUtils.createTestComponents());
        eventManager.sendEvent(Event.GridChanged); // build would do this later

        return gridScene.getCanvas();
    }

    private void initGui(Stage primaryStage, Node canvas) throws IOException {
        Node mainGui = FXMLLoader.load(getClass().getResource("UI.fxml"));

        Group root = new Group();
        root.getChildren().addAll(canvas, mainGui);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(Color.LIGHTGRAY);

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