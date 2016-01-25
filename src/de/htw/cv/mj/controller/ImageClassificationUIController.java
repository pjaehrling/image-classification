package de.htw.cv.mj.controller;

import java.io.File;
import java.util.List;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 * @author Marie Manderla, Philipp Jährling
 */
public class ImageClassificationUIController {
	
	private String[] imageSetChoices = new String[]{"Easy (250 Images)", "Hard (720 Images)"};
	private String[] imageSetPathes = new String[]{"images/easy", "images/hard"};
	private String[] featureTypeChoices = new String[]{"Mean Color"};
	private String[] classMeasureChoices = new String[]{"Eucledian (1vsAll)"};
	
	private String defaultImagePath = "images/default.jpg";
	private Image defaultImage;
	private ImageView[] categoryImageViews;
	
	private ImageManager imageManager;
	private FeatureExtractor extractor;
	private Classifier classifier;
	
	@FXML
	ComboBox<String> imageSetComboBox;
	@FXML
	ComboBox<String> testImageComboBox;
	@FXML
	ComboBox<String> featureTypeComboBox;
	@FXML
	ComboBox<String> classMeasureComboBox;
	
	@FXML
	Button trainButton;
	@FXML
	Button calculateButton;
	@FXML
	Button calculateAllButton;
	
	@FXML
	ImageView testImageView;
	@FXML
	GridPane categoryImagesGrid;
	
	@FXML
	Label statusLabel;
	@FXML
	Label testImageLabel;
	@FXML
	Label categoryLabel;
	
	
	@FXML
	public void initialize() {
		imageManager = new ImageManager();
		
		File defaultImageFile = new File(defaultImagePath);
		defaultImage = new Image(defaultImageFile.toURI().toString());
		
		initTestImageComboBox();
		initImageSetComboBox();
		initFeatureTypeComboBox();
		initClassMeasureComboBox();
		
		initTrainButton();
		initCalculateButton();
		initCalculateAlButton();
	}
	
	@FXML
	public void runMethod(ActionEvent event) {
		System.out.println("Run!");
	}
	
	/* ***************************************************************************************************
	 * Helper
	 * ***************************************************************************************************/	
	/**
	 * 
	 */
	private void fillTestImageChoices() {
		// Fill
		List<String> nameList = imageManager.getImageNames();
		ObservableList<String> imageNameChoiceList = FXCollections.observableArrayList(nameList);
        testImageComboBox.setItems(imageNameChoiceList);
        
        // Select first item by default
		testImageComboBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * 
	 * @param isTrained
	 */
	private void setTrained(boolean isTrained) {
		trainButton.setDisable(isTrained);
		calculateButton.setDisable(!isTrained);
		calculateAllButton.setDisable(!isTrained);
	}
	
	/**
	 * 
	 * @param image
	 * @return
	 */
	private ImageView createCategoryImageView(Image image) {
		ImageView iv = new ImageView();
        iv.setImage(image);
        iv.setFitWidth(45);
        iv.setFitHeight(45);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        return iv;
	}
	
	/**
	 * 
	 * @param category
	 */
	private void showCategoryImages(String categoryName) {
		List<Pic> images = imageManager.getImagesForCategoryName(categoryName);
		categoryImageViews = new ImageView[images.size()];
		
		int x = 0;
		int y = 0;
		for (int i = 0; i < images.size(); i++) {
			x = i % 10;
			y = i / 10;
			categoryImageViews[i] = createCategoryImageView(images.get(i).getImageData());
			categoryImagesGrid.add(categoryImageViews[i], x, y);
		}
	}
	
	/**
	 * 
	 */
	private void clearCategoryImages() {
		categoryImageViews = new ImageView[0];
		categoryImagesGrid.getChildren().remove(0, categoryImagesGrid.getChildren().size());
	}
	
	/* ***************************************************************************************************
	 * Component initialize methods
	 * ***************************************************************************************************/
	/**
	 * ImageSet Choice
	 */
	private void initImageSetComboBox() {
		ObservableList<String> imageSetChoiceList = FXCollections.observableArrayList(imageSetChoices);
        imageSetComboBox.setItems(imageSetChoiceList);
        
        // Add event listener
        imageSetComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldIndex, newIndex) -> {
        	setTrained(false);
        	imageManager.loadImages( imageSetPathes[(int)newIndex] );
        	fillTestImageChoices();
        	statusLabel.setText("Images and Testimage loaded (not trained)");
        });
        imageSetComboBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * ImageSet Choice
	 */
	private void initTestImageComboBox() {
		// Add event listener
		testImageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (oldValue != newValue) {
				setTrained(false);
				imageManager.setTestImageByName(newValue);
				categoryLabel.setText("Not classified");
				clearCategoryImages();
				
				Pic testImage = imageManager.getTestImage();
		    	if (testImage != null) {
		    		testImageView.setImage(testImage.getImageData());
		    		testImageLabel.setText(testImage.getName());
		    	} else {
		    		testImageView.setImage(defaultImage);
		    		testImageLabel.setText("... not available");
		    	}
			}
        });
	}
	
	/**
	 * Feature Choice
	 */
	private void initFeatureTypeComboBox() {
		ObservableList<String> featureTypeChoiceList = FXCollections.observableArrayList(featureTypeChoices);
		featureTypeComboBox.setItems(featureTypeChoiceList);
        
        // Add event listener
		featureTypeComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldIndex, newIndex) -> {
			if (oldIndex != newIndex) {
				setTrained(false);
	        	switch ((int) newIndex) {
	        		case 0:
	        			extractor = new MeanColor();
	        			break;
	        	}
			}
        });
		featureTypeComboBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Measure Choice
	 */
	private void initClassMeasureComboBox() {
		ObservableList<String> featureTypeChoiceList = FXCollections.observableArrayList(classMeasureChoices);
		classMeasureComboBox.setItems(featureTypeChoiceList);
        
        // Add event listener
		classMeasureComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldIndex, newIndex) -> {
			if (oldIndex != newIndex) {
				setTrained(false);
				switch ((int) newIndex) {
		    		case 0:
		    			classifier = new EuclideanOneVsAll();    			
		    			break;
				}
			}
        });
		classMeasureComboBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Train Button
	 */
	private void initTrainButton() {
		trainButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
		    public void handle(ActionEvent e) {
		        imageManager.trainImages(extractor);
		        String feature = featureTypeChoices[featureTypeComboBox.getSelectionModel().getSelectedIndex()];
		        setTrained(true);
		        statusLabel.setText("Trained using " + feature);
		    }
		});
	}
	
	/**
	 * Calculate Button
	 */
	private void initCalculateButton() {
		calculateButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
		    public void handle(ActionEvent e) {
		    	String categoryName = classifier.classify(imageManager.getTestImage(), imageManager);		        
		    	String classifierLabel = classMeasureChoices[classMeasureComboBox.getSelectionModel().getSelectedIndex()];
		    	categoryLabel.setText(categoryName);
		    	showCategoryImages(categoryName);
		    	statusLabel.setText("Calculated a category using " + classifierLabel);
		    }
		});
	}
	
	/**
	 * Calculate All Button
	 */
	private void initCalculateAlButton() {
		calculateAllButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
		    public void handle(ActionEvent e) {
		    	int rank = classifier.classifyRank(imageManager.getTestImage(), imageManager);		        
		    	System.out.println(rank);
		    }
		});
	}

}