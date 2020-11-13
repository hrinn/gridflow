package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.Grid;
import simulation.EnergySimulator;
import visualization.GraphDisplay;

import java.io.IOException;

public class Main extends Application {

    private static final String TITLE = "GridFlow";
    private static final int WINDOW_WIDTH = 1300;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Grid grid = DevUtils.createTestGrid(1300, 700);

        GraphDisplay graphDisplay = new GraphDisplay(grid);
        initGui(primaryStage, graphDisplay.getGraphRoot());

        EnergySimulator energySimulator = new EnergySimulator(grid);
        energySimulator.energyDFS();
        graphDisplay.displayGrid(grid);
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