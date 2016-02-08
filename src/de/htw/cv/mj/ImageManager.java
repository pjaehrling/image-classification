package de.htw.cv.mj;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.image.Image;
import de.htw.cv.mj.featureextractor.FeatureExtractor;
import de.htw.cv.mj.featureextractor.HarrisMeanColor;
import de.htw.cv.mj.model.Pic;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class ImageManager {
	
	private static final String[] EXTENSIONS = new String[]{"jpg", "png"};
	
	private HashMap<String, List<Pic>> imageCache; // images (directory path is the key)
	private HashMap<String, List<String>> categoryCache;
	private HashMap<String, String> testImageNames;
	private String currentPath;
	
	/**
	 * Constructor
	 */
	public ImageManager() {
		imageCache = new HashMap<String, List<Pic>>();
		categoryCache = new HashMap<String, List<String>>();
		testImageNames = new HashMap<String, String>();
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
	 * @return
	 */
	public List<Pic> getImagesForCategoryName(String categoryName) {
		List<Pic> allImages = imageCache.get(currentPath);
		List<Pic> categoryImages = new ArrayList<Pic>();
		
		for (Pic pic : allImages) {
			if (pic.getCategoryName().equals(categoryName))
				categoryImages.add(pic);
		}
		return categoryImages;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<String> getCategoryNames() {
		return categoryCache.get(currentPath);
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getImageNames() {
		List<Pic> pics = imageCache.get(currentPath);
		List<String> names = new ArrayList<String>();
		
		for (Pic pic : pics) {
			names.add(pic.getName());
		}
		return names;
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
		List<String> categoryList = new ArrayList<String>();
		for (File imageFile : imageFiles) {
			Pic pic = loadImage(imageFile);
			imageList.add(pic);
			if (!categoryList.contains(pic.getCategoryName())) {
				categoryList.add(pic.getCategoryName());
			}
		}
		imageCache.put(currentPath, imageList);
		categoryCache.put(currentPath, categoryList);
	}
	
	/**
	 * Set the image features.
	 * 
	 * @param imagePath
	 * @param extractor
	 */
	public void trainImages(FeatureExtractor extractor) {
		// Training images
		List<Pic> pics = imageCache.get(currentPath);
		
		for (Pic pic : pics) {
			double [] features = extractor.extract(pic.getPixels(), pic.getWidth(), pic.getHeight());
			pic.setFeatures(features);
		}
	}
	
	/**
	 * Take the given image name and set the image as the test image for this image set.
	 * 
	 * @param pic
	 */
	public void setTestImageByName(String imageName) {
		testImageNames.put(currentPath, imageName);
	}
	
	
	
	/**
	 * Get the test image for the current path.
	 * 
	 * @param pic
	 */
	public Pic getTestImage() {
		String testImageName = testImageNames.get(currentPath);
		if (testImageName != null) {
			return getImage(testImageName);
		} else {
			return null;
		}
	}
	
	/* ***************************************************************************************************
	 * Private Methods
	 * ***************************************************************************************************/
	
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
