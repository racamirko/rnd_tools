package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CoordReader {
	
	protected Scanner dataScanner;
	protected int xCol, yCol, xCol2, yCol2;
	protected String separator;
	protected float tempoMilis, timeDuration;
	protected Point2d lastPoint;

	public CoordReader(String fnCoords, int xColumnIndex1, int yColumnIndex1, int xColumnIndex2, int yColumnIndex2, String separator, float tempoMilis){
		xCol = xColumnIndex1;
		yCol = yColumnIndex1;
		xCol2 = xColumnIndex2;
		yCol2 = yColumnIndex2;
		this.separator = separator;
		this.tempoMilis = tempoMilis;
		timeDuration = 0;
		lastPoint = null;
		try {
			open(fnCoords);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public CoordReader(String fnCoords, int xColumnIndex, int yColumnIndex, String separator, float tempoMilis){
		xCol = xColumnIndex;
		yCol = yColumnIndex;
		xCol2 = -1;
		yCol2 = -1;
		this.separator = separator;
		this.tempoMilis = tempoMilis;
		timeDuration = 0;
		lastPoint = null;
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
	}
	
	public Point2d getNextValue(float milisPassed){
		if( !dataScanner.hasNextLine() )
			return null;
		timeDuration += milisPassed;
		if(timeDuration < tempoMilis){
			return lastPoint;
		}
		timeDuration -= tempoMilis;
		String tmpStr = dataScanner.nextLine();
		String[] parts = tmpStr.split(separator);
		if( xCol2 != -1 && yCol2 != -1 ){
			float x = (Float.parseFloat(parts[xCol]) + Float.parseFloat(parts[xCol2]))/2.0f;
			float y = (Float.parseFloat(parts[yCol]) + Float.parseFloat(parts[yCol2]))/2.0f;
			lastPoint = new Point2d(x,y);
		} else 
			lastPoint = new Point2d(Float.parseFloat(parts[xCol]),Float.parseFloat(parts[yCol]));
		return lastPoint;
	}
}
