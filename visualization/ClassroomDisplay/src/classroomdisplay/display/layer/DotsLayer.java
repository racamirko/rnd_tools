package classroomdisplay.display.layer;

import java.util.Random;
import java.util.Vector;

import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;
import classroomdisplay.display.item.VisualComposition;
import classroomdisplay.display.item.VisualItem;

import processing.core.PApplet;

public class DotsLayer implements Layer {
	protected Vector<VisualItem> thingsToDraw;
	protected PApplet pa;
	protected Random rnd;
	protected VisualComposition composition; 

	public DotsLayer(PApplet pa){
		this.pa = pa;
		rnd = new Random(System.currentTimeMillis());
		thingsToDraw = new Vector<VisualItem>();
	}
	
	public void initialize(Vector<DataItem> items, VisualComposition composition, ClassroomLayout layout){
		VisualItem tmpItem = null;
		this.composition = composition;
		for( DataItem di : items ){
			tmpItem = new VisualItem(this.pa, di, new Point2f(670,330));
			tmpItem.setPosition(layout.getCoordinateForSeat(di.x, di.y), 20+rnd.nextInt(80)); // instant layout
			tmpItem.setComposition(composition);
			thingsToDraw.add(tmpItem);
		}
	}
	
	public void setComposition(VisualComposition composition){
		for( VisualItem vi : thingsToDraw )
			vi.setComposition(composition);
		this.composition = composition;
	}
	
	public VisualComposition getComposition(){
		return composition;
	}
	
	@Override
	public void draw() {
		for( VisualItem vi : thingsToDraw)
			vi.draw();
	}
	
	public VisualItem getItemAt(float x, float y){
		VisualItem item = null;
		for( VisualItem vi : thingsToDraw )
			if( vi.mouseInside(x, y) ){
				item = vi;
				break;
			}
		return item;
	}

}
