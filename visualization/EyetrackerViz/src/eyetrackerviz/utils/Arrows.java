package eyetrackerviz.utils;

import processing.core.PApplet;

/**
 * Taken from http://www.openprocessing.org/sketch/7029
 * @author J David Eisenberg
 *
 */
public class Arrows {

	/*
	 * Draws a lines with arrows of the given angles at the ends.
	 * x0 - starting x-coordinate of line
	 * y0 - starting y-coordinate of line
	 * x1 - ending x-coordinate of line
	 * y1 - ending y-coordinate of line
	 * startAngle - angle of arrow at start of line (in radians)
	 * endAngle - angle of arrow at end of line (in radians)
	 * solid - true for a solid arrow; false for an "open" arrow
	 */
	public static void arrowLine(PApplet p, float x0, float y0, float x1, float y1,
	  float startAngle, float endAngle, boolean solid)
	{
	  p.line(x0, y0, x1, y1);
	  if (startAngle != 0)
	  {
	    arrowhead(p, x0, y0, (float) Math.atan2(y1 - y0, x1 - x0), startAngle, solid);
	  }
	  if (endAngle != 0)
	  {
	    arrowhead(p, x1, y1, (float) Math.atan2(y0 - y1, x0 - x1), endAngle, solid);
	  }
	}

	/*
	 * Draws an arrow head at given location
	 * x0 - arrow vertex x-coordinate
	 * y0 - arrow vertex y-coordinate
	 * lineAngle - angle of line leading to vertex (radians)
	 * arrowAngle - angle between arrow and line (radians)
	 * solid - true for a solid arrow, false for an "open" arrow
	 */
	private static void arrowhead(PApplet p, float x0, float y0, float lineAngle,
	  float arrowAngle, boolean solid)
	{
	  float phi;
	  float x2;
	  float y2;
	  float x3;
	  float y3;
	  final float SIZE = 8;
	   
	  x2 = (float) (x0 + SIZE * Math.cos(lineAngle + arrowAngle));
	  y2 = (float) (y0 + SIZE * Math.sin(lineAngle + arrowAngle));
	  x3 = (float) (x0 + SIZE * Math.cos(lineAngle - arrowAngle));
	  y3 = (float) (y0 + SIZE * Math.sin(lineAngle - arrowAngle));
	  if (solid)
	  {
	    p.triangle(x0, y0, x2, y2, x3, y3);
	  }
	  else
	  {
	    p.line(x0, y0, x2, y2);
	    p.line(x0, y0, x3, y3);
	  }
	}

}
