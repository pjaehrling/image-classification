package de.htw.cv.mj.featureextractor;

import java.awt.Point;
import java.util.List;

import de.htw.cv.mj.helper.HarrisCornerDetector;
import de.htw.cv.mj.helper.ImageTransformations;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class HarrisMeanColor implements FeatureExtractor {
	
	private int windowSize;
	
	public HarrisMeanColor(int windowSize) {
		this.windowSize = windowSize;
	}
	
	@Override
	public double[] extract(int[] pixels, int width, int height) {
		// get grayscale image
		int[] grayPixel = ImageTransformations.calcGrayscale(pixels);
		
		// get interest points using Harris Corner Detector
		List<Point> interestPoints = HarrisCornerDetector.detect(grayPixel, width, height);
		
		int r = 0;
		int g = 0;
		int b = 0;
		int sum = 0;
		int val = 0;
		
		int windowGap = windowSize / 2;
		int windowPosX = 0;
		int windowPosY = 0;

		for (Point p : interestPoints) {
			
			// Get Window around point
			for (int y = 0; y < windowSize; y++) {
				windowPosY = Math.max( Math.min(p.y + (y - windowGap), (height - 1)) , 0);
			
				for (int x = 0; x < windowSize; x++) {
					windowPosX = Math.max( Math.min(p.x + (x - windowGap), (width - 1)) , 0);
					
					val = pixels[(windowPosY * width) + windowPosX];
					r += (val >> 16) & 0xFF;
					g += (val >> 8 ) & 0xFF;
					b += (val      ) & 0xFF;
					sum++;
				}
			}
			
		}
		
		double[] featureVector = new double[3];
		featureVector[0] = r / sum;
		featureVector[1] = g / sum;
		featureVector[2] = b / sum;
			
		return featureVector;
	}
}



