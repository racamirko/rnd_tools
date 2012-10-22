package classroomdisplay.loaders;

import java.util.Vector;

import processing.core.PApplet;
import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.DataDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.AttributeDescription.eAttributeDisplayType;

public class CSVDataLoaderExperiment1 {
	PApplet pa;
	public DataDescription dataDesc;
	public Vector<DataItem> data;

	public CSVDataLoaderExperiment1(PApplet pa) {
		this.pa = pa;
		dataDesc = new DataDescription();
		data = new Vector<DataItem>();
		
		generateDataDescription();
	}
	
	private void generateDataDescription() {
		dataDesc.addAttribute(new AttributeDescription( "Attention1", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(255, 99, 83)) );
		dataDesc.addAttribute(new AttributeDescription( "Energy1", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(101, 233, 79)) );
		dataDesc.addAttribute(new AttributeDescription( "Positivity1", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(98, 80, 220)) );
		// general attributes, not repeated
		dataDesc.addAttribute( new AttributeDescription("Gender", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(5,175,208)));
		dataDesc.addAttribute( new AttributeDescription("Has laptop", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(105,175,8)));

		// rings (this should be repeated 4x)
		dataDesc.addAttribute( new AttributeDescription("Taking notes1", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(105,175,208)));
		dataDesc.addAttribute( new AttributeDescription("Repeating key ideas1", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(86,248,130)));
		dataDesc.addAttribute( new AttributeDescription("Thinking examples1", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(255,228,122)));

		dataDesc.addAttribute( new AttributeDescription("Searching the web1", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(135,175,178)));
		dataDesc.addAttribute( new AttributeDescription("Listening to lecture1", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(86,228,160)));
		dataDesc.addAttribute( new AttributeDescription("Thinking about other things1", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(225,188,122)));

		dataDesc.addAttribute( new AttributeDescription("Interacting with someone1", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(155,85,208)));
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
				di.attributes.put("Gender", 1.0f);
			else
				di.attributes.put("Gender", 0.0f);
			di.attributes.put("Has laptop", Float.valueOf(fields[2]));
			di.y = Float.valueOf(fields[3]); // row
			di.x = Float.valueOf(fields[4]); // seat
			
			// first period
			if( fields[5].length() > 0 )
				di.attributes.put("Attention1", Float.valueOf(fields[5]));
			else 
				di.attributes.put("Attention1", dataDesc.getAttrbDesc("Attention1").minRange);
			
			if( fields[6].length() > 0 )
				di.attributes.put("Energy1", Float.valueOf(fields[6]));
			else 
				di.attributes.put("Energy1", dataDesc.getAttrbDesc("Energy1").minRange);
			
			if( fields[7].length() > 0 )
				di.attributes.put("Positivity1", Float.valueOf(fields[7]));
			else 
				di.attributes.put("Positivity1", dataDesc.getAttrbDesc("Positivity1").minRange);
			
			if( fields[8].length() > 0 )
				di.attributes.put("Taking notes1", Float.valueOf(fields[8]));
			else 
				di.attributes.put("Taking notes1", dataDesc.getAttrbDesc("Taking notes1").minRange);
			
			if( fields[9].length() > 0 )
				di.attributes.put("Repeating key ideas1", Float.valueOf(fields[9]));
			else 
				di.attributes.put("Repeating key ideas1", dataDesc.getAttrbDesc("Repeating key ideas1").minRange);
			
			if( fields[10].length() > 0 )
				di.attributes.put("Thinking examples1", Float.valueOf(fields[10]));
			else 
				di.attributes.put("Thinking examples1", dataDesc.getAttrbDesc("Thinking examples1").minRange);
			
			if( fields[11].length() > 0 )
				di.attributes.put("Searching the web1", Float.valueOf(fields[11]));
			else 
				di.attributes.put("Searching the web1", dataDesc.getAttrbDesc("Searching the web1").minRange);
			
			if( fields[12].length() > 0 )
				di.attributes.put("Listening to lecture1", Float.valueOf(fields[12]));
			else 
				di.attributes.put("Listening to lecture1", dataDesc.getAttrbDesc("Listening to lecture1").minRange);
			
			if( fields[13].length() > 0 )
				di.attributes.put("Thinking about other things1", Float.valueOf(fields[13]));
			else 
				di.attributes.put("Thinking about other things1", dataDesc.getAttrbDesc("Thinking about other things1").minRange);
			
			if( fields[14].length() > 0 )
				di.attributes.put("Interacting with someone1", Float.valueOf(fields[14]));
			else 
				di.attributes.put("Interacting with someone1", dataDesc.getAttrbDesc("Interacting with someone1").minRange);
			
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
