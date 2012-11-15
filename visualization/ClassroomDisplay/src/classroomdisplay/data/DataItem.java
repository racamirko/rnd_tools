package classroomdisplay.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class DataItem {
	protected HashMap<String, Float> fixedAttributes;
	protected HashMap<String, Vector<Float>> attributes;
	protected Set<String> fixedAttributesSet;
	public float x, y; // representing row and seat
	
	public DataItem(){
		attributes = new HashMap<String, Vector<Float>>();
		fixedAttributes = new HashMap<String, Float>();
		fixedAttributesSet = new HashSet<String>();
		x = 0;
		y = 0;
	}
	
	public void addFixedAttribute(String name, float value){
		fixedAttributes.put(name, value);
		fixedAttributesSet.add(name);
	}
	
	public void addTimeAttribute(String name, int series, float value){
		if( !attributes.containsKey(name) )
			attributes.put(name, new Vector<Float>());
		attributes.get(name).insertElementAt(value, series);
	}
	
	public float getAttributeValue(AttributeDescription attrDesc){
		if( fixedAttributesSet.contains(attrDesc.name) )
			return fixedAttributes.get(attrDesc.name);
		return attributes.get(attrDesc.name).get(0);
	}
	
	public float getAttributeValue(AttributeDescription attrDesc, int series){
		if( fixedAttributesSet.contains(attrDesc.name) )
			return fixedAttributes.get(attrDesc.name);
		return attributes.get(attrDesc.name).get(series);
	}

	public Vector<Float> getAttributeSeries(AttributeDescription attrDesc){
		return attributes.get(attrDesc.name);
	}
}
