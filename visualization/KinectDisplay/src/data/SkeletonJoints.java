package data;

import java.util.Map;

import processing.core.PVector;

public class SkeletonJoints {
	
	public enum enumSkelPart { SKEL_PELVIS (0), SKEL_BACKBONE (1), SKEL_SHOULDERS_CENTER(2),
		  SKEL_HEAD(3), SKEL_SHOULDER_L(4), SKEL_ELBOW_L(5), SKEL_HAND_L(6), SKEL_FINGERS_L(7), 
		  SKEL_SHOULDER_R(8), SKEL_ELBOW_R(9), SKEL_HAND_R(10), SKEL_FINGERS_R(11),
		  SKEL_PELVIS_L(12), SKEL_KNEE_L(13), SKEL_ANKLE_L(14), SKEL_TOES_L(15),
		  SKEL_PELVIS_R(16), SKEL_KNEE_R(17), SKEL_ANKLE_R(18), SKEL_TOES_R(20);
	
			private final int index;
			
			enumSkelPart(int index){
				this.index = index;
			}
			
			int getIndex(){
				return index;
			}
	};
	
	Map<enumSkelPart, PVector>	

}
