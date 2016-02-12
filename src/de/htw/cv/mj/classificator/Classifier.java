package de.htw.cv.mj.classificator;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.model.Pic;

/**
 * Classifier interface.
 * 
 * @author Marie Mandrela, Philipp Jährling
 */
public interface Classifier {
	
	/**
	 * Classify the given Pic.
	 * 
	 * @param image
	 * @param imageManager
	 * @return
	 */
	public String classify(Pic image, ImageManager imageManager);
	
	/**
	 * Classify the given Pic until the correct category is found. 
	 * Return the rank of the correct category.
	 * 
	 * @param image
	 * @param imageManager
	 * @return
	 */
	public int classifyRank(Pic image, ImageManager imageManager);
}
