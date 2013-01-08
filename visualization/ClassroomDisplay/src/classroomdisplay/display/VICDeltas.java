package classroomdisplay.display;

import processing.core.PApplet;
import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.CombinedAttributeDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;

public class VICDeltas extends VisualItemComponent {
	// display components
	// NOTE: this is all relative to the parent position
	//	and defined in the parent class
//	public Point2f position, size;
//	protected Point2f endPt, stepPt, endSize, stepSize;
	
	// this is in percents of the total size of the element
	protected Point2f directionPt, endDirectionPt, stepDirectionPt;
	protected Point2f edgePoint1, edgePoint2; // startPoints for the triangle. can be either upper or lower edge points
	protected String text;
	
	/**
	 * Calculates the size of each segment of the rose graph
	 * @author raca
	 *
	 */
	public class VICDeltaHelper{
		
		public VICDeltaHelper( DataItem data, AttributeDescription attrDesc, int series ){
			float val1 = data.getAttributeValue(attrDesc, series);
			if( val1 > 0 )
				setTriangleStartPoints(new Point2f(-20,-10), new Point2f(20,-10));
			else
				setTriangleStartPoints(new Point2f(-20,10), new Point2f(20,10));
			
			float valRaw = (float) ((-1.0)*Math.signum(val1)*Math.pow( Math.abs( val1/(attrDesc.getRange()/2.0f)),0.3));
			
			setDirectionalPtPerc(new Point2f(0.0f, valRaw));
			setText(String.format("%.2f", val1));
		}
	}
	
	public VICDeltas(VisualItem parent, AttributeDescription attributeRepresented){
		super(parent, attributeRepresented);
		// middle point size
		
		directionPt = new Point2f(0, 0);
		stepDirectionPt = new Point2f(0,0);
		endDirectionPt = directionPt;
		edgePoint1 = new Point2f(-20, -10);
		edgePoint2 = new Point2f(20, -10);
	}

	public void setDirectionalPtPerc(Point2f pt){
		setDirectionalPtPerc(pt, 20.0f);
	}
	
	public void setDirectionalPtPerc(Point2f pt, float steps){
		endDirectionPt = pt;
		stepDirectionPt.x = ( endDirectionPt.x - directionPt.x )/steps;
		stepDirectionPt.y = ( endDirectionPt.y - directionPt.y )/steps;
	}
	
	public void setTriangleStartPoints(Point2f pt1, Point2f pt2){
		edgePoint1 =  pt1;
		edgePoint2 = pt2;
	}
	
	public void setText(String text){
		this.text = text;  
	}
	
	public void draw(){
		update();
		parent.pa.fill(attributeRepresented.color);
		// inner square with text
//		parent.pa.ellipse( parent.position.x+position.x, parent.position.y+position.y, size.x*percInnerCircle, size.y*percInnerCircle);
		int color = attributeRepresented.color;
		float centerX = parent.position.x+position.x,
			  centerY = parent.position.y+position.y;
		if( !text.equals("0.00") )
			parent.pa.triangle(centerX+edgePoint1.x, centerY+edgePoint1.y, centerX+edgePoint2.x, centerY+edgePoint2.y,
						   centerX+size.x*directionPt.x, centerY+size.x*directionPt.y);
		parent.pa.rect(centerX-20, centerY-10, 40, 20);
		int redC = (int) parent.pa.red(color),
			greenC = (int) parent.pa.green(color),
			blueC = (int) parent.pa.blue(color);
		parent.pa.pushStyle();
		parent.pa.fill(255-redC, 255-greenC, 255-blueC);
		parent.pa.textSize(13.0f);
		parent.pa.text(text, centerX-15, centerY+5);
		parent.pa.popStyle();
		// direction arrow
		// yes, this is stupid and I should use push/pop transaction stacks
	}
	
	
	protected void update(){
		super.update();
		// direction
		if( directionPt != endDirectionPt ){
			isChanging = true;
			directionPt.add(stepDirectionPt);
			if( directionPt.distance(endDirectionPt) < 3.0f)
				directionPt = endDirectionPt;

		}
	}	
}
