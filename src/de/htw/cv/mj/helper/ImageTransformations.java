package de.htw.cv.mj.helper;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class ImageTransformations {
	
	/***************************************************************************
	 * 
	 * Grayscale Stuff
	 * 
	 ***************************************************************************/
	
	private static final double RED_WEIGHT =   0.299;
	private static final double GREEN_WEIGHT = 0.587;
	private static final double BLUE_WEIGHT =  0.114;
	
	/**
	 * Convert a given rgb pixel array into grayscale pixel array
	 * 
	 * @param pixels
	 * @return
	 */
	public static int[] calcGrayscale(int[] pixels) {
		int[] gsPixels = new int[pixels.length];
    	for(int i = 0; i < pixels.length; i++) {
    		gsPixels[i] = getGrayValue(pixels[i]);
    	}
    	return gsPixels;
	}
	
	/**
	 * Calculate gray value (0 = black, 255 = white) for a rgb-value. 
	 * 
	 * @param rgb - the int holding the rbg data
	 * @return int - grey value
	 */
	private static int getGrayValue(int rgb) {
		int red = 	(rgb >> 16) & 0xFF; // 255
		int green = (rgb >> 8 ) & 0xFF;
		int blue = 	(rgb	  ) & 0xFF; 
		return (int)((red * RED_WEIGHT) + (green * GREEN_WEIGHT) + (blue * BLUE_WEIGHT));
	}
	
	
	
	/***************************************************************************
	 * 
	 * Filter, ...
	 * 
	 ***************************************************************************/
	
	private static final int[] GRADIENT_FILTER = new int[]{-1, 0, 1};
	private static final int GRADIENT_DEVIDER = 2;
	
	private static final int[] SOBEL_FILTER_X = new int[]{
		-1, 0, 1, 
		-2, 0, 2, 
		-1, 0, 1
	};
	private static final int[] SOBEL_FILTER_Y = new int[]{
		-1, -2, -1, 
		 0,  0,  0, 
		 1,  2,  1
	};
	private static final int SOBEL_DEVIDER = 8;
	
	/**
	 * 
	 * @param grayPixel
	 * @param srcWidth
	 * @param srcHeight
	 * @return
	 */
	public static int[] calcXGradient(int[] grayPixel, int srcWidth, int srcHeight) {
		return doFilter(grayPixel, srcWidth, srcHeight, GRADIENT_FILTER, 3, 1, GRADIENT_DEVIDER);
		//return doFilter(grayPixel, srcWidth, srcHeight, SOBEL_FILTER_X, 3, 3, SOBEL_DEVIDER);
	}
	
	/**
	 * 
	 * @param grayPixel
	 * @param srcWidth
	 * @param srcHeight
	 * @return
	 */
	public static int[] calcYGradient(int[] grayPixel, int srcWidth, int srcHeight) {
		return doFilter(grayPixel, srcWidth, srcHeight, GRADIENT_FILTER, 1, 3, GRADIENT_DEVIDER);
		//return doFilter(grayPixel, srcWidth, srcHeight, SOBEL_FILTER_Y, 3, 3, SOBEL_DEVIDER);
	}
	
	
	private static final int[] GAUSSIAN_FILTER = new int[]{
		1, 2, 1,
		2, 4, 2,
		1, 2, 1
	};
	private static final int GAUSSIAN_DEVIDER = 16;
	
	/**
	 * 
	 * @param grayPixel
	 * @param srcWidth
	 * @param srcHeight
	 * @return
	 */
	public static int[] calcGaussian(int[] grayPixel, int srcWidth, int srcHeight) {
		return doFilter(grayPixel, srcWidth, srcHeight, GAUSSIAN_FILTER, 3, 3, GAUSSIAN_DEVIDER);
	}
	
	
	/**
	 * 
	 * 
	 * @param gsPixel
	 * @param srcWidth
	 * @param srcHeight
	 * @param filter
	 * @param filterWidth
	 * @param filterHeight
	 * @param filterDevider
	 * @return 
	 */
	private static int[] doFilter(int[] grayPixel, int srcWidth, int srcHeight, int[] filter, int filterWidth, int filterHeight, int filterDevider) {
		int[] resPixel = new int[grayPixel.length];
		
		// check for right filter window size
		if ((filterWidth % 2 == 0) || (filterHeight % 2 == 0)) return null;
		
		// Calculate the filter gap (position -> relative position where p(0,0) is the filter center)
		int filterYGap = filterHeight / 2;
		int filterXGap = filterWidth / 2;
		
		// run through source image (gray-scale)
		for (int y = 0; y < srcHeight; y++) {
			for (int x = 0; x < srcWidth; x++) {
				
				int center = (y * srcWidth) + x;
				int newVal = 0;
				
				// run through filter
				for (int filterY = 0; filterY < filterHeight; filterY++) {
					int srcY = Math.max( Math.min(y + filterY - filterYGap, srcHeight-1) , 0); // srcY + relative Y
					
					for (int filterX = 0; filterX < filterWidth; filterX++) {
						int srcX = Math.max( Math.min(x + filterX - filterXGap, srcWidth-1) , 0); // srcX + relative X
						
						int filterPos = (filterY * filterWidth) + filterX;
						int srcPos = (srcY * srcWidth) + srcX;
						
						// rgb values all represent gray value -> just get blue value
						newVal += filter[filterPos] * grayPixel[srcPos];
					}
				}
				// end filter
				
				resPixel[center] = newVal;
			}
		}
		
		return resPixel;
	}
}
