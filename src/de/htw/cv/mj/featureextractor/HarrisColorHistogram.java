package de.htw.cv.mj.featureextractor;

import java.awt.Point;
import java.util.List;

import de.htw.cv.mj.helper.HarrisCornerDetector;
import de.htw.cv.mj.helper.ImageTransformations;

/**
 * Feature extraction using Harris Corner with Color Histograms.
 * 
 * @author Marie Mandrela, Philipp Jährling
 */
public class HarrisColorHistogram implements FeatureExtractor  {

	private int windowSize;
	private int buckets;
	private boolean useMinMaxNormalisation;
	
	public HarrisColorHistogram(int buckets, int windowSize, boolean useMinMaxNormalisation) {
		this.buckets = buckets;
		this.windowSize = windowSize;
		this.useMinMaxNormalisation = useMinMaxNormalisation;
		System.out.println("HarrisColorHistogram: " + buckets + " / " + windowSize + " / " + useMinMaxNormalisation);
	}

	@Override
	public double[] extract(int[] pixels, int width, int height) {
		// get grayscale image
		int[] grayPixel = ImageTransformations.calcGrayscale(pixels);
		
		// get interest points using Harris Corner Detector
		List<Point> interestPoints = HarrisCornerDetector.detect(grayPixel, width, height);
		
		// prepare histogram
		int bucketSize = 256 / buckets;
		double[] featureVector = new double[buckets * buckets * buckets];
		
		// normalize by interest point count
		double normUnit = 1.0 / interestPoints.size();

		int windowGap = windowSize / 2;
		int windowPosX = 0;
		int windowPosY = 0;
		
		int val = 0;
		int redPos = 0;
		int greenPos = 0;
		int bluePos = 0;
		int histogramPos = 0;

		for (Point p : interestPoints) {
			
			// Get Window around point
			for (int y = 0; y < windowSize; y++) {
				windowPosY = Math.max( Math.min(p.y + (y - windowGap), (height - 1)) , 0);
			
				for (int x = 0; x < windowSize; x++) {
					windowPosX = Math.max( Math.min(p.x + (x - windowGap), (width - 1)) , 0);
					
					val = pixels[(windowPosY * width) + windowPosX];
					
					redPos = handleBorderCase(((val >> 16) & 0xFF) / bucketSize);
					greenPos = handleBorderCase(((val >> 8) & 0xFF) / bucketSize);
					bluePos = handleBorderCase(((val) & 0xFF) / bucketSize);
					histogramPos = (bluePos * buckets * buckets) + (greenPos * buckets) + redPos;
					
					
					if (useMinMaxNormalisation)
						featureVector[histogramPos]++; // just add pixel to bin
					else
						featureVector[histogramPos] += normUnit;
				}
			}
			
		}
		
		if (useMinMaxNormalisation) normalizeMinMax(featureVector);
			
		return featureVector;
	}

	/**
	 * Make sure the chosen bucket index is not to big
	 * 
	 * @param pos
	 * @return
	 */
	private int handleBorderCase(int pos) {
		return pos >= buckets ? buckets - 1 : pos;
	}
	
	/**
	 * Normalize with min and max value
	 * @param hist
	 */
	private void normalizeMinMax(double[] hist) {
		double maxVal = 0;
		double minValue = Double.MAX_VALUE;
		
		// Get Min/Max
		for (int i = 0; i < hist.length; i++) {
			if (hist[i] > maxVal) {
				maxVal = hist[i];
			}
			if (hist[i] < minValue) {
				minValue = hist[i];
			}
		}
		
		// Calc
		for (int i = 0; i < hist.length; i++) {
			hist[i] = (hist[i] - minValue) / (maxVal - minValue);
		}
	}
	
}
