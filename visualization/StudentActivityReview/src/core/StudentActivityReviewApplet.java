package core;

import java.awt.Rectangle;

import data.Point2f;
import data.RectF;
import processing.core.PApplet;
import processing.video.Movie;
import visualizemotionmetrics.helpers.SecondFrame;
import widgets.ClockDisplay;

public class StudentActivityReviewApplet extends PApplet {
	private static final long serialVersionUID = 7710603571038989771L;
	protected Movie studentMovie, teacherMovie;
	protected int studentOffset, teacherOffset;
	protected RectF teacherArea, studentArea, studentDisplayArea;
	protected int wndWidth, wndHeight;
	
	// movie controls
	protected boolean isPlaying, initDone;
	protected float playbackSpeed;
	
	// other controls
	protected ClockDisplay teachClock, studentClock;
	
	protected SecondFrame frameSelect;
	protected AreaSelectApplet selectApplet;

	public void setup(){
		String strVideoStud = "/media/Local Disk/data/11_classroom_recording/05_131030_hpl_roland_02/01_orig_videos/cam1/P1010266.MP4";
		String strVideoTeach = "/media/Local Disk/data/11_classroom_recording/05_131030_hpl_roland_02/01_orig_videos/cam3/Video 4.mp4";
		studentOffset = 0; // to be read from config file
		teacherOffset = 0;
		
		wndWidth = 1400;
		wndHeight = 600;
		
		size(wndWidth, wndHeight);
		teacherArea = new RectF(0.0f, 0.0f, wndWidth/2.0f, wndHeight);
		studentArea = new RectF(wndWidth/2.0f, 0.0f, wndWidth/2.0f, wndHeight);
		// init streams
		studentMovie = new Movie(this, strVideoStud);
		studentMovie.volume(0.0f);
		studentMovie.jump(studentOffset);
		studentMovie.play();
		studentMovie.read();
		
		teacherMovie = new Movie(this, strVideoTeach);
		teacherMovie.volume(0.6f);
		teacherMovie.jump(teacherOffset);
		teacherMovie.play();
		teacherMovie.read();
		
		studentMovie.pause();
		teacherMovie.pause();
		
		isPlaying = false;
		playbackSpeed = 1.0f;
		initDone = false;
		teachClock = new ClockDisplay(this, new Point2f(20.0f, 20.0f));
		studentClock = new ClockDisplay(this, new Point2f( wndWidth/2.0f + 20.0f, 20.0f));
		teachClock.setOffset(teacherOffset);
		studentClock.setOffset(studentOffset);
		
		selectApplet = new AreaSelectApplet(studentMovie.get());
		frameSelect = new SecondFrame(selectApplet, "Select student region", new Rectangle(100,100,selectApplet.getImage().width, selectApplet.getImage().height));
		
		fill(0.0f);
		rect(0,0,wndWidth,wndHeight);
//		debugOutput();
//		recalibrateDispayAreas();
	}
	
	protected void debugOutput(){
		teacherMovie.read();
		studentMovie.read();
		System.out.println( String.format("Teacher stream %dx%d, length: %f", teacherMovie.width, teacherMovie.height, teacherMovie.duration()) );
		System.out.println( String.format("Student stream %dx%d, length: %f", studentMovie.width, studentMovie.height, studentMovie.duration()) );
	}
	
	protected void recalibrateDispayAreas(){
		float teacherRatio = teacherArea.getWidth()/((float)teacherMovie.width);
		// recalc offsets
		teacherArea.setY( (teacherArea.getHeight() - ((float)teacherMovie.height)*teacherRatio) / 2.0f );
		teacherArea.setHeight( ((float)teacherMovie.height)*teacherRatio );
		teachClock.setY( teacherArea.getY() + 20.0f );
		
		studentArea.setX( teacherArea.getWidth()+ teacherArea.getX() + ( studentArea.getWidth() - studentDisplayArea.getWidth() ) / 2.0f );
		studentArea.setY( ( studentArea.getHeight() - studentDisplayArea.getHeight() ) / 2.0f );
		studentArea.setHeight( studentDisplayArea.getHeight() );
		studentArea.setWidth( studentDisplayArea.getWidth() );
		
		studentClock.setX(studentArea.getX()+10.0f);
		studentClock.setY(studentArea.getY()+20.0f);
	}
	
	public void draw() {
		if( !selectApplet.getSelectionFinal() ){
			return;
		} else {
			if( !initDone ){
				studentDisplayArea = selectApplet.getSelectionArea();
				frameSelect.dispose();
				studentMovie.play();
				teacherMovie.play();
				teacherArea = new RectF(0.0f, 0.0f, wndWidth/2.0f, wndHeight);
				studentArea = new RectF(wndWidth/2.0f, 0.0f, wndWidth/2.0f, wndHeight);
				recalibrateDispayAreas();
				isPlaying = true;
				initDone = true;
			}
		}
		
		if( teacherMovie.available() ){
			teacherMovie.read();
			image(teacherMovie, teacherArea.getX(), teacherArea.getY(), teacherArea.getWidth(), teacherArea.getHeight());
			teachClock.setTime(teacherMovie.time());
			studentClock.setTime(studentMovie.time());
		}
		
		if( studentMovie.available() ){
			studentMovie.read();
			image( studentMovie.get((int)studentDisplayArea.getX(), (int)studentDisplayArea.getY(),(int) studentDisplayArea.getWidth(), (int)studentDisplayArea.getHeight()),
									studentArea.getX(), studentArea.getY(), studentArea.getWidth(), studentArea.getHeight() );
		}
		
		teachClock.draw();
		studentClock.draw();
	}
	
	public void keyPressed() {
		if( key == ' ' ){
			if( isPlaying ){
				studentMovie.pause();
				teacherMovie.pause();
				isPlaying = false;
			}
			else {
				studentMovie.play();
				teacherMovie.play();
				isPlaying = true;
			}
		}
		if( key == '1' ){
			playbackSpeed = playbackSpeed/2.0f;
			studentMovie.speed(playbackSpeed);
			teacherMovie.speed(playbackSpeed);
		}
		if( key == '2' ){
			playbackSpeed = playbackSpeed*2.0f;
			studentMovie.speed(playbackSpeed);
			teacherMovie.speed(playbackSpeed);
		}
		
		if( key == '3'){
			teacherMovie.jump(teacherMovie.time() - 60.0f);
			studentMovie.jump(studentMovie.time() - 60.0f);
		}
		if (key == '4'){
			teacherMovie.jump(teacherMovie.time() + 60.0f);
			studentMovie.jump(studentMovie.time() + 60.0f);
		}
		
	}
	
}
