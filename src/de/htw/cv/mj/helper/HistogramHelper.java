package de.htw.cv.mj.helper;

public class HistogramHelper {

	/**
	 * Normalize with min and max value
	 * 
	 * @param hist
	 */
	public static void normalizeMinMax(double[] hist) {
		double maxVal = 0;
		double minValue = Double.MAX_VALUE;
		
		// Get Min/Max
		for (int i = 0; i < hist.length; i++) {
			if (hist[i] > maxVal) {
				maxVal = hist[i];
			}
			if (hist[i] < minValue) {
				minValue = hist[i];
			}
		}
		
		// Calc
		for (int i = 0; i < hist.length; i++) {
			hist[i] = (hist[i] - minValue) / (maxVal - minValue);
		}
	}
	
	/**
	 * Make sure the chosen bucket index is not to big
	 * 
	 * @param pos
	 * @return
	 */
	public static int handleBorderCase(int pos, int buckets) {
		return pos >= buckets ? buckets - 1 : pos;
	}
}
