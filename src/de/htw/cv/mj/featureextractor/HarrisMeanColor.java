package de.htw.cv.mj.featureextractor;

import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import de.htw.cv.mj.helper.HarrisCornerDetector;

public class HarrisMeanColor implements FeatureExtractor {
	private int windowSize;
	private ImageView imageView;
	
	public HarrisMeanColor (int windowSize, ImageView imageView) {
		this.setWindowSize(windowSize);
		this.imageView = imageView;
	}

	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}

	@Override
	public double[] extract(int[] pixels, int width, int height) {
		double[] hcr = HarrisCornerDetector.detect(pixels, width, height);

		int[] hcrImage = new int[hcr.length];
		for (int i = 0; i < hcr.length; i++) {
			int val = Math.max( Math.min((int)hcr[i], 255), 0) ;
			hcrImage[i] = (0xFF000000 | (val << 16) | (val << 8) | val);
		}
		
		WritableImage wr = new WritableImage(width, height);
		PixelWriter pw = wr.getPixelWriter();
		pw.setPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), hcrImage, 0, width);
		
		imageView.setImage(wr);
		
		double[] featureVector = new double[0];
			
		return featureVector;
	}
}



