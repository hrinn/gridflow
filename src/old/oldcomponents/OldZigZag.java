package old.oldcomponents;

import model.components.geometry.Point;
import old.main.OldMain;
import processing.core.PConstants;

import java.util.ArrayList;

public class OldZigZag extends OldComponent {


    public OldZigZag(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
        setNormalState(0);  // ensures "closed"
        setCurrentState(0); // ensures "closed"
        setCanOpen(false);  // ensures logic will not allow this component to operate

    } // END Constructor #1
    // DO NOT USE with Construtor #0 or #2


    public void renderEnergy(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_FAT * scale;

        // Set drawing parameters
        mainSketch.stroke(252, 252, 3);  // yellow
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw energy lines if present
        if (getInNode().isEnergized() || getOutNode().isEnergized()) {
            drawLine(0, 1);
            drawLine(1, 2); // Elbow A inside
            drawLine(2, 5); // Elbow A outside
            drawLine(1, 3); // Elbow B inside
            drawLine(3, 6); // Elbow B outside
            drawLine(1, 4); // Elbow C inside
            drawLine(4, 7); // Elbow C outside
        }

    } // END renderEnergy()

    public void renderLines(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_THIN * scale;
        float unit = mainSketch.UNIT * scale;

        int x = calcPos((int) (getInNode().getCoord().getX()), scale, panX);
        int y = calcPos((int) (getInNode().getCoord().getY()), scale, panY);

        // Set drawing parameters
        mainSketch.stroke(0);  // black
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw lines regardless
        drawLine(0, 1);
        drawLine(1, 2); // Elbow A inside
        drawLine(2, 5); // Elbow A outside
        drawLine(1, 3); // Elbow B inside
        drawLine(3, 6); // Elbow B outside
        drawLine(1, 4); // Elbow C inside
        drawLine(4, 7); // Elbow C outside

        // Place text/label at dS(8)
        drawText(8, getLabel(), getTextAnchor(), getLabelOrientation());

    } // END renderLines()

    private void calcDrawingCoords() {

        ArrayList<Point> points = new ArrayList<>();
        float x = this.getInNode().getCoord().getX();
        float y = this.getInNode().getCoord().getY();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        points.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        points.add(this.getOutNode().getCoord());

        // These next steps put coordinates into the array for clicking (mouse-hovering)
        Point point = new Point(x + 0.174f, y + 2.985f); // Element #2 - inside elbow A
        points.add(point);
        point = new Point(x - 0.940f, y + 1.658f); // Element #3 - inside elbow B
        points.add(point);
        point = new Point(x + 0.766f, y + 1.357f); // Element #4 - inside elbow C
        points.add(point);
        point = new Point(x - 0.811f, y + 3.159f); // Element #5 - outside elbow A
        points.add(point);
        point = new Point(x - 0.598f, y + 0.718f); // Element #6 - outside elbow B
        points.add(point);
        point = new Point(x + 1.409f, y + 2.123f); // Element #7 - outside elbow C
        points.add(point);
        point = new Point(x, y + 4f); // Element #8 - text anchor
        points.add(point);
        point = new Point(x - 1f, y + 1f); // Element #9 - TL click coordinate
        points.add(point);
        point = new Point(x + 1f, y + 3f); // Element #10 - BR click coordinate
        points.add(point);

        // This next step determines if the coordinates have to be rotated, rotates them, and
        // then executes the setDs() method for this component.
        switch (this.getOrientation()) {
            case 'U':
                setDs(rotateDwgCoords(points, 180));
                break;
            case 'L':
                setDs(rotateDwgCoords(points, 90));
                break;
            case 'R':
                setDs(rotateDwgCoords(points, 270));
                break;
            default:
                setDs(points);
                break;
        } // END switch (orientation)

        // Establish the click coordinates
        this.setClickCoords(2, 3);

    } // END calcDrawingCoords

} // END public class ZigZag
