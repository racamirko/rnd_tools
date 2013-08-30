package kinectdisplay.elements;

import processing.core.PApplet;
import sun.swing.MenuItemLayoutHelper.RectSize;
import kinectdisplay.IDrawable;

public class Viewpoint implements IDrawable {

	protected PApplet p;
	protected final float rectSize = 25.0f,
						  rectDist = 50.0f;
	
	
	public Viewpoint(PApplet p){
		this.p = p;
	}
	
	@Override
	public void draw() {
		p.pushMatrix();
		p.pushStyle();

		p.fill(128.0f);
		p.noStroke();
		p.sphere(2.0f);
		
		p.noFill(); // view rectangle
		p.strokeWeight(1.5f);
		p.stroke(128.0f);
		p.beginShape();
			p.vertex(-rectSize, -rectSize, rectDist);
			p.vertex(rectSize, -rectSize, rectDist);
			p.vertex(rectSize, rectSize, rectDist);
			p.vertex(-rectSize, rectSize, rectDist);
			p.vertex(-rectSize, -rectSize, rectDist);
		p.endShape();
		
		p.beginShape(PApplet.LINES);
			p.vertex(0.0f, 0.0f, 0.0f);
			p.vertex(-rectSize, -rectSize, rectDist);
			p.vertex(0.0f, 0.0f, 0.0f);
			p.vertex(rectSize, -rectSize, rectDist);
			p.vertex(0.0f, 0.0f, 0.0f);
			p.vertex(rectSize, rectSize, rectDist);
			p.vertex(0.0f, 0.0f, 0.0f);
			p.vertex(-rectSize, rectSize, rectDist);
			// central arrow
			p.stroke(93.0f, 178.0f, 231.0f);
			p.vertex(0.0f, 0.0f, 0.0f);
			p.vertex(0.0f, 0.0f, rectDist+20.0f);
			
			p.vertex(1.0f, 1.0f, rectDist+15.0f);
			p.vertex(0.0f, 0.0f, rectDist+20.0f);
			
			p.vertex(-1.0f, -1.0f, rectDist+15.0f);
			p.vertex(0.0f, 0.0f, rectDist+20.0f);
			
			// long line
			p.stroke(255.0f, 0.0f, 0.0f);
			p.strokeWeight(1.0f);
			p.vertex(0.0f, 0.0f, 0.0f);
			p.vertex(0.0f, 0.0f, 30.0f*rectDist);
		p.endShape();
		
		p.popMatrix();
		p.popStyle();
	}

}
