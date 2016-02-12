package de.htw.cv.mj.accuracy;

import java.util.List;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.classificator.Classifier;
import de.htw.cv.mj.model.Pic;

/**
 * Overall Correct Rate. The percentage of correctly classified images in the data set.
 * 
 * @author Marie Mandrela, Philipp Jährling
 */
public class CorrectRate {
	
	/**
	 * Calculates the overall correct rate.
	 * 
	 * @param classifier
	 * @param imageManager
	 * @return
	 */
	public static double calculate(Classifier classifier, ImageManager imageManager) {
		
		int correct = 0;
		int sum = 0;
		
		List<Pic> pics = imageManager.getImages();
		
		for (Pic pic : pics) {
			String category = classifier.classify(pic, imageManager);
			sum++;
			if(category.equals(pic.getCategoryName()))
				correct++;
		}
		
		return (double)correct / (double)sum;
	}
	
	/**
	 * Calculates the correct rate for one category.
	 * 
	 * @param classifier
	 * @param imageManager
	 * @param category
	 * @return
	 */
	public static double calculate(Classifier classifier, ImageManager imageManager, String category) {
		
		int correct = 0;
		int sum = 0;
		
		List<Pic> pics = imageManager.getImagesForCategoryName(category);
		
		for (Pic pic : pics) {
			String foundCategory = classifier.classify(pic, imageManager);
			sum++;
			if(foundCategory.equals(category))
				correct++;
		}
		
		return (double)correct / (double)sum;
	}
}
