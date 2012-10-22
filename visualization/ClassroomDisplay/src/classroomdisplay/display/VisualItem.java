package classroomdisplay.display;

import java.util.Collections;
import java.util.Vector;

import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.AttributeDescription.eAttributeDisplayType;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;

import processing.core.PApplet;

public class VisualItem {
	public PApplet pa;
	protected DataItem dataItem;
	protected Vector<VisualItemComponent> visualParts;
	
	protected Point2f endPt, stepPt;
	public Point2f position;
	
	public VisualItem( PApplet pa, DataItem dataItem ){
		this.pa = pa;
		this.dataItem = dataItem;
		visualParts = new Vector<VisualItemComponent>();
		position = new Point2f(0,0);
		endPt = position;
		stepPt = new Point2f(0, 0);
	}
	
	public void setPosition(Point2f position){
		setPosition(position, 100);
	}
	
	public void setPosition(Point2f position, float steps){
		endPt = position;
		stepPt.x = ( endPt.x - this.position.x )/steps;
		stepPt.y = ( endPt.y - this.position.y )/steps;
	}
	
	public void draw(){
		update();
		for( VisualItemComponent vic : visualParts )
			vic.draw();
	}
	
	protected void update(){
		// movement
		if( position != endPt ){
			position.add(stepPt);
			if( position.distance(endPt) < 3.0f)
				position = endPt;
		}
	}
	
	public void setComposition(VisualComposition composition){
		Vector<VisualItemComponent> newDisplayVec = new Vector<VisualItemComponent>();
		// find inner radius - not dealing with several radiuses right now
		float maxRadius = 30.0f, radius = 5.0f, ringStep = 10.0f;
		AttributeDescription centerAttribute = null;
		VisualItemComponent tmpCmp = null;
		for( AttributeDescription attr : composition.attributesToDisplay ){
			if( attr.displayType == eAttributeDisplayType.ATD_CENTER ){
				radius += maxRadius*(dataItem.getAttributeValue(attr)-attr.minRange)/(attr.maxRange-attr.minRange);
				tmpCmp = new VisualItemComponent(this, attr);
				tmpCmp.setSize(new Point2f(radius, radius));
				newDisplayVec.add(tmpCmp);
				centerAttribute = attr;
				break;
			}
		}
		
		if( centerAttribute == null ){
			radius =  2.0f;
		}
		
		for( AttributeDescription attr : composition.attributesToDisplay ){
			boolean found = false;
			if( attr == centerAttribute )
				continue;
			for( VisualItemComponent vi : visualParts ){
				if( vi.attributeRepresented == attr ){
					radius += ringStep;
					vi.setSize(new Point2f(radius, radius));
					newDisplayVec.add(vi);
					found = true;
					break;
				}
			}
			if( found )
				continue;
			// if it reaches here - didn't find it
			// check if the value should be displayed at all
			if( dataItem.getAttributeValue(attr) == attr.minRange )
				continue;
			tmpCmp = new VisualItemComponent(this, attr);
			radius += ringStep;
			tmpCmp.setSize(new Point2f(radius, radius));
			newDisplayVec.add(tmpCmp);
		}
		// update new display list
		visualParts.clear();
		visualParts.addAll(newDisplayVec);
		Collections.reverse(visualParts);
	}

}
