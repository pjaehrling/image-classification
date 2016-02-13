package de.htw.cv.mj.featureextractor;

import de.htw.cv.mj.helper.HistogramHelper;

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
				redPos = HistogramHelper.handleBorderCase(((pixels[pos] >> 16) & 255) / bucketSize, buckets);
				greenPos = HistogramHelper.handleBorderCase(((pixels[pos] >> 8) & 255) / bucketSize, buckets);
				bluePos = HistogramHelper.handleBorderCase(((pixels[pos]) & 255) / bucketSize, buckets);
				histogramPos = bluePos * buckets * buckets + greenPos * buckets + redPos;
				
				if (this.useMinMaxNormalisation) {
					featureVector[histogramPos]++; // just add pixel to bin
				} else {
					featureVector[histogramPos] += normUnit; // normalize by pixel count
				}
			}
		}
		
		// normalize just using max -> got best results
		if (this.useMinMaxNormalisation) HistogramHelper.normalizeMinMax(featureVector);
			
		return featureVector;
	}
	
}
