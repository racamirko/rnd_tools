package data;

import java.util.Vector;

public interface HeadPositionProviderGeneric {
	
	static public class FaceBoundingBox {
		public Rect2d bbox;
		public float angle;
		public float score;
		public int frameNo;
		
		public FaceBoundingBox( Rect2d bbox, float angle, float score, int frameNo ){
			this.bbox = bbox;
			this.angle = angle;
			this.score = score;
			this.frameNo = frameNo;
		}
	}

	public void getFaces(int frameNo, Vector<FaceBoundingBox> output);
}
