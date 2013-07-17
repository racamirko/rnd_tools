package kinectdisplay.elements;

import processing.core.PApplet;
import kinectdisplay.IDrawable;

public class Rapper implements IDrawable {

	protected PApplet p;
	
	public Rapper(PApplet p){
		this.p = p;
	}
	
	@Override
	public void draw() {
		final float lineLength = 20.0f;
		p.pushStyle();
		p.strokeWeight(2.0f);
		// x
		p.stroke(0.0f, 0.0f, 255.0f);
		p.beginShape(PApplet.LINES);
			p.vertex(0.0f, 0.0f, 0.0f);
			p.vertex(lineLength, 0.0f, 0.0f);
		p.endShape();
		// y
		p.stroke(255.0f, 0.0f, 0.0f);
		p.beginShape(PApplet.LINES);
			p.vertex(0.0f, 0.0f, 0.0f);
			p.vertex(0.0f, lineLength, 0.0f);
		p.endShape();
		// z
		p.stroke(0.0f, 255.0f, 0.0f);
		p.beginShape(PApplet.LINES);
			p.vertex(0.0f, 0.0f, 0.0f);
			p.vertex(0.0f, 0.0f, lineLength);
		p.endShape();
		p.popStyle();
	}

}
