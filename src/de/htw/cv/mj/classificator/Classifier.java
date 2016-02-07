package de.htw.cv.mj.classificator;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.model.Pic;

public interface Classifier {
	public String classify(Pic image, ImageManager imageManager);	
	public int classifyRank(Pic image, ImageManager imageManager);
}
