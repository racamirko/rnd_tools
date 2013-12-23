package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class HeadPositionProviderTLD implements HeadPositionProviderGeneric {
	
	protected Map<Integer, FaceBoundingBox> mFrameMapping;

	public HeadPositionProviderTLD(String _filename){
		mFrameMapping = new HashMap<Integer, FaceBoundingBox>();
		init(_filename);
	}
	
	protected void init(String _filename){
		Scanner dataSrc;
		try {
			dataSrc = new Scanner(new File(_filename));
		} catch (FileNotFoundException e) {
			System.out.println("File not found: "+_filename);
			e.printStackTrace();
			return;
		}
		
		while(dataSrc.hasNextLine()){
			String curLine = dataSrc.nextLine();
			String[] parts = curLine.split(" ");
			int frameNoInFile = Integer.parseInt(parts[0]);
			if( parts[1].equalsIgnoreCase("NaN") ){
				continue;
			}
			int x = Integer.parseInt(parts[1]),
			    y = Integer.parseInt(parts[2]),
			    w = Integer.parseInt(parts[3]),
			    h = Integer.parseInt(parts[4]);
			float score = Float.parseFloat(parts[5].replaceAll(" ", ""));
			Rect2d bbox = new Rect2d(x, y, x+w, y+h);
			mFrameMapping.put(frameNoInFile, new FaceBoundingBox(bbox, 0.0f, score, frameNoInFile));
		}
		dataSrc.close();
	}
	
	@Override
	public void getFaces(int frameNo, Vector<FaceBoundingBox> output) {
		output.clear();
		if( mFrameMapping.containsKey(frameNo) )
			output.add(mFrameMapping.get(frameNo));
	}

}
