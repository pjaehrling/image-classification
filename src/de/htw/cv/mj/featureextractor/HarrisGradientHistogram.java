package de.htw.cv.mj.featureextractor;

import java.awt.Point;
import java.util.List;

import de.htw.cv.mj.helper.HarrisCornerDetector;
import de.htw.cv.mj.helper.ImageTransformations;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class HarrisGradientHistogram implements FeatureExtractor  {
	
	private int buckets;
	private int windowSize;
	private boolean useMinMaxNormalisation;
	
	public HarrisGradientHistogram(int buckets, int windowSize, boolean useMinMaxNormalisation) {
		this.buckets = buckets;
		this.windowSize = windowSize;
		this.useMinMaxNormalisation = useMinMaxNormalisation;
		System.out.println("HarrisGradientHistogram: " + buckets + " / " + windowSize + " / " + useMinMaxNormalisation);
	}

	@Override
	public double[] extract(int[] pixels, int width, int height) {
		// get grayscale image
		int[] grayPixel = ImageTransformations.calcGrayscale(pixels);
		
		// get interest points using Harris Corner Detector
		List<Point> interestPoints = HarrisCornerDetector.detect(grayPixel, width, height);
		
		// Gradient
		int[] xGradient = ImageTransformations.calcXGradient(grayPixel, width, height);
		int[] yGradient = ImageTransformations.calcYGradient(grayPixel, width, height); 
		
		// prepare histogram
		int bucketSize = 360 / buckets;
		double[] featureVector = new double[buckets];
		
		// normalize by interest point count
		double normUnit = 1.0 / interestPoints.size();

		int windowGap = windowSize / 2;
		int windowPosX = 0;
		int windowPosY = 0;
		int windowPos = 0;
		
		int bucketIndex = 0;
		
		double intensity = 0;
		double angle = 0;
		
		for (Point p : interestPoints) {
			
			// Get Window around point
			for (int y = 0; y < windowSize; y++) {
				windowPosY = Math.max( Math.min(p.y + (y - windowGap), (height - 1)) , 0);
			
				for (int x = 0; x < windowSize; x++) {
					windowPosX = Math.max( Math.min(p.x + (x - windowGap), (width - 1)) , 0);
					windowPos = (windowPosY * width) + windowPosX;
					
					intensity = Math.hypot(
						(double) xGradient[windowPos], (double) yGradient[windowPos]
					);
					angle = Math.toDegrees(
						Math.atan2((double) xGradient[windowPos], (double) yGradient[windowPos])
					) + 180; // -180° - 180° --> 0° - 360°
					
					bucketIndex = Math.min((int)(angle / bucketSize), buckets - 1);
					
					if (useMinMaxNormalisation)
						featureVector[bucketIndex] += intensity;
					else
						featureVector[bucketIndex] += intensity * normUnit; // normalize by interest point count
					
					//System.out.println(windowPosX + "/" + windowPosY + " = " + angle + " --> " + bucketIndex + " : " + intensity);
				}
			}
			//System.out.println("-----------------------------");
		}
		
		if (useMinMaxNormalisation) normalizeMinMax(featureVector);
		
		return featureVector;
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
