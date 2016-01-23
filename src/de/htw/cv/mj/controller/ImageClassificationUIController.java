package de.htw.cv.mj.controller;

import de.htw.cv.featureextraction.FeatureExtractor;
import de.htw.cv.mj.ImageManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class ImageClassificationUIController {
	
	private String[] imageSetChoices = new String[]{"Easy (250 Images)", "Hard (720 Images)"};
	private String[] imageSetPathes	 = new String[]{"images/easy", "images/hard"};
	private String[] featureTypeChoices = new String[]{"Mean Color"};
	
	private enum FeatureExtraction { MeanColor };
	private enum ClassMeasure { EucledianDistance };
	
	private ImageManager imageManager;
	
	@FXML
	ChoiceBox<String> imageSetChoiceBox;
	@FXML
	ChoiceBox<String> featureTypeChoiceBox;
	
	@FXML
	public void initialize() {
		imageManager = new ImageManager();
		
		initImageSetChoiceBox();
		initFeatureTypeChoiceBox();
        
		System.out.println("Initialized!");
	}
	
	@FXML
	public void runMethod(ActionEvent event) {
		System.out.println("Run!");
	}
	
	
	/* ***************************************************************************************************
	 * component initialize methods
	 * ***************************************************************************************************/
	private void initImageSetChoiceBox() {
		ObservableList<String> imageSetChoiceList = FXCollections.observableArrayList(imageSetChoices);
        imageSetChoiceBox.setItems(imageSetChoiceList);
        
        // Add event listener
        imageSetChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
        	imageManager.loadImages( imageSetPathes[(int) newValue] );
        });
        
        // Select first item by default
        imageSetChoiceBox.getSelectionModel().selectFirst();
	}
	
	private void initFeatureTypeChoiceBox() {
		ObservableList<String> featureTypeChoiceList = FXCollections.observableArrayList(featureTypeChoices);
		featureTypeChoiceBox.setItems(featureTypeChoiceList);
        
        // Add event listener
		featureTypeChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
        	// TODO
        });
        
        // Select first item by default
		featureTypeChoiceBox.getSelectionModel().selectFirst();
	}
	    
}