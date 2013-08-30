package data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import animation.IAnimator.enumManipulationAttrib;
import animation.IDataSource;

public class DataReader implements IDataSource {
	protected String filename;
	protected float tempo;
	protected enumManipulationAttrib[] format;
	protected Scanner dataScanner;

	
	public class InvalidState extends RuntimeException {
		private static final long serialVersionUID = 9061465707363796272L;
		protected String msg;
		
		public InvalidState(String msg){
			this.msg = msg;
		}
		
		public String getMessage(){
			return msg;
		}
	}
	
	public DataReader(String filename) throws FileNotFoundException{
		this.filename = filename;
		// reasonable default values
		tempo = 3.3f; // 4 miliseconds == 25 fps
		format = new enumManipulationAttrib[7]; //{ enumManipulationAttrib.ANI_ROTX };
		format[0] = enumManipulationAttrib.ANI_IGNORE;
		format[1] = enumManipulationAttrib.ANI_ROTX_ABS;
		format[2] = enumManipulationAttrib.ANI_ROTY_ABS;
		format[3] = enumManipulationAttrib.ANI_ROTZ_ABS;
		format[4] = enumManipulationAttrib.ANI_TRANSX_ABS;
		format[5] = enumManipulationAttrib.ANI_TRANSY_ABS;
		format[6] = enumManipulationAttrib.ANI_TRANSZ_ABS;
		// open the input
		open(filename);
	}
	
	protected void open(String filename) throws FileNotFoundException {
		dataScanner = new Scanner(new FileInputStream(filename));
	}

	@Override
	public void getNextValue(Vector<Float> values) {
		values.clear();
		if( dataScanner == null ){
			throw new InvalidState("Input stream not opened.");
		}
		if( dataScanner.hasNextLine() ){
			String tmpStr = dataScanner.nextLine();
			if(tmpStr.length() == 0 || tmpStr.charAt(0) == '#'){
				return;
			}
			String[] parts = tmpStr.split(",");
			for( int i = 0; i < parts.length; i++ ){
				values.add( Float.parseFloat(parts[i]) );
			}
		}
	}

	@Override
	public enumManipulationAttrib[] getFormat() {
		return format;
	}
	
	public void setFormat(enumManipulationAttrib[] newFormat){
		format = newFormat.clone();
	}

	@Override
	public float getTempo() {
		return tempo;
	}

}
