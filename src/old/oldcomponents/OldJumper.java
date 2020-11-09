package old.oldcomponents;

import model.geometry.Point;
import old.main.OldMain;
import processing.core.PConstants;

import java.util.ArrayList;

public class OldJumper extends OldComponent {

    // TODO:  create upfeed option

    public OldJumper(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, int xPos, int yPos, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
    } // END Constructor #0

    public OldJumper(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
    } // END Constructor #1

    public OldJumper(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedToIn, String inoutIn, OldComponent connectedToOut, String inoutOut, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith) {
        super(mainSketch, id, name, type, orientation, normalState, connectedToIn, inoutIn, connectedToOut, inoutOut, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        calcDrawingCoords();
    } // END Constructor #2

    public void renderEnergy(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_FAT * scale;

        // Set drawing parameters
        mainSketch.stroke(252, 252, 3);  // yellow
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.ROUND);

        // Draw top energy line if present
        if (getInNode().isEnergized()) {
            drawLine(0, 2);
        }

        // Draw bottom energy line if present
        if (getOutNode().isEnergized()) {
            drawLine(1, 3);
        }

        // Draw energy arc if present, depending on whether the jumper is closed or open
        if (getCurrentState() == 0 && (getInNode().isEnergized() || getOutNode().isEnergized())) {
            drawBezier(3, 4, 2);
        } // END if energized and closed
        // Else if jumper is open, but outNode is energized, render open
        else if(getCurrentState() == 1 && getOutNode().isEnergized()){
            drawBezier(3, 5, 6);
        } // END if cutout open with outNode energized

    } // END renderEnergy()

    public void renderLines(float scale, float panX, float panY) {

        float strokeWt = mainSketch.STROKE_THIN * scale;
        float unit = mainSketch.UNIT * scale;

        int x = calcPos((int) (getInNode().getCoord().getX()), scale, panX);
        int y = calcPos((int) (getInNode().getCoord().getY()), scale, panY);


        // Set drawing parameters
        mainSketch.stroke(0);  // black
        mainSketch.strokeWeight(strokeWt);
        mainSketch.strokeCap(PConstants.ROUND);

        // Draw top black line regardless
        drawLine(0, 2);

        // Draw bottom black line regardless
        drawLine(1, 3);

        // Draw arc regardless, depending on whether the jumper is closed or open
        mainSketch.noFill();
        if (getCurrentState() == 0) drawBezier(3, 4, 2);
        else drawBezier(3, 5, 6);

        // Place yellow dot if locked out
        float xCir = getDs().get(7).getX();
        float yCir = getDs().get(7).getY();
        xCir = calcPos(xCir, scale, panX);
        yCir = calcPos(yCir, scale, panY);
        if(getCurrentState() == 2) {
            mainSketch.strokeWeight(mainSketch.STROKE_FAT);
            mainSketch.stroke(252, 252, 3);
            mainSketch.fill(252,252,3); // yellow
            mainSketch.ellipse( xCir, yCir,unit/2, unit/2);
            mainSketch.fill(0); // black
            drawText(7, "LOCK", "CC", 'H');
        }
        // Draw green dot if present
        if(getNormalState() == 0 && getCurrentState() == 1) {
            mainSketch.stroke(0, 255, 0); // green line
            mainSketch.fill(0, 255, 0); // green fill

            mainSketch.ellipse(xCir, yCir, unit/2, unit/2);
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
        point = new Point(x, y + 1f); // Element #2 - bottom of top vertical line
        points.add(point);
        point = new Point(x, y + 2f); // Element #3 - top of bottom vertical line
        points.add(point);
        point = new Point(x + 0.5f, y + 1.5f); // Element #4 - control point for arc, closed
        points.add(point);
        point = new Point(x + 0.707f, y + 2f); // Element #5 - control point for arc, open
        points.add(point);
        point = new Point(x + 0.707f, y + 1.293f); // Element #6 - end point for arc, open
        points.add(point);
        point = new Point(x, y + 1.5f); // Element #7 - anchor point for text/label
        points.add(point);
        point = new Point(x - 0.5f, y + 1f); // Element #8 - top left mouse click area
        points.add(point);
        point = new Point(x + 0.5f, y + 2f); // Element #9 - bot right mouse click area
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

        // Update the location of the outNode
        this.getOutNode().setCoord(getDs().get(1));

        // Establish the click coordinates
        this.setClickCoords(8, 9);


    } // END calcDrawingCoords

} // END public class Jumper
