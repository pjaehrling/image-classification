package de.htw.cv.mj.classificator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
			}
			
			distance = calcEuclideanDistance(image.getFeatures(), trainedImage.getFeatures());
			// System.out.println(trainedImage.getName() + " --> " + distance + " (min: " + minDistance + ")");
			
			if (distance < minDistance) {
				minDistance = distance;
				bestFittingImage = trainedImage;
			}
		}
		
		return bestFittingImage.getCategoryName();
	}
	
	@Override
	public int classifyRank(Pic image, ImageManager imageManager) {
		List<Pic> trainedImages = imageManager.getImages();
		String category = image.getCategoryName();
		Map<String, EucledianOneVsAllResult> distances = new HashMap<String, EucledianOneVsAllResult>();
		
		for (Pic trainedImage : trainedImages) {
			if (image.getName().equals(trainedImage.getName())) {
				continue; // do not compare with itself
			}
				
			double distance = calcEuclideanDistance(image.getFeatures(), trainedImage.getFeatures());
			
			String trainedCategory = trainedImage.getCategoryName();
			EucledianOneVsAllResult savedDistance = distances.get(trainedCategory);
			if (savedDistance == null || distance < savedDistance.getDistance()) {
				distances.put(trainedCategory, new EucledianOneVsAllResult(trainedCategory, distance));
			}
		}
		
		List<EucledianOneVsAllResult> distancesList = new ArrayList<EucledianOneVsAllResult>(distances.values());
		Collections.sort(distancesList);
		
		int rank = distancesList.indexOf(new EucledianOneVsAllResult(category, 0.0));
		
		return rank + 1;
	}	
	
	/**
	 * Calc the euclidean distance between two points.
	 * The dimensions doesn't matter, but both should have the same.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private double calcEuclideanDistance(double[] a, double[] b) {
		double sum = 0;
		for (int i = 0; i < a.length; i++) {
			if (b.length <= i) break;
			
			sum += Math.pow( (a[i] - b[i]), 2);
		}
		return Math.sqrt(sum);
	}

}
