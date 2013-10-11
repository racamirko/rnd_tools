package data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.tools.ant.filters.StringInputStream;
import org.apache.tools.tar.TarEntry;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import data.HeadPositionProvider.FaceBoundingBox;

public class HeadPositionProviderTar {
	protected String tarFilename;
	protected Map<Integer, byte[]> dataMap;
	
	// you have to find the whole bounding box of the data

	public HeadPositionProviderTar(String tarFilename){
		this.tarFilename = tarFilename;
		dataMap = new HashMap<Integer, byte[]>();
		init();
	}
	
	protected void init(){
		TarArchiveInputStream tarSrc;
		try {
			tarSrc = new TarArchiveInputStream(new FileInputStream(new File(tarFilename)));
		} catch (FileNotFoundException e1) {
			System.out.println("Tar file not found: "+tarFilename);
			e1.printStackTrace();
			return;
		}
		TarArchiveEntry entry;
		try {
			while ((entry = tarSrc.getNextTarEntry()) != null) {
			    /* Get the name of the file */
			    String filename = entry.getName();
			    int frameNo = Integer.parseInt(filename.substring(16, 22));
			    byte[] content = new byte[(int) entry.getSize()];
			    tarSrc.read(content, 0, (int) entry.getSize());
			    dataMap.put(frameNo, content);
			}
		} catch (IOException e) {
			System.out.println("Reading tar file: " + tarFilename +" failed." );
			e.printStackTrace();
		}
	}
	
	public void getFaces(int frameNo, Vector<FaceBoundingBox> output){
		output.clear();
		// find filename
		Scanner dataSrc;
		if( !dataMap.containsKey(frameNo) ){
			System.out.println("Could not find: " + frameNo);
		}
		dataSrc = new Scanner(new ByteArrayInputStream(dataMap.get(frameNo)));
		while(dataSrc.hasNextLine()){
			String curLine = dataSrc.nextLine();
			String[] parts = curLine.split(",");
			// find the biggest bounding box
			int mixtureNo = Integer.parseInt(parts[2].replaceAll(" ", ""));
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
			FaceBoundingBox tmpFace = new FaceBoundingBox(bbox, mixtureNo, score, frameNoInFile);
			output.add(tmpFace);
		}
		dataSrc.close();
	}
	
}
