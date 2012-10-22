package classroomdisplay;

import java.util.Random;
import java.util.Vector;

import classroomdisplay.data.DataDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;
import classroomdisplay.display.VisualComposition;
import classroomdisplay.display.VisualItem;

import processing.core.*;


public class ClassroomDisplay extends PApplet {
	private static final long serialVersionUID = 8767955545276677628L;
	Vector<VisualItem> thingsToDraw;
	protected DataDescription dataDesc;
	protected Vector<DataItem> data;
	protected Random rnd;

	int sizeX = 1040;
	int sizeY = 660;

	public void setup() {
	  size(sizeX, sizeY);
	  rnd = new Random(System.currentTimeMillis());
	  TestDataGenerator td = new TestDataGenerator(this, 100);
	  dataDesc = td.dataDesc;
	  data = td.data;
	  generateVisualItems();
	}

	private void generateVisualItems() {
		thingsToDraw = new Vector<VisualItem>();
		VisualItem tmpItem = null;
		for( DataItem di : data ){
			tmpItem = new VisualItem(this, di);
			tmpItem.setPosition(new Point2f(70+di.x*90, 70+di.y*60) ); // instant layout
			thingsToDraw.add(tmpItem);
		}
		mousePressed();
	}

	public void draw() {
	  fill(color(150,150,150));
	  rect(0,0, sizeX, sizeY);
	  
	  for( VisualItem vi : thingsToDraw)
		  vi.draw();
	}
	
	public void mousePressed() {
		// switch to the next random layout
		// generate composition
		VisualComposition visComp = new VisualComposition();
		visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsCenter.get( rnd.nextInt( dataDesc.attrsCenter.size() ))  )  );
		
		switch( rnd.nextInt(3) ){
			case 0:
				visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsRings.get( rnd.nextInt( dataDesc.attrsRings.size() ))  )  );
				break;
			case 1:
				int skipIndex = rnd.nextInt(dataDesc.attrsRings.size());
				for( int i = 0; i < dataDesc.attrsRings.size(); i++ ){
					if( i == skipIndex )
						continue;
					visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsRings.get( i )));
				}
				break;
			case 2:
				for( int i = 0; i < dataDesc.attrsRings.size(); i++ ){
					visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsRings.get( i )));
				}
				break;
		}
		// update all visible items
		for( VisualItem vi : thingsToDraw )
			vi.setComposition(visComp);
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { classroomdisplay.ClassroomDisplay.class.getName() });
//		DisplayUnit a = new DisplayUnit(null, 2,3);
	}
}
