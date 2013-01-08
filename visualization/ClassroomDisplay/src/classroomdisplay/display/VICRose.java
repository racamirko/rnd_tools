package classroomdisplay.display;

import processing.core.PApplet;
import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.CombinedAttributeDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;

public class VICRose extends VisualItemComponent {
	public CombinedAttributeDescription attributeRepresented;
	protected float percInnerCircle; 
	
	// display components
	// NOTE: this is all relative to the parent position
	//	and defined in the parent class
//	public Point2f position, size;
//	protected Point2f endPt, stepPt, endSize, stepSize;
	
	// this is in percents of the total size of the element
	protected Point2f directionPt, endDirectionPt, stepDirectionPt;
	
	
	/**
	 * Calculates the size of each segment of the rose graph
	 * @author raca
	 *
	 */
	public class VICRoseHelper{
		
		public VICRoseHelper( DataItem data, CombinedAttributeDescription attrDesc, int series ){
			float val1 = data.getAttributeValue(attrDesc.getSubAttribute(0), series),
				  val2 = data.getAttributeValue(attrDesc.getSubAttribute(1), series),
				  val3 = data.getAttributeValue(attrDesc.getSubAttribute(2), series);
			float innRad = (float) (0.2+0.2*(val1-attrDesc.getSubAttribute(0).getRangeMean())/attrDesc.getSubAttribute(0).getRange());

			float valXRaw = (val2-attrDesc.getSubAttribute(1).getRangeMean())/attrDesc.getSubAttribute(1).getRange();
			float valYRaw = (val3-attrDesc.getSubAttribute(2).getRangeMean())/attrDesc.getSubAttribute(2).getRange();
			
			float dirX = (float) (Math.signum(valXRaw)*(innRad+ Math.pow((1.0f-innRad)*Math.abs(valXRaw),0.3)));
			float dirY = (float) (Math.signum(valYRaw)*(innRad+ Math.pow((1.0f-innRad)*Math.abs(valYRaw),0.3)));
			
			setDirectionalPtPerc(new Point2f(dirX, dirY));
			setInnerCirclePerc(innRad);
		}
	}
	
	public VICRose(VisualItem parent, CombinedAttributeDescription attributeRepresented){
		super(parent, attributeRepresented);
		this.attributeRepresented = attributeRepresented;
		// middle point size
		
		directionPt = new Point2f(0, 0);
		stepDirectionPt = new Point2f(0,0);
		endDirectionPt = directionPt;
		
		percInnerCircle = 20.0f;
	}

	public void setDirectionalPtPerc(Point2f pt){
		setDirectionalPtPerc(pt, 20.0f);
	}
	
	public void setDirectionalPtPerc(Point2f pt, float steps){
		endDirectionPt = pt;
		stepDirectionPt.x = ( endDirectionPt.x - directionPt.x )/steps;
		stepDirectionPt.y = ( endDirectionPt.y - directionPt.y )/steps;
	}
	
	public void setInnerCirclePerc( float perc ){
		percInnerCircle = perc;
	}
	
	public void draw(){
		update();
		parent.pa.ellipseMode(PApplet.CENTER);
		parent.pa.fill(attributeRepresented.color);
		// inner circle
		parent.pa.ellipse( parent.position.x+position.x, parent.position.y+position.y, size.x*percInnerCircle, size.y*percInnerCircle);
		// direction arrow
		// yes, this is stupid and I should use push/pop transaction stacks
		parent.pa.triangle(parent.position.x+position.x-1, parent.position.y+position.y-1,
  						   parent.position.x+position.x+1, parent.position.y+position.y+1,
						   parent.position.x+position.x+size.x*directionPt.x, parent.position.y+position.y+size.x*directionPt.y);
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
