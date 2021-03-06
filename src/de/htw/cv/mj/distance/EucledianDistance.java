package de.htw.cv.mj.distance;

/**
 * @author Marie Mandrela, Philipp Jährling
 */
public class EucledianDistance implements Distance {

	/**
	 * Calc the euclidean distance between two points.
	 * The dimensions doesn't matter, but both should have the same.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public double calculate(double[] a, double[] b) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			if (b.length <= i) break;
			
			sum += Math.pow( (a[i] - b[i]), 2);
		}
		return Math.sqrt(sum);
	}
}
