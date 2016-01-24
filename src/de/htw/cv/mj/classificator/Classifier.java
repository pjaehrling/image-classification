package de.htw.cv.mj.classificator;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.model.Category;
import de.htw.cv.mj.model.Pic;

public interface Classifier {

	// TODO: Vielleicht sollte der Rückgabewert vom Typ "Category" sein
	// Dann sollten wir aber auch Pic als "category" wert keine String sondern ein Category-Object geben
	public String classify(Pic image, ImageManager imageManager);
	
	public int classifyRank(Pic image, ImageManager imageManager);
	
}
