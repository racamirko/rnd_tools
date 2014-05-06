package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class HeadPositionProviderFolder extends HeadPositionProvider_filereading {
	protected String filePattern;
	
	// you have to find the whole bounding box of the data
	public HeadPositionProviderFolder(String frameFilePattern){
		filePattern = frameFilePattern;
	}
	
	public void getFaces(int frameNo, Vector<FaceBoundingBox> output){
		System.out.println("Getting data for frame #"+frameNo);
		output.clear();
		// find filename
		String fullFilename = String.format(filePattern, frameNo);
		Scanner dataSrc;
		try {
			dataSrc = new Scanner(new FileInputStream(fullFilename));
		} catch (FileNotFoundException e) {
			System.out.println("Could not find: " + fullFilename);
			return;
		}
		readSingleFile(dataSrc, output);
		dataSrc.close();
		System.out.println("\tFound faces: " + output.size());
	}
	
}
