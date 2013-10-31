package widgets;

import data.Point2f;
import processing.core.PApplet;

public class ClockDisplay {
	
	float time;
	float offset;
	Point2f point;
	PApplet pa;
	
	int hours, minutes, seconds, milisecs;

	public ClockDisplay(PApplet pa, Point2f pt){
		point = pt;
		this.pa = pa;
	}
	
	public void draw(){
		pa.pushStyle();
		pa.fill(220.0f);
		pa.rect(point.x, point.y-12.0f, 90.0f, 15.0f);
		pa.fill(230.0f, 20.0f, 20.0f);
		pa.text( String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milisecs), point.x, point.y);
		pa.popStyle();
	}
	
	public void setTime(float t){
		time = t-offset;
		hours = (int) Math.floor(time/3600.0f);
		minutes = (int) Math.floor((time % 3600.0f)/60.0f);
		seconds = (int) Math.floor( time % 60.0f );
		milisecs = (int) Math.floor( (time % 1.0f )*100.0f );
	}
	
	public void setOffset(float t){
		offset = t;
	}
	
	public void setX(float x){
		point.x = x;
	}
	
	public void setY(float y){
		point.y = y;
	}
}
