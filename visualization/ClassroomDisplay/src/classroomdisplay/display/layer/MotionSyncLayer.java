package classroomdisplay.display.layer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import processing.core.PApplet;

import classroomdisplay.data.DataItem;
import classroomdisplay.data.Point2f;
import classroomdisplay.display.item.VisualItem;

public class MotionSyncLayer implements Layer {
	protected Map<Integer, Point2f> personId2Location;
	protected Map<Integer, Connections> data;
	protected PApplet pa;
	protected float correlLimit;
	protected eDisplaySyncMode displayMode;
	protected float maxLineWidth;
	
	protected int colorSynced, colorDesynced;
	
	protected Vector<Line> itemsToDraw;
	
	public MotionSyncLayer(PApplet pa){
		this.pa = pa;
		itemsToDraw = new Vector<Line>();
		personId2Location = new HashMap<Integer, Point2f>();
		data = new HashMap<Integer, MotionSyncLayer.Connections>();
		correlLimit = 0.05f;
		displayMode = eDisplaySyncMode.DSM_ALL;
		// generate colors
		colorSynced = pa.color(36, 95, 203, 128.0f);
		colorDesynced = pa.color(241, 80, 80, 128.0f);
		maxLineWidth = 6.0f;
	}
	
	@Override
	public void draw() {
		pa.pushStyle();
		for( Line line : itemsToDraw ){
			pa.stroke(line.color, 120.0f);
			pa.strokeWeight(line.width);
			pa.line(line.x1, line.y1, line.x2, line.y2);
		}
		pa.popStyle();
	}
	
	public void setActive( VisualItem item ){
		setActive(item.getDataItem().getId());
	}
	
	public void readPositions( Vector<DataItem> persons, ClassroomLayout layout){
		for( DataItem person : persons )
			personId2Location.put(person.personId, layout.getCoordinateForSeat(person.x, person.y) );
	}
	
	public void loadData( String filename ){
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.out.println("File " + filename + " not found");
			return;
		}
		
		int personId = 0;
		Connections conns = null;
		eReadingMode mode = eReadingMode.RM_PERSON; 
		while( scanner.hasNextLine() ){
			String tmpStr = scanner.nextLine();
			
			if( tmpStr.length() == 0  ){
				if( conns != null ){
					data.put(personId, conns);
					personId = 0;
					conns = null;
					mode = eReadingMode.RM_PERSON;
				}
				continue;
			}
			
			if( mode == eReadingMode.RM_SYNCED ){
				String[] parts = tmpStr.split("; ");
				for( String part : parts ){
					if( part.length() == 0 )
						continue;
					int separator = part.indexOf(":");
					conns.correlation.put( Integer.valueOf(part.substring(0, separator)), Float.valueOf(part.substring(separator+1)));
				}
				mode = eReadingMode.RM_DESYNCED;
				scanner.nextLine(); // to skip 1 line
				continue;
			}
			
			if( mode == eReadingMode.RM_DESYNCED ){
				String[] parts = tmpStr.split("; ");
				for( String part : parts ){
					if( part.length() == 0 )
						continue;
					int separator = part.indexOf(":");
					conns.correlation.put( Integer.valueOf(part.substring(0, separator)), Float.valueOf(part.substring(separator+1)));
				}
				mode = eReadingMode.RM_DESYNCED;
			}
			
			int loc = tmpStr.indexOf("Person");
			if( loc != -1 ){
				personId = Integer.parseInt(tmpStr.substring(loc+7, loc+9).trim()); // i'm expecting less then 100 students
				conns = new Connections();
				mode = eReadingMode.RM_SYNCED;
			}
		}
		// insert last person
		if( conns != null )
			data.put(personId, conns);
	}
	
	public void setActive( int personId ){
		itemsToDraw.clear();
		if( displayMode != eDisplaySyncMode.DSM_ALL_SYNCED_TO_ME ) {
			// synced
			Connections connects = data.get(personId);
			if( connects == null )
				return;
			if( displayMode == eDisplaySyncMode.DSM_ALL || displayMode == eDisplaySyncMode.DSM_SYNC ){
				Iterator<Map.Entry<Integer, Float>> iter = connects.correlation.entrySet().iterator();
				while( iter.hasNext() ){
					Map.Entry<Integer, Float> pair = iter.next();
					if( pair.getValue() < correlLimit )
						addLine(personId, pair.getKey(), colorSynced, pair.getValue());
				}
			}
			// desynced
			if( displayMode == eDisplaySyncMode.DSM_ALL || displayMode == eDisplaySyncMode.DSM_NONSYNC ){
				Iterator<Map.Entry<Integer, Float>> iter = connects.correlation.entrySet().iterator();
				while( iter.hasNext() ){
					Map.Entry<Integer, Float> pair = iter.next();
					if( pair.getValue() >= correlLimit )
						addLine(personId, pair.getKey(), colorDesynced, pair.getValue());
				}
			}
		} else {
			// nothing for now
		}
	}
	
	public void clearDisplay(){
		itemsToDraw.clear();
	}
	
	protected void addLine(int startId, int endId, int color, float corrVal){
		Point2f pt1 = personId2Location.get(startId),
				pt2 = personId2Location.get(endId);
		Line tmpLine = new Line();
		tmpLine.x1 = pt1.x; tmpLine.y1 = pt1.y;
		tmpLine.x2 = pt2.x; tmpLine.y2 = pt2.y;
		tmpLine.color = color;
		if( corrVal < correlLimit ){
			tmpLine.width = Math.abs(corrVal-correlLimit)/correlLimit*6.0f;
		} else {
			tmpLine.width = Math.abs(corrVal-correlLimit)/(1.0f-correlLimit)*6.0f;
		}
		tmpLine.width = Math.max(tmpLine.width, 1.0f);
		itemsToDraw.add(tmpLine);
	}
	
	//// Helper classes
	
	public enum eDisplaySyncMode { DSM_ALL, DSM_SYNC, DSM_NONSYNC, DSM_ALL_SYNCED_TO_ME };
	protected enum eReadingMode{ RM_PERSON, RM_SYNCED, RM_DESYNCED};

	public class Connections {
		public Map<Integer, Float> correlation;
		
		public Connections(){
			correlation = new HashMap<Integer, Float>();
		}
	}
	
	public class Line{
		public float x1, y1, x2, y2;
		public float width;
		public int color;
	}
}
