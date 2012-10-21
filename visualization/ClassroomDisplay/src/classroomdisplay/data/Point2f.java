package classroomdisplay.data;


public class Point2f {
	public float x, y;
	
	public Point2f(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public float distance(Point2f p2){
		return (float) Math.sqrt( Math.pow( p2.x - this.x ,2) + Math.pow( p2.y - this.y ,2) );
	}
	
	public void add(Point2f p2){
		this.x += p2.x;
		this.y += p2.y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Point2f))
			return false;
		Point2f other = (Point2f) obj;
		if( this.x == other.x && this.y == other.y )
			return true;
		return false;
	}
}
