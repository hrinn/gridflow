package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import main.events.Event;
import main.events.EventManager;
import model.Grid;
import simulation.EnergySimulator;
import visualization.GridScene;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private static final String TITLE = "GridFlow";
    private static final String WINDOWICONPATH = "/resources/iconScaled.png";
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

        Group root = new Group();
        root.getChildren().addAll(moduleGuiRoots);
        root.getChildren().add(mainGui);
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        //scene.setFill(Color.LIGHTGRAY);

        primaryStage.setTitle(TITLE);
        primaryStage.getIcons().add(new Image(WINDOWICONPATH));
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

/*    private Button CreateRoundButton(){
        Button roundButton = new Button("Account");

        roundButton.setStyle(


        );

        return roundButton;
    }*/


    public static void main(String[] args) {
        launch(args);
    }
}