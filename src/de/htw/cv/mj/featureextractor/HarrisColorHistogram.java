package de.htw.cv.mj.featureextractor;

import java.awt.Point;
import java.util.List;

import de.htw.cv.mj.helper.HarrisCornerDetector;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class HarrisColorHistogram implements FeatureExtractor  {

	private static final int WINDOW_SIZE = 3;
	private int buckets;
	
	public HarrisColorHistogram(int buckets) {
		this.buckets = buckets;
	}

	public void setBuckets(int buckets) {
		this.buckets = buckets;
	}

	@Override
	public double[] extract(int[] pixels, int width, int height) {
		// get interest points using Harris Corner Detector
		List<Point> interestPoints = HarrisCornerDetector.detect(pixels, width, height);
		
		// prepare histogram
		int bucketSize = 256 / buckets;
		double[] featureVector = new double[buckets * buckets * buckets];
		
		// NORMALIZE -> pixel count
		double normUnit = 1.0 / interestPoints.size();

		int windowGap = WINDOW_SIZE / 2;
		int windowPosX = 0;
		int windowPosY = 0;
		
		int val = 0;
		int redPos = 0;
		int greenPos = 0;
		int bluePos = 0;
		int histogramPos = 0;

		for (Point p : interestPoints) {
			
			// Get Window around point
			for (int y = 0; y < WINDOW_SIZE; y++) {
				windowPosY = Math.max( Math.min(p.y + (y - windowGap), (height - 1)) , 0);
			
				for (int x = 0; x < WINDOW_SIZE; x++) {
					windowPosX = Math.max( Math.min(p.x + (x - windowGap), (width - 1)) , 0);
					
					val = pixels[(windowPosY * width) + windowPosX];
					
					redPos = handleBorderCase(((val >> 16) & 0xFF) / bucketSize);
					greenPos = handleBorderCase(((val >> 8) & 0xFF) / bucketSize);
					bluePos = handleBorderCase(((val) & 0xFF) / bucketSize);
					histogramPos = (bluePos * buckets * buckets) + (greenPos * buckets) + redPos;
					
					// just add pixel to bin
					//featureVector[histogramPos]++;
					featureVector[histogramPos] += normUnit;
				}
			}
			
		}
		
		// NORMALIZE -> max
		// normalizeMax(featureVector);
			
		return featureVector;
	}

	private int handleBorderCase(int pos) {
		return pos >= buckets ? buckets - 1 : pos;
	}
	
	/**
	 * Normalize with bin-max value
	 * @param hist
	 */
	private void normalizeMax(double[] hist) {
		double maxVal = 0;
		
		// Get Max
		for (int i = 0; i < hist.length; i++) {
			if (hist[i] > maxVal) {
				maxVal = hist[i];
			}
		}
		
		// Calc
		for (int i = 0; i < hist.length; i++) {
			hist[i] = hist[i] / maxVal;
		}
	}
	
}
