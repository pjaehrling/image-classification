package de.htw.cv.mj.model;

import javafx.scene.image.Image;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class Pic {

	private String name;
	private String category;
	
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
	}

	public String getCategory() {
		return category;
	}
	
	public String getName() {
		return name;
	}
	
	public Image getImageData() {
		return imageData;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
