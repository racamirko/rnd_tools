package eyetrackerviz;

import java.util.Vector;

import data.CoordReader;
import data.HeadPositionProvider;
import data.HeadPositionProviderTar;
import data.Point2d;
import processing.core.PApplet;
import processing.core.PImage;
import processing.video.Movie;


public class EyetrackerViz extends PApplet {
	
	protected Movie eyeVideo;
	protected CoordReader dataReader;
	protected int lastTime;
	protected HeadPositionProviderTar headSrc;
	protected FaceDrawer fdraw;
	protected boolean moviePlaying;
	protected PImage screenShotPause;
	protected Point2d gazePoint;

	public void setup() {
//		String fnTrackVideo = "/media/Local Disk/data/09_facedetect/04_eyetracker_exp2/mirko3.avi";
//		String fnTrackTextFile = "/media/Local Disk/data/09_facedetect/04_eyetracker_exp2/mirko3_binocular_raw.txt";
//		String ptrnHeadLocFiles = "/media/Local Disk/data/09_facedetect/04_eyetracker_exp2/result/faceLocData%d.txt";
		
		String fnTrackVideo = "/media/Local Disk/data/11_classroom_recording/05_131030_hpl_roland_02/02_videos/cam1/cropped_P1010266.MP4";
//		String ptrnHeadLocFiles = "/media/Local Disk/data/11_classroom_recording/01_131002_hpl_roland_01/faceDetect/facedetect_frame%06d.txt";
		String ptrnHeadLocTar = "/media/Local Disk/data/11_classroom_recording/05_131030_hpl_roland_02/05_head_orient/cam1_converted.tar";
//		String ptrnHeadLocTar = "/media/Local Disk/data/11_classroom_recording/05_131030_hpl_roland_02/02_videos/cam4/detections/facedetect_frame%06d.txt";
//		String fnTrackTextFile = "/media/Local Disk/data/11_classroom_recording/01_131002_hpl_roland_01/roland_eyetracker_data_pure_transf2.txt";
		String fnTrackTextFile = "";
		
		if( fnTrackTextFile.length() == 0 )
			dataReader = null;
		else
			dataReader = new CoordReader(fnTrackTextFile, 3, 4, ",");
		
		if( ptrnHeadLocTar.length() == 0 ){
			fdraw = null;
			headSrc = null;
		} else {
			headSrc = new HeadPositionProviderTar(ptrnHeadLocTar);
			fdraw = new FaceDrawer(this);
		}
		
		eyeVideo = new Movie(this, fnTrackVideo);
		eyeVideo.play();
		eyeVideo.loop();
		eyeVideo.read();
		
		size(eyeVideo.width, eyeVideo.height);
		lastTime = millis();
		moviePlaying = true;
		gazePoint = null;
	}

	public void draw() {
		float time = eyeVideo.time();
		float frameRate = eyeVideo.frameRate;
		int frameNo = (int)(time*frameRate);
		if(eyeVideo.available()){
			eyeVideo.read();
			image(eyeVideo, 0, 0, eyeVideo.width, eyeVideo.height);
		}
		
		if( !moviePlaying ){
			fill(0.0f,0.0f,0.0f);
			rect(20, 10, 300, 80);
		}
		
		fill(255.0f, 0.0f, 0.0f);
		noStroke();
		
		// draw eye-tracking data
		int curTime = millis();
		if( dataReader != null ){
			if( moviePlaying )
				gazePoint = dataReader.getNextValue(curTime - lastTime);
			if( gazePoint != null )
				ellipse(gazePoint.getX(), gazePoint.getY(), 10.0f, 10.0f);
		}
		// draw face-detection data
		if( headSrc != null && fdraw != null ){
			Vector<HeadPositionProvider.FaceBoundingBox> faceData = new Vector<HeadPositionProvider.FaceBoundingBox>();
			headSrc.getFaces(frameNo, faceData);
			fdraw.draw(faceData);
		}
		
		text( String.format("Approx Frame #%d", frameNo) , 20, 20);
		text( String.format("Mouse [x=%d, y=%d]", mouseX, mouseY ), 20, 40 );
		if( gazePoint != null )
			text( String.format("Gaze  [x=%f, y=%f]", gazePoint.getX(), gazePoint.getY() ), 20, 60 );
		else
			text( "Gaze  [x=?, y=?]", 20, 60 );
		
		//if(moviePlaying)
		lastTime = curTime;
	}
	
	public void keyPressed() {
		if( key == 'n' ){
			if( moviePlaying ){
				eyeVideo.pause();
				if( eyeVideo.available() )
					screenShotPause = eyeVideo;
			}else
				eyeVideo.play();
			moviePlaying = !moviePlaying;
		}
	}
}
