package classroomdisplay.data;

import java.util.Vector;

import classroomdisplay.data.AttributeDescription.eAttributeDisplayType;

public class CombinedAttributeDescription extends AttributeDescription {
	public Vector<AttributeDescription> subAttribs;
	
	public CombinedAttributeDescription(String name, eAttributeDisplayType displayType, int color, boolean timeVariable){
		super(name, Float.MIN_VALUE, Float.MIN_VALUE, displayType, color, timeVariable);
		subAttribs = new Vector<AttributeDescription>();
	}
	
	public void addSubAttribute(AttributeDescription attrDesc){
		subAttribs.add(attrDesc);
	}
}
