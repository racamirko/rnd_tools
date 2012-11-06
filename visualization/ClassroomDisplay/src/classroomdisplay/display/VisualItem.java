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
	protected Vector<VisualItemComponent> toRemove;
	
	protected Point2f endPt, stepPt;
	public Point2f position;

	
	public VisualItem( PApplet pa, DataItem dataItem, Point2f startPos ){
		this.pa = pa;
		this.dataItem = dataItem;
		visualParts = new Vector<VisualItemComponent>();
		toRemove = new Vector<VisualItemComponent>();
		position = startPos;
		endPt = position;
		stepPt = new Point2f(0, 0);
	}
	
	public VisualItem( PApplet pa, DataItem dataItem ){
		this.pa = pa;
		this.dataItem = dataItem;
		visualParts = new Vector<VisualItemComponent>();
		toRemove = new Vector<VisualItemComponent>();
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
		toRemove.clear();
		for( VisualItemComponent vic : visualParts ){
			vic.draw();
			if( vic.size.x == 0 && vic.size.y == 0 && !vic.isChanging )
				toRemove.add(vic);
		}
		// remove things which are not active any more
		for( VisualItemComponent vic : toRemove )
			visualParts.remove(vic);
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
		Vector<VisualItemComponent> newDisplayVec = new Vector<VisualItemComponent>(),
									toDiscardWhenDone = new Vector<VisualItemComponent>(); 
		// find inner radius - not dealing with several radiuses right now
		float maxRadius = 30.0f, radius = 5.0f, ringStep = 10.0f;
		AttributeDescription centerAttribute = null;
		VisualItemComponent tmpCmp = null;
		for( AttributeDescription attr : composition.attributesToDisplay ){
			if( attr.displayType == eAttributeDisplayType.ATD_CENTER ){
				// test if it's already displayed
				if( visualParts.size() > 0 && visualParts.get(visualParts.size()-1).attributeRepresented == attr )
				{
					newDisplayVec.add(visualParts.get(visualParts.size()-1));
					radius += visualParts.get(visualParts.size()-1).size.x;
				} else {
					// if it's a different element, calculate it's new size and insert it
					radius += maxRadius*(dataItem.getAttributeValue(attr)-attr.minRange)/(attr.maxRange-attr.minRange);
					tmpCmp = new VisualItemComponent(this, attr);
					tmpCmp.setSize(new Point2f(radius, radius));
					newDisplayVec.add(tmpCmp);
				}
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
		// now add the stuff that's going to dissapear
		for( VisualItemComponent vis : visualParts ){
			boolean found = false;
			for( AttributeDescription attr : composition.attributesToDisplay ){
				if( vis.attributeRepresented == attr ){
					found = true;
					break;
				}
			}
			if(!found){
				vis.setPosition(new Point2f(0,0)); // just to be on a safe side
				vis.setSize(new Point2f(0,0));
				newDisplayVec.add(vis);
			}
		}
		// update new display list
		visualParts.clear();
		visualParts.addAll(newDisplayVec);
		Collections.reverse(visualParts);
	}

}
