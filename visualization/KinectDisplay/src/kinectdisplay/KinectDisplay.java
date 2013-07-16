package kinectdisplay;

import com.jogamp.graph.math.Quaternion;

import processing.core.PApplet;
import processing.event.MouseEvent;


public class KinectDisplay extends PApplet {
	protected Quaternion rot, trans;
	protected float lastMouseX, lastMouseY;
	protected float wndSize[];

	public void setup() {
		// window
		wndSize = new float[2];
		wndSize[0] = 1240.0f; wndSize[1] = 880.0f;
		size((int)wndSize[0], (int)wndSize[1], P3D);
		rot = new Quaternion(0.0f,0.0f,PI,0.0f);
		trans = new Quaternion(631.0f,438.0f,500.0f,0.0f);
		// perspective
		float fov = (float) (PI/3.5);
		float cameraZ = (float) ((height/2.0) / tan(fov/2.0f));
		perspective(fov, (float)(width/height), cameraZ/10.0f, cameraZ*100.0f);
	}

	public void draw() {
		fill(200.0f);
		rect(0.0f, 0.0f, wndSize[0], wndSize[1]);
		drawOverlay();
		pushStyle();
		lights();
		translate(trans.getX(), trans.getY(), trans.getZ());
		rotateX(rot.getX());
		rotateY(rot.getY());
		rotateZ(rot.getZ());
		drawRapper();
		noStroke();
		translate(50.0f, 150.0f, 0.0f);
		sphere(10.0f);
		popStyle();
		rot.setY(rot.getY() + 0.01f);
//		trans.setZ(trans.getZ()+1.5f);
	}
	
	public void drawOverlay(){
		pushStyle();
		textSize(18.0f);
		fill(0,102,153);
//		System.out.println("["+trans.getX()+","+trans.getY()+","+trans.getZ()+"]");
//		System.out.println("["+rot.getX()+","+rot.getY()+","+rot.getZ()+"]");
		text("["+trans.getX()+","+trans.getY()+","+trans.getZ()+"]", 20.0f, 18.0f);
		text("["+rot.getX()+","+rot.getY()+","+rot.getZ()+"]", 20.0f, 36.0f);
		popStyle();
	}
	
	public void drawRapper(){
		final float lineLength = 20.0f;
		pushStyle();
		pushMatrix();
			strokeWeight(2.0f);
			// x
			stroke(0.0f, 0.0f, 255.0f);
			beginShape(LINES);
				vertex(0.0f, 0.0f, 0.0f);
				vertex(lineLength, 0.0f, 0.0f);
			endShape();
			// y
			stroke(255.0f, 0.0f, 0.0f);
			beginShape(LINES);
				vertex(0.0f, 0.0f, 0.0f);
				vertex(0.0f, lineLength, 0.0f);
			endShape();
			// z
			stroke(0.0f, 255.0f, 0.0f);
			beginShape(LINES);
				vertex(0.0f, 0.0f, 0.0f);
				vertex(0.0f, 0.0f, lineLength);
			endShape();
		popMatrix();
		popStyle();
	}
	
	public void mousePressed() {
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}
	
	public void mouseDragged(){
		if( mouseButton == LEFT ){
			rot.setY( rot.getY() + (mouseX-lastMouseX)*0.005f);
			rot.setX( rot.getX() + (mouseY-lastMouseY)*0.005f);
		}
		
		if( mouseButton == RIGHT ){
			trans.setX(trans.getX() + (mouseX-lastMouseX));
			trans.setY(trans.getY() + (mouseY-lastMouseY));
		}
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}
	
	public void mouseWheel(MouseEvent event) {
		  float e = event.getClickCount();
		  System.out.println(e);
		  trans.setZ(trans.getZ() + e*10.0f);
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { kinectdisplay.KinectDisplay.class.getName() });
	}
}
