package de.htw.cv.mj.helper;

public class HarrisCornerDetector {
	
	private static final double HCR_ALPHA = 0.1;

	public static double[] detect(int[] pixel, int width, int height) {
		int size = pixel.length;
		int[] grayPixel = ImageTransformations.calcGrayscale(pixel);
		
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
		double hcrVal = 0; 	// det(M) − α * trace(M)^2
		
		for (int i = 0; i < size; i++) {
			A = xx[i];
			B = yy[i];
			C = xy[i];
			
			det = (A * B - (C * C));
			trace = A + B;
			hcrVal = det - (HCR_ALPHA * (trace * trace));
			System.out.println(hcrVal);
			
			// TODO have a threshold/range?
			hcr[i] = hcrVal;
		}
		
		return hcr;
	}
	
	
}
