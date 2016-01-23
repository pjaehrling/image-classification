package de.htw.cv.featureextraction;

public class MeanColor implements FeatureExtractor {

	@Override
	public double[] extract(int[] pixels, int x, int y, int width, int height, int imageWidth) {
		
		int r_sum = 0;
		int g_sum = 0;
		int b_sum = 0;
		int sum = 0;
		
		for (int i = x; i < width; i++) {
			for (int j = y; i < height; j++) {
				int c = pixels[j * imageWidth + i];
				r_sum += (c>>16)&0xFF;
				g_sum += (c>> 8)&0xFF;
				b_sum += (c    )&0xFF;
				sum++;
			}
		}
		
		double[] features = new double[3];
		features[0] = r_sum / sum;
		features[1] = g_sum / sum;
		features[2] = b_sum / sum;
		
		return features;
	}
}
