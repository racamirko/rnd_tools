package kinectdisplay;

import java.io.FileNotFoundException;
import java.util.Vector;

import com.jogamp.opengl.math.Quaternion;

import kinectdisplay.elements.GroundPlane;
import kinectdisplay.elements.Rapper;
import kinectdisplay.elements.Sphere;
import kinectdisplay.elements.Viewpoint;

import animation.IAnimator;
import animation.Animator;


import data.DataReader;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;


public class KinectDisplay extends PApplet {
	protected PVector trans;
	protected Quaternion rot;
	protected float lastMouseX, lastMouseY;
	protected float wndSize[];
	protected Vector<IDrawable> drawables;
	protected Vector<IAnimator> animators;
	protected String fnCameraFile;
	protected int lastTime;
	
	// needed objects in the scene
	GraphicalNode viewptNode;

	public void setup() {
		// window
		wndSize = new float[2];
		wndSize[0] = 1240.0f; wndSize[1] = 880.0f;
		size((int)wndSize[0], (int)wndSize[1], P3D);
		rot = new Quaternion(-PI/8.0f,-PI/6.0f,PI,1.0f);
		trans = new PVector(631.0f,538.0f,300.0f);
		// perspective
		fnCameraFile = "/home/raca/data/eyetracking/01_test_exp/coords/camera_position_normal.txt";
//		perspective(fov, (float)(width/height), cameraZ/10.0f, cameraZ*10000.0f);
		drawables = new Vector<IDrawable>();
		animators = new Vector<IAnimator>();
		lastTime = millis();
		// object setup
		setupObjects();
		setupLife();
	}
	
	protected void setupObjects(){
		drawables.add(new Rapper(this));
		drawables.add(new GraphicalNode(this, new Sphere(this, 8.0f)).translate(new PVector(50.0f, 150.0f, 0.0f)) );
		drawables.add(new GraphicalNode(this, new GroundPlane(this, 350.0f)));
		viewptNode = new GraphicalNode(this, new Viewpoint(this)).translate(new PVector(-50.0f, 100.0f, 0.0f));
		drawables.add(viewptNode);
	}
	
	public void setupLife(){
		try {
			animators.add(new Animator(viewptNode, new DataReader(fnCameraFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void draw() {
		// setup
		float fov = (float) (PI/3.5);
		float cameraZ = (float) ((height/2.0) / tan(fov/2.0f));
		perspective(fov, (float)(width/height), 1.0f, 1000000000.0f);
		// life
		life();
		// drawing
		fill(200.0f);
		rect(0.0f, 0.0f, wndSize[0], wndSize[1]);
		drawOverlay();
		lights();
		translate(trans.x, trans.y, trans.z);
		rotateX(rot.getX());
		rotateY(rot.getY());
		rotateZ(rot.getZ());
		
		for( IDrawable d : drawables )
			d.draw();

//		rot.setY(rot.getY() + 0.01f);
//		trans.setZ(trans.getZ()+1.5f);
	}
	
	public void life(){
		int currentTime = millis();
		int timeDiff = currentTime - lastTime;
		for( IAnimator ani : animators ){
			ani.tick(timeDiff);
		}
		
		lastTime = currentTime;
	}
	
	public void drawOverlay(){
		pushStyle();
		textSize(18.0f);
		fill(0,102,153);
//		System.out.println("["+trans.getX()+","+trans.getY()+","+trans.getZ()+"]");
//		System.out.println("["+rot.getX()+","+rot.getY()+","+rot.getZ()+"]");
		text("["+trans.x+","+trans.y+","+trans.z+"]", 20.0f, 18.0f);
		text("["+rot.getX()+","+rot.getY()+","+rot.getZ()+"]", 20.0f, 36.0f);
		popStyle();
	}
	
	public void mousePressed() {
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}
	
	public void mouseDragged(){
		if( mouseButton == LEFT ){
			rot.setY( rot.getY() + (mouseX-lastMouseX)*0.005f);
			rot.setX( rot.getX() - (mouseY-lastMouseY)*0.005f);
		}
		if( mouseButton == RIGHT ){
			if( key == CODED && keyCode == SHIFT ){
				trans.z = trans.z + (mouseY-lastMouseY);
			} else {
				trans.x = trans.x + (mouseX-lastMouseX);
				trans.y = trans.y + (mouseY-lastMouseY);
			}
		}
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}
	
	public void mouseWheel(MouseEvent event){
		// don't know why this doesn't work, API says it's ok ?
		float e = event.getClickCount();
		System.out.println(e);
		trans.z = trans.z + e*10.0f;
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { kinectdisplay.KinectDisplay.class.getName() });
	}
}
