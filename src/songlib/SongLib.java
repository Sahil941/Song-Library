//Malik Ameer(mma174) & Sahil Kumbhani(srk112)

package songlib;

import javafx.application.Application;
import javafx.stage.Stage;import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import songlib.Controller;
import javafx.scene.Scene;
import java.io.IOException;


public class SongLib extends Application{
	Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception{

	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/SongLibrary.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		this.controller = loader.getController();
		controller.start(primaryStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Song Library");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	@Override
	public void stop() throws IOException{
		this.controller.upload();
		System.exit(0);
	}

	public static void main(String[] args){
		launch(args);
	}
}