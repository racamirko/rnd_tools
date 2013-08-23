package data;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Vector;

public class SkelAsciiReader {
	protected String filename;
	protected BufferedReader inFile;

	public SkelAsciiReader(){
		filename = "";
	}
	
	public SkelAsciiReader(String filename) throws FileNotFoundException{
		open(filename);
	}
	
	public void open(String filename) throws FileNotFoundException{
		inFile = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
		this.filename = filename;
	}
	
	public Vector<SkeletonJoints> getNextFrame(){
		return null;
	}
	
	public SkeletonJoints getNextFrame(int skelId){
		return null;
	}

}
