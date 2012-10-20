package classroomdisplay;

import java.util.Vector;

import processing.core.PApplet;

class DisplayUnit{
	private static final long serialVersionUID = -6871487376732783465L;

	protected float endX, endY, stepX, stepY;
	public float curX, curY;

	protected float endSizeX, endSizeY, stepSizeX, stepSizeY;
	public float sizeX, sizeY;

	protected int endColor, stepColor;
	public int curColor;
	  
	PApplet applet;	  
	public Vector<Attribute> attributes;

	DisplayUnit( PApplet applet, float startX, float startY ){
		this.applet = applet;
		curX = startX; curY = startY;
		endX = curX; endY = curY;

		sizeX = 20; sizeY = 20;
		endSizeX = sizeX; endSizeY = sizeY;

		curColor = applet.color(68,22,170);
		endColor = curColor;
	}

	void setDestinationLocation(float x1, float y1, int numOfSteps){
		endX = x1;
		endY = y1;
		stepX = (endX - curX) / numOfSteps;
		stepY = (endY - curY) / numOfSteps;
	}

	void setDestinationSize(float sizeX1, float sizeY1, int numOfSteps){
		endSizeX = sizeX1;
		endSizeY = sizeY1;
		stepSizeX = (endSizeX - sizeX) / numOfSteps;
		stepSizeY = (endSizeY - sizeY) / numOfSteps;
	}

	void setDestinationColor( int endColor1, int numOfSteps ){
		endColor = endColor1;
		stepColor = applet.color( (applet.red(endColor) - applet.red(curColor))/numOfSteps,
				(applet.green(endColor) - applet.green(curColor))/numOfSteps,
				(applet.blue(endColor) - applet.blue(curColor))/numOfSteps );
	}

	public void draw(){
		updateDeltas();

		applet.fill(curColor);
		applet.rect(curX,curY, sizeX, sizeY);
	}

	protected void updateDeltas(){
		// position
		if( curX != endX ){
			curX += stepX;
			if( Math.abs(curX-endX) < 2 ){
				stepX = 0;
				curX = endX;
			} 
		}

		if( curY != endY ){
			curY += stepY;
			if( Math.abs(curY-endY) < 2 ){
				stepY = 0;
				curY = endY;
			} 
		}

		// size
		if( sizeX != endSizeX ){
			sizeX += stepSizeX;
			if( Math.abs( sizeX - endSizeX ) < 2 ){
				sizeX = endSizeX;
				stepSizeX = 0;
			}
		}

		if( sizeY != endSizeY ){
			sizeY += stepSizeY;
			if( Math.abs( sizeY - endSizeY ) < 2 ){
				sizeY = endSizeY;
				stepSizeY = 0;
			}
		}

		// color
		if( curColor != endColor ){
			float tmpRed = applet.red(curColor) + applet.red(stepColor),
					tmpGreen = applet.green(curColor) + applet.green(stepColor),
					tmpBlue = applet.blue(curColor) + applet.blue(stepColor);
			curColor = applet.color( tmpRed, tmpGreen, tmpBlue );
			float endRed = applet.red(endColor),
					endGreen = applet.green(endColor),
					endBlue = applet.blue(endColor);
			float totalDiff = Math.abs( tmpRed - endRed ) + Math.abs( tmpGreen - endGreen ) + Math.abs(tmpBlue - endBlue);
			if( totalDiff < 6 ){
				curColor = endColor;
				stepColor = applet.color(0,0,0);
			}
		}
	}

};