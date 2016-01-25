package de.htw.cv.mj.accuracy;

import java.util.List;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.classificator.Classifier;
import de.htw.cv.mj.model.Pic;

public class MeanRank {

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
	
	public static double calculate(Classifier classifier, ImageManager imageManager, String category) {
		int rank = 0;
		int sum = 0;
		List<Pic> pics = imageManager.getImages();
		
		for (Pic pic : pics) {
			if (pic.getCategoryName().equals(category)) {
				rank += classifier.classifyRank(pic, imageManager);
				sum++;
			}
		}
		
		return (double)rank / (double)sum;
	}
}
