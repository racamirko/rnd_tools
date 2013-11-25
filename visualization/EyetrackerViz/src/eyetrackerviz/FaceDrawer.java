package eyetrackerviz;

import java.util.Vector;

import data.HeadPositionProvider;
import data.Point2d;
import data.Rect2d;
import eyetrackerviz.utils.Arrows;
import processing.core.PApplet;

public class FaceDrawer {
	protected PApplet pa;
	private float arrowAngle;

	public FaceDrawer(PApplet pa ) {
		this.pa = pa;
		arrowAngle = 0.436332313f;
	}
	
	public void draw( Vector<HeadPositionProvider.FaceBoundingBox> material ){
		pa.pushStyle();
		pa.noFill();
		pa.color(243,69,69);
		pa.stroke(243.0f,69.0f,69.0f);
		pa.strokeWeight(1.5f);
		for( HeadPositionProvider.FaceBoundingBox face : material ){
			pa.rect(face.bbox.getX1(), face.bbox.getY1(), face.bbox.getWidth(), face.bbox.getHeight());
			drawAngle(face.bbox, face.angle);

			pa.text(String.format("%.1fdeg", face.angle), face.bbox.getCenterX(), face.bbox.getCenterY()-30);
		}
		pa.popStyle();
	}
	
	private void drawAngle(Rect2d bbox, double angle){
		Point2d endPt = new Point2d();
		float OUTSIDE_OF_BOX_OFFSET = 40.0f;
		if( angle == 0.0f ){
			pa.ellipse(bbox.getCenterX(), bbox.getCenterY(), 10.0f, 10.0f);
			pa.line(bbox.getCenterX(), bbox.getCenterY(), bbox.getCenterX(), bbox.getCenterY());
			return;
		} else {
			endPt.setX( (float) (bbox.getCenterX() - (Math.sin( Math.toRadians(angle) ) * (bbox.getWidth()/2.0f+OUTSIDE_OF_BOX_OFFSET))) );
			endPt.setY( (float) (bbox.getCenterY() + (Math.cos( Math.toRadians(angle) / Math.PI ) * (bbox.getHeight()/4.0f))));
		}
		Arrows.arrowLine(pa, bbox.getCenterX(), bbox.getCenterY(), endPt.getX(), endPt.getY(), 0.0f, arrowAngle, true);
	}
}
