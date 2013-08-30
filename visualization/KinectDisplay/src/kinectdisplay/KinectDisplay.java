package kinectdisplay;

import java.io.FileNotFoundException;
import java.util.Vector;

import codeanticode.gsvideo.GSMovie;

import com.jogamp.opengl.math.Quaternion;

import kinectdisplay.elements.GroundPlane;
import kinectdisplay.elements.Rapper;
import kinectdisplay.elements.Sphere;
import kinectdisplay.elements.Viewpoint;
import kinectdisplay.elements.factory.TagFactory;

import animation.IAnimator;
import animation.Animator;


import data.DataReader;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;


public class KinectDisplay extends PApplet {
	private static final long serialVersionUID = -8229822154771043123L;
	protected PVector trans;
	protected Quaternion rot;
	protected float lastMouseX, lastMouseY;
	protected float wndSize[];
	protected Vector<IDrawable> drawables;
	protected Vector<IAnimator> animators;
	protected String fnCameraFile, fnMovie;
	protected int lastTime;
	protected GSMovie overlayMovie;
	
	// needed objects in the scene
	GraphicalNode viewptNode;

	public void setup() {
		// parameters
		fnCameraFile = "/home/raca/data/eyetracking/01_test_exp/coords/camera_position_normal.txt";
		fnMovie = "/home/raca/data/eyetracking/01_test_exp/ex01_mirko_cut02.avi";
		// window
		wndSize = new float[2];
		wndSize[0] = 1240.0f; wndSize[1] = 880.0f;
		size((int)wndSize[0], (int)wndSize[1], P3D);
		rot = new Quaternion(-PI/8.0f,-PI/6.0f,PI,1.0f);
		trans = new PVector(631.0f,538.0f,300.0f);
		// perspective
		float fov = (float) (PI/3.5);
//		float cameraZ = (float) ((height/2.0) / tan(fov/2.0f));
//		perspective(fov, (float)(width/height), cameraZ/10.0f, cameraZ*10000.0f);
		perspective(fov, (float)(width/height), 10.0f, 100000000.0f);
		drawables = new Vector<IDrawable>();
		animators = new Vector<IAnimator>();
		lastTime = millis();
		// object setup
		setupObjects();
		setupLife();
		// testing
		overlayMovie = new GSMovie(this, fnMovie);
//		overlayMovie.play();
//		overlayMovie.goToBeginning();
//		overlayMovie.play();
//		overlayMovie.pause();
		overlayMovie.loop();
		overlayMovie.play();
//		overlayMovie.volume(0.0f);
	}
	
	void movieEvent(GSMovie movie) {
		overlayMovie.read();
		System.out.println("movieEvent");
	}
	
	protected void setupObjects(){
		drawables.add(new Rapper(this));
		drawables.add(new GraphicalNode(this, new Sphere(this, 8.0f)).translate(new PVector(50.0f, 150.0f, 0.0f)) );
		drawables.add(new GraphicalNode(this, new GroundPlane(this, 350.0f)));
		viewptNode = new GraphicalNode(this, new Viewpoint(this)).translate(new PVector(-50.0f, 100.0f, 0.0f));
		drawables.add(viewptNode);
		
		// tags
		TagFactory tmpFactory = new TagFactory(this, "/home/raca/data/facetracking/tags/medium_noborder", "%d.png", 20);
		drawables.add( tmpFactory.createTag(0, new PVector(-10.0f, 186.0f, 0.0f), new Quaternion(0.0f, 0.0f, 0.0f, 1.0f)));
		drawables.add( tmpFactory.createTag(1, new PVector(-147.0f, 186.0f, 0.0f), new Quaternion(0.0f, 0.0f, 0.0f, 1.0f)));
		drawables.add( tmpFactory.createTag(2, new PVector(-10.0f, 118.5f, 0.0f), new Quaternion(0.0f, 0.0f, 0.0f, 1.0f)));
		drawables.add( tmpFactory.createTag(3, new PVector(-147.0f, 118.5f, 0.0f), new Quaternion(0.0f, 0.0f, 0.0f, 1.0f)));
	}
	
	public void setupLife(){
		try {
			animators.add(new Animator(viewptNode, new DataReader(fnCameraFile)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void draw() {
		// life
		life();
		// drawing
		background(200.0f);
		lights();

		translate(trans.x, trans.y, trans.z);
		rotateX(rot.getX());
		rotateY(rot.getY());
		rotateZ(rot.getZ());
		
		for( IDrawable d : drawables )
			d.draw();

		drawOverlay();
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
//		text("["+trans.x+","+trans.y+","+trans.z+"]", 20.0f, 18.0f);
//		text("["+rot.getX()+","+rot.getY()+","+rot.getZ()+"]", 20.0f, 36.0f);
//		text("["+trans.x+","+trans.y+","+trans.z+"]", 0.0f, 0.0f);
//		text("["+rot.getX()+","+rot.getY()+","+rot.getZ()+"]", 0.0f, 3.0f);
		// overlay image
//		beginShape();
//			texture(overlayImg);
//			vertex(0.0f, 0.0f, -10.0f, 0.0f, 0.0f);
//			vertex(0.0f, -10.0f, -10.0f, 0.0f, overlayImg.height);
//			vertex(10.0f, -10.0f, -10.0f, overlayImg.width, overlayImg.height);
//			vertex(0.0f, 10.0f, -10.0f, overlayImg.width, 0.0f);
//		endShape();

		rotateZ(-rot.getZ());
		rotateY(-rot.getY());
		rotateX(-rot.getX());

		translate(-137.0f, -224.0f, 500.0f - trans.z );
		
//		rotateZ(-rot.getZ());
//		rotateY(-rot.getY());
//		rotateX(-rot.getX());
		if( overlayMovie.ready() ){
//			overlayMovie.read();
			image(overlayMovie, 0.0f,0.0f, 50.0f, 50.0f);
		}

		popStyle();
	}
	
	public void mousePressed() {
		lastMouseX = mouseX;
		lastMouseY = mouseY;
	}
	
	public void mouseDragged(){
		if( mouseButton == LEFT ){
			rot.setY( rot.getY() + (mouseX-lastMouseX)*0.005f );
			rot.setX( rot.getX() - (mouseY-lastMouseY)*0.005f );
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
		float e = event.getClickCount();
		trans.z = trans.z + e*100.0f;
	}
	
	public static void main(String _args[]) {
		PApplet.main(new String[] { kinectdisplay.KinectDisplay.class.getName() });
	}
}
