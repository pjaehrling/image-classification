package de.htw.cv.mj.model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;

/**
 * @author Marie Mandrela, Philipp Jährling
 */
public class Pic {

	private String name;
	private String category;
	
	private double[] features;

	private Image imageData;
	private int width;
	private int height;
	
	
	public Pic(String name, String category, Image imageData) {
		super();
		this.name = name;
		this.category = category;
		this.imageData = imageData;
		this.width = (int)imageData.getWidth();
		this.height = (int)imageData.getHeight();
		this.features = new double[0];
	}
	
	public double[] getFeatures() {
		return features;
	}

	public void setFeatures(double[] features) {
		this.features = features;
	}

	public String getCategoryName() {
		return category;
	}
	
	public String getName() {
		return name;
	}
	
	public Image getImageData() {
		return imageData;
	}
	
	public int[] getPixels() {
		int[] pixels = new int[width * height];
		imageData.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), pixels, 0, width);
		return pixels;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
