/**
 * @author Marie Manderla, Philipp Jährling
 * @date 20.1.2016
 */
package de.htw.cv.mj.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ImageClassificationUIController {
	
	@FXML
	public void initialize() {
		System.out.println("Init!");
	}
	
	@FXML
	public void runMethod(ActionEvent event) {
		System.out.println("Run!");
	}
	    
}