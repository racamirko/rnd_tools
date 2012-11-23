package classroomdisplay;

import java.util.Calendar;
import java.util.Random;
import java.util.Vector;

import controlP5.CColor;
import controlP5.CheckBox;
import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.RadioButton;

import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.DataDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;
import classroomdisplay.display.ClassroomLayout;
import classroomdisplay.display.VisualComposition;
import classroomdisplay.display.VisualItem;
import classroomdisplay.loaders.CSVDataLoaderExperiment1;
import classroomdisplay.loaders.CSVDataLoaderExperiment2;

import processing.core.*;


public class ClassroomDisplay extends PApplet {
	private static final long serialVersionUID = 8767955545276677628L;
	Vector<VisualItem> thingsToDraw;
	protected DataDescription dataDesc;
	protected Vector<DataItem> data;
	protected Random rnd;
	protected ClassroomLayout layout;
	VisualComposition visComp;
	
	protected PImage imgOkTick;
	protected Vector<Point2f> okTicksLocations;
	protected Point2f offsetLegend, shiftLegend;
	protected int numColumnsLegend;
	
	protected ControlP5 ctrls;
	protected RadioButton periodRadioBtn;

	int sizeX = 1340;
	int sizeY = 660;

	public void setup() {
	  size(1340, 660);
//	  size(sizeX, sizeY);
	  rnd = new Random(System.currentTimeMillis());
//	  TestDataGenerator td = new TestDataGenerator(this, 100);
	  CSVDataLoaderExperiment2 td = new CSVDataLoaderExperiment2(this);
	  imgOkTick = loadImage("/home/raca/repo/rnd_tools/visualization/ClassroomDisplay/resources/ok_tick_20px.png");
//	  td.load("/home/raca/data/video_material/lecture02_12.10.17_aac117/questionnaire02_results/study02-hpl_class.csv");
	  td.load("/home/raca/data/video_material/lecture03 - 12.10.30 - bc03/01_first_period/study03-pierre_class.csv");
	  dataDesc = td.dataDesc;
	  data = td.data;

//	  AAC117 layout	  
//	  int[] corridorsSpaces = {4, 10};
//	  layout = new ClassroomLayout(this, 14, 6, corridorsSpaces, 1200, 450, 50.0f, 80.0f);
	  
//		BC01 layout
	  int[] corridorsSpaces = {6};
	  layout = new ClassroomLayout(this, 10, 4, corridorsSpaces, 1200, 450, 50.0f, 80.0f);
	  
	  
	  visComp = new VisualComposition();
	  visComp.attributesToDisplay.add(dataDesc.getAttrbDesc( dataDesc.attrsCenter.get( rnd.nextInt( dataDesc.attrsCenter.size() ))));
	  visComp.series = 0;
	  generateVisualItems();
	  okTicksLocations = new Vector<Point2f>();
	  
	  // controls
	  ctrls = new ControlP5(this);
	  setupControls();
	  updateTicks();
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
	}
	
	private void setupControls(){
		int counter = 0;
		numColumnsLegend = 5;
		offsetLegend = new Point2f(30.0f, 500.0f);
		shiftLegend = new Point2f(200.0f, 30.0f);
		// selection buttons
		for( AttributeDescription attr : dataDesc.attribDescriptions.values() ){
			CColor thisColor = new CColor( color(125.0f), attr.color, color(125.0f), color(255.0f), color(255.0f) );
			ctrls.addButton(attr.name).setPosition(offsetLegend.x + (float)( Math.floor(counter / numColumnsLegend )*shiftLegend.x ),
												   offsetLegend.y + (float)( Math.floor(counter % numColumnsLegend )*shiftLegend.y))
												   .setColor(thisColor)
												   .setWidth(170);
			counter += 1;
		}
		// period checkbox
		int numOfPeriods = 4;
		periodRadioBtn = ctrls.addRadioButton("timeRadioBtn").setPosition(offsetLegend.x + (float)( (Math.floor(counter / numColumnsLegend )+1)*shiftLegend.x ),
														 offsetLegend.y)
										    .setSize(50,20)
										    .setItemsPerRow(numOfPeriods)
										    .setColorLabels(color(255))
										    .showLabels();
		for( int i = 0; i < numOfPeriods; ++i ){
			periodRadioBtn.addItem("Period "+(i+1), i);
		}
		periodRadioBtn.activate(0);
	}

	public void draw() {
	  clearCanvas();
	  
	  layout.draw();
	  
	  for( VisualItem vi : thingsToDraw)
		  vi.draw();

	  ctrls.draw();
	  drawTicks();
	}

	private void clearCanvas() {
		fill(color(250,250,250));
		rect(0,0, sizeX, sizeY);
	}
	
	public void mousePressed() {
		// nothing more here
	}
	
	public void keyPressed(){
		if( key == 'a' )
			System.out.println("Bla");
		if( key == BACKSPACE )
			saveScreenShot();
		if( key == CODED && keyCode == LEFT ){
			if( visComp.series == 0 )
				return;
			visComp.series -= 1;
			periodRadioBtn.activate(visComp.series);
			for( VisualItem vi : thingsToDraw )
				vi.setComposition(visComp);

		}
		if( key == CODED && keyCode == RIGHT ){
			if( visComp.series == 3 )
				return;
			visComp.series += 1;
			periodRadioBtn.activate(visComp.series);
			for( VisualItem vi : thingsToDraw )
				vi.setComposition(visComp);
		}
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
		if( theEvent.isFrom(periodRadioBtn) ){
			changePeriod(theEvent);
			return;
		}
		// handle buttons
//		println(theEvent.getController().getName());
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
		// update ticks
		updateTicks();
	}
	
	private void changePeriod(ControlEvent theEvent) {
		visComp.series = (int) theEvent.getValue();
		for( VisualItem vi : thingsToDraw )
			vi.setComposition(visComp);
	}

	public void drawTicks(){
		for( Point2f pt : okTicksLocations ){
			image(imgOkTick, pt.x, pt.y);
		}
	}
	
	public void updateTicks(){
		int counter = 0;   
		okTicksLocations.clear();
		for( AttributeDescription attr : dataDesc.attribDescriptions.values() ){
			if( visComp.attributesToDisplay.contains(attr) ){
				okTicksLocations.add(new Point2f(offsetLegend.x + (float)( Math.floor(counter / numColumnsLegend )*shiftLegend.x )-22,
						   offsetLegend.y + (float)( Math.floor(counter % numColumnsLegend )*shiftLegend.y)));
			}
			counter += 1;
		}
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { classroomdisplay.ClassroomDisplay.class.getName() });
	}
}
