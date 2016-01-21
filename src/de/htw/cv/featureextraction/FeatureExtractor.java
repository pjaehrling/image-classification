package de.htw.cv.featureextraction;

public interface FeatureExtractor {

	public double[] extract(int[] pixels, int width, int height);
}
