package kinectdisplay.elements;

import processing.core.PApplet;
import kinectdisplay.IDrawable;

public class GroundPlane implements IDrawable {
	
	protected PApplet p;
	protected float size; // yes, it's a square
	
	public GroundPlane(PApplet p, float size) {
		this.p = p;
		this.size = size;
	}
	
	public void draw(){
		p.pushStyle();
		p.noStroke();
		p.fill(200.0f, 255.0f, 200.0f);
		p.box(size, 0.8f, size);
		p.popStyle();
	}

}
