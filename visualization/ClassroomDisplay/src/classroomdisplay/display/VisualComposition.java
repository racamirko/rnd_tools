package classroomdisplay.display;

import java.util.Vector;

import classroomdisplay.data.AttributeDescription;

public class VisualComposition {
	public Vector<AttributeDescription> attributesToDisplay;
	public int series;
	
	public VisualComposition(){
		attributesToDisplay = new Vector<AttributeDescription>();
		series = 0;
	}
}
