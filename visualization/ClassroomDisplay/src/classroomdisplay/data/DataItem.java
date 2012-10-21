package classroomdisplay.data;

import java.util.HashMap;

public class DataItem {
	public HashMap<String, Float> attributes;
	public float x, y; // representing row and seat
	
	public DataItem(){
		attributes = new HashMap<String, Float>();
		x = 0;
		y = 0;
	}
	
	public float getAttributeValue(AttributeDescription attrDesc){
		return attributes.get(attrDesc.name);
	}
}
