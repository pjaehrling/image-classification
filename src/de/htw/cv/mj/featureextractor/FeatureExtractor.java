package de.htw.cv.mj.featureextractor;

public interface FeatureExtractor {

	public double[] extract(int[] pixels, int x, int y, int width, int height, int image_width);
}
