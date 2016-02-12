package de.htw.cv.mj.classificator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.distance.EucledianDistance;
import de.htw.cv.mj.model.Pic;

/**
 * K-Nearest Neighbor algorithm. 
 * Determines class by majority vote amongst the nearest neighbors.
 * 
 * @author Marie Mandrela, Philipp Jährling
 */
public class KNearestNeighbors implements Classifier {

	private double neighbors;
	
	/**
	 * Constructor. Sets the neighbors if the number is greater 0. Otherwise it's 1.
	 * 
	 * @param neighbors
	 */
	public KNearestNeighbors(double neighbors) {
		if (neighbors > 0)
		{
			this.neighbors = neighbors;
		} else {
			this.neighbors = 1;
		}
	}

	/**
	 * Sets the neighbors if the number is greater 0.
	 * 
	 * @param neighbors
	 */
	public void setNeighbours(double neighbors) {
		if (neighbors > 0) {
			this.neighbors = neighbors;
		}
	}

	@Override
	public String classify(Pic image, ImageManager imageManager) {
		
		List<Pic> trainedImages = imageManager.getImages();
		List<Result> neighborImages = new ArrayList<Result>();
		double distance = 0;
		
		for (Pic trainedImage : trainedImages) {
			if (image.getName().equals(trainedImage.getName())) {
				continue; // do not compare with itself
			}

			distance = EucledianDistance.calculate(image.getFeatures(), trainedImage.getFeatures());
			Result result = new Result(trainedImage.getCategoryName(), distance);
			
			if (neighborImages.size() < neighbors) {
				neighborImages.add(result); // just fill the neighbors first
			} else {
				updateNeighbors(result, neighborImages);
			}
		}
		
		return majorityVote(neighborImages);
	}
	
	/**
	 * Determine the most common class amongst the neighbors
	 * @param neighborImages
	 * @return
	 */
	public String majorityVote(List<Result> neighborImages) {
		
		Map<Object, Long> votes =
				neighborImages.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		
		Map.Entry<Object, Long> maxVote = null;
		for (Map.Entry<Object, Long> vote : votes.entrySet()) {
			if (maxVote == null || vote.getValue() > maxVote.getValue()) {
				maxVote = vote;
			}
		}
		
		return ((Result)maxVote.getKey()).getCategory();
	}
	
	/**
	 * Replaces the farthest neighbor with the given result if it is closer.
	 * @param result
	 * @param neighbors
	 */
	public void updateNeighbors(Result result, List<Result> neighborImages) {
		int maxIndex = neighborImages.indexOf(Collections.max(neighborImages));
		
		if (neighborImages.get(maxIndex).getDistance() > result.getDistance()) {
			neighborImages.remove(maxIndex);
			neighborImages.add(result);
		}
	}

	@Override
	public int classifyRank(Pic image, ImageManager imageManager) {
		return 0;
	}

	
}
