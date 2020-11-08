package main;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.components.geometry.Point;
import visualization.GraphDisplay;

import java.io.IOException;

public class Main extends Application {

    private static final String TITLE = "GridFlow";
    private static final int WINDOW_WIDTH = 1300;
    private static final int WINDOW_HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GraphDisplay graphDisplay = new GraphDisplay();
        initGui(primaryStage, graphDisplay.getGraphRoot());

        graphDisplay.addComponent(new Point(650, 350));
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