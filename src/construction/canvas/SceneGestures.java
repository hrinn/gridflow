package construction.canvas;

import construction.DragContext;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class SceneGestures {

    private static final double MAX_SCALE = 5.0d;
    private static final double MIN_SCALE = 0.1d;
    private boolean panning = false;

    private final DragContext sceneDragContext = new DragContext();

    GridCanvas canvas;

    public SceneGestures( GridCanvas canvas) {
        this.canvas = canvas;
    }

    public EventHandler<MouseEvent> getBeginPanEventHandler() {
        return beginPanEventHandler;
    }

    public EventHandler<MouseEvent> getOnPanEventHandler() {
        return onPanEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    public EventHandler<MouseEvent> getEndPanEventHandler() {
        return endPanEventHandler;
    }

    private final EventHandler<MouseEvent> beginPanEventHandler = event -> {
            // right mouse button => panning
            if(!event.isSecondaryButtonDown()) return;
            panning = true;

            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();

            sceneDragContext.translateAnchorX = canvas.getTranslateX();
            sceneDragContext.translateAnchorY = canvas.getTranslateY();

            event.consume();
    };

    private final EventHandler<MouseEvent> onPanEventHandler = event -> {
            // right mouse button => panning
            if(!event.isSecondaryButtonDown()) return;
            if (!panning) return;

            canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

            event.consume();
    };

    private final EventHandler<MouseEvent> endPanEventHandler = event -> {
        if (!panning) return;
        panning = false;
        event.consume();
    };

    private final EventHandler<ScrollEvent> onScrollEventHandler = event -> {
            if (panning) return;

            double delta = 1.2;

            double scale = canvas.getScale(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = clamp(scale, MIN_SCALE, MAX_SCALE);

            double f = (scale / oldScale)-1;

            double dx = (event.getSceneX() - (canvas.getBoundsInParent().getWidth()/2 + canvas.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (canvas.getBoundsInParent().getHeight()/2 + canvas.getBoundsInParent().getMinY()));

            canvas.setScale(scale);

            // note: pivot value must be untransformed, i. e. without scaling
            canvas.setPivot(f*dx, f*dy);

            event.consume();
    };

    public static double clamp( double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }


}
