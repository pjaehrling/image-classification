package de.htw.cv.mj.distance;

/**
 * @author Marie Mandrela, Philipp Jährling
 */
public interface Distance {

	/**
	 * Calculates the distance between two points.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double calculate(double[] a, double[] b);
	
}
