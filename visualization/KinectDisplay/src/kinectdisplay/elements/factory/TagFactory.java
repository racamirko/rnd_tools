package kinectdisplay.elements.factory;

import com.jogamp.opengl.math.Quaternion;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import kinectdisplay.GraphicalNode;
import kinectdisplay.elements.Tag;

public class TagFactory {
	protected String tagFolder;
	protected String tagNameFormat;
	protected float tagSize;
	protected PApplet pa;

	public TagFactory( PApplet pa, String tagFolder, String tagNameFormat, float tagSize ){
		this.tagFolder = tagFolder;
		this.tagNameFormat = tagNameFormat;
		this.tagSize = tagSize;
		this.pa = pa;
	}
	
	public GraphicalNode createTag(int tagId, PVector location, Quaternion rot ){
		String texFilename = String.format(tagNameFormat, tagId);
		String fullpath = String.format("%s/%s", tagFolder, texFilename);
		PImage newTex = pa.loadImage(fullpath);
		GraphicalNode newNode = new GraphicalNode(pa).addDrawable(new Tag(pa, tagSize, newTex));
		return newNode.setLocation(location).setRotation(rot);
	}
}
