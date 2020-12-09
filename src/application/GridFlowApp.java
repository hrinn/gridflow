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
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import application.events.Event;
import application.events.EventManager;
import domain.Grid;
import application.canvas.PannableCanvas;
import application.canvas.SceneGestures;
import simulation.SimulationController;
import visualization.VisualizationController;

public class GridFlowApp extends Application {

    private static final String TITLE = "GridFlow";
    private static final String WINDOW_ICON_PATH = "/resources/iconLarge.png";
    private static final int WINDOW_WIDTH = 1300;
    private static final int WINDOW_HEIGHT = 700;
    private static final String GRIDFLOW_BLUE = "#008EB0";

    @Override
    public void start(Stage primaryStage) throws Exception {

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
        constructionController.initController(grid, canvas);
        FXMLLoader constructionViewLoader = new FXMLLoader(getClass().getResource("/construction/ConstructionView.fxml"));

        SimulationController simulationController = new SimulationController();
        simulationController.initController(grid, eventManager);
        eventManager.addListener(simulationController);

        FXMLLoader baseUIViewLoader = new FXMLLoader(getClass().getResource("/baseui/BaseUIView.fxml"));

        // Load components into grid
        grid.loadComponents(DevUtils.createTestComponents());
        eventManager.sendEvent(Event.GridChanged); // build would do this later

        // Init GUI
        initGui(primaryStage, canvas, constructionViewLoader.load(), baseUIViewLoader.load());
    }

    private PannableCanvas createCanvas() {
        PannableCanvas canvas = new PannableCanvas();
        canvas.setTranslateX(-5350); // get this from application settings?
        canvas.setTranslateY(-2650);

        SceneGestures sceneGestures = new SceneGestures(canvas);
        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        canvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        canvas.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());

        return canvas;
    }

    private void initGui(Stage primaryStage, PannableCanvas canvas, Node constructionView, Node baseUIView) {
        Group root = new Group();

        BorderPane ui = new BorderPane();
        ui.setPickOnBounds(false);
        ui.setLeft(constructionView);
        ui.setTop(baseUIView);

        root.getChildren().addAll(canvas, ui);

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