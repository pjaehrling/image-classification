package de.htw.cv.mj.helper;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Harris Corner Detector.
 * 
 * @author Marie Mandrela, Philipp Jährling
 */
public class HarrisCornerDetector {
	
	private static final double HCR_ALPHA = 0.1;
	private static final double HCR_THRESHOLD = 10000;
	private static final int NMS_WINDOW_SIZE = 7;

	/**
	 * Get a list of corners of the image.
	 * 
	 * @param grayPixel
	 * @param width
	 * @param height
	 * @return
	 */
	public static List<Point> detect(int[] grayPixel, int width, int height) {
		int size = grayPixel.length;
		
		// Gradient
		int[] xGradient = ImageTransformations.calcXGradient(grayPixel, width, height);
		int[] yGradient = ImageTransformations.calcYGradient(grayPixel, width, height);
		
		// Multiply
		int[] xx = new int[size];
		int[] yy = new int[size];
		int[] xy = new int[size];
		for (int i = 0; i < size; i++) {
			xx[i] = xGradient[i] * xGradient[i];
			yy[i] = yGradient[i] * yGradient[i];
			xy[i] = xGradient[i] * yGradient[i];
		}
		
		// Blur
		xx = ImageTransformations.calcGaussian(xx, width, height);
		yy = ImageTransformations.calcGaussian(yy, width, height);
		xy = ImageTransformations.calcGaussian(xy, width, height);
		
		// Corner Detection (Harris Corner Response)
		double hcr[] = new double[size];
		
		double A = 0; // Hg * Ix^2 	-> (xx)
		double B = 0; // Hg * Iy^2 	-> (yy)
		double C = 0; // Hg * Ixy 	-> (xy)
		
		double det = 0; 	// AB − C^2
		double trace = 0; 	// A + B;
		
		for (int i = 0; i < size; i++) {
			A = xx[i];
			B = yy[i];
			C = xy[i];
			
			det = (A * B - (C * C));
			trace = A + B;
			hcr[i] = det - (HCR_ALPHA * (trace * trace));
		}
		
		// Non-Maxima Suppression
		double maxima[] = reduceToMaxima(hcr, width, height);
		
		// Fill List with left over Points
		List<Point> corners = new ArrayList<Point>();
		int pos = 0;
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = (y * width) + x;
				if (maxima[pos] > 0) {
					corners.add( new Point(x, y) );
				}
			}
		}
		
		return corners;
	}
	
	/**
	 * Simple Non-Maxima Suppression
	 * 
	 * @param hcr
	 * @param width
	 * @param height
	 */
	private static double[] reduceToMaxima(double[] hcr, int width, int height) {
		double[] maxima = new double[hcr.length];
		int pos = 0;
		double val = 0;
		
		// run through image
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pos = (y * width) + x;
				val = hcr[pos];
				
				if (val > HCR_THRESHOLD && isGreatestInWindow(hcr, val, x, y,  width, height)) {
					maxima[pos] = val;
				} // else -> value stays 0
			}
		}
		
		return maxima;
	}
	
	/**
	 * Check if the given value is the greatest in a given window
	 * 
	 * @param hcr
	 * @param val
	 * @param xStart
	 * @param yStart
	 * @param imgWidth
	 * @param imgHeight
	 * @return
	 */
	private static boolean isGreatestInWindow(double[] hcr, double val, int xStart, int yStart,  int imgWidth, int imgHeight) {
		int xSrc, ySrc;
		int gap = NMS_WINDOW_SIZE / 2;
		int pos = 0;
		
		for (int y = 0; y < NMS_WINDOW_SIZE; y++) {
			ySrc = Math.max( Math.min(yStart + (y - gap), (imgHeight - 1)) , 0); // src Y + relative Y
			
			for (int x = 0; x < NMS_WINDOW_SIZE; x++) {
				xSrc = Math.max( Math.min(xStart + (x - gap), (imgWidth - 1)) , 0); // src X + relative X
				
				pos = (ySrc * imgWidth) + xSrc;
				
				if (hcr[pos] > val) {
					return false; // value is not the greatest value
				}	
			}
		}
		return true;
	}
	
	
}
