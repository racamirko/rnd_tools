package kinectdisplay;

import java.util.Vector;

import com.jogamp.opengl.math.Quaternion;

import processing.core.PApplet;
import processing.core.PVector;

public class GraphicalNode implements IDrawable {
	protected PVector trans;
	protected Quaternion rot;
	protected Vector<IDrawable> vecDrawables;
	protected PApplet p;
	public boolean visible;
	
	public GraphicalNode(PApplet p){
		this.p = p;
		trans = new PVector(0.0f, 0.0f, 0.0f);
		rot = new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
		vecDrawables = new Vector<IDrawable>();
		visible = true;
	}
	
	public GraphicalNode(PApplet p, IDrawable d){
		this(p);
		vecDrawables.add(d);
	}
	
	@Override
	public void draw() {
		if(!visible)
			return;
		p.pushMatrix();

		p.translate(trans.x, trans.y, trans.z);
		p.rotateX(rot.getX());
		p.rotateY(rot.getY());
		p.rotateZ(rot.getZ());
		
		for( IDrawable d : vecDrawables ){
			d.draw();
		}
		
		p.popMatrix();
	}
	
	public GraphicalNode addDrawable( IDrawable d ){
		vecDrawables.add(d);
		return this;
	}
	
	public GraphicalNode setVisible(boolean b){
		visible = b;
		return this;
	}
	
	public GraphicalNode translate( PVector vec ){
		trans.add(vec);
		return this;
	}
	
	public PVector getLocation(){
		return trans;
	}
	
	public GraphicalNode setLocation(PVector vec){
		trans = vec;
		return this;
	}
	
	public GraphicalNode rotate(Quaternion q){
		rot.add(q);
		return this;
	}
	
	public GraphicalNode rotateX(float angleRad){
		rot.setX(rot.getX() + angleRad);
		return this;
	}
	
	public GraphicalNode rotateY(float angleRad){
		rot.setY(rot.getY() + angleRad);
		return this;
	}

	public GraphicalNode rotateZ(float angleRad){
		rot.setZ(rot.getZ() + angleRad);
		return this;
	}
	
	public Quaternion getRotation(){
		return rot;
	}
	
	public GraphicalNode setRotation(Quaternion q){
		rot = q;
		return this;
	}
		
	
}
