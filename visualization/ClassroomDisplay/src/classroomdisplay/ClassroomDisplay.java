package classroomdisplay;

import java.util.Vector;

import processing.core.*;


public class ClassroomDisplay extends PApplet {
	private static final long serialVersionUID = 8767955545276677628L;
	Vector<DisplayUnit> thingsToDraw;
	int sizeX = 1040;
	int sizeY = 660;

	public void setup() {
	  size(sizeX, sizeY);
	  thingsToDraw = new Vector<DisplayUnit>();
	  for( int i = 0; i < 100; i++){
	    thingsToDraw.add(new DisplayUnit(this, random(sizeX), random(sizeY)));
	  }
	}

	public void draw() {
	  fill(color(0,0,0));
	  rect(0,0, sizeX, sizeY);
	  fill(color(124, 44, 240));
	  rect(100,100,30,50);
	  for( int i = 0; i < thingsToDraw.size(); i++){
	    DisplayUnit d = (DisplayUnit) thingsToDraw.get(i);
	    d.draw();
	  }
	}
	
	public void mousePressed() {
	  for( int i = 0; i < thingsToDraw.size(); i++){
	    DisplayUnit d = (DisplayUnit) thingsToDraw.get(i);
	    switch( (int)random(4) ){
	      case 0:
	        d.setDestinationLocation( random(sizeX), random(sizeY), 100 );
	        break;
	      case 1:
	        d.setDestinationSize( 10+random(50), 10+random(50), 100 );
	        break;
	      case 2:
	        d.setDestinationLocation( random(sizeX), random(sizeY), 100 );
	        d.setDestinationSize( 10+random(50), 10+random(50), 100 );
	        break;
	      case 3:
	        d.setDestinationColor( color( 10 + random(240), 10 + random(240), 10+random(240) ), 100  );
	        break;
	    }
	  }
	}
	
	public static void main(String _args[]) {
//		PApplet.main(new String[] { classroomdisplay.ClassroomDisplay.class.getName() });
		DisplayUnit a = new DisplayUnit(null, 2,3);
	}
}
