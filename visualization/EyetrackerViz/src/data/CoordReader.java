package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CoordReader {
	
	protected Scanner dataScanner;
	protected int xCol, yCol, xCol2, yCol2;
	protected String separator;
	protected float segmentDuration, timeDuration, lastTimestamp;
	protected Point2d lastPoint, nextPoint;

	public CoordReader(String fnCoords, int xColumnIndex1, int yColumnIndex1, int xColumnIndex2, int yColumnIndex2, String separator){
		xCol = xColumnIndex1;
		yCol = yColumnIndex1;
		xCol2 = xColumnIndex2;
		yCol2 = yColumnIndex2;
		this.separator = separator;
		segmentDuration = 0;
		timeDuration = 0;
		lastPoint = new Point2d(0,0);
		nextPoint = new Point2d(0,0);
		try {
			open(fnCoords);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public CoordReader(String fnCoords, int xColumnIndex, int yColumnIndex, String separator){
		xCol = xColumnIndex;
		yCol = yColumnIndex;
		xCol2 = -1;
		yCol2 = -1;
		this.separator = separator;
		segmentDuration = 0;
		timeDuration = 0;
		lastPoint = new Point2d(0,0);
		nextPoint = new Point2d(0,0);
		try {
			open(fnCoords);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	protected void open(String filename) throws FileNotFoundException{
		dataScanner = new Scanner(new FileInputStream(filename));
		String currentLine = dataScanner.nextLine();
		while( currentLine.charAt(0) == '#' ){
			currentLine = dataScanner.nextLine();
		}
		float time1 = 0;
		time1 = getPoint(lastPoint);
		lastTimestamp = getPoint(nextPoint);
		segmentDuration = (lastTimestamp - time1) / 1000.0f;
//		System.out.println( String.format("First segment duration: %.2f", segmentDuration));
	}
	
	public Point2d getNextValue(float milisPassed){
		if( !dataScanner.hasNextLine() )
			return null;
		timeDuration += milisPassed;
		if(timeDuration < segmentDuration){
			return lastPoint;
		}
		timeDuration -= segmentDuration;
		lastPoint = nextPoint;
		// get new information
		float newTimestamp = 0;
		newTimestamp = getPoint(nextPoint);
//		System.out.println(String.format("New timestamp: %.2f",newTimestamp));
		segmentDuration = (newTimestamp - lastTimestamp)/1000.0f;
//		System.out.println(String.format("New segment duration: %.2f",segmentDuration));
		lastTimestamp = newTimestamp;
		// return
		return lastPoint;
	}
	
	protected float getPoint(Point2d pt){
		if( !dataScanner.hasNextLine() ){
			pt.clear();
			return 0.0f;
		}
		String tmpStr = dataScanner.nextLine();
		String[] parts = tmpStr.split(separator);
		if( xCol2 != -1 && yCol2 != -1 ){
			pt.setX( (Float.parseFloat(parts[xCol]) + Float.parseFloat(parts[xCol2]))/2.0f );
			pt.setY( (Float.parseFloat(parts[yCol]) + Float.parseFloat(parts[yCol2]))/2.0f );
		} else { 
			pt.setX(Float.parseFloat(parts[xCol]));
			pt.setY(Float.parseFloat(parts[yCol]));
		}
		return Float.parseFloat(parts[0]);
	}
}
