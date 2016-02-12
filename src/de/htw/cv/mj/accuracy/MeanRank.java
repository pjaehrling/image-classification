package de.htw.cv.mj.accuracy;

import java.util.List;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.classificator.Classifier;
import de.htw.cv.mj.model.Pic;

/**
 * Mean Rank. The average rank at which the images are correctly classified in the dataset.
 * 
 * @author Marie Mandrela, Philipp Jährling
 */
public class MeanRank {

	/**
	 * Calculates the overall mean rank.
	 * 
	 * @param classifier
	 * @param imageManager
	 * @return
	 */
	public static double calculate(Classifier classifier, ImageManager imageManager) {
		int rank = 0;
		int sum = 0;
		List<Pic> pics = imageManager.getImages();
		
		for (Pic pic : pics) {
			rank += classifier.classifyRank(pic, imageManager);
			sum++;
		}
		
		return (double)rank / (double)sum;
	}
	
	/**
	 * Calculates the mean rank for one category.
	 * 
	 * @param classifier
	 * @param imageManager
	 * @param category
	 * @return
	 */
	public static double calculate(Classifier classifier, ImageManager imageManager, String category) {
		int rank = 0;
		int sum = 0;
		List<Pic> pics = imageManager.getImagesForCategoryName(category);
		
		for (Pic pic : pics) {
			rank += classifier.classifyRank(pic, imageManager);
			sum++;
		}
		
		return (double)rank / (double)sum;
	}
}
