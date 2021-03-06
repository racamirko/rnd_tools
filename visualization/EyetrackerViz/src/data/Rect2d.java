package data;

public class Rect2d {
	protected float x1, y1, x2, y2;
	
	public Rect2d(){
		x1 = 0;
		x2 = 0;
		y1 = 0;
		y2 = 0;
	}
	
	public Rect2d(float x1, float y1, float x2, float y2){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public float getX1() {
		return x1;
	}

	public void setX1(float x1) {
		this.x1 = x1;
	}

	public float getY1() {
		return y1;
	}

	public void setY1(float y1) {
		this.y1 = y1;
	}

	public float getX2() {
		return x2;
	}

	public void setX2(float x2) {
		this.x2 = x2;
	}

	public float getY2() {
		return y2;
	}

	public void setY2(float y2) {
		this.y2 = y2;
	}
	
	public float getWidth(){
		return Math.abs(x2-x1);
	}
	
	public void setWidth(float w){
		x2 = x1 + w;
	}
	
	public float getHeight(){
		return Math.abs(y2-y1);
	}
	
	public void setHeight(float h){
		y2 = y1+h;
	}
	
	public float getCenterX(){
		return (x1 + x2)/2.0f;
	}
	
	public float getCenterY(){
		return (y1 + y2)/2.0f;
	}

}
