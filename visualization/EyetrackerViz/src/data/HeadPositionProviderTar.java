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

public class HeadPositionProviderTar extends HeadPositionProvider_filereading {
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
			    if(entry.getSize() == 0)
			    	continue;
			    int fnSize = filename.length();
			    int frameNo = Integer.parseInt(filename.substring(fnSize-10, fnSize-4));
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
			return;
		}
		dataSrc = new Scanner(new ByteArrayInputStream(dataMap.get(frameNo)));
		readSingleFile(dataSrc, output);
		dataSrc.close();
	}
	
}
