package animation;

import java.util.Vector;

import com.jogamp.graph.math.Quaternion;

import processing.core.PVector;

import kinectdisplay.GraphicalNode;

public class Animator implements IAnimator {
	
	protected GraphicalNode node;
	protected IDataSource dataSrc;
	protected enumManipulationAttrib attrib[];
	protected float tempo, passedTime;
	
	public Animator(GraphicalNode node, IDataSource dataSrc){
		this.node = node;
		this.dataSrc = dataSrc;
		this.attrib = dataSrc.getFormat();
		this.tempo = dataSrc.getTempo();
	}

	@Override
	public void tick(float passedTime) {
		this.passedTime = this.passedTime + passedTime;
		if( this.passedTime < tempo ){
			return;
		}
		Vector<Float> values = new Vector<Float>();
		dataSrc.getNextValue(values);
		// get initial values (in case of relative changes)
		PVector curTrans = node.getLocation();
		Quaternion curRot = node.getRotation();
		for( int i = 0; i < attrib.length; ++i ){
			enumManipulationAttrib curAttrib = attrib[i];
			float curVal = values.get(i);
			switch( curAttrib ){
				case ANI_TRANSX:
					curTrans.x = curTrans.x + curVal;
					node.setLocation(curTrans);
					break;
				case ANI_TRANSY:
					curTrans.y = curTrans.y + curVal;
					node.setLocation(curTrans);
					break;
				case ANI_TRANSZ:
					curTrans.z = curTrans.z + curVal;
					node.setLocation(curTrans);
					break;
				case ANI_ROTX:
					node.rotateX(curRot.getX() + curVal );
					break;
				case ANI_ROTY:
					node.rotateY(curRot.getY() + curVal );
					break;
				case ANI_ROTZ:
					node.rotateZ(curRot.getZ() + curVal );
					break;
					// _ABS values set the value absolutely
				case ANI_TRANSX_ABS:
					curTrans.x = curVal;
					node.setLocation(curTrans);
					break;
				case ANI_TRANSY_ABS:
					curTrans.y = curVal;
					node.setLocation(curTrans);
					break;
				case ANI_TRANSZ_ABS:
					curTrans.z = curVal;
					node.setLocation(curTrans);
					break;
				case ANI_ROTX_ABS:
					curRot.setX(curVal);
					node.setRotation(curRot);
					break;
				case ANI_ROTY_ABS:
					curRot.setY(curVal);
					node.setRotation(curRot);
					break;
				case ANI_ROTZ_ABS:
					curRot.setZ(curVal);
					node.setRotation(curRot);
					break;
			}
		}
		passedTime = 0;
	}

}
