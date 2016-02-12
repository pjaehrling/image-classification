package de.htw.cv.mj.controller;

import java.io.File;
import java.util.List;

import de.htw.cv.mj.ImageManager;
import de.htw.cv.mj.accuracy.ConfusionMatrix;
import de.htw.cv.mj.accuracy.MeanRank;
import de.htw.cv.mj.accuracy.CorrectRate;
import de.htw.cv.mj.classificator.Classifier;
import de.htw.cv.mj.classificator.EuclideanOneVsAll;
import de.htw.cv.mj.classificator.EuclideanLinearQuantified;
import de.htw.cv.mj.classificator.KNearestNeighbors;
import de.htw.cv.mj.featureextractor.ColorHistogram;
import de.htw.cv.mj.featureextractor.FeatureExtractor;
import de.htw.cv.mj.featureextractor.HarrisGradientHistogram;
import de.htw.cv.mj.featureextractor.HarrisMeanColor;
import de.htw.cv.mj.featureextractor.HarrisColorHistogram;
import de.htw.cv.mj.featureextractor.HarrisColorGradientHistogram;
import de.htw.cv.mj.featureextractor.MeanColor;
import de.htw.cv.mj.model.Pic;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;

/**
 * @author Marie Mandrela, Philipp Jährling
 */
public class ImageClassificationUIController {
	
	private String[] imageSetChoices = new String[]{"Easy (250 Images)", "Hard (720 Images)"};
	private String[] imageSetPathes = new String[]{"images/easy", "images/hard"};
	private String[] classMeasureChoices = new String[]{"Eucledian (1vsAll)", "Eucledian (Linear Quantified)", "3-Nearest Neighbors (1vsAll)", "5-Nearest Neighbors (1vsAll)"};
	private String[] featureTypeChoices = new String[]{
				"Mean Color (All)", "Color Histogram (All)", 
				"Mean Color (IP)", "Color Histogram (IP)",
				"Gradient Histogram (IP)", "Color + Gradient Histogram (IP)"
			};
	
	private Integer[] ipWindowSizeChoices = new Integer[]{1, 3, 5, 7};
	private Integer[] gradientHistogramBinChoices = new Integer[]{6, 12, 36, 72, 120, 180};
	private Integer[] colorHistogramBinChoices = new Integer[]{2, 4, 8, 16};
	
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
	ComboBox<Integer> ipWindowSizeComboBox;
	@FXML
	ComboBox<Integer> colorHistoBinsComboBox;
	@FXML
	ComboBox<Integer> gradientHistoBinsComboBox;
	
	@FXML
	CheckBox minMaxNormCheckBox;
	
	@FXML
	Button trainButton;
	@FXML
	Button calculateButton;
	@FXML
	Button accuracyMeasurementButton;
	
	@FXML
	ImageView testImageView;
	@FXML
	ImageView accuracyImageView;
	@FXML
	GridPane categoryImagesGrid;
	
	@FXML
	Label statusLabel;
	@FXML
	Label testImageLabel;
	@FXML
	Label categoryLabel;
	@FXML
	Label averageRankLabel;
	@FXML
	Label overallCorrectLabel;
	
	/**
	 * Initializes the GUI and Init Manager.
	 */
	@FXML
	public void initialize() {
		imageManager = new ImageManager();
		
		File defaultImageFile = new File(defaultImagePath);
		defaultImage = new Image(defaultImageFile.toURI().toString());
		
		initTestImageComboBox();
		initImageSetComboBox();
		initFeatureTypeComboBox();
		initClassMeasureComboBox();
		
		initIPWindowSizeComboBox();
		initColorHistogramBinsComboBox();
		initGradientHistogramBinsComboBox();
		initMinMaxNormCheckBox();
		
		initTrainButton();
		initCalculateButton();
		initAccuracyMeasurementButton();
	}
	
	@FXML
	public void runMethod(ActionEvent event) {
		System.out.println("Run!");
	}
	
	/* ***************************************************************************************************
	 * Helper
	 * ***************************************************************************************************/	
	
	/**
	 * Fills the test image ComboCox with all image names
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
	 * Set all inputs to match if the classifier is trained for the current settings
	 * 
	 * @param isTrained
	 */
	private void setTrained(boolean isTrained) {
		trainButton.setDisable(isTrained);
		calculateButton.setDisable(!isTrained);
		accuracyMeasurementButton.setDisable(!isTrained);
		if (!isTrained) {
			averageRankLabel.setText("");
			overallCorrectLabel.setText("");
			accuracyImageView.setImage(null);
			clearCategoryImages();
		}
	}
	
	/**
	 * Create an ImageView component for the category image grid
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
	 * Show all images the have the calculated category in an image grid
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
	 * Clear the images for the calculated class
	 */
	private void clearCategoryImages() {
		categoryImageViews = new ImageView[0];
		categoryImagesGrid.getChildren().remove(0, categoryImagesGrid.getChildren().size());
	}
	
	/**
	 * Set the current Feature Extractor object to fit the current settings
	 */
	private void setFeatureExtractor() {
		setTrained(false);
		
		int colorBinCount = 0;
		int ipWindowSize = 0;
		int gradientBinCount = 0;
		boolean useMinMaxNormalisation = false;
		
		int featureTypeIndex = featureTypeComboBox.getSelectionModel().selectedIndexProperty().get();

    	switch (featureTypeIndex) {
    		case 0: // --> Mean Color All
    			extractor = new MeanColor();
    			ipWindowSizeComboBox.setDisable(true);
    			colorHistoBinsComboBox.setDisable(true);
    			gradientHistoBinsComboBox.setDisable(true);
    			minMaxNormCheckBox.setDisable(true);
    			break;
    		case 1: // --> Color Histogram All
    			useMinMaxNormalisation = minMaxNormCheckBox.isSelected();
    			colorBinCount = (int)(colorHistoBinsComboBox.getSelectionModel().selectedItemProperty().get());
    			extractor = new ColorHistogram(colorBinCount, useMinMaxNormalisation);
    			break;
    		case 2: // --> Mean Color IP
    			ipWindowSize = (int)(ipWindowSizeComboBox.getSelectionModel().selectedItemProperty().get());
    			extractor = new HarrisMeanColor(ipWindowSize);
    			break;
    		case 3: // --> Color Histogram IP
    			useMinMaxNormalisation = minMaxNormCheckBox.isSelected();
    			ipWindowSize = (int)(ipWindowSizeComboBox.getSelectionModel().selectedItemProperty().get());
    			colorBinCount = (int)(colorHistoBinsComboBox.getSelectionModel().selectedItemProperty().get());
    			extractor = new HarrisColorHistogram(colorBinCount, ipWindowSize, useMinMaxNormalisation);
    			break;
    		case 4: // --> Gradient Histogram IP
    			useMinMaxNormalisation = minMaxNormCheckBox.isSelected();
    			ipWindowSize = (int)(ipWindowSizeComboBox.getSelectionModel().selectedItemProperty().get());
    			gradientBinCount = (int)(gradientHistoBinsComboBox.getSelectionModel().selectedItemProperty().get());
    			extractor = new HarrisGradientHistogram(gradientBinCount, ipWindowSize, useMinMaxNormalisation);
    			break;
    		case 5: // --> Color + Gradient Histogram IP
    			useMinMaxNormalisation = minMaxNormCheckBox.isSelected();
    			ipWindowSize = (int)(ipWindowSizeComboBox.getSelectionModel().selectedItemProperty().get());
    			colorBinCount = (int)(colorHistoBinsComboBox.getSelectionModel().selectedItemProperty().get());
    			gradientBinCount = (int)(gradientHistoBinsComboBox.getSelectionModel().selectedItemProperty().get());
    			extractor = new HarrisColorGradientHistogram(colorBinCount, gradientBinCount, ipWindowSize, useMinMaxNormalisation);
    			break;
    	}
	}
	
	/**
	 * Set the confusion Matrix Image
	 * @param matrix
	 * @param categoryNames
	 */
	private void setConfusionMatrixImage(double[][] matrix, List<String> categoryNames) {
		int size = categoryNames.size();
		int[] pixels = new int[size*size];
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int saturation = (int)(255 * matrix[i][j]);
				pixels[j * size + i] = (0xFF << 24) | (saturation << 16) | (saturation << 8) | saturation;
			}
		}
		
		WritableImage wr = new WritableImage(size, size);
		PixelWriter pw = wr.getPixelWriter();
		pw.setPixels(0, 0, size, size, PixelFormat.getIntArgbInstance(), pixels, 0, size);
		
		accuracyImageView.setImage(wr);
		accuracyImageView.setFitWidth(size * 2);
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
				setFeatureExtractor();
	        	switch ((int) newIndex) {
		        	case 0: // --> Mean Color All
		    			ipWindowSizeComboBox.setDisable(true);
		    			colorHistoBinsComboBox.setDisable(true);
		    			gradientHistoBinsComboBox.setDisable(true);
		    			minMaxNormCheckBox.setDisable(true);
		    			break;
		    		case 1: // --> Color Histogram All
		    			ipWindowSizeComboBox.setDisable(true);
		    			colorHistoBinsComboBox.setDisable(false);
		    			gradientHistoBinsComboBox.setDisable(true);
		    			minMaxNormCheckBox.setDisable(false);
		    			break;
		    		case 2: // --> Mean Color IP
		    			ipWindowSizeComboBox.setDisable(false);
		    			colorHistoBinsComboBox.setDisable(true);
		    			gradientHistoBinsComboBox.setDisable(true);
		    			minMaxNormCheckBox.setDisable(true);
		    			break;
		    		case 3: // --> Color Histogram IP
		    			ipWindowSizeComboBox.setDisable(false);
		    			colorHistoBinsComboBox.setDisable(false);
		    			gradientHistoBinsComboBox.setDisable(true);
		    			minMaxNormCheckBox.setDisable(false);
		    			break;
		    		case 4: // --> Gradient Histogram IP
		    			ipWindowSizeComboBox.setDisable(false);
		    			colorHistoBinsComboBox.setDisable(true);
		    			gradientHistoBinsComboBox.setDisable(false);
		    			minMaxNormCheckBox.setDisable(false);
		    			break;
		    		case 5: // --> Color + Gradient Histogram IP
		    			ipWindowSizeComboBox.setDisable(false);
		    			colorHistoBinsComboBox.setDisable(false);
		    			gradientHistoBinsComboBox.setDisable(false);
		    			minMaxNormCheckBox.setDisable(false);
		    			break;
	        	}
			}
        });
		featureTypeComboBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Class Measure Choice
	 */
	private void initClassMeasureComboBox() {
		ObservableList<String> featureTypeChoiceList = FXCollections.observableArrayList(classMeasureChoices);
		classMeasureComboBox.setItems(featureTypeChoiceList);
        
        // Add event listener
		classMeasureComboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldIndex, newIndex) -> {
			if (oldIndex != newIndex) {
				switch ((int) newIndex) {
		    		case 0:
		    			classifier = new EuclideanOneVsAll();
		    			break;
		    		case 1:
		    			classifier = new EuclideanLinearQuantified();
		    			break;
		    		case 2:
		    			classifier = new KNearestNeighbors(3);
		    			break;
		    		case 3:
		    			classifier = new KNearestNeighbors(5);
		    			break;
				}
			}
        });
		classMeasureComboBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Interest Point Window Size
	 */
	private void initIPWindowSizeComboBox() {
		ObservableList<Integer> ipWindowSizeChoiceList = FXCollections.observableArrayList(ipWindowSizeChoices);
		ipWindowSizeComboBox.setItems(ipWindowSizeChoiceList);
		ipWindowSizeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != oldValue) setFeatureExtractor();
        });
		ipWindowSizeComboBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Color Histogram Bin Count
	 */
	private void initColorHistogramBinsComboBox() {
		ObservableList<Integer> choiceList = FXCollections.observableArrayList(colorHistogramBinChoices);
		colorHistoBinsComboBox.setItems(choiceList);
		colorHistoBinsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != oldValue) setFeatureExtractor();
        });
		colorHistoBinsComboBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Gradient Histogram Bin Count
	 */
	private void initGradientHistogramBinsComboBox() {
		ObservableList<Integer> choiceList = FXCollections.observableArrayList(gradientHistogramBinChoices);
		gradientHistoBinsComboBox.setItems(choiceList);
		gradientHistoBinsComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != oldValue) setFeatureExtractor();
        });
		gradientHistoBinsComboBox.getSelectionModel().selectFirst();
	}
	
	/**
	 * Min/Max Normalisation Checkbox
	 */
	private void initMinMaxNormCheckBox() {
		minMaxNormCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != oldValue) setFeatureExtractor();
	    });
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
	 * Accuracy Measurement
	 */
	private void initAccuracyMeasurementButton() {
		accuracyMeasurementButton.setOnAction(new EventHandler<ActionEvent>() {
		    @Override 
		    public void handle(ActionEvent e) {
		    	double overallcorrectRate = CorrectRate.calculate(classifier, imageManager);
		    	overallCorrectLabel.setText(String.valueOf(overallcorrectRate));
		    	
		    	double meanRank = MeanRank.calculate(classifier, imageManager);
		    	averageRankLabel.setText(String.valueOf(meanRank));
		    	
		    	List<String> categoryNames = imageManager.getCategoryNames();
		    	double[][] matrix = ConfusionMatrix.calculate(classifier, imageManager);
		    	setConfusionMatrixImage(matrix, categoryNames);
		    }
		});
	}

}