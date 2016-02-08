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
		// double normUnit = 1.0 / pixels.length;

		int pos = 0;
		int histogramPos = 0;
		int redPos = 0;
		int greenPos = 0;
		int bluePos = 0;
		double maxVal = 0;
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pos = j * width + i;
				redPos = handleBorderCase(((pixels[pos] >> 16) & 255) / bucketSize);
				greenPos = handleBorderCase(((pixels[pos] >> 8) & 255) / bucketSize);
				bluePos = handleBorderCase(((pixels[pos]) & 255) / bucketSize);
				histogramPos = bluePos * buckets * buckets + greenPos * buckets + redPos;
				
				featureVector[histogramPos]++;
				//featureVector[histogramPos] += normUnit;
				
				if (featureVector[histogramPos] > maxVal) {
					maxVal = featureVector[histogramPos];
				}
			}
		}
		normalize(featureVector, maxVal);
			
		return featureVector;
	}

	private int handleBorderCase(int pos)
	{
		return pos >= buckets ? buckets - 1 : pos;
	}
	
	private void normalize(double[] hist, double max) {
		double scale = 1.0 / max;
		for (int i = 0; i < hist.length; i++) {
			hist[i] = hist[i] * scale;
		}
	}
	
}
