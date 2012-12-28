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
	
	public AttributeDescription load(String filename, String attributeName, Vector<DataItem> items, DataDescription dataDesc, AttributeDescription.eAttributeDisplayType displayType ){
		// for Attribute description
		Scanner scanner;
		try {
			scanner = new Scanner(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			System.out.println("File " + filename + " not found");
			return null;
		}
		AttributeDescription attrDesc = new AttributeDescription();
		attrDesc.name = attributeName;
		attrDesc.color = pa.color(rnd.nextInt(255),rnd.nextInt(255),rnd.nextInt(255));
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
				if( item.x == seat && item.y == row ){
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
				curItem.addTimeAttribute(attributeName, i-3, value);
			}
		}
		scanner.close();
		return attrDesc;
	}

}
