package visualization.componentIcons;

import javafx.scene.shape.*;

public class ShapeCopier {

    public static Shape copyShape(Shape shape) {
        if (shape instanceof Line) {
            return copyLine((Line)shape);
        } else if (shape instanceof Rectangle) {
            return copyRectangle((Rectangle)shape);
        } else if (shape instanceof Arc) {
            return copyArc((Arc)shape);
        } else if (shape instanceof QuadCurve) {
            return copyQuadCurve((QuadCurve)shape);
        } else if (shape instanceof Circle) {
            return copyCircle((Circle)shape);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    private static Line copyLine(Line line) {
        return new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
    }

    private static Rectangle copyRectangle(Rectangle rectangle) {
        return new Rectangle(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight());
    }

    private static Arc copyArc(Arc arc) {
        return new Arc(arc.getCenterX(), arc.getCenterY(), arc.getRadiusX(), arc.getRadiusY(),
                arc.getStartAngle(), arc.getLength());
    }

    private static QuadCurve copyQuadCurve(QuadCurve qc) {
        return new QuadCurve(qc.getStartX(), qc.getStartY(), qc.getControlX(), qc.getControlY(),
                qc.getEndX(), qc.getEndY());
    }

    private static Circle copyCircle(Circle circle) {
        return new Circle(circle.getCenterX(), circle.getCenterY(), circle.getRadius());
    }
}
