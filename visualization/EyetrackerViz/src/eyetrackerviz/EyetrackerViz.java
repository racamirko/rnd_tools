package eyetrackerviz;

import data.CoordReader;
import data.Point2d;
import processing.core.PApplet;
import processing.video.Movie;


public class EyetrackerViz extends PApplet {
	
	protected Movie eyeVideo;
	protected CoordReader dataReader;
	protected int lastTime;

	public void setup() {
		String fnTrackVideo = "/home/raca/data/eyetracking/01_test_exp/ex01_mirko.avi";
		String fnTrackTextFile = "/home/raca/data/eyetracking/01_test_exp/Raw Data Export_mirko_mirko-1-recording (1).txt";
		
		dataReader = new CoordReader(fnTrackTextFile, 3, 4, 5, 6, ",", 33.33f);
		
		eyeVideo = new Movie(this, fnTrackVideo);
		eyeVideo.play();
		eyeVideo.loop();
		eyeVideo.read();
		
		size(eyeVideo.width, eyeVideo.height);
		lastTime = millis();
	}

	public void draw() {
		if(eyeVideo.available()){
			eyeVideo.read();
			image(eyeVideo, 0, 0, eyeVideo.width, eyeVideo.height);
		}
		
		fill(255.0f, 0.0f, 0.0f);
		noStroke();
		
		int curTime = millis();
		Point2d tmpPoint = dataReader.getNextValue(curTime - lastTime);
		if( tmpPoint != null )
			ellipse(tmpPoint.getX(), tmpPoint.getY(), 10.0f, 10.0f);
		lastTime = curTime;
	}
}
