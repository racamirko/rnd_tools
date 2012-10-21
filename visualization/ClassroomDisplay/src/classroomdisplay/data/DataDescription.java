package classroomdisplay.data;

import java.util.HashMap;
import java.util.Vector;

import classroomdisplay.data.AttributeDescription.eAttributeDisplayType;

public class DataDescription {
	public HashMap<String, AttributeDescription> attribDescriptions;
	public Vector<String> attrsCenter;
	public Vector<String> attrsRings;
	
	public DataDescription(){
		attribDescriptions = new HashMap<String, AttributeDescription>();
		attrsCenter = new Vector<String>();
		attrsRings = new Vector<String>();
	}
	
	public void addAttribute( AttributeDescription attr ){
		attribDescriptions.put(attr.name, attr);
		if( attr.displayType == eAttributeDisplayType.ATD_CENTER )
			attrsCenter.add(attr.name);
		else
			attrsRings.add(attr.name);
	}
	
	public AttributeDescription getAttrbDesc( String attrName ){
		return attribDescriptions.get(attrName);
	}
}
