package de.htw.cv.mj.featureextractor;

public interface FeatureExtractor {

	public double[] extract(int[] pixels, int width, int height);
}
