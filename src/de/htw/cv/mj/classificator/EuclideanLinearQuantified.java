package de.htw.cv.mj.classificator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.distance.EucledianDistance;
import de.htw.cv.mj.model.Pic;

public class EuclideanLinearQuantified implements Classifier {

	@Override
	public String classify(Pic image, ImageManager imageManager) {
		List<Pic> trainedImages = imageManager.getImages();
		List<String> categories = imageManager.getCategoryNames();
		
		double[][] categoryFeatures = quantifyClassFeature(trainedImages, categories, image);
		
		double minDistance = Double.MAX_VALUE;
		double distance = 0;
		String bestFittingCategory = "";
		
		// loop through categories
		for (int ci = 0; ci < categories.size(); ci++) {
			distance = EucledianDistance.calculate(image.getFeatures(), categoryFeatures[ci]);
			if (distance < minDistance) {
				minDistance = distance;
				bestFittingCategory = categories.get(ci);
			}
		}	
		
		return bestFittingCategory;
	}
	
	@Override
	public int classifyRank(Pic image, ImageManager imageManager) {
		List<Pic> trainedImages = imageManager.getImages();
		List<String> categories = imageManager.getCategoryNames();
		
		double[][] categoryFeatures = quantifyClassFeature(trainedImages, categories, image);
		List<Result> distancesList = new ArrayList<Result>();
		double distance = 0;
		
		
		for (int ci = 0; ci < categories.size(); ci++) {
			distance = EucledianDistance.calculate(image.getFeatures(), categoryFeatures[ci]);
			distancesList.add(new Result(categories.get(ci), distance));
		}	
		Collections.sort(distancesList);
		
		int rank = distancesList.indexOf(new Result(image.getCategoryName(), 0.0));
		return rank + 1;
	}
	
	/**
	 * Just calculating the mean value here -> linear quantification
	 * 
	 * @param trainedImages
	 * @param categories
	 * @param doNotIncludeImage
	 * @return
	 */
	private double[][] quantifyClassFeature (List<Pic> trainedImages, List<String> categories, Pic doNotIncludeImage) {
		int featureDimension = trainedImages.get(0).getFeatures().length;
		int categoryCount = categories.size();
		
		// (first) index is the category index
		double[][] categoryFeatures = new double[categoryCount][featureDimension];
		int[] categoryImageCounts = new int[categoryCount];
		
		// will be filled & used in the loop
		double[] imageFeatures; 
		int categoryIndex = 0;
		
		// Add up all features per category
		for (Pic trainedImage : trainedImages) {
			if (doNotIncludeImage.getName().equals(trainedImage.getName())) {
				continue; // reject the image which should be classified
			}
			
			imageFeatures = trainedImage.getFeatures();
			categoryIndex = categories.indexOf(trainedImage.getCategoryName());
			categoryImageCounts[categoryIndex]++;
			for (int i = 0; i < imageFeatures.length; i++) {
				categoryFeatures[categoryIndex][i] += imageFeatures[i];
			}
		}
		
		// Divide by image count per category
		for (categoryIndex = 0; categoryIndex < categoryCount; categoryIndex++) {
			for (int i = 0; i < categoryFeatures[categoryIndex].length; i++) {
				categoryFeatures[categoryIndex][i] /= categoryImageCounts[categoryIndex];
			}
		}
		
		return categoryFeatures;
	}
	

}
