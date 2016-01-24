package de.htw.cv.mj;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.image.Image;
import de.htw.cv.mj.featureextractor.FeatureExtractor;
import de.htw.cv.mj.model.Pic;
import de.htw.cv.mj.model.Category;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class ImageManager {
	
	private static final String[] EXTENSIONS = new String[]{"jpg", "png"};
	
	private HashMap<String, List<Pic>> imageCache; // images (directory path is the key)
	private HashMap<String, Set<Category>> categoryCache;
	private HashMap<String, Pic> testImages;
	private String currentPath;
	
	/**
	 * Constructor
	 */
	public ImageManager() {
		imageCache = new HashMap<String, List<Pic>>();
		categoryCache = new HashMap<String, Set<Category>>();
		testImages = new HashMap<String, Pic>();
	}
	
	/* ***************************************************************************************************
	 * Public Methods
	 * ***************************************************************************************************/
	
	/**
	 * 
	 * @param imageName
	 * @param imagePath
	 * @return
	 */
	public void removeImage(String imageName) {
		List<Pic> pics = imageCache.get(currentPath);
		
		for (Pic pic : pics) {
			if (pic.getName().equals(imageName)) {
				pics.remove(pic);
				return;
			}  
		}
	}
	
	/**
	 * 
	 * @param imagePath
	 * @return
	 */
	public List<Pic> getImagesForPath(String imagePath) {
		return imageCache.get(imagePath.toLowerCase());
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Pic> getImages() {
		return imageCache.get(currentPath);
	}
	
	/**
	 * 
	 * @param imagePath
	 * @return
	 */
	public Pic getImage(String imageName) {
		List<Pic> pics = imageCache.get(currentPath);
		
		for (Pic pic : pics) {
			if (pic.getName().equals(imageName)) { return pic; }  
		}
		return null;
	}
	
	
	/**
	 * Load images from a path and put them into the image map.
	 * Adds categories for all images and puts them into the category map.
	 * Sets the current image path, to the loaded one.
	 * 
	 * @param imagePath
	 */
	public void loadImages(String imagePath) {
		currentPath = imagePath.toLowerCase();
		
		// do not load images twice
		if (imageCache.containsKey(currentPath))  return;
		
		// get all images from the directory
		File imageDir = new File(currentPath);
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
		imageCache.put(currentPath, imageList);
		

		// create categories and add them to a category set that is stored for a path
		Set<Category> categorySet = new HashSet<Category>();
		for (File imageFile : imageFiles) {
			categorySet.add(loadCategory(imageFile));
		}
		categoryCache.put(currentPath, categorySet);
	}
	
	/**
	 * Set the image features.
	 * 
	 * @param imagePath
	 * @param extractor
	 */
	public void trainImages(FeatureExtractor extractor) {
		List<Pic> pics = imageCache.get(currentPath);
		
		for (Pic pic : pics) {
			double [] features = extractor.extract(pic.getPixels(), 0, 0, pic.getWidth(), pic.getHeight(), pic.getWidth());
			pic.setFeatures(features);
		}
	}
	
	/**
	 * Set a test image for the current path.
	 * 
	 * @param pic
	 */
	public void setTestImage(Pic pic) {
		testImages.put(currentPath, pic);
	}
	
	/**
	 * Get the test image for the current path.
	 * 
	 * @param pic
	 */
	public Pic getTestImage() {
		return testImages.get(currentPath);
	}
	
	/* ***************************************************************************************************
	 * Private Methods
	 * ***************************************************************************************************/
	
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
	
	/**
	 * Gets the category name from a file
	 * 
	 * @param imageFile
	 * @return
	 */
	private String getCategory(File imageFile) {
		String filename = imageFile.getName().toLowerCase();
		return filename.substring(0, filename.indexOf("_"));
	}
	
}
