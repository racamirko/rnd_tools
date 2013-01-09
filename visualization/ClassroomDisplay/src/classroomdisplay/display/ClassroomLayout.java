package classroomdisplay.display;

import processing.core.PApplet;
import classroomdisplay.data.Point2f;

public class ClassroomLayout implements Layer {

	PApplet p;
	protected int seats, rows;
	protected int[] spacesAfter;
	protected float sizeX, sizeY, offsetTop, offsetLeft, corridorSize, seatingSize;
	
	float boundaryPx, numberOfSpacesNeededX, numberOfSpacesNeededY, tableWidthPx, tableHeightPx;
	
	public ClassroomLayout( PApplet p, int seats, int rows, int[] spacesAfter, float sizeX, float sizeY, float offsetTop, float offsetLeft ){
		this.p = p;
		this.seats = seats;
		this.rows = rows;
		this.spacesAfter = spacesAfter;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.offsetTop = offsetTop;
		this.offsetLeft = offsetLeft;
		
		// fixed values for now
		corridorSize = 0.5f; // relative to respect to table width
		seatingSize = 0.5f; // relative to respect to table height
		
		// calculated values
		boundaryPx = sizeX*0.01f;
		numberOfSpacesNeededX = (seats/2.0f)+corridorSize*((float)spacesAfter.length);
		numberOfSpacesNeededY = rows+(rows-1)*seatingSize;
		tableWidthPx = (sizeX-boundaryPx*2.0f)/numberOfSpacesNeededX;
		tableHeightPx = (sizeY-boundaryPx*2.0f)/numberOfSpacesNeededY;
	}
	
	public void draw(){
		// teacher, table
		p.fill(38, 199, 46);
		p.rect(offsetLeft+sizeX/2.0f-tableWidthPx, offsetTop-30.0f, tableWidthPx*2.0f, 10.0f);
		// start off top right
		p.fill(255.0f, 247.0f, 214.0f);
		float posX = 0,
			  posY = boundaryPx+offsetTop;
		for( int r = 0; r < rows; ++r ){
			posX = sizeX+offsetLeft-boundaryPx-tableWidthPx;
			for(int s = 0; s < seats/2; ++s){
				p.rect(posX, posY, tableWidthPx, tableHeightPx, 1.0f);
				posX -= tableWidthPx;
				// check for spaces
				for(  int j = 0; j < spacesAfter.length; ++j ){
					if( 2*(s+1) == spacesAfter[j] ){
						posX -= tableWidthPx*corridorSize;
						break;
					}
				}
			}
			posY += tableHeightPx*(1+seatingSize);
		}
	}
	
	public Point2f getCoordinateForSeat(float seat, float row){
		Point2f p = new Point2f(0.0f, 0.0f);
		p.x = offsetLeft + sizeX - boundaryPx - (seat-1)*(tableWidthPx/2.0f)-0.25f*tableWidthPx;
		for(  int j = 0; j < spacesAfter.length; ++j ){
			if( seat > spacesAfter[j] ){
				p.x -= tableWidthPx*corridorSize;
			}
		}
		p.y = offsetTop + tableHeightPx*(row-1) + tableHeightPx*(row-1)*seatingSize + 0.75f*tableHeightPx;
		return p;
	}
	
}
