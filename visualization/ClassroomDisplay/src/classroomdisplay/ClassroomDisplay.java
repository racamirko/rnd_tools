package classroomdisplay;

import java.util.Calendar;
import java.util.Random;
import java.util.Vector;

import controlP5.CColor;
import controlP5.ControlEvent;
import controlP5.ControlP5;

import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.DataDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;
import classroomdisplay.display.ClassroomLayout;
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
	protected ClassroomLayout layout;
	VisualComposition visComp;
	
	protected ControlP5 ctrls;

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
	  
	  int[] corridorsSpaces = {4,10};
	  layout = new ClassroomLayout(this, 14, 6, corridorsSpaces, 1200, 450, 50.0f, 80.0f);
	  
	  visComp = new VisualComposition();
	  visComp.attributesToDisplay.add(dataDesc.getAttrbDesc( dataDesc.attrsCenter.get( rnd.nextInt( dataDesc.attrsCenter.size() ))));
	  generateVisualItems();
	  
	  // controls
	  
	  ctrls = new ControlP5(this);
	  setupControls();
//	  ctrls.addButton("First control").setPosition(100,200).setColor(arg0);
	}

	private void generateVisualItems() {
		
		thingsToDraw = new Vector<VisualItem>();
		VisualItem tmpItem = null;
		for( DataItem di : data ){
			tmpItem = new VisualItem(this, di, new Point2f(670,330));
			tmpItem.setPosition(layout.getCoordinateForSeat(di.x, di.y), 20+rnd.nextInt(80)); // instant layout
			tmpItem.setComposition(visComp);
			thingsToDraw.add(tmpItem);
		}
//		mousePressed();
	}
	
	private void setupControls(){
		float xOffset = 30.0f,
				  yOffset = 500.0f,
				  yShift = 30.0f,
				  xShift = 200.0f;
			int numInColumn = 5,
				counter = 0;
			
			for( AttributeDescription attr : dataDesc.attribDescriptions.values() ){
				CColor thisColor = new CColor( color(125.0f), attr.color, color(125.0f), color(255.0f), color(255.0f) );
				ctrls.addButton(attr.name).setPosition(xOffset + (float)( Math.floor(counter / numInColumn )*xShift ),
													   yOffset + (float)( Math.floor(counter % numInColumn )*yShift)).setColor(thisColor);
				counter += 1;
			}
	}

	public void draw() {
	  fill(color(250,250,250));
	  rect(0,0, sizeX, sizeY);
	  
	  layout.draw();
	  
	  for( VisualItem vi : thingsToDraw)
		  vi.draw();
			  
	  drawLegend();
	  ctrls.draw();
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
//		visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsCenter.get( rnd.nextInt( dataDesc.attrsCenter.size() ))  )  );
//		
//		switch( rnd.nextInt(2) ){
//			case 0:
//				visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsRings.get( rnd.nextInt( dataDesc.attrsRings.size() ))  )  );
//				break;
//			case 1:
//				int index1 = rnd.nextInt(dataDesc.attrsRings.size()),
//				 	index2 = rnd.nextInt(dataDesc.attrsRings.size());
//					if(index1==index2)
//						if( index1 == dataDesc.attrsRings.size()-1 )
//							index2 = 0;
//						else
//							index2 = dataDesc.attrsRings.size()-1;
//					visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsRings.get( index1 )));
//					visComp.attributesToDisplay.add( dataDesc.getAttrbDesc( dataDesc.attrsRings.get( index2 )));
//				break;
//		}
//		// update all visible items
		for( VisualItem vi : thingsToDraw )
			vi.setComposition(visComp);
//		ctrls.mouseEvent(mouseEvent);
	}
	
	public void keyPressed(){
		if( key == 'a' )
			System.out.println("Bla");
		if( key == BACKSPACE )
			saveScreenShot();
//			if (key == CODED) {
//			    if (keyCode == UP) {
//		ctrls.keyEvent(keyEvent);
	}
	
	public void saveScreenShot(){
		// form path
		StringBuffer buf = new StringBuffer();
		Calendar now = Calendar.getInstance();
		buf.append("/home/raca/Pictures/Screenshot-classDisplay_").append(now.get(Calendar.YEAR)).append(".");
		buf.append(now.get(Calendar.MONTH)).append(".").append(now.get(Calendar.DAY_OF_MONTH)).append("_");
		buf.append(now.get(Calendar.HOUR)).append(".").append(now.get(Calendar.MINUTE)).append(".").append(now.get(Calendar.SECOND));
		buf.append(".png");
		// take screenshot
		this.save(buf.toString());
	}

	
	public void controlEvent(ControlEvent theEvent) {
		  println(theEvent.getController().getName());
		  // find attribute
		  AttributeDescription selectedDesc = null;
		  for( AttributeDescription attr : dataDesc.attribDescriptions.values() ){
			  if( attr.name.equals(theEvent.getController().getName()) ){
				  selectedDesc = attr;
				  break;
			  }
		  }
		  if( selectedDesc == null ){
			  System.out.println("Theres been a problem");
			  return;
		  }
		  if( visComp.attributesToDisplay.contains(selectedDesc) )
			  visComp.attributesToDisplay.remove(selectedDesc);
		  else 
			  visComp.attributesToDisplay.add(selectedDesc);
		  // update
		  for( VisualItem vi : thingsToDraw )
				vi.setComposition(visComp);
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { classroomdisplay.ClassroomDisplay.class.getName() });
	}
}
