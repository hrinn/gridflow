package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import model.Grid;
import model.components.*;
import model.geometry.*;
import simulation.EnergySimulator;
import visualization.GraphDisplay;

import java.io.IOException;
import java.util.List;

public class Main extends Application {

    private static final String TITLE = "GridFlow";
    private static final int WINDOW_WIDTH = 1300;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Grid grid = createTestGrid();

        GraphDisplay graphDisplay = new GraphDisplay(grid);
        initGui(primaryStage, graphDisplay.getGraphRoot());

        graphDisplay.displayGrid(grid);

        EnergySimulator energySimulator = new EnergySimulator(grid);
        energySimulator.EnergyDFS();
    }

    private Grid createTestGrid() {
        // Points used to place components
        Point center = new Point(WINDOW_WIDTH/2, WINDOW_HEIGHT/2);
        Point xShift = new Point(100, 0);
        Point yShift = new Point(0, 75);

        // Components
        Breaker dd3 = new Breaker("dd3",
                center.add(yShift.multiply(2)).add(xShift.multiply(2)),
                Voltage.KV12, true);
        Breaker dd5 = new Breaker("dd5",
                center.add(yShift.multiply(2)).add(xShift),
                Voltage.KV12, true);
        Breaker dd7 = new Breaker("dd7",
                center.add(yShift.multiply(2)),
                Voltage.KV12, true);
        Breaker dd8 = new Breaker("dd8",
                center.add(yShift.multiply(2)).add(xShift.negative()),
                Voltage.KV12, true);
        Breaker dd9 = new Breaker("dd9",
                center.add(yShift.multiply(2)).add(xShift.negative().multiply(2)),
                Voltage.KV12, true);
        Wire w1 = new Wire("w1", center.add(yShift));
        Breaker dd1main = new Breaker("dd1 Main", center, Voltage.KV12, true);
        Wire w2 = new Wire("w2", center.add(yShift.negative()));
        Transformer dd1 = new Transformer("transformer dd1", center.add(yShift.negative().multiply(2)));

        // Connections
        dd3.connectInWire(w1);
        dd5.connectInWire(w1);
        dd7.connectInWire(w1);
        dd8.connectInWire(w1);
        dd9.connectInWire(w1);
        w1.setConnections(List.of(dd3, dd5, dd7, dd8, dd9, dd1main));
        dd1main.connectInWire(w1);
        dd1main.connectOutWire(w2);
        w2.setConnections(List.of(dd1main, dd1));
        dd1.connectInWire(w2);

        // Return grid
        Grid grid = new Grid();
        grid.setComponents(List.of(dd3, dd5, dd7, dd8, dd9, w1, dd1main, w2, dd1));
        return grid;
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