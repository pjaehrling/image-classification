package de.htw.cv.mj.classificator;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.distance.Distance;
import de.htw.cv.mj.model.Pic;

/**
 * Classifier interface.
 * 
 * @author Marie Mandrela, Philipp Jährling
 */
public abstract class Classifier {
	
	Distance distance;
	
	public Classifier(Distance distance) {
		this.distance = distance;
	}
	
	public Distance getDistance() {
		return distance;
	}

	public void setDistance(Distance distance) {
		this.distance = distance;
	}
	
	/**
	 * Classify the given Pic.
	 * 
	 * @param image
	 * @param imageManager
	 * @return
	 */
	public abstract String classify(Pic image, ImageManager imageManager);
	
	/**
	 * Classify the given Pic until the correct category is found. 
	 * Return the rank of the correct category.
	 * 
	 * @param image
	 * @param imageManager
	 * @return
	 */
	public abstract int classifyRank(Pic image, ImageManager imageManager);
}
