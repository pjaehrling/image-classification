package de.htw.cv.mj.featureextractor;

import java.awt.Point;
import java.util.List;

import de.htw.cv.mj.helper.HarrisCornerDetector;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class HarrisMeanColor implements FeatureExtractor {

	private static final int WINDOW_SIZE = 3;
	
	@Override
	public double[] extract(int[] pixels, int width, int height) {
		// get interest points using Harris Corner Detector
		List<Point> interestPoints = HarrisCornerDetector.detect(pixels, width, height);
		
		int r = 0;
		int g = 0;
		int b = 0;
		int sum = 0;
		int val = 0;
		
		int windowGap = WINDOW_SIZE / 2;
		int windowPosX = 0;
		int windowPosY = 0;

		for (Point p : interestPoints) {
			
			// Get Window around point
			for (int y = 0; y < WINDOW_SIZE; y++) {
				windowPosY = Math.max( Math.min(p.y + (y - windowGap), (height - 1)) , 0);
			
				for (int x = 0; x < WINDOW_SIZE; x++) {
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



