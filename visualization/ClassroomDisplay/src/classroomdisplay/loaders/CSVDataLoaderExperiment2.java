package classroomdisplay.loaders;

import java.util.Vector;

import processing.core.PApplet;
import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.DataDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.AttributeDescription.eAttributeDisplayType;

public class CSVDataLoaderExperiment2 {
	PApplet pa;
	public DataDescription dataDesc;
	public Vector<DataItem> data;

	public CSVDataLoaderExperiment2(PApplet pa) {
		this.pa = pa;
		dataDesc = new DataDescription();
		data = new Vector<DataItem>();
		
		generateDataDescription();
	}
	
	private void generateDataDescription() {
		// general attributes, not repeated
		dataDesc.addAttribute( new AttributeDescription("Gender", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(5,175,208), false));
		dataDesc.addAttribute( new AttributeDescription("Has net", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(105,175,8), false));

		// rings (this should be repeated 4x)
		dataDesc.addAttribute(new AttributeDescription( "Attention", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(255, 99, 83), true) );
		dataDesc.addAttribute(new AttributeDescription( "Class attention", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(101, 233, 79), true) );
		dataDesc.addAttribute(new AttributeDescription( "Teacher energy", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(98, 80, 220), true) );
		dataDesc.addAttribute(new AttributeDescription( "Infomation important", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(18, 80, 220), true) );
		
		dataDesc.addAttribute( new AttributeDescription("Listening", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(105,175,208), true));
		dataDesc.addAttribute( new AttributeDescription("Taking notes", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(86,248,130), true));
		dataDesc.addAttribute( new AttributeDescription("Repeating ideas", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(255,228,122), true));

		dataDesc.addAttribute( new AttributeDescription("Surfing for fun", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(135,175,178), true));
		dataDesc.addAttribute( new AttributeDescription("Thinking about other things", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(225,188,122), true));
		dataDesc.addAttribute( new AttributeDescription("Interacting with someone", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(155,85,208), true));
	}

	public void load(String filename){
		String lines[] = pa.loadStrings(filename);
		processHeader(lines[1]);

		for (int i=2; i < lines.length; i++) {
			String [] fields = PApplet.split(lines[i],',');
			if( fields[1].length() == 0 && fields[2].length()==0 )
				break; // first two empty = reached the end of data section
			
			DataItem di = new DataItem();
			// static information
			if( fields[1].equals("m") )
				di.addFixedAttribute("Gender", 1.0f);
			else
				di.addFixedAttribute("Gender", 0.0f);
			di.addFixedAttribute("Has net", Float.valueOf(fields[2]));
			di.y = Float.valueOf(fields[3]); // row
			di.x = Float.valueOf(fields[4]); // seat
			
			// first period
			int offset = 5, sectionWidth = 10;
			for( int j = 0; j < 4; ++j ){
				if( fields[offset+j*sectionWidth].length() > 0 )
					di.addTimeAttribute("Attention", j, Float.valueOf(fields[offset+j*sectionWidth]));
				else 
					di.addTimeAttribute("Attention", j, dataDesc.getAttrbDesc("Attention").minRange);
				
				if( fields[offset+j*sectionWidth+1].length() > 0 )
					di.addTimeAttribute("Class attention", j, Float.valueOf(fields[offset+j*sectionWidth+1]));
				else 
					di.addTimeAttribute("Class attention", j, dataDesc.getAttrbDesc("Class attention").minRange);
				
				if( fields[offset+j*sectionWidth+2].length() > 0 )
					di.addTimeAttribute("Teacher energy", j, Float.valueOf(fields[offset+j*sectionWidth+2]));
				else 
					di.addTimeAttribute("Teacher energy", j, dataDesc.getAttrbDesc("Teacher energy").minRange);
				
				if( fields[offset+j*sectionWidth+3].length() > 0 )
					di.addTimeAttribute("Infomation important", j, Float.valueOf(fields[offset+j*sectionWidth+3]));
				else 
					di.addTimeAttribute("Infomation important", j, dataDesc.getAttrbDesc("Infomation important").minRange);
				
				if( fields[offset+j*sectionWidth+4].length() > 0 )
					di.addTimeAttribute("Listening", j, Float.valueOf(fields[offset+j*sectionWidth+4]));
				else 
					di.addTimeAttribute("Listening", j, dataDesc.getAttrbDesc("Listening").minRange);
				
				if( fields[offset+j*sectionWidth+5].length() > 0 )
					di.addTimeAttribute("Taking notes", j, Float.valueOf(fields[offset+j*sectionWidth+5]));
				else 
					di.addTimeAttribute("Taking notes", j, dataDesc.getAttrbDesc("Taking notes").minRange);
				
				if( fields[offset+j*sectionWidth+6].length() > 0 )
					di.addTimeAttribute("Repeating ideas", j, Float.valueOf(fields[offset+j*sectionWidth+6]));
				else 
					di.addTimeAttribute("Repeating ideas", j, dataDesc.getAttrbDesc("Repeating ideas").minRange);
				
				if( fields[offset+j*sectionWidth+7].length() > 0 )
					di.addTimeAttribute("Thinking about other things", j, Float.valueOf(fields[offset+j*sectionWidth+7]));
				else 
					di.addTimeAttribute("Thinking about other things", j, dataDesc.getAttrbDesc("Thinking about other things").minRange);
				
				if( fields[offset+j*sectionWidth+8].length() > 0 )
					di.addTimeAttribute("Interacting with someone", j, Float.valueOf(fields[offset+j*sectionWidth+8]));
				else 
					di.addTimeAttribute("Interacting with someone", j, dataDesc.getAttrbDesc("Interacting with someone").minRange);
				
				if( fields[offset+j*sectionWidth+9].length() > 0 )
					di.addTimeAttribute("Surfing for fun", j, Float.valueOf(fields[offset+j*sectionWidth+9]));
				else 
					di.addTimeAttribute("Surfing for fun", j, dataDesc.getAttrbDesc("Surfing for fun").minRange);
			}
			data.add(di);
		}
	}

	private void processHeader(String line) {
		String [] fields = PApplet.split(line,',');
		// hardcoded information - very bad
		int nonRepeatedValues = 5,
			repeatedValues = 10;
//		for(  )
	}
}
