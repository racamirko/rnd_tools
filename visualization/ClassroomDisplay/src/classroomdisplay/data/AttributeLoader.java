package classroomdisplay.data;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import processing.core.PApplet;

public class AttributeLoader {
	PApplet pa;
	Random rnd;
	
	public AttributeLoader(PApplet pa){
		this.pa = pa;
		Date d = new Date();
		rnd = new Random(d.getTime());
	}
	
	public AttributeDescription load(String filename, Vector<DataItem> items, DataDescription dataDesc, AttributeDescription.eAttributeDisplayType displayType ){
		// for Attribute description
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.out.println("File " + filename + " not found");
			return null;
		}
		AttributeDescription attrDesc = new AttributeDescription();
		boolean timeVariant = true;
		attrDesc.name = "";
		attrDesc.displayType = displayType;
		attrDesc.color = pa.color(30+rnd.nextInt(190),40+rnd.nextInt(180),60+rnd.nextInt(160));
		while( scanner.hasNextLine() ){
			String tmpStr = scanner.nextLine();
			if(tmpStr.length() == 0)
				break;
			String[] parts = tmpStr.split(":");
			if( parts[0].equals("min") ){
				attrDesc.minRange = Float.parseFloat(parts[1].trim());
				continue;
			}
			if( parts[0].equals("max") ){
				attrDesc.maxRange = Float.parseFloat(parts[1].trim());
				continue;
			}
			if( parts[0].equals("name") ){
				attrDesc.name = parts[1].trim();
				continue;
			}
			if( parts[0].equals("time-variable") ){
				if( parts[1].trim().equalsIgnoreCase("false") )
					timeVariant = false;
				continue;
			}
			
		}
		// for data attributes addition
		while( scanner.hasNextLine() ){
			String tmpStr = scanner.nextLine();
			String[] parts = tmpStr.split(",");
			int id = Integer.parseInt(parts[0]);
			float row = Float.parseFloat(parts[1]),
				  seat = Float.parseFloat(parts[2]);
			// find person
			DataItem curItem = null;
			for( DataItem item : items ){
				if( item.getId() == id ){
					curItem = item;
					break;
				}
			}
			// add additional information
			if( curItem == null ){
				System.out.println( "Could not find person "+id+" at (" + seat +", "+row+")" );
				continue;
			}
			// make attribute and append
			for( int i = 3; i < parts.length; ++i ){
				float value = Float.parseFloat(parts[i]);
				if( timeVariant )
					curItem.addTimeAttribute(attrDesc.name, i-3, value);
				else
					curItem.addFixedAttribute(attrDesc.name, value);
			}
		}
		scanner.close();
		return attrDesc;
	}

}
