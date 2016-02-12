package de.htw.cv.mj.featureextractor;

/**
 * Feature extractor interface.
 * 
 * @author Marie Mandrela, Philipp Jährling
 */
public interface FeatureExtractor {

	/**
	 * Extract a feature vector from the given image.
	 * 
	 * @param pixels
	 * @param width
	 * @param height
	 * @return
	 */
	public double[] extract(int[] pixels, int width, int height);
}
