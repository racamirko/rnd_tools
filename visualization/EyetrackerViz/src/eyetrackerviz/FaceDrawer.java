package eyetrackerviz;

import java.util.Vector;

import data.HeadPositionProvider;
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
			switch( face.mixtureNo ){
				case 1:
					draw90l(face.bbox);
					break;
				case 2:
					draw60l(face.bbox);
					break;
				case 3:
					draw45l(face.bbox);
					break;
				case -4:
				case 4:
					draw0(face.bbox);
					break;
				case -3:
					draw45r(face.bbox);
					break;
				case -2:
					draw60r(face.bbox);
					break;
				case -1:
					draw90r(face.bbox);
					break;
			}
			pa.text(String.format("%ddeg", mixToDeg( face.mixtureNo)), face.bbox.getCenterX(), face.bbox.getCenterY()-30);
		}
		pa.popStyle();
	}
	
	private int mixToDeg( int mixNo ){
		switch( mixNo ){
			case 1:
				return 90;
			case 2:
				return 60;
			case 3:
				return 45;
			case -4:
			case 4:
				return 0;
			case -3:
				return -45;
			case -2:
				return -60;
			case -1:
				return -90;
		}
		return 0;
	}
	
	private void draw90l(Rect2d bbox){
		Arrows.arrowLine(pa, bbox.getX1(), bbox.getHeight()/2.0f+bbox.getY1(),
				bbox.getX1()-40.0f, bbox.getHeight()/2.0f+bbox.getY1(), 0.0f, arrowAngle, true);
	}

	private void draw60l(Rect2d bbox){
		Arrows.arrowLine(pa, bbox.getX1()+bbox.getWidth()/6.0f, bbox.getHeight()/2.0f+bbox.getY1()+10.0f,
				bbox.getX1()-20.0f, bbox.getHeight()/1.5f+bbox.getY1(), 0.0f, arrowAngle, true);
	}

	private void draw45l(Rect2d bbox){
		Arrows.arrowLine(pa, bbox.getX1()+bbox.getWidth()/4.0f, bbox.getHeight()/2.0f+bbox.getY1()+15.0f,
				bbox.getX1()-10.0f, bbox.getHeight()*0.75f+bbox.getY1(), 0.0f, arrowAngle, true);
	}

	private void draw0(Rect2d bbox){
		float centerX = bbox.getX1()+bbox.getWidth()/2.0f,
			  centerY = bbox.getY1()+bbox.getHeight()/2.0f;
		pa.ellipse(centerX, centerY, 10.0f, 10.0f);
		pa.line(centerX, centerY, centerX, centerY);
	}

	private void draw45r(Rect2d bbox){
		Arrows.arrowLine(pa, bbox.getX1()+bbox.getWidth()*0.75f, bbox.getHeight()/2.0f+bbox.getY1()+15.0f,
				bbox.getX1()+bbox.getWidth()+10.0f, bbox.getHeight()*0.75f+bbox.getY1(), 0.0f, arrowAngle, true);
	}

	private void draw60r(Rect2d bbox){
		Arrows.arrowLine(pa, bbox.getX1()+bbox.getWidth()*0.86f, bbox.getHeight()/2.0f+bbox.getY1()+10.0f,
				bbox.getX1()+bbox.getWidth()+20.0f, bbox.getHeight()/1.5f+bbox.getY1(), 0.0f, arrowAngle, true);		
	}

	private void draw90r(Rect2d bbox){
		Arrows.arrowLine(pa, bbox.getX1()+bbox.getWidth(), bbox.getHeight()/2.0f+bbox.getY1(),
				bbox.getX1()+bbox.getWidth()+40.0f, bbox.getHeight()/2.0f+bbox.getY1(), 0.0f, arrowAngle, true);
	}
}
