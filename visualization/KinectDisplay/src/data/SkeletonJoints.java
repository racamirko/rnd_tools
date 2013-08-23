package data;

import java.util.HashMap;
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
	
	public enum enumPartStatus {
		SKELPART_STAT_NOT_TRACKED(0), SKELPART_STAT_INFERRED(1), SKELPART_STAT_TRACKED(2);
		
		private final int index;
		
		enumPartStatus(int index){
			this.index = index;
		}
		
		int getIndex(){
			return index;
		}
	}
	
	public Map<enumSkelPart, PVector> bodyParts;
	public Map<enumSkelPart, enumPartStatus> bodyPartStatus;
	static public Map<enumSkelPart, enumSkelPart> mapParentPart = new HashMap<SkeletonJoints.enumSkelPart, SkeletonJoints.enumSkelPart>();

	public static void initSkelPartsParents(){
		mapParentPart.put(enumSkelPart.SKEL_PELVIS, null);
		mapParentPart.put(enumSkelPart.SKEL_BACKBONE, enumSkelPart.SKEL_PELVIS);
		mapParentPart.put(enumSkelPart.SKEL_SHOULDERS_CENTER, enumSkelPart.SKEL_BACKBONE);
		mapParentPart.put(enumSkelPart.SKEL_SHOULDER_L, enumSkelPart.SKEL_SHOULDERS_CENTER);
		mapParentPart.put(enumSkelPart.SKEL_ELBOW_L, enumSkelPart.SKEL_SHOULDER_L);
		mapParentPart.put(enumSkelPart.SKEL_HAND_L, enumSkelPart.SKEL_ELBOW_L);
		mapParentPart.put(enumSkelPart.SKEL_FINGERS_L, enumSkelPart.SKEL_HAND_L);
		mapParentPart.put(enumSkelPart.SKEL_SHOULDER_R, enumSkelPart.SKEL_SHOULDERS_CENTER);
		mapParentPart.put(enumSkelPart.SKEL_ELBOW_R, enumSkelPart.SKEL_SHOULDER_R);
		mapParentPart.put(enumSkelPart.SKEL_HAND_R, enumSkelPart.SKEL_ELBOW_R);
		mapParentPart.put(enumSkelPart.SKEL_FINGERS_R, enumSkelPart.SKEL_HAND_R);
		mapParentPart.put(enumSkelPart.SKEL_PELVIS_L, enumSkelPart.SKEL_PELVIS);
		mapParentPart.put(enumSkelPart.SKEL_KNEE_L, enumSkelPart.SKEL_PELVIS_L);
		mapParentPart.put(enumSkelPart.SKEL_ANKLE_L, enumSkelPart.SKEL_KNEE_L);
		mapParentPart.put(enumSkelPart.SKEL_TOES_L, enumSkelPart.SKEL_ANKLE_L);
		mapParentPart.put(enumSkelPart.SKEL_PELVIS_R, enumSkelPart.SKEL_PELVIS);
		mapParentPart.put(enumSkelPart.SKEL_KNEE_R, enumSkelPart.SKEL_PELVIS_R);
		mapParentPart.put(enumSkelPart.SKEL_ANKLE_R, enumSkelPart.SKEL_KNEE_R);
		mapParentPart.put(enumSkelPart.SKEL_TOES_R, enumSkelPart.SKEL_ANKLE_R);
	}
	
	
}
