package de.htw.cv.mj.distance;

public class ManhattanDistance implements Distance {

	/**
	 * Calc the Manhattan distance between two points.
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
			
			sum += Math.abs(a[i] - b[i]);
		}
		return sum;
	}

}
