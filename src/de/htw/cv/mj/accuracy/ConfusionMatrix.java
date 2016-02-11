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
		
		printConfusionMatrixToConsole(matrix, categories);
		
		for (int i = 0; i < categories.size(); i++)	{
			for (int j = 0; j < categories.size(); j++) {
				matrix[i][j] /= sum[i];
			}
		}
		
		return matrix;
	}
	
	private static void printConfusionMatrixToConsole(double[][] matrix, List<String> categories) {
		
		// --- Print Matrix ---
		System.out.println("Wahre Klasse (vertikal) v  & Erkannte Klasse (horizontal) > ");
		System.out.println();
		
		// Actual category index (Head)
		System.out.print("C. || ");
		for (int i = 0; i < categories.size(); i++)	{
			System.out.printf("%2d | ", i);
		}
		System.out.println();
		
		// Devider
		for (int i = 0; i < categories.size(); i++)	{
			System.out.print("=====");
		}
		System.out.println();
		
		// Matrix
		for (int y = 0; y < matrix.length; y++) {
			// First column shows category index
			System.out.printf("%2d || ", y);
			
			for (int x = 0; x < matrix[y].length; x++) {
				System.out.printf("%2.0f | ", matrix[y][x]);
			}
			System.out.println();
		}
		
		System.out.println();
		
		// ---- Print category association ----
		String line = "";
		for (int actualIndex = 0; actualIndex < categories.size(); actualIndex++)	{
			line = actualIndex + " = " + categories.get(actualIndex) + "    -->    ";
			for (int foundIndex = 0; foundIndex < categories.size(); foundIndex++) {
				if (matrix[actualIndex][foundIndex] > 0) {
					line += categories.get(foundIndex) + ": " + (int)matrix[actualIndex][foundIndex] + ", ";
				}	
			}
			System.out.println(line.substring(0, line.length()-2));
		}
		System.out.println();
		System.out.println();
	}
	
}
