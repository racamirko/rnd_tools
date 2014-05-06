package data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;
import java.util.zip.ZipFile;

public class HeadPositionProviderZip extends HeadPositionProvider_filereading {

	ZipFile zip;
	final String fileNameFormat;
	
	public HeadPositionProviderZip(String filename){
		this(filename, "facedetect_frame%06d.txt");
	}
			
	public HeadPositionProviderZip(String filename, String _fileNameFormat){
		try {
			zip = new ZipFile(filename);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error reading ZIP-source: "+filename);
			zip = null;
		}
		fileNameFormat = _fileNameFormat;
	}
	
	protected String formatName(int frameNo){
		return String.format(fileNameFormat, frameNo);
	}
	
	@Override
	public void getFaces(int frameNo, Vector<FaceBoundingBox> output) {
		output.clear();
		InputStream tmpStream;
		try {
			tmpStream = zip.getInputStream(zip.getEntry(formatName(frameNo)));
		} catch (IOException e) {
			System.err.println("Frame "+frameNo+" was not found");
			return;
		}
		readSingleFile( new Scanner(tmpStream) , output);
	}

}
