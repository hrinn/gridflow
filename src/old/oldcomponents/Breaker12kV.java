package old.oldcomponents;

import model.geometry.Point;
import old.main.OldMain;
import processing.core.PConstants;

import java.util.ArrayList;

public class Breaker12kV extends OldComponent {

    public Breaker12kV(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, int xPos, int yPos, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
    calcDrawingCoords();
    } // END Constructor #0

    public Breaker12kV(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
    } // END Constructor #1

    public Breaker12kV(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedToIn, String inoutIn, OldComponent connectedToOut, String inoutOut, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedToIn, inoutIn, connectedToOut, inoutOut, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
    } // END Constructor #2

    public void renderEnergy(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_FAT * scale;

        // Set drawing parameters
        mainSketch.stroke(252, 252, 3);  // yellow
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.SQUARE);

        // Draw top energy lines if present
        if (getInNode().isEnergized()) {
            drawLine(12, 0);
            drawLine(12, 10);
            drawLine(12, 14);
            drawLine(18, 2);
            drawLine(18, 16);
            drawLine(18, 20);
        }

        // Draw bottom energy lines if present
        if (getOutNode().isEnergized()) {
            drawLine(19, 3);
            drawLine(19, 17);
            drawLine(19, 21);
            drawLine(13, 1);
            drawLine(13, 11);
            drawLine(13, 15);
        }

        // Draw energy box if present
        if ((getInNode().isEnergized() && getCurrentState() == 0) ||
                getOutNode().isEnergized() && getCurrentState() == 0) {
            drawBox( 4, 5, 6, 7, 1);
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

        // Draw top black lines regardless
        drawLine(12, 0);
        drawLine(12, 10);
        drawLine(12, 14);
        drawLine(18, 2);
        drawLine(18, 16);
        drawLine(18, 20);

        // Draw bottom black lines regardless
        drawLine(19, 3);
        drawLine(19, 17);
        drawLine(19, 21);
        drawLine(13, 1);
        drawLine(13, 11);
        drawLine(13, 15);

        // Draw box but fill with green or red depending on state open/closed
        if (getCurrentState() == 0) mainSketch.fill(255, 0, 0); // red
        else if(getCurrentState() == 2) mainSketch.fill(252, 252, 3);  // yellow
        else mainSketch.fill(0, 255, 0); // green
        drawBox(4, 5, 6, 7, 1);

        // Inscribe "LOCK" when locked open
        if(getCurrentState() == 2) {
            mainSketch.fill(0); // black
            drawText(8, "LOCK", "CC", 'H');
        }

        // Inscribe "N/C" when open and "N/O" when closed at dS(8) always facing upward ==> 'R' orientation
        else if (!(getNormalState() == getCurrentState())) {
            mainSketch.fill(0);
            if (getNormalState() == 0) {
                drawText(8, "N/C", "CC", 'H');
            } else {
                drawText(8, "N/O", "CC", 'H');
            }
        }
        // Place text/label at dS(9) if Right, otherwise ds(22) if left placement
        mainSketch.fill(0); // black
        if(getLabelPlacement() == 'R') {
            drawText(9, getLabel(), getTextAnchor(), getLabelOrientation());
        } else {
            drawText(22, getLabel(), getTextAnchor(), getLabelOrientation());
        }

    } // END renderLines()

    private void calcDrawingCoords() {
        ArrayList<Point> points = new ArrayList<>();
        float x = this.getInNode().getCoord().getX();
        float y = this.getInNode().getCoord().getY();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        points.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        points.add(this.getOutNode().getCoord());

        // These next steps define specific points
        Point point;
        point = new Point(x, y + 1.5f);              // Element #2 - bottom of top vertical line
        points.add(point);
        point = new Point(x, y + 2.5f);              // Element #3 - top of bottom vertical line
        points.add(point);
        point = new Point(x - 0.5f, y + 1.5f); // Element #4 - top left corner of box
        points.add(point);
        point = new Point(x - 0.5f, y + 2.5f); // Element #5 - bottom left corner of box
        points.add(point);
        point = new Point(x + 0.5f, y + 1.5f); // Element #6 - top right corner of box
        points.add(point);
        point = new Point(x + 0.5f, y + 2.5f); // Element #7 - bottom right corner of box
        points.add(point);
        point = new Point(x, y + 2f);                // Element #8 - center-point for N/C, N/O text
        points.add(point);
        point = new Point(x + 0.75f, y + 2f);  // Element #9 - Right text label anchor point
        points.add(point);
        point = new Point(x - 0.5f, y + 1f);   // Element #10 - top chevron TL outer point
        points.add(point);
        point = new Point(x - 0.5f, y + 3f);   // Element #11 - bot chevron BL outer point
        points.add(point);
        point = new Point(x, y + 0.75f);             // Element #12 - top chevron TC point
        points.add(point);
        point = new Point(x, y + 3.25f);             // Element #13 - bot chevron BC point
        points.add(point);
        point = new Point(x + 0.5f, y + 1f);   // Element #14 - top chevron TR outer point
        points.add(point);
        point = new Point(x + 0.5f, y + 3f);   // Element #15 - bot chevron BR outer point
        points.add(point);
        point = new Point(x - 0.5f, y + 1.25f);// Element #16 - top chevron BL outer point
        points.add(point);
        point = new Point(x - 0.5f, y + 2.75f);// Element #17 - bot chevron TL outer point
        points.add(point);
        point = new Point(x, y + 1f);                // Element #18 - top chevron BC point
        points.add(point);
        point = new Point(x, y + 3f);                // Element #19 - bot chevron TC point
        points.add(point);
        point = new Point(x + 0.5f, y + 1.25f);// Element #20 - top chevron BR outer point
        points.add(point);
        point = new Point(x + 0.5f, y + 2.75f);// Element #21 - bot chevron TR outer point
        points.add(point);
        point = new Point(x - 0.75f, y + 2f);  // Element #22 - Left text label anchor point
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
        this.setClickCoords(4, 7);

    } // END calcDrawingCoords

} // END public class Breaker12kV
