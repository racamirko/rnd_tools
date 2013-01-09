package classroomdisplay.display.item;

import java.util.Collections;
import java.util.Vector;

import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.AttributeDescription.eAttributeDisplayType;
import classroomdisplay.data.CombinedAttributeDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;
import classroomdisplay.display.item.VICRose.VICRoseHelper;

import processing.core.PApplet;

public class VisualItem {
	public PApplet pa;
	protected DataItem dataItem;
	protected Vector<VisualItemComponent> visualParts;
	protected Vector<VisualItemComponent> toRemove;
	protected VisualComposition currentComposition;
	
	protected Point2f endPt, stepPt;
	public Point2f position;
	protected float maxRadius;

	
	public VisualItem( PApplet pa, DataItem dataItem, Point2f startPos ){
		this.pa = pa;
		this.dataItem = dataItem;
		visualParts = new Vector<VisualItemComponent>();
		toRemove = new Vector<VisualItemComponent>();
		position = startPos;
		endPt = position;
		stepPt = new Point2f(0, 0);
		maxRadius = 30.0f;
	}
	
	public VisualItem( PApplet pa, DataItem dataItem ){
		this.pa = pa;
		this.dataItem = dataItem;
		visualParts = new Vector<VisualItemComponent>();
		toRemove = new Vector<VisualItemComponent>();
		position = new Point2f(0,0);
		endPt = position;
		stepPt = new Point2f(0, 0);
		maxRadius = 30.0f;
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
	
	public boolean mouseInside(float x, float y){
		float xMin = position.x - maxRadius,
			  xMax = position.x + maxRadius,
			  yMin = position.y - maxRadius,
			  yMax = position.y + maxRadius;
		if( xMin <= x && x <= xMax && yMin <= y && y <= yMax )
			return true;
		return false;
	}
	
	public void setComposition(VisualComposition composition){
		Vector<VisualItemComponent> newDisplayVec = new Vector<VisualItemComponent>(),
									toDiscardWhenDone = new Vector<VisualItemComponent>(); 
		// find inner radius - not dealing with several radiuses right now
		float radius = 5.0f, ringStep = 10.0f;
		AttributeDescription centerAttribute = null;
		VisualItemComponent tmpCmp = null;
		for( AttributeDescription attr : composition.attributesToDisplay ){
			if( attr.displayType == eAttributeDisplayType.ATD_CENTER && centerAttribute == null ){
				radius += maxRadius*(dataItem.getAttributeValue(attr, composition.series)-attr.minRange)/(attr.maxRange-attr.minRange);
				// test if it's already displayed
				if( visualParts.size() > 0 && visualParts.get(visualParts.size()-1).attributeRepresented == attr )
				{
					// set new size, if different
					visualParts.get(visualParts.size()-1).setSize(new Point2f(radius, radius));
					newDisplayVec.add(visualParts.get(visualParts.size()-1));
				} else {
					// if it's a different element, calculate it's new size and insert it
					tmpCmp = new VisualItemComponent(this, attr);
					tmpCmp.setSize(new Point2f(radius, radius));
					newDisplayVec.add(tmpCmp);
				}
				centerAttribute = attr;
			}
			if( attr.displayType == eAttributeDisplayType.ATD_COMBINED_ROSE ){
				// ignore all others, we can only display 1 complex attribute at the time
				centerAttribute = attr;
				composition.attributesToDisplay.clear();
				composition.attributesToDisplay.add(centerAttribute);
				VICRose tmpRose = new VICRose(this, (CombinedAttributeDescription) attr);
				tmpRose.setSize(new Point2f(maxRadius, maxRadius));
				tmpRose.new VICRoseHelper(dataItem, (CombinedAttributeDescription) centerAttribute, composition.series);
				newDisplayVec.add(tmpRose);
				break;
			}
			if( attr.displayType == eAttributeDisplayType.ATD_DELTA ){
				// ignore all others, we can only display 1 complex attribute at the time
				centerAttribute = attr;
				composition.attributesToDisplay.clear();
				composition.attributesToDisplay.add(centerAttribute);
				VICDeltas tmpDelta = new VICDeltas(this, attr);
				tmpDelta.setSize(new Point2f(maxRadius, maxRadius));
				tmpDelta.new VICDeltaHelper(dataItem, centerAttribute, composition.series);
				newDisplayVec.add(tmpDelta);
				break;
			}
		}
		
		if( centerAttribute == null ){
			radius =  2.0f;
		}
		
		if( centerAttribute == null || centerAttribute.displayType != eAttributeDisplayType.ATD_COMBINED_ROSE && centerAttribute.displayType != eAttributeDisplayType.ATD_DELTA ){
			for( AttributeDescription attr : composition.attributesToDisplay ){
				boolean found = false;
				if( attr == centerAttribute )
					continue;
				for( VisualItemComponent vi : visualParts ){
					if( vi.attributeRepresented == attr ){
						// should the item still be represented
						if( dataItem.getAttributeValue(attr, composition.series) != attr.minRange ){
							radius += ringStep;
							vi.setSize(new Point2f(radius, radius));
						} else {
							vi.setPosition(new Point2f(0,0)); // just to be on a safe side
							vi.setSize(new Point2f(0,0));
						}
						newDisplayVec.add(vi);
						found = true;
						break;
					}
				}
				if( found )
					continue;
				// if it reaches here - didn't find it
				// check if the value should be displayed at all
				if( dataItem.getAttributeValue(attr, composition.series) == attr.minRange )
					continue;
				tmpCmp = new VisualItemComponent(this, attr);
				radius += ringStep;
				tmpCmp.setSize(new Point2f(radius, radius));
				newDisplayVec.add(tmpCmp);
			}
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
		currentComposition = composition;
	}
	
	public boolean isHighlighted(){
		boolean value = false;
		for( AttributeDescription attDesc : currentComposition.attributesToDisplay )
			if( attDesc.displayType == eAttributeDisplayType.ATD_HIGHLIGHT ){
				value = true;
				break;
			}
		return value;
	}

	public VisualComposition getComposition(){
		return currentComposition;
	}

	public DataItem getDataItem() {
		return dataItem;
	}

}
