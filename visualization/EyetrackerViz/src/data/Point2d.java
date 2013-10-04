package data;

public class Point2d {
	private float x,y;

	public Point2d(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public String toString(){
		return "("+x+", "+y+")";
	}

	public void clear(){
		x = 0.0f;
		y = 0.0f;
	}
}
