package old.oldcomponents;

import model.geometry.Point;
import old.main.OldMain;
import processing.core.PConstants;

import java.util.ArrayList;

public class CloneBreaker12kV extends Breaker12kV {

    private OldComponent clone;
    private String cloneType;
    private String color;
    private String substationAttach;
    private int substationAttachNode;


    public CloneBreaker12kV(OldMain mainSketch, int id, String name, String type, char orientation, int normalState, int xPos, int yPos, int length, String label, String textAnchor, char labelOrientation, char labelPlacement, String associatedWith, OldComponent clone,
                            String cloneType, String color, String substationAttach, int substationAttachNode) {
        super(mainSketch, id, name, type, orientation, normalState, xPos, yPos, length, label, textAnchor, labelOrientation, labelPlacement, associatedWith);
        this.clone = clone;
        this.cloneType = cloneType;
        this.color = color;
        this.substationAttach = substationAttach;
        this.substationAttachNode = substationAttachNode;

        if(cloneType.equals("12KV_DIAGRAM")) {
            calcCloneDrawingCoords();
        }

    } // END Constructor #0

    public OldComponent getClone() {
        return clone;
    }

    public void setInNodeEnergy(boolean energized) {
        this.getInNode().setEnergized(energized);
    }

    public void renderEnergy(float scale, float panX, float panY) {

        // Draw energy lines depending on cloneType
        if(!cloneType.equals("12KV_DIAGRAM")) {

            // Set drawing parameters
            float strokeWt = mainSketch.STROKE_FAT * scale;
            mainSketch.stroke(mainSketch.YELLOW);  // yellow
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
                drawBox(4, 5, 6, 7, 1);
            }
        } // END if not a 12 kV diagram clone
        else {

            // Set drawing parameters
            float strokeWt = mainSketch.STROKE_FAT * scale;
            mainSketch.stroke(getColor(color)[0], getColor(color)[1], getColor(color)[2]);
            mainSketch.strokeWeight(strokeWt);
            mainSketch.strokeCap(PConstants.SQUARE);

            // Draw energy line for 12KV_DIAGRAM clone
            if ((getInNode().isEnergized() && getCurrentState() == 0) ||
                    getOutNode().isEnergized() && getCurrentState() == 0) {
                drawLine(0, 1);
            }

        } // END if it is a 12 kV diagram clone

    } // END renderEnergy()

    public void renderLines(float scale, float panX, float panY) {


        float unit = mainSketch.UNIT * scale;

        int x = calcPos((int) (getInNode().getCoord().getX()), scale, panX);
        int y = calcPos((int) (getInNode().getCoord().getY()), scale, panY);

        // Draw lines depending on cloneType
        if(!cloneType.equals("12KV_DIAGRAM")) {

            // Set drawing parameters
            float strokeWt = mainSketch.STROKE_THIN * scale;
            mainSketch.stroke(getColor("BLACK")[0], getColor("BLACK")[1], getColor("BLACK")[2]);  // black
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
            else if (getCurrentState() == 2) mainSketch.fill(252, 252, 3);  // yellow
            else mainSketch.fill(0, 255, 0); // green
            drawBox(4, 5, 6, 7, 1);

            // Inscribe "LOCK" when locked open
            if (getCurrentState() == 2) {
                drawText(8, "LOCK", "CC", 'H');
            }

            // Inscribe "N/C" when open and "N/O" when closed at dS(8) always facing upward ==> 'R' orientation
            else if (!(getNormalState() == getCurrentState())) {
                if (getNormalState() == 0) {
                    drawText(8, "N/C", "CC", 'H');
                } else {
                    drawText(8, "N/O", "CC", 'H');
                }
            }
            // Place text/label at dS(9) if Right, otherwise ds(22) if left placement
            mainSketch.fill(0); // black
            if (getLabelPlacement() == 'R') {
                drawText(9, getLabel(), getTextAnchor(), getLabelOrientation());
            } else {
                drawText(22, getLabel(), getTextAnchor(), getLabelOrientation());
            }
        } // END draw lines for normal clone
        else { // for 12KV_DIAGRAM clone

            // Set drawing parameters
            float strokeWt = mainSketch.STROKE_THIN * scale;
            mainSketch.stroke(0);
            mainSketch.strokeWeight(strokeWt);
            mainSketch.strokeCap(PConstants.SQUARE);

            // Draw colored line regardless
            drawLine(0, 1);

            // Place text
            int textAnchorPt = 0;
            String textAnchorType = "CC";
            char labelOrientation = 'H';
            switch (getOrientation()) {
                case 'L' :
                    textAnchorPt = 3;
                    textAnchorType = "CT";
                    break;
                case 'U'  :
                    textAnchorPt = 2;
                    textAnchorType = "LC";
                    break;
                case 'R' :
                    textAnchorPt = 2;
                    textAnchorType = "CT";
                    break;
                default :
                    textAnchorPt = 3;
                    textAnchorType = "LC";
                    break;
            } // END switch(getOrientation)
            mainSketch.fill(getColor(color)[0], getColor(color)[1], getColor(color)[2]);
            drawText(textAnchorPt, getLabel(), textAnchorType, labelOrientation);

        } // END else for 12KV_DIAGRAM clone

    } // END renderLines()

    private void calcCloneDrawingCoords() {

        ArrayList<Point> points = new ArrayList<>();

        // Set the inNode, by first looking for the substation it is attached to
        boolean foundSub = false;
        Point inNodePoint = null;
        OldNode inOldNodeClone = clone.getInNode();
        OldNode outOldNodeClone = clone.getOutNode();

        // Set the inNode
        this.setInNode(new OldNode(inNodePoint, inOldNodeClone.isEnergized()));
        // Set the outNode coordinate
        int length = 2;
        Point newOutNode = new Point(inNodePoint.getX(), inNodePoint.getY() + length);
        switch (getOrientation()) {
            case 'U' :
                newOutNode = new Point(inNodePoint.getX(), inNodePoint.getY() - length);
                break;
            case 'L' :
                newOutNode = new Point(inNodePoint.getX() - length, inNodePoint.getY());
                break;
            case 'R' :
                newOutNode = new Point(inNodePoint.getX() + length, inNodePoint.getY());
                break;
            default :
                break;
        } // END switch(orientation)
        this.setOutNode(new OldNode(newOutNode, outOldNodeClone.isEnergized()));

        float x = this.getInNode().getCoord().getX();
        float y = this.getInNode().getCoord().getY();

        // This step puts the inNode in the ArrayList as element #0.  This is important!
        points.add(this.getInNode().getCoord());

        // This next step puts the outNode in the ArrayList as element #1.  This is also important!
        points.add(this.getOutNode().getCoord());

        // These next steps define specific points
        Point point;
        point = new Point(x + 0.75f, y + 1.0f);     // Element #2 - Right text label anchor point
        points.add(point);
        point = new Point(x - 0.75f, y + 1.0f);     // Element #3 - Left text label anchor
        points.add(point);
        point = new Point(x - 0.25f, y);                  // Element #4 - Top left click area
        points.add(point);
        point = new Point(x + 0.25f, y + 2.0f);     // Element #5 - Bottom right click area
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
        this.setClickCoords(4, 5);

    } // END calcCloneDrawingCoords

    private int[] getColor(String color) {

        int[] colorRGB = {0, 0, 0};
        switch (color) {
            case "RED" :
                colorRGB[0] = 255;   // red component
                colorRGB[1] = 0;     // green component
                colorRGB[2] = 0;     // blue component
                break;
            case "GREEN" :
                colorRGB[0] = 0;     // red component
                colorRGB[1] = 255;   // green component
                colorRGB[2] = 0;     // blue component
                break;
            case "LTGREEN" :
                colorRGB[0] = 0;     // red component
                colorRGB[1] = 255;   // green component
                colorRGB[2] = 128;     // blue component
                break;
            case "BLUE" :
                colorRGB[0] = 0;     // red component
                colorRGB[1] = 0;     // green component
                colorRGB[2] = 255;   // blue component
                break;
            case "ORANGE" :
                colorRGB[0] = 255;   // red component
                colorRGB[1] = 128;   // green component
                colorRGB[2] = 0;     // blue component
                break;
            case "PURPLE" :
                colorRGB[0] = 102;   // red component
                colorRGB[1] = 0;   // green component
                colorRGB[2] = 102;   // blue component
                break;
            case "YELLOW" :
                colorRGB[0] = 252;   // red component
                colorRGB[1] = 252;   // green component
                colorRGB[2] = 3;   // blue component
                break;
            default:
                colorRGB[0] = 0;     // red component
                colorRGB[1] = 0;     // green component
                colorRGB[2] = 0;     // blue component
                break;
        } // END switch(color)

        return colorRGB;

    } // END getColor

} // END public class CloneBreaker12kV
