package de.htw.cv.mj.featureextractor;

public class MeanColor implements FeatureExtractor {

	@Override
	public double[] extract(int[] pixels, int width, int height) {
		
		int r_sum = 0;
		int g_sum = 0;
		int b_sum = 0;
		int sum = 0;
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int c = pixels[j * width + i];
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
