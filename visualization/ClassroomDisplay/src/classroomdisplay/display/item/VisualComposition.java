package classroomdisplay.display.item;

import java.util.Vector;

import classroomdisplay.data.AttributeDescription;

public class VisualComposition {
	public Vector<AttributeDescription> attributesToDisplay;
	public int series;
	
	public VisualComposition(){
		attributesToDisplay = new Vector<AttributeDescription>();
		series = 0;
	}
	
	public VisualComposition clone(){
		VisualComposition vc2 = new VisualComposition();
		vc2.attributesToDisplay.addAll(this.attributesToDisplay);
		vc2.series = this.series;
		return vc2;
	}
}
