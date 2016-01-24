package de.htw.cv.mj.classificator;

import java.util.List;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.model.Pic;

public class EuclideanOneVsAll implements Classifier {

	@Override
	public String classify(Pic image, ImageManager imageManager) {
		List<Pic> trainedImages = imageManager.getImages();
		
		double minDistance = Double.MAX_VALUE;
		double distance = 0;
		Pic bestFittingImage = null;
		
		for (Pic trainedImage : trainedImages) {
			if (image.getName().equals(trainedImage.getName())) {
				continue; // do not compare with itself
			} else {
				distance = calcEuclideanDistance(image.getFeatures(), trainedImage.getFeatures());
				// System.out.println(trainedImage.getName() + " --> " + distance + " (min: " + minDistance + ")");
				
				if (distance < minDistance) {
					minDistance = distance;
					bestFittingImage = trainedImage;
				}
			}
		}
		
		return bestFittingImage.getCategory();
	}
	
	private double calcEuclideanDistance(double[] a, double[] b) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			if (b.length <= i) break;
			
			sum += Math.pow( (a[i] - b[i]), 2);
		}
		return Math.sqrt(sum);
	}

}
