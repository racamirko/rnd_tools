package kinectdisplay.elements;

import processing.core.PApplet;
import processing.core.PImage;
import kinectdisplay.IDrawable;

public class Tag implements IDrawable {

	protected float halfSize;
	protected PApplet p;
	protected PImage texImg;
	
	public Tag(PApplet p, float size, PImage tex){
		this.p = p;
		this.halfSize = Math.abs(size/2.0f);
		this.texImg = tex;
	}

	@Override
	public void draw() {
		p.pushStyle();
		p.beginShape();
			p.texture(texImg);
			p.vertex(-halfSize, -halfSize, 0.0f, 0.0f, texImg.height);
			p.vertex(halfSize, -halfSize, 0.0f, texImg.width, texImg.height);
			p.vertex(halfSize, halfSize, 0.0f, texImg.width, 0.0f);
			p.vertex(-halfSize, halfSize, 0.0f, 0.0f, 0.0f);
		p.endShape();
		p.popStyle();
	}

}
