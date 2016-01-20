package de.htw.cv.mj;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent ui = new FXMLLoader(getClass().getResource("view/ImageClassificationUIView.fxml")).load();
		Scene scene = new Scene(ui);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Image Classification - M. Mandrela, P. Jährling");
		primaryStage.show();
	}
	
	@Override
	public void stop(){
	    System.out.println("Closing!");
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
