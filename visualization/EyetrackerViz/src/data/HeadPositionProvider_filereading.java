package data;

import java.util.Scanner;
import java.util.Vector;

public class HeadPositionProvider_filereading implements
		HeadPositionProviderGeneric {

	// nope, don't instance this one 
	protected HeadPositionProvider_filereading(){
		
	}
	
	@Override
	public void getFaces(int frameNo, Vector<FaceBoundingBox> output) {
		System.err.println("ERROR: abstract method HeadPositionProvider_filereading::getFaces() called");
		throw new UnsupportedOperationException();
	}

	protected void readSingleFile(Scanner dataSrc, Vector<FaceBoundingBox> output){
		while(dataSrc.hasNextLine()){
			String curLine = dataSrc.nextLine();
			String[] parts = curLine.split(",");
			// find the biggest bounding box
			float angle = Float.parseFloat(parts[2].replaceAll(" ", ""));
			float score = Float.parseFloat(parts[3].replaceAll(" ", ""));
			int frameNoInFile = Integer.parseInt(parts[0].replaceAll(" ", ""));
			int maxComponents = Integer.parseInt(parts[1].replaceAll(" ", ""));
			Rect2d bbox = new Rect2d(100000.0f, 100000.0f, 0.0f, 0.0f); // makes no sense
			int offset = 4;
			// find the total bounding box around all the elements
			for( int i = 0; i < maxComponents; ++i ){
				float cx1 = Float.parseFloat(parts[offset+i*4].replaceAll(" ", "")),
					  cy1 = Float.parseFloat(parts[offset+i*4+1].replaceAll(" ", "")),
					  cx2 = Float.parseFloat(parts[offset+i*4+2].replaceAll(" ", "")),
					  cy2 = Float.parseFloat(parts[offset+i*4+3].replaceAll(" ", ""));
				if( cx1 > cx2 ){
					float tmpVar = cx1;
					cx1 = cx2;
					cx2 = tmpVar;
				}
				if( cy1 > cy2 ){
					float tmpVar = cy1;
					cy1 = cy2;
					cy2 = tmpVar;
				}
				if( cx1 < bbox.getX1() ) bbox.setX1(cx1);
				if( cx2 > bbox.getX2() ) bbox.setX2(cx2);
				if( cy1 < bbox.getY1() ) bbox.setY1(cy1);
				if( cy2 > bbox.getY2() ) bbox.setY2(cy2);
			}
			bbox.setX1( Math.max(0.0f, bbox.getX1()-10.0f) );
			bbox.setY1( Math.max(0.0f, bbox.getY1()-10.0f) );
			bbox.setX2( bbox.getX2() + 10.0f );
			bbox.setY2( bbox.getY2() + 10.0f );
			// end of finding the total bounding box
			FaceBoundingBox tmpFace = new FaceBoundingBox(bbox, angle, score, frameNoInFile);
			output.add(tmpFace);
		}
	}
}