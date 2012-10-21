package classroomdisplay;

import java.util.Random;
import java.util.Vector;

import processing.core.PApplet;
import classroomdisplay.data.AttributeDescription;
import classroomdisplay.data.DataItem;
import classroomdisplay.data.AttributeDescription.eAttributeDisplayType;
import classroomdisplay.data.DataDescription;

public class TestDataGenerator {
	PApplet pa;
	public DataDescription dataDesc;
	public Vector<DataItem> data;
	
	public TestDataGenerator(PApplet pa, int numOfItems){
		this.pa = pa;
		initDataDesc();
		initData(numOfItems);
	}

	private void initData(int numOfItems) {
		data = new Vector<DataItem>();
		DataItem tmpItem = null;
		Random rnd = new Random(System.currentTimeMillis());
		AttributeDescription ad = null;
		for( int i = 0; i < numOfItems; ++i ){
			tmpItem = new DataItem();
			// generate location
			tmpItem.x = i / 10;
			tmpItem.y = i % 10;
			// generate attributes
			ad = dataDesc.getAttrbDesc("attention");
			tmpItem.attributes.put("attention", rnd.nextFloat()*ad.getRange()+ad.minRange );
			ad = dataDesc.getAttrbDesc("attentionNeight");
			tmpItem.attributes.put("attentionNeight", rnd.nextFloat()*ad.getRange()+ad.minRange );
			ad = dataDesc.getAttrbDesc("energyTeacher");
			tmpItem.attributes.put("energyTeacher", rnd.nextFloat()*ad.getRange()+ad.minRange );
			
			tmpItem.attributes.put("listening", (float)rnd.nextInt(2));
			tmpItem.attributes.put("writting", (float)rnd.nextInt(2));
			tmpItem.attributes.put("repeatingKeyIdeas", (float)rnd.nextInt(2));
			data.add(tmpItem);
		}
	}

	private void initDataDesc() {
		dataDesc = new DataDescription();
		dataDesc.addAttribute( new AttributeDescription("attention", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(255, 0, 0)));
		dataDesc.addAttribute( new AttributeDescription("attentionNeight", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(0, 255, 0)));
		dataDesc.addAttribute( new AttributeDescription("energyTeacher", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(0, 0, 255)));
		// rings
		dataDesc.addAttribute( new AttributeDescription("listening", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(30,0,80)));
		dataDesc.addAttribute( new AttributeDescription("writting", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(30,60,10)));
		dataDesc.addAttribute( new AttributeDescription("repeatingKeyIdeas", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(80,20,10)));
	}
	
	
}
