package kinectdisplay.elements;

import kinectdisplay.IDrawable;
import processing.core.PApplet;

public class Sphere implements IDrawable {

	protected float size;
	protected PApplet p;
	
	public Sphere(PApplet p, float size){
		this.p = p;
		this.size = size;
	}

	@Override
	public void draw() {
		p.pushStyle();
		p.noStroke();
		p.sphere(size);
		p.popStyle();
	}
	
	
}
