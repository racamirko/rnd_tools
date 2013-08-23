package animation;

/**
 * Common interface for any animation object.
 * 
 * Animation object connects the data to the object which reflects the change in data.
 * 
 * @author Mirko Raca <mirko.raca@epfl.ch>
 *
 */
public interface IAnimator {

	/**
	 * Specifies which attribute is changing.
	 *
	 */
	public enum enumManipulationAttrib {
		// normal values show the increment of change
		ANI_TRANSX, ANI_TRANSY, ANI_TRANSZ, ANI_ROTX, ANI_ROTY, ANI_ROTZ,
		// _ABS values set the value absolutely
		ANI_TRANSX_ABS, ANI_TRANSY_ABS, ANI_TRANSZ_ABS, ANI_ROTX_ABS, ANI_ROTY_ABS, ANI_ROTZ_ABS
	};
	
	/**
	 * One render cycle.
	 */
	public void tick(float passedTime);
}
