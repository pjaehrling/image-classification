package de.htw.cv.mj;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.image.Image;
import de.htw.cv.featureextraction.FeatureExtractor;
import de.htw.cv.mj.model.Pic;
import de.htw.cv.mj.model.Category;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class ImageManager {
	
	private static final String[] EXTENSIONS = new String[]{"jpg", "png"};
	
	private HashMap<String, List<Pic>> imageCache; // images (directory path is the key)
	private HashMap<String, Set<Category>> categoryCache;
 
	/**
	 * Default constructor
	 */
	public ImageManager() {
		imageCache = new HashMap<String, List<Pic>>();
		categoryCache = new HashMap<String, Set<Category>>();
	}
	
	/**
	 * Load images from one path on object creation.
	 * 
	 * @param imageDirectory
	 */
	public ImageManager(String imagePath) {
		imageCache = new HashMap<String, List<Pic>>();
		this.loadImages(imagePath);
	}
	
	/**
	 * Load images from multiple paths on object creation.
	 * 
	 * @param imageDirectories
	 */
	public ImageManager(String[] imagePaths) {
		imageCache = new HashMap<String, List<Pic>>();
		for (String path : imagePaths) {
			this.loadImages(path);
		}
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	public List<Pic> getImageForPath(String path) {
		return imageCache.get(path.toLowerCase());
	}
	
	
	/**
	 * Load all images from a directory and put them into the image map
	 * Adds categories for all images and puts them into the category map
	 * 
	 * @param imageDir
	 */
	public void loadImages(String path) {
		// do not load images twice
		if (imageCache.containsKey(path.toLowerCase())) return;
		
		// get all images from the directory
		File imageDir = new File(path);
		File[] imageFiles = imageDir.listFiles((File dir, String name) -> {
			for (String ext : EXTENSIONS) {
                if (name.toLowerCase().endsWith("." + ext)) { return true; }
            }
			return false;
		});
		
		// load images files and add them to an image list that is stored for a path
		List<Pic> imageList = new ArrayList<Pic>();
		for (File imageFile : imageFiles) {
			imageList.add(loadImage(imageFile));
		}
		imageCache.put(imageDir.getPath().toLowerCase(), imageList);
		

		// create categories and add them to a category set that is stored for a path
		Set<Category> categorySet = new HashSet<Category>();
		for (File imageFile : imageFiles) {
			categorySet.add(loadCategory(imageFile));
		}
		categoryCache.put(imageDir.getPath().toLowerCase(), categorySet);
	}
	
	private Category loadCategory(File imageFile) {
		String category = getCategory(imageFile);
		
		return new Category(category);
	}
	
	/**
	 * Load an image and return a "Pic" object as it's representation
	 * 
	 * @param imageFile
	 * @return
	 */
	private Pic loadImage(File imageFile) {
		Image image 	= new Image(imageFile.toURI().toString());
		String filename = imageFile.getName().toLowerCase();
		String category = getCategory(imageFile);

		return new Pic(filename, category, image);
	}
	
	private String getCategory(File imageFile) {
		String filename = imageFile.getName().toLowerCase();
		return filename.substring(0, filename.indexOf("_"));
	}
	
	/**
	 * Set the image features.
	 * 
	 * @param imagePath
	 * @param extractor
	 */
	public void trainImages(String imagePath, FeatureExtractor extractor) {
		List<Pic> pics = imageCache.get(imagePath);
		
		for (Pic pic : pics) {
			double [] features = extractor.extract(pic.getPixels(), 0, 0, pic.getWidth(), pic.getHeight(), pic.getWidth());
			pic.setFeatures(features);
		}
	}
	
}
