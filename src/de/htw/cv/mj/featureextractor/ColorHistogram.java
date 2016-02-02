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
	public double[] extract(int[] pixels, int x, int y, int width, int height, int image_width) {
		
		int bucketSize = 256 / buckets;

		double[] featureVector = new double[buckets * buckets * buckets];

		int pos = 0;
		int histogramPos = 0;
		int redPos = 0;
		int greenPos = 0;
		int bluePos = 0;
		
		for (int i = x; i < width; i++) {
			for (int j = y; j < height; j++) {
				pos = j * image_width + i;
				redPos = handleBorderCase(((pixels[pos] >> 16) & 255) / bucketSize);
				greenPos = handleBorderCase(((pixels[pos] >> 8) & 255) / bucketSize);
				bluePos = handleBorderCase(((pixels[pos]) & 255) / bucketSize);
				histogramPos = bluePos * buckets * buckets + greenPos * buckets + redPos;
				
				featureVector[histogramPos]++;
			}
		}
			
		return featureVector;
	}

	private int handleBorderCase(int pos)
	{
		return pos >= buckets ? buckets - 1 : pos;
	}
	
}
