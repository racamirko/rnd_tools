package classroomdisplay.display;

import processing.core.PApplet;
import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.Point2f;

public class VisualItemComponent {
	public VisualItem parent;
	public AttributeDescription attributeRepresented;
	
	// display components
	// NOTE: this is all relative to the parent position
	public Point2f position, size;
	protected Point2f endPt, stepPt, endSize, stepSize;
	

	public VisualItemComponent(VisualItem parent, AttributeDescription attributeRepresented){
		this.parent = parent;
		this.attributeRepresented = attributeRepresented;
		
		position = new Point2f(0, 0);
		stepPt = new Point2f(0,0);
		endPt = position;
		
		size = new Point2f(1,1);
		stepSize = new Point2f(0,0);
		endSize = size;
	}
	
	public void draw(){
		update();
		parent.pa.ellipseMode(PApplet.CENTER);
		parent.pa.fill(attributeRepresented.color);
		parent.pa.ellipse( parent.position.x+position.x, parent.position.y+position.y, size.x, size.y);
	}
	
	public void setPosition(Point2f position){
		setPosition(position, 100);
	}
	
	public void setPosition(Point2f position, float steps){
		endPt = position;
		stepPt.x = ( endPt.x - this.position.x )/steps;
		stepPt.y = ( endPt.y - this.position.y )/steps;
	}
	
	public void setSize( Point2f size, float steps ){
		endSize = size;
		stepSize.x = ( size.x - this.size.x ) / steps;
		stepSize.y = ( size.y - this.size.y ) / steps;
	}
	
	public void setSize(Point2f size){
		setSize(size, 100);
	}
	
	protected void update(){
		// movement
		if( position != endPt ){
			position.add(stepPt);
			if( position.distance(endPt) < 3.0f)
				position = endPt;
		}
		// size
		if( size != endSize ){
			size.add(stepSize);
			if( size.distance(endSize) < 3.0f ){
				size = endSize;
			}
		}
	}	
}
