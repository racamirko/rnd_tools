package classroomdisplay;

import java.util.Random;
import java.util.Vector;

import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.DataDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;
import classroomdisplay.display.VisualComposition;
import classroomdisplay.display.VisualItem;
import classroomdisplay.loaders.CSVDataLoaderExperiment1;

import processing.core.*;


public class ClassroomDisplay extends PApplet {
	private static final long serialVersionUID = 8767955545276677628L;
	Vector<VisualItem> thingsToDraw;
	protected DataDescription dataDesc;
	protected Vector<DataItem> data;
	protected Random rnd;

	int sizeX = 1340;
	int sizeY = 660;

	public void setup() {
	  size(1340, 660);
//	  size(sizeX, sizeY);
	  rnd = new Random(System.currentTimeMillis());
//	  TestDataGenerator td = new TestDataGenerator(this, 100);
	  CSVDataLoaderExperiment1 td = new CSVDataLoaderExperiment1(this);
	  td.load("/home/raca/data/video_material/lecture02_12.10.17_aac117/questionnair01_results/study01-hpl_class.csv");
	  dataDesc = td.dataDesc;
	  data = td.data;
	  generateVisualItems();
	}

	private void generateVisualItems() {
		thingsToDraw = new Vector<VisualItem>();
		VisualItem tmpItem = null;
		for( DataItem di : data ){
			tmpItem = new VisualItem(this, di);
			tmpItem.setPosition(new Point2f(sizeX - (70+di.x*80), 70+di.y*60) ); // instant layout
			thingsToDraw.add(tmpItem);
		}
		mousePressed();
	}

	public void draw() {
	  fill(color(250,250,250));
	  rect(0,0, sizeX, sizeY);
	  
	  for( VisualItem vi : thingsToDraw)
		  vi.draw();
			  
	  drawLegend();
	}
	
	private void drawLegend() {
		float xOffset = 30.0f,
			  yOffset = 500.0f,
			  yShift = 30.0f,
			  xShift = 200.0f;
		int numInColumn = 5,
			counter = 0;
		
		for( AttributeDescription attr : dataDesc.attribDescriptions.values() ){
			fill(attr.color);
			rect(xOffset + (float)( Math.floor(counter / numInColumn )*xShift ),
				 yOffset + (float)( Math.floor(counter % numInColumn )*yShift ), 30, 10);
			text( attr.name,
				  xOffset + (float)( Math.floor(counter / numInColumn )*xShift + 35 ),
				  yOffset + (float)( Math.floor(counter % numInColumn )*yShift + 10) );
			counter += 1;
		}
	}

	public void mousePressed() {
		// switch to the next random layout
		// generate composition
		VisualComposition visComp = new VisualComposition();
		visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsCenter.get( rnd.nextInt( dataDesc.attrsCenter.size() ))  )  );
		
		switch( rnd.nextInt(2) ){
			case 0:
				visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsRings.get( rnd.nextInt( dataDesc.attrsRings.size() ))  )  );
				break;
			case 1:
				int index1 = rnd.nextInt(dataDesc.attrsRings.size()),
				 	index2 = rnd.nextInt(dataDesc.attrsRings.size());
					if(index1==index2)
						if( index1 == dataDesc.attrsRings.size()-1 )
							index2 = 0;
						else
							index2 = dataDesc.attrsRings.size()-1;
					visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsRings.get( index1 )));
					visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsRings.get( index2 )));
				break;
		}
		// update all visible items
		for( VisualItem vi : thingsToDraw )
			vi.setComposition(visComp);
	}
	
	public void keyPressed(){
		if( key == 'a' )
			System.out.println("Bla");
//			if (key == CODED) {
//			    if (keyCode == UP) {
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { classroomdisplay.ClassroomDisplay.class.getName() });
	}
}
