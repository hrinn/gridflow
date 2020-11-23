package visualization;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ShapeCopier {

    public static Shape copyShape(Shape shape) {
        if (shape instanceof Line) {
            return copyLine((Line)shape);
        } else if (shape instanceof Rectangle) {
            return copyRectangle((Rectangle)shape);
        }
        else {
            throw new UnsupportedOperationException();
        }
    }

    private static Line copyLine(Line line) {
        return new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
    }

    private static Rectangle copyRectangle(Rectangle rectangle) {
        return new Rectangle(rectangle.getX(), rectangle.getX(), rectangle.getWidth(), rectangle.getHeight());
    }
}
