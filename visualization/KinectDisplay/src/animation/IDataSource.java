package animation;

import java.util.Vector;

public interface IDataSource {

	/**
	 * Returns the next set of requested values. It always moves by 1 position,
	 * {@link IAnimator} should consider the tempo of requests of the values.
	 * 
	 * @param values
	 */
	public void getNextValue(Vector<Float> values);
	
	/**
	 * Returns the explanation of the vector of values it returns on each request.
	 * 
	 * @return
	 */
	public IAnimator.enumManipulationAttrib[] getFormat();

	/**
	 * To be used by an {@link IAnimator} instance.
	 * 
	 * @return float - speed of reproduction in seconds
	 */
	public float getTempo();

}
