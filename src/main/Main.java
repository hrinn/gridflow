package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import main.events.Event;
import main.events.EventManager;
import model.Grid;
import simulation.EnergySimulator;
import visualization.GraphVisualizer;

import java.io.IOException;

public class Main extends Application {

    private static final String TITLE = "GridFlow";
    private static final int WINDOW_WIDTH = 1300;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Grid grid = new Grid();
        EventManager eventManager = new EventManager();

        // Init modules
        GraphVisualizer graphVisualizer = new GraphVisualizer(grid, eventManager);
        EnergySimulator energySimulator = new EnergySimulator(grid, eventManager);

        // Draw base GUI
        initGui(primaryStage, graphVisualizer.getGraphRoot());

        // Load components into grid
        grid.loadComponents(DevUtils.createTestComponents(WINDOW_WIDTH, WINDOW_HEIGHT));
        eventManager.sendEvent(Event.GridChanged); // build would do this later
    }

    private void initGui(Stage primaryStage, Group graphRoot) throws IOException {
        Node mainUI = FXMLLoader.load(getClass().getResource("main.fxml"));

        Group root = new Group(graphRoot, mainUI);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}