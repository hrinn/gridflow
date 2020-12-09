package application;

import construction.ConstructionController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import application.events.Event;
import application.events.EventManager;
import domain.Grid;
import application.canvas.PannableCanvas;
import application.canvas.SceneGestures;
import simulation.SimulationController;
import visualization.VisualizationController;

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

        // Custom event manager for our events
        EventManager eventManager = new EventManager();

        // Create shared objects
        Grid grid = new Grid();
        PannableCanvas canvas = createCanvas();

        // Init modules
        VisualizationController visualizationController = new VisualizationController();
        visualizationController.initController(grid, canvas);
        eventManager.addListener(visualizationController);

        ConstructionController constructionController = new ConstructionController();
        /* this ^ will be done differently once construction has its own FXML
           FXMLLoader constructionLoader = new FXMLLoader(getClass().getResources() [ui fxml file] )
           ConstructionController constructionController = constructionLoader.getController();
         */
        constructionController.initController(grid, canvas);

        SimulationController simulationController = new SimulationController();
        simulationController.initController(grid, eventManager);
        eventManager.addListener(simulationController);



        // Load components into grid
        grid.loadComponents(DevUtils.createTestComponents());
        eventManager.sendEvent(Event.GridChanged); // build would do this later

        return canvas;
    }

    private PannableCanvas createCanvas() {
        PannableCanvas canvas = new PannableCanvas();
        canvas.setTranslateX(-5350);
        canvas.setTranslateY(-2650);

        SceneGestures sceneGestures = new SceneGestures(canvas);
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        return canvas;
    }

    private void initGui(Stage primaryStage, Node canvas) throws IOException {
        FXMLLoader mainGuiLoader = new FXMLLoader(getClass().getResource("UI.fxml"));

        Group root = new Group();
        root.getChildren().addAll(canvas, mainGuiLoader.load());
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