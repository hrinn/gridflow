package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import main.events.Event;
import main.events.EventManager;
import model.Grid;
import simulation.EnergySimulator;
import visualization.GridScene;
import visualization.PannableCanvas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static final String TITLE = "GridFlow";
    private static final int WINDOW_WIDTH = 1300;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception {
        List<Node> moduleGuiRoots = initModules();
        initGui(primaryStage, moduleGuiRoots);
    }

    private List<Node> initModules() {
        List<Node> moduleGuiRoots = new ArrayList<>();

        // Create instance of Grid and Event manager
        Grid grid = new Grid();
        EventManager eventManager = new EventManager();

        // Init Visualization Module
        GridScene gridScene = new GridScene(grid, eventManager);
        moduleGuiRoots.add(gridScene.getCanvas());

        // Init Simulation Module
        new EnergySimulator(grid, eventManager);

        // Load components into grid
        grid.loadComponents(DevUtils.createTestComponents());
        eventManager.sendEvent(Event.GridChanged); // build would do this later

        return moduleGuiRoots;
    }

    private void initGui(Stage primaryStage, List<Node> moduleGuiRoots) throws IOException {
        Node mainGui = FXMLLoader.load(getClass().getResource("main.fxml"));

        Group root = new Group(mainGui);
        root.getChildren().addAll(moduleGuiRoots);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setFill(Color.LIGHTGRAY);

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}