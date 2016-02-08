package de.htw.cv.mj.featureextractor;

public class ColorHistogram implements FeatureExtractor  {

	private int buckets;
	
	public ColorHistogram(int buckets) {
		this.buckets = buckets;
	}

	public void setBuckets(int buckets) {
		this.buckets = buckets;
	}

	@Override
	public double[] extract(int[] pixels, int width, int height) {
		
		int bucketSize = 256 / buckets;

		double[] featureVector = new double[buckets * buckets * buckets];
		
		// NORMALIZE -> pixel count
		// double normUnit = 1.0 / pixels.length;

		int pos = 0;
		int histogramPos = 0;
		int redPos = 0;
		int greenPos = 0;
		int bluePos = 0;
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pos = j * width + i;
				redPos = handleBorderCase(((pixels[pos] >> 16) & 255) / bucketSize);
				greenPos = handleBorderCase(((pixels[pos] >> 8) & 255) / bucketSize);
				bluePos = handleBorderCase(((pixels[pos]) & 255) / bucketSize);
				histogramPos = bluePos * buckets * buckets + greenPos * buckets + redPos;
				
				// just add pixel to bin
				featureVector[histogramPos]++;
				
				// NORMALIZE -> pixel count
				// featureVector[histogramPos] += normUnit;
			}
		}
		
		// NORMALIZE -> max
		normalizeMax(featureVector);
		
		// NORMALIZE -> min/max
		// normalizeMinMax(featureVector);
			
		return featureVector;
	}

	private int handleBorderCase(int pos)
	{
		return pos >= buckets ? buckets - 1 : pos;
	}
	
	/**
	 * Normalize with bin-max value
	 * @param hist
	 */
	private void normalizeMax(double[] hist) {
		double maxVal = 0;
		
		// Get Max
		for (int i = 0; i < hist.length; i++) {
			if (hist[i] > maxVal) {
				maxVal = hist[i];
			}
		}
		
		// Calc
		for (int i = 0; i < hist.length; i++) {
			hist[i] = hist[i] / maxVal;
		}
	}
	
	/**
	 * Normalize with min and max value
	 * @param hist
	 */
	private void normalizeMinMax(double[] hist) {
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
	
}
