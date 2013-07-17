package data;

import com.jogamp.graph.math.Quaternion;

public class QuaternionMathematics {

	public static Quaternion normByW(Quaternion q){
		Quaternion qOut = new Quaternion(q.getX(),q.getY(),q.getZ(), q.getW());
		qOut.divide(q.getW());
		qOut.setW(1.0f);
		return qOut;
	}
	
}
