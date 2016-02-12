package de.htw.cv.mj.featureextractor;

/**
 * @author Marie Mandrela, Philipp Jährling
 */
public class ColorHistogram implements FeatureExtractor  {

	private int buckets;
	private boolean useMinMaxNormalisation;
	
	/**
	 * Constructor. Sets the number of buckets if it's between 2 and 256. Otherwise it's 2.
	 * 
	 * @param buckets
	 */
	public ColorHistogram(int buckets, boolean useMinMaxNormalisation) {
		if (buckets >= 2 && buckets <= 256) {
			this.buckets = buckets;
		} else {
			buckets = 2;
		}
		this.useMinMaxNormalisation = useMinMaxNormalisation;
		System.out.println("ColorHistogram: " + buckets + " / " + useMinMaxNormalisation);
	}

	/**
	 * Sets the number of buckets if it's between 2 and 256.
	 * 
	 * @param buckets
	 */
	public void setBuckets(int buckets) {
		if (buckets >= 2 && buckets <= 256) {
			this.buckets = buckets;
		}
	}

	@Override
	public double[] extract(int[] pixels, int width, int height) {
		
		int bucketSize = 256 / buckets;

		double[] featureVector = new double[buckets * buckets * buckets];
		
		// normalize by pixel count
		double normUnit = 1.0 / pixels.length;

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
				
				if (this.useMinMaxNormalisation) {
					featureVector[histogramPos]++; // just add pixel to bin
				} else {
					featureVector[histogramPos] += normUnit; // normalize by pixel count
				}
			}
		}
		
		// normalize just using max -> got best results
		if (this.useMinMaxNormalisation) normalizeMinMax(featureVector);
			
		return featureVector;
	}

	/**
	 * Make sure the chosen bucket index is not to big
	 * 
	 * @param pos
	 * @return
	 */
	private int handleBorderCase(int pos) {
		return pos >= buckets ? buckets - 1 : pos;
	}
	
	/**
	 * Normalize with min and max value
	 * 
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
