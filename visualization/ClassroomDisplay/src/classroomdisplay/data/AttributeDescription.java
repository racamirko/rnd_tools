package classroomdisplay.data;

public class AttributeDescription {
	public enum eAttributeDisplayType { ATD_CENTER, ATD_RING };
	
	public String name;
	public float minRange, maxRange;
	public int color;
	public eAttributeDisplayType displayType;
	
	
	public AttributeDescription(){
		
	}
	
	public AttributeDescription(String name, float minRange, float maxRange, eAttributeDisplayType displayType, int color){
		this.name = name;
		this.minRange = minRange;
		this.maxRange = maxRange;
		this.color = color;
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
