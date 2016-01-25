package de.htw.cv.mj.accuracy;

import java.util.List;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.classificator.Classifier;
import de.htw.cv.mj.model.Pic;

public class ConfusionMatrix {
	
	public static double[][] calculate(Classifier classifier, ImageManager imageManager, List<String> categories) {
		
		double[][] matrix = new double[categories.size()][categories.size()];
		int[] sum = new int[categories.size()];
		List<Pic> pics = imageManager.getImages();
		
		for (Pic pic : pics) {
			String foundCategory = classifier.classify(pic, imageManager);
			if (categories.contains(foundCategory) && categories.contains(pic.getCategoryName())) {
				int actualIndex = categories.indexOf(pic.getCategoryName());
				int foundIndex = categories.indexOf(foundCategory);
				matrix[actualIndex][foundIndex]++;
				sum[actualIndex]++;
			}
		}
		
		for (int i = 0; i < categories.size(); i++)	{
			for (int j = 0; j < categories.size(); j++) {
				matrix[i][j] /= sum[i];
			}
		}
		
		return matrix;
	}
}
