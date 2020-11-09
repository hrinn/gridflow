package old.oldcomponents;

import model.geometry.Point;
import old.main.OldMain;
import processing.core.PConstants;

import java.util.ArrayList;

public class Cutout extends OldComponent {

    // TODO:  Cutout teardrop does not render energy correctly when locked out

    private String cutoutDirection;

    public Cutout(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, int xPos, int yPos, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith, String cutoutDirection) {
        super(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        this.cutoutDirection = cutoutDirection;
        calcDrawingCoords();
    } // END Constructor #0

    public Cutout(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedTo, String inout, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith, String cutoutDirection) {
        super(mainSketch, id, name, type, orientation, normalState, connectedTo, inout, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        this.cutoutDirection = cutoutDirection;
        calcDrawingCoords();
    } // END Constructor #1

    public Cutout(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, OldComponent connectedToIn, String inoutIn, OldComponent connectedToOut, String inoutOut, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith, String cutoutDirection) {
        super(mainSketch, id, name, type, orientation, normalState, connectedToIn, inoutIn, connectedToOut, inoutOut, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        this.cutoutDirection = cutoutDirection;
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

        // Draw energy teardrop if present, depending on whether the cutout is closed or open
        // Energy teardrop is present IF 1) inNode is hot AND cutout is closed
        // OR IF 2) outNode is energized (then draw depending on open/closed)
        float startArcAngle = 0; // expressed in PI later
        // If cutout is closed and either node is energized
        if (getCurrentState() == 0 && (getInNode().isEnergized() || getOutNode().isEnergized())) {
            if(cutoutDirection.equals("UPFEED")) {
                switch (this.getOrientation()) {
                    case 'U':
                        startArcAngle = 1;
                        break;
                    case 'R':
                        startArcAngle = 1.5f;
                        break;
                    case 'L':
                        startArcAngle = 0.5f;
                        break;
                    default:
                        startArcAngle = 0;
                        break;
                } // END switch (orientation) for "UPFEED"
                // Draw lines and semi-circle to make teardrop
                drawLine(2, 4);
                drawLine(2, 6);
                drawArc(5, startArcAngle, 0.25f);
            } else {
                switch (this.getOrientation()) {
                    case 'U':
                        startArcAngle = 0;
                        break;
                    case 'R':
                        startArcAngle = 0.5f;
                        break;
                    case 'L':
                        startArcAngle = 1.5f;
                        break;
                    default:
                        startArcAngle = 1;
                        break;
                } // END switch (orientation)
                // Draw lines and semi-circle to make teardrop
                drawLine(3, 4);
                drawLine(3, 6);
                drawArc(5, startArcAngle, 0.25f);
            } // END else if not "UPFEED"

        } // END if energized and closed
        // Else if cutout is open, but outNode is energized, render open
            else if(getCurrentState() == 1){
                if(cutoutDirection.equals("UPFEED") && getInNode().isEnergized()) {
                    switch (this.getOrientation()) {
                        case 'U':
                            startArcAngle = 1.75f;
                            break;
                        case 'R':
                            startArcAngle = 0.25f;
                            break;
                        case 'L':
                            startArcAngle = 1.25f;
                            break;
                        default:
                            startArcAngle = 0.75f;
                            break;
                    } // END switch (orientation)
                    drawLine(2, 7);
                    drawLine(2, 9);
                    drawArc(8, startArcAngle, 0.25f);
                } else if(!cutoutDirection.equals("UPFEED") && getOutNode().isEnergized()){
                    switch (this.getOrientation()) {
                        case 'U':
                            startArcAngle = 0.75f;
                            break;
                        case 'R':
                            startArcAngle = 1.25f;
                            break;
                        case 'L':
                            startArcAngle = 0.25f;
                            break;
                        default:
                            startArcAngle = 1.75f;
                            break;
                    } // END switch (orientation)
                    drawLine(3, 7);
                    drawLine(3, 9);
                    drawArc(8, startArcAngle, 0.25f);
                }

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

        // Draw teardrop depending on whether the cutout is closed or open
        float startArcAngle = 0; // expressed in PI later
        // If closed
        if (getCurrentState() == 0) {
            if(cutoutDirection.equals("UPFEED")) {
                switch (this.getOrientation()) {
                    case 'U':
                        startArcAngle = 1;
                        break;
                    case 'R':
                        startArcAngle = 1.5f;
                        break;
                    case 'L':
                        startArcAngle = 0.5f;
                        break;
                    default:
                        startArcAngle = 0;
                        break;
                } // END switch (orientation)
                // Draw lines and semi-circle to make teardrop
                drawLine(2, 4);
                drawLine(2, 6);
                drawArc(5, startArcAngle, 0.25f);
                drawTeardropDot(5, 0.02f);
            } else {
                switch (this.getOrientation()) {
                    case 'U':
                        startArcAngle = 0;
                        break;
                    case 'R':
                        startArcAngle = 0.5f;
                        break;
                    case 'L':
                        startArcAngle = 1.5f;
                        break;
                    default:
                        startArcAngle = 1;
                        break;
                } // END switch (orientation)
                // Draw lines and semi-circle to make teardrop
                drawLine(3, 4);
                drawLine(3, 6);
                drawArc(5, startArcAngle, 0.25f);
                drawTeardropDot(5, 0.02f);
            }

        } // END if closed
        else { // If open
            if(cutoutDirection.equals("UPFEED")) {
                switch (this.getOrientation()) {
                    case 'U':
                        startArcAngle = 1.75f;
                        break;
                    case 'R':
                        startArcAngle = 0.25f;
                        break;
                    case 'L':
                        startArcAngle = 1.25f;
                        break;
                    default:
                        startArcAngle = 0.75f;
                        break;
                }
                drawLine(2, 7);
                drawLine(2, 9);
                drawArc(8, startArcAngle, 0.25f);
            } else {
                switch (this.getOrientation()) {
                    case 'U':
                        startArcAngle = 0.75f;
                        break;
                    case 'R':
                        startArcAngle = 1.25f;
                        break;
                    case 'L':
                        startArcAngle = 0.25f;
                        break;
                    default:
                        startArcAngle = 1.75f;
                        break;
                } // END switch (orientation)
                drawLine(3, 7);
                drawLine(3, 9);
                drawArc(8, startArcAngle, 0.25f);
            }

            // this step puts a little black dot in the middle of the teardrop
            drawTeardropDot(8, 0.02f);
        } // END if open

        // Place yellow dot if locked out
        float xCir = getDs().get(10).getX();
        float yCir = getDs().get(10).getY();
        xCir = calcPos(xCir, scale, panX);
        yCir = calcPos(yCir, scale, panY);
        if(getCurrentState() == 2) {
            mainSketch.strokeWeight(mainSketch.STROKE_FAT);
            mainSketch.stroke(252, 252, 3);
            mainSketch.fill(252,252,3); // yellow
            mainSketch.ellipse( xCir, yCir,unit/2, unit/2);
            mainSketch.fill(0); // black
            drawText(10, "LOCK", "CC", 'H');
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

        if(cutoutDirection.equals("UPFEED")) {
            point = new Point(x + 0.125f, y + 1.875f); // Element #4 - left edge of closed cutout
            points.add(point);
            point = new Point(x, y + 1.875f); // Element #5 - center point of closed cutout semi-circle
            points.add(point);
            point = new Point(x - 0.125f, y + 1.875f); // Element #6 - right edge of closed cutout
            points.add(point);
            point = new Point(x - 0.707f, y + 0.47f); // Element #7 - left edge of open cutout
            points.add(point);
            point = new Point(x - 0.619f, y + 0.381f); // Element #8 - centerpoint of open cutout semi-circle
            points.add(point);
            point = new Point(x - 0.53f, y + 0.293f); // Element #9 - right edge of open cutout
            points.add(point);
        } else {
            point = new Point(x - 0.125f, y + 1.125f); // Element #4 - left edge of closed cutout
            points.add(point);
            point = new Point(x, y + 1.125f); // Element #5 - center point of closed cutout semi-circle
            points.add(point);
            point = new Point(x + 0.125f, y + 1.125f); // Element #6 - right edge of closed cutout
            points.add(point);
            point = new Point(x + 0.707f, y + 2.53f); // Element #7 - left edge of open cutout
            points.add(point);
            point = new Point(x + 0.619f, y + 2.619f); // Element #8 - centerpoint of open cutout semi-circle
            points.add(point);
            point = new Point(x + 0.53f, y + 2.707f); // Element #9 - right edge of open cutout
            points.add(point);
        }
        point = new Point(x, y + 1.5f); // Element #10 - dot anchor for lockout and N/C
        points.add(point);
        point = new Point(x - 0.5f, y + 1f); // Element #11 - top left mouse click area
        points.add(point);
        point = new Point(x + 0.5f, y + 2f); // Element #12 - bot right mouse click area
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
        this.setClickCoords(11, 12);

    } // END calcDrawingCoords

} // END public class Cutout
