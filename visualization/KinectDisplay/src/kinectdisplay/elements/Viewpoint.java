package kinectdisplay.elements;

import processing.core.PApplet;
import sun.swing.MenuItemLayoutHelper.RectSize;
import kinectdisplay.IDrawable;

public class Viewpoint implements IDrawable {

	protected PApplet p;
	protected final float rectSize = 50.0f,
						  rectDist = 100.0f;
	
	
	public Viewpoint(PApplet p){
		this.p = p;
	}
	
	@Override
	public void draw() {
		p.pushMatrix();
		p.pushStyle();
		
		p.sphere(2.0f);
		
		p.noFill(); // view rectangle
		p.beginShape();
			p.vertex(-rectSize, -rectSize, rectDist);
			p.vertex(rectSize, -rectSize, rectDist);
			p.vertex(rectSize, rectSize, rectDist);
			p.vertex(-rectSize, rectSize, rectDist);
		p.endShape();
		
//		p.beginShape(PApplet.LINES);
//			p.vertex(0.0f, 0.0f, 0.0f);
//			p.vertex(-rectSize, -rectSize, rectDist);
//			p.vertex(0.0f, 0.0f, 0.0f);
//			p.vertex(rectSize, -rectSize, rectDist);
//			p.vertex(0.0f, 0.0f, 0.0f);
//			p.vertex(rectSize, rectSize, rectDist);
//			p.vertex(0.0f, 0.0f, 0.0f);
//			p.vertex(-rectSize, rectSize, rectDist);
//		p.endShape();
		
		p.popMatrix();
		p.popStyle();
	}

}
