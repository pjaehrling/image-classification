package de.htw.cv.mj.classificator;

import java.util.List;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.model.Category;
import de.htw.cv.mj.model.Pic;

public class EuclideanOneVsAll implements Classifier {

	@Override
	public String classify(Pic image, ImageManager imageManager) {
		List<Pic> knownImages = imageManager.getImages();
		
		double minDistance = Double.MAX_VALUE;
		double distance = 0;
		Pic bestFittingImage = null;
		
		for (Pic knownImage : knownImages) {
			// TODO: getFeatures() liefert leere Arrays!!!
			distance = calcEuclideanDistance(image.getFeatures(), knownImage.getFeatures());
			if (distance < minDistance) {
				bestFittingImage = knownImage;
			}
		}
		
		return bestFittingImage.getCategory();
	}
	
	private double calcEuclideanDistance(double[] a, double[] b) {
		assert(a.length == b.length); 
		
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += Math.pow( (a[i] - b[i]), 2);
		}
		return Math.sqrt(sum);
	}

}
