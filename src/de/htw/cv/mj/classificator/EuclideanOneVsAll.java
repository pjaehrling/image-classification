package de.htw.cv.mj.classificator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.distance.EucledianDistance;
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
			
			distance = EucledianDistance.calculate(image.getFeatures(), trainedImage.getFeatures());
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
		Map<String, Result> distances = new HashMap<String, Result>();
		
		for (Pic trainedImage : trainedImages) {
			if (image.getName().equals(trainedImage.getName())) {
				continue; // do not compare with itself
			}
				
			double distance = EucledianDistance.calculate(image.getFeatures(), trainedImage.getFeatures());
			
			String trainedCategory = trainedImage.getCategoryName();
			Result savedDistance = distances.get(trainedCategory);
			if (savedDistance == null || distance < savedDistance.getDistance()) {
				distances.put(trainedCategory, new Result(trainedCategory, distance));
			}
		}
		
		List<Result> distancesList = new ArrayList<Result>(distances.values());
		Collections.sort(distancesList);
		
		int rank = distancesList.indexOf(new Result(category, 0.0));
		
		return rank + 1;
	}	
}
