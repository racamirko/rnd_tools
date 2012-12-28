package classroomdisplay.data;

public class AttributeDescription {
	public enum eAttributeDisplayType { ATD_CENTER, ATD_RING, ATD_SUBPART, ATD_COMBINED_ROSE };
	
	public String name;
	public float minRange, maxRange;
	public int color;
	public eAttributeDisplayType displayType;
	public boolean timeVariable;
	
	public AttributeDescription(){
		// nothing to do here
	}
	
	public AttributeDescription(String name, float minRange, float maxRange, eAttributeDisplayType displayType, int color, boolean timeVariable){
		this.name = name;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.color = color;
		this.displayType = displayType;
		this.timeVariable = timeVariable;
	}
	
	public float getRange(){
		return maxRange - minRange;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AttributeDescription))
			return false;
		AttributeDescription other = (AttributeDescription) obj;
		if( other.name.equals(this.name) )
			return true;
		return false;
	}
}
