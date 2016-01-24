package de.htw.cv.mj.controller;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.classificator.Classifier;
import de.htw.cv.mj.classificator.EuclideanOneVsAll;
import de.htw.cv.mj.featureextractor.FeatureExtractor;
import de.htw.cv.mj.featureextractor.MeanColor;
import de.htw.cv.mj.model.Pic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class ImageClassificationUIController {
	
	private String[] imageSetChoices = new String[]{"Easy (250 Images)", "Hard (720 Images)"};
	private String[] imageSetPathes = new String[]{"images/easy", "images/hard"};
	private String[] imageSetTestEntries = new String[]{"vittel_1.jpg", "sunsets_22.jpg"};
	private String[] featureTypeChoices = new String[]{"Mean Color"};
	private String[] classMeasureChoices = new String[]{"Eucledian (1 vs. All)"};
	
	private ImageManager imageManager;
	private FeatureExtractor extractor;
	private Classifier classifier;
	
	@FXML
	ChoiceBox<String> imageSetChoiceBox;
	@FXML
	ChoiceBox<String> featureTypeChoiceBox;
	@FXML
	ChoiceBox<String> classMeasureChoiceBox;
	@FXML
	Button trainButton;
	@FXML
	Button calculateButton;
	@FXML
	ImageView testImageView;
	@FXML
	ImageView matchImageView;
	
	@FXML
	Label statusLabel;
	@FXML
	Label testImageLabel;
	@FXML
	Label categoryLabel;
	
	
	@FXML
	public void initialize() {
		imageManager = new ImageManager();
		
		initImageSetChoiceBox();
		initFeatureTypeChoiceBox();
		initClassMeasureChoiceBox();
		initTrainButton();
		initCalculateButton();
	}
	
	@FXML
	public void runMethod(ActionEvent event) {
		System.out.println("Run!");
	}
	
	/* ***************************************************************************************************
	 * Event handler
	 * ***************************************************************************************************/
	/**
	 * ImageSet Choice
	 */
	private void changeImageSetChoice(int setIndex) {
		// Load images
    	imageManager.loadImages( imageSetPathes[setIndex] );
    	
    	// Set the test image (imageManager & imageView)
    	Pic testImage = imageManager.getImage(imageSetTestEntries[setIndex]);
    	if (testImage != null) {
    		// test image was still part of imageCache (first load)
        	imageManager.removeImage( imageSetTestEntries[setIndex] ); // the test image shouldn't be part of the training-data
        	imageManager.setTestImage(testImage);
    	} else {
    		// if "null", the image is not in the list for the path anymore -> already set as test image
    		testImage = imageManager.getTestImage();
    	}
    	testImageView.setImage(testImage.getImageData());
    	
    	testImageLabel.setText(testImage.getName());
    	statusLabel.setText("Images and Testimage loaded (not trained)");
	}
	
	
	/* ***************************************************************************************************
	 * component initialize methods
	 * ***************************************************************************************************/
	/**
	 * ImageSet Choice
	 */
	private void initImageSetChoiceBox() {
		ObservableList<String> imageSetChoiceList = FXCollections.observableArrayList(imageSetChoices);
        imageSetChoiceBox.setItems(imageSetChoiceList);
        
        // Add event listener
        imageSetChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
        	changeImageSetChoice((int) newValue);
        });
        
        // Select first item by default
        imageSetChoiceBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Feature Choice
	 */
	private void initFeatureTypeChoiceBox() {
		ObservableList<String> featureTypeChoiceList = FXCollections.observableArrayList(featureTypeChoices);
		featureTypeChoiceBox.setItems(featureTypeChoiceList);
        
        // Add event listener
		featureTypeChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
        	switch ((int) newValue) {
        		case 0:
        			extractor = new MeanColor();
        			break;
        	}
        });
        
        // Select first item by default
		featureTypeChoiceBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Measure Choice
	 */
	private void initClassMeasureChoiceBox() {
		ObservableList<String> featureTypeChoiceList = FXCollections.observableArrayList(classMeasureChoices);
		classMeasureChoiceBox.setItems(featureTypeChoiceList);
        
        // Add event listener
		classMeasureChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
			switch ((int) newValue) {
    		case 0:
    			classifier = new EuclideanOneVsAll();    			
    			break;
    	}
        });
        
        // Select first item by default
		classMeasureChoiceBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Train Button
	 */
	private void initTrainButton() {
		trainButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    @Override public void handle(ActionEvent e) {
		        imageManager.trainImages(extractor);
		        String feature = featureTypeChoices[featureTypeChoiceBox.getSelectionModel().getSelectedIndex()];
		        statusLabel.setText("Trained using " + feature);
		    }
		});
	}
	
	/**
	 * Calculate Button
	 */
	private void initCalculateButton() {
		calculateButton.setOnAction(new EventHandler<ActionEvent>() {
			
		    @Override public void handle(ActionEvent e) {
		    	String categoryName = classifier.classify(imageManager.getTestImage(), imageManager);		        
		    	String classifierLabel = classMeasureChoices[classMeasureChoiceBox.getSelectionModel().getSelectedIndex()];
		    	categoryLabel.setText(categoryName);
		    	statusLabel.setText("Calculated a category using " + classifierLabel);
		    }
		});
	}

}