package core;

import data.RectF;
import processing.core.PApplet;
import processing.core.PImage;

public class AreaSelectApplet extends PApplet {

	private static final long serialVersionUID = -1582943497037674284L;
	protected PImage image;
	protected RectF selectedArea;
	protected boolean selectionFinal;
	

	public AreaSelectApplet( PImage image ){
		this.image = image;
		selectedArea = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
		selectionFinal = false;
	}
	
	public void setup(){
		size(image.width, image.height);
	}
	
	public void draw(){
		image(image, 0.0f, 0.0f);
		if( selectedArea.getWidth() > 0.0f && selectedArea.getHeight() > 0.0f ){
			pushStyle();
				fill(82, 175, 234, 128);
				stroke(28, 125, 186, 255);
				rect( selectedArea.getX(), selectedArea.getY(), selectedArea.getWidth(), selectedArea.getHeight() );			
			popStyle();
		}
	}
	
	public void mousePressed(){
//		System.out.println( selectedArea.toString() );
		selectedArea.setX(mouseX);
		selectedArea.setY(mouseY);
		selectedArea.setWidth(0.0f);
		selectedArea.setHeight(0.0f);
	}
	
	public void mouseDragged(){
//		System.out.println( "Drag: "+ selectedArea.toString() );
		float w = mouseX - selectedArea.getX(),
			  h = mouseY - selectedArea.getY();
		if( w < 0 )
			selectedArea.setX(mouseX);
		selectedArea.setWidth(Math.abs(w));

		if( h < 0 )
			selectedArea.setY(mouseY);
		selectedArea.setHeight(Math.abs(h));
	}
	
	public void keyPressed(){
		System.out.println("ahah");
		selectionFinal = true;
	}
	
	public boolean getSelectionFinal(){
		return selectionFinal;
	}
	
	public RectF getSelectionArea(){
		return selectedArea;
	}
	
	public PImage getImage(){
		return image;
	}
	
}
