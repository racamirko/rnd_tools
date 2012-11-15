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
			for( int j = 0; j < 4; ++j ){
				ad = dataDesc.getAttrbDesc("attention");
				tmpItem.addTimeAttribute("attention", j, rnd.nextFloat()*ad.getRange()+ad.minRange );
				ad = dataDesc.getAttrbDesc("attentionNeight");
				tmpItem.addTimeAttribute("attentionNeight", j, rnd.nextFloat()*ad.getRange()+ad.minRange );
				ad = dataDesc.getAttrbDesc("energyTeacher");
				tmpItem.addTimeAttribute("energyTeacher", j, rnd.nextFloat()*ad.getRange()+ad.minRange );
				
				tmpItem.addTimeAttribute("listening", j, (float)rnd.nextInt(2));
				tmpItem.addTimeAttribute("writting", j, (float)rnd.nextInt(2));
				tmpItem.addTimeAttribute("repeatingKeyIdeas", j, (float)rnd.nextInt(2));
			}
			data.add(tmpItem);
		}
	}

	private void initDataDesc() {
		dataDesc = new DataDescription();
		dataDesc.addAttribute( new AttributeDescription("attention", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(255, 99, 83), true));
		dataDesc.addAttribute( new AttributeDescription("attentionNeight", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(101, 233, 79), true));
		dataDesc.addAttribute( new AttributeDescription("energyTeacher", 1.0f, 10.0f, eAttributeDisplayType.ATD_CENTER, pa.color(98, 80, 220), true));
		// rings
		dataDesc.addAttribute( new AttributeDescription("listening", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(105,175,208), true));
		dataDesc.addAttribute( new AttributeDescription("writting", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(86,248,130), true));
		dataDesc.addAttribute( new AttributeDescription("repeatingKeyIdeas", 0.0f, 1.0f, eAttributeDisplayType.ATD_RING, pa.color(255,228,122), true));
	}
	
	
}
