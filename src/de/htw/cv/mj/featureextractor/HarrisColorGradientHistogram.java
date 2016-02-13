package de.htw.cv.mj.featureextractor;

import java.awt.Point;
import java.util.List;

import de.htw.cv.mj.helper.HarrisCornerDetector;
import de.htw.cv.mj.helper.HistogramHelper;
import de.htw.cv.mj.helper.ImageTransformations;

/**
 * Feature extraction using Harris Corner with Color and Gradient Histograms
 * 
 * @author Marie Mandrela, Philipp Jährling
 */
public class HarrisColorGradientHistogram implements FeatureExtractor  {

	private int windowSize;
	private int colorBuckets;
	private int gradientBuckets;
	private boolean useMinMaxNormalisation;
	
	public HarrisColorGradientHistogram(int colorBuckets, int gradientBuckets, int windowSize, boolean useMinMaxNormalisation) {
		this.colorBuckets = colorBuckets;
		this.gradientBuckets = gradientBuckets;
		this.windowSize = windowSize;
		this.useMinMaxNormalisation = useMinMaxNormalisation;
		
		System.out.println("HarrisColorGradientHistogram: " + colorBuckets + " / " + gradientBuckets + " / " + windowSize + " / " + useMinMaxNormalisation);
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
		int colorHistLength = (colorBuckets * colorBuckets * colorBuckets);
		int colorBucketSize = 256 / colorBuckets;
		int gradientBucketSize = 360 / gradientBuckets;
		double[] featureVector = new double[colorHistLength + gradientBuckets];
		
		// normalize by interestPointCount
		double normUnit = 1.0 / interestPoints.size();

		int windowGap = windowSize / 2;
		int windowPosX = 0;
		int windowPosY = 0;
		int windowPos = 0;
		
		double intensity = 0;
		double angle = 0;
		int gradientBinIndex = 0;
		
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
					windowPos = (windowPosY * width) + windowPosX;
					
					// ------- Color -------
					val = pixels[windowPos];
					redPos = HistogramHelper.handleBorderCase(((val >> 16) & 0xFF) / colorBucketSize, colorBuckets);
					greenPos = HistogramHelper.handleBorderCase(((val >> 8) & 0xFF) / colorBucketSize, colorBuckets);
					bluePos = HistogramHelper.handleBorderCase(((val) & 0xFF) / colorBucketSize, colorBuckets);
					histogramPos = (bluePos * colorBuckets * colorBuckets) + (greenPos * colorBuckets) + redPos;
					
					if (useMinMaxNormalisation) 
						featureVector[histogramPos]++; // just add pixel to bin
					else
						featureVector[histogramPos] += normUnit; // normalize by interestPointCount
					
					// ------- Gradient -------
					intensity = Math.hypot(
						(double) xGradient[windowPos], (double) yGradient[windowPos]
					);
					angle = Math.toDegrees(
						Math.atan2((double) xGradient[windowPos], (double) yGradient[windowPos])
					) + 180; // -180° - 180° --> 0° - 360°
					
					gradientBinIndex = Math.min((int)(angle / gradientBucketSize), gradientBuckets - 1);
					
					if (useMinMaxNormalisation)
						featureVector[colorHistLength + gradientBinIndex] += intensity;
					else
						featureVector[colorHistLength + gradientBinIndex] += intensity * normUnit; // normalize by interest point count
				}
			}
		}
		
		if (useMinMaxNormalisation) HistogramHelper.normalizeMinMax(featureVector);
			
		return featureVector;
	}
	
}
