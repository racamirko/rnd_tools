package classroomdisplay.data;

public class AttributeDescription {
	public enum eAttributeDisplayType { ATD_CENTER, ATD_RING, ATD_SUBPART, ATD_COMBINED_ROSE, ATD_DELTA, ATD_HIGHLIGHT };
	
	public String name;
	public float minRange, maxRange;
	public int color;
	public eAttributeDisplayType displayType;
	public boolean timeVariable;

	public AttributeDescription(){
		this.name = "";
		this.minRange = 0.0f;
		this.maxRange = 0.0f;
		this.color = 0;
		this.displayType = eAttributeDisplayType.ATD_CENTER;
		this.timeVariable = false;
	}

	public AttributeDescription(eAttributeDisplayType displayType){
		this.displayType = displayType;
		// defaults
		this.name = "";
		this.minRange = 0.0f;
		this.maxRange = 0.0f;
		this.color = 0;
		this.timeVariable = false;
	}
	
	public AttributeDescription(String name, float minRange, float maxRange, eAttributeDisplayType displayType, int color, boolean timeVariable){
		this.name = name;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.color = color;
		this.displayType = displayType;
		this.timeVariable = timeVariable;
	}
	
	public float getRangeMean(){
		return minRange+getRange()/2;
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
