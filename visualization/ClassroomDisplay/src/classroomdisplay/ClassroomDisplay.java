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
import classroomdisplay.data.AttributeDescription.eAttributeDisplayType;
import classroomdisplay.data.AttributeLoader;
import classroomdisplay.data.CombinedAttributeDescription;
import classroomdisplay.data.DataDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;
import classroomdisplay.display.item.VisualComposition;
import classroomdisplay.display.item.VisualItem;
import classroomdisplay.display.layer.ClassroomLayout;
import classroomdisplay.display.layer.DotsLayer;
import classroomdisplay.display.layer.Layer;
import classroomdisplay.display.layer.MotionSyncLayer;
import classroomdisplay.loaders.CSVDataLoaderExperiment1;
import classroomdisplay.loaders.CSVDataLoaderExperiment2;

import processing.core.*;


public class ClassroomDisplay extends PApplet {
	private static final long serialVersionUID = 8767955545276677628L;
	protected DataDescription dataDesc;
	protected Vector<DataItem> data;
	protected Random rnd;
	protected ClassroomLayout layout;
	protected Vector<Layer> layers;
	
	protected DotsLayer dotsLayer;
	protected MotionSyncLayer connectionsLayer;
	
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
//	  td.load("/home/raca/data/video_material/lecture03_12.10.30_bc03/02_second_period/fake_questionnaire_results/fake_study04-pierre_class.csv");
//	  td.load("/home/raca/data/video_material/lecture03_12.10.30_bc03/01_first_period/questionnaire_results/study03-pierre_class.csv");
	  td.load("/home/raca/data/video_material/lecture02_12.10.17_aac117/questionnaire02_results/study02-hpl_class.csv");
	  
	  ////////////////	Additional attributes loading	////////////////
	  AttributeLoader attribLoader = new AttributeLoader(this);
//	  AttributeDescription a1 = attribLoader.load("/home/raca/repo/raca-personal/trunk/data/video_material/filmsyncr_annotations/lecture_02/questionnaire02_results/postprocess_measurements/meOnWorld.csv",
//			  				td.data, td.dataDesc, AttributeDescription.eAttributeDisplayType.ATD_SUBPART);
//	  AttributeDescription a2 = attribLoader.load("/home/raca/repo/raca-personal/trunk/data/video_material/filmsyncr_annotations/lecture_02/questionnaire02_results/postprocess_measurements/relAttention.csv",
//			  			   td.data, td.dataDesc, AttributeDescription.eAttributeDisplayType.ATD_SUBPART);
//	  AttributeDescription a3 = attribLoader.load("/home/raca/repo/raca-personal/trunk/data/video_material/filmsyncr_annotations/lecture_02/questionnaire02_results/postprocess_measurements/worldOnMe.csv",
//			  			   td.data, td.dataDesc, AttributeDescription.eAttributeDisplayType.ATD_SUBPART);
//	  CombinedAttributeDescription roseAttrib = new CombinedAttributeDescription("Relative attention", eAttributeDisplayType.ATD_COMBINED_ROSE, color(255,77,77), true);
//	  roseAttrib.addSubAttribute(a1);
//	  roseAttrib.addSubAttribute(a2);
//	  roseAttrib.addSubAttribute(a3);
//	  td.dataDesc.addAttribute(roseAttrib);
//	  
	  AttributeDescription a4 = attribLoader.load("/home/raca/data/video_material/lecture02_12.10.17_aac117/questionnaire02_results/postprocess_measurements/attDeltas.csv",
 			   					td.data, td.dataDesc, AttributeDescription.eAttributeDisplayType.ATD_DELTA);
	  AttributeDescription a5 = attribLoader.load("/home/raca/data/video_material/lecture02_12.10.17_aac117/motion_metrics/syncIndex.csv",
					td.data, td.dataDesc, AttributeDescription.eAttributeDisplayType.ATD_CENTER);
//	  AttributeDescription a5 = attribLoader.load("/home/raca/data/video_material/lecture03_12.10.30_bc03/02_second_period/motion_metrics/syncIndex.csv",
//			   td.data, td.dataDesc, AttributeDescription.eAttributeDisplayType.ATD_CENTER);
//	  AttributeDescription a5 = attribLoader.load("/home/raca/data/video_material/lecture03_12.10.30_bc03/01_first_period/motion_metrics/syncIndex.csv",
//			   td.data, td.dataDesc, AttributeDescription.eAttributeDisplayType.ATD_CENTER);
	  td.dataDesc.addAttribute(a4);
	  td.dataDesc.addAttribute(a5);
	  ////////////////	end of additional attributes	////////////////
	  
	  
	  
	  imgOkTick = loadImage("/home/raca/repo/rnd_tools/visualization/ClassroomDisplay/resources/ok_tick_20px.png");
	  dataDesc = td.dataDesc;
	  data = td.data;

//	  AAC117 layout	  
	  int[] corridorsSpaces = {4, 10};
	  layout = new ClassroomLayout(this, 14, 6, corridorsSpaces, 1200, 450, 50.0f, 80.0f);
	  
//		BC01 layout
//	  int[] corridorsSpaces = {6};
//	  layout = new ClassroomLayout(this, 10, 4, corridorsSpaces, 1200, 450, 50.0f, 80.0f);
	  
	  
	  generateVisualItems();
	  okTicksLocations = new Vector<Point2f>();
	  
	  // controls
	  ctrls = new ControlP5(this);
	  setupControls();
	  updateTicks();
	}

	private void generateVisualItems() {
		VisualComposition visComp = new VisualComposition();
		visComp.attributesToDisplay.add(dataDesc.getAttrbDesc( dataDesc.attrsCenter.get( rnd.nextInt( dataDesc.attrsCenter.size() ))));
		visComp.series = 0;

		dotsLayer = new DotsLayer(this);
		dotsLayer.initialize(data, visComp, layout);
		
		// graphs
		connectionsLayer = new MotionSyncLayer(this);
		connectionsLayer.loadData("/home/raca/data/video_material/lecture02_12.10.17_aac117/motion_metrics/motion_correl_matlab.txt");
//		connectionsLayer.loadData("/home/raca/data/video_material/lecture03_12.10.30_bc03/01_first_period/motion_metrics/motion_correl_matlab.txt");
//		connectionsLayer.loadData("/home/raca/data/video_material/lecture03_12.10.30_bc03/02_second_period/motion_metrics/motion_correl_matlab.txt");
		connectionsLayer.readPositions(data, layout);
		
		layers = new Vector<Layer>();
		layers.add(layout);
		layers.add(dotsLayer);
		layers.add(connectionsLayer);
	}
	
	private void setupControls(){
		int counter = 0;
		numColumnsLegend = 5;
		offsetLegend = new Point2f(30.0f, 500.0f);
		shiftLegend = new Point2f(200.0f, 30.0f);
		// selection buttons
		for( AttributeDescription attr : dataDesc.attribDescriptions.values() ){
			if( attr.displayType == eAttributeDisplayType.ATD_SUBPART )
				continue;
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

	  for( Layer layer : layers )
		  layer.draw();

	  ctrls.draw();
	  drawTicks();
	}

	private void clearCanvas() {
		fill(color(250,250,250));
		rect(0,0, sizeX, sizeY);
	}
	
	public void mousePressed() {
		if( mouseButton == LEFT ){
			VisualItem clickedItem =  dotsLayer.getItemAt(mouseX, mouseY);
			if( clickedItem != null )
				connectionsLayer.setActive(clickedItem);
			else
				connectionsLayer.clearDisplay();
		}
	}
	
	public void keyPressed(){
		if( key == BACKSPACE )
			saveScreenShot();
		VisualComposition visComp = dotsLayer.getComposition();
		if( key == CODED && keyCode == LEFT ){
			if( visComp.series == 0 ){
				visComp.series = 4;
			}
			visComp.series -= 1;
			periodRadioBtn.activate(visComp.series);
			dotsLayer.setComposition(visComp);
		}
		if( key == CODED && keyCode == RIGHT ){
			if( visComp.series == 3 ){
				visComp.series = -1;
			}
			visComp.series += 1;
			periodRadioBtn.activate(visComp.series);
			dotsLayer.setComposition(visComp);
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
		VisualComposition visComp = dotsLayer.getComposition();
		
		if( visComp.attributesToDisplay.contains(selectedDesc) )
			visComp.attributesToDisplay.remove(selectedDesc);
		else 
			visComp.attributesToDisplay.add(selectedDesc);
		// update
		dotsLayer.setComposition(visComp);
		// update ticks
		updateTicks();
	}
	
	private void changePeriod(ControlEvent theEvent) {
		VisualComposition visComp = dotsLayer.getComposition();
		visComp.series = (int) theEvent.getValue();
		dotsLayer.setComposition(visComp);
	}

	public void drawTicks(){
		for( Point2f pt : okTicksLocations ){
			image(imgOkTick, pt.x, pt.y);
		}
	}
	
	public void updateTicks(){
		int counter = 0;
		okTicksLocations.clear();
		VisualComposition visComp = dotsLayer.getComposition();
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
