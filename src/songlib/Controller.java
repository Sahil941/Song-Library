//Malik Ameer(mma174) & Sahil Kumbhani(srk112)

package songlib;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import songlib.Song;

public class Controller {
	
	//FXML declarations for main stage
	@FXML Button Edit;
	@FXML Button Add;
	@FXML Button Delete;
	@FXML TextField Title;
	@FXML TextField Artist;
	@FXML ListView<Song> listView;
	
	SongDbReader dataBase = new SongDbReader();
	private ObservableList<Song> obsList;

	public void start(Stage mainStage) throws IOException{
		//create obsList from ArrList
		obsList = FXCollections.observableArrayList();
		if(dataBase.input.hasNextLine()){
			while(dataBase.input.hasNextLine()){
				obsList.add(dataBase.nextSong());
			}
		}
		
		listView.setItems(obsList);
		obsList.sort(null);
		listView.getSelectionModel().select(0);
		this.showItemDetails();

		listView
        .getSelectionModel()
        .selectedIndexProperty()
        .addListener(
           (obs, oldVal, newVal) ->
               showItemDetails());
	}
	
	public void upload() throws IOException{
		dataBase.databaseWriter(obsList.toArray());
		dataBase.databaseCloser();
	}


	//Adds song to library when "Add" button is hit
	public void addSong(ActionEvent e){
		Button b = (Button)e.getSource();
		if(b == Add){
			
			Dialog<Pair<String, String>> dialog = new Dialog<>();
		    dialog.setTitle("Add A New Song");

		    // Set the button types.
		    ButtonType addbutton = new ButtonType("OK", ButtonData.OK_DONE);
		    dialog.getDialogPane().getButtonTypes().addAll(addbutton, ButtonType.CANCEL);

		    GridPane gridPane = new GridPane();
		    gridPane.setHgap(10);
		    gridPane.setVgap(20);
		    gridPane.setPadding(new Insets(30, 10, 10, 10));

		    TextField newTitle = new TextField();
		    newTitle.setPromptText("Enter new song's title");
		    TextField newArtist = new TextField();
		    newArtist.setPromptText("Enter new song's artist");
		    
		    gridPane.add(new Label("Title"), 0, 1);
		    gridPane.add(newTitle, 1, 1);
		    gridPane.add(new Label("Artist"), 2, 1);
		    gridPane.add(newArtist, 3, 1);

		    dialog.getDialogPane().setContent(gridPane);

		    // Convert the result to a title-artist pair when the login button is clicked.
		    dialog.setResultConverter(dialogButton -> {
		        if (dialogButton == addbutton) {
		            return new Pair<>(newTitle.getText(), newArtist.getText());
		        }
		        return null;
		    });
		    
		    Optional<Pair<String, String>> result = dialog.showAndWait();
		    
		    Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(null);
			alert.setTitle("Confirm Song Addition");
			alert.setHeaderText("You are adding: " + result.get().getKey() + " by " + result.get().getValue() + "\n Please confirm.");
			alert.showAndWait();
			
			//Check for confirmation result
			if(alert.getResult() == ButtonType.OK) {
				
				//Send pair information to the main display and update song object.
			    result.ifPresent(pair -> {
			    	Song newSong = new Song(pair.getKey(), pair.getValue());
			    	if(obsList.contains(newSong)){
			    		Alert duplicate = new Alert(AlertType.ERROR);
			    		duplicate.initOwner(null);
			    		duplicate.setTitle("ERROR: Song Already Exists");
			    		duplicate.setHeaderText("The song you are trying to add already exists in this library!");
			    		duplicate.showAndWait();
			    		return;
			    	}
			    	obsList.add(newSong);
			    	listView.setItems(obsList);
			    	obsList.sort(null);
			    	listView.getSelectionModel().select(newSong);
			        this.showItemDetails();
			    });
			}
		}
	}
	
	
	/* 
	 * Delete currently selected song when "Delete" button is hit
	 *	and sets previous song as currently selected song
	 */ 
	public void deleteSong(ActionEvent e){
		Button b = (Button)e.getSource();
		if(b == Delete){
			
			//Requests confirmation to delete
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initOwner(null);
			alert.setTitle("Confirm Delete");
			alert.setHeaderText("Are you sure you want to delete this song?");
			alert.showAndWait();
			
			//check for confirmation result
			if(alert.getResult() == ButtonType.OK) {
				obsList.remove(listView.getSelectionModel().getSelectedItem());
				
				//show new item
				if(!obsList.isEmpty()) {
					this.showItemDetails();
				}
				//else clear all fields if no songs remain
				else {
					Title.clear();
					Artist.clear();
					//Album.clear();
				}
			}
		}
	}
	
	
	public void editSong(ActionEvent e) throws Exception{
		Button b = (Button)e.getSource();
		
		if(b == Edit){
			 Dialog<Pair<String, String>> dialog = new Dialog<>();
			    dialog.setTitle("Song Editor");

			    // Set the button types.
			    ButtonType editbutton = new ButtonType("OK", ButtonData.OK_DONE);
			    dialog.getDialogPane().getButtonTypes().addAll(editbutton, ButtonType.CANCEL);

			    GridPane gridPane = new GridPane();
			    gridPane.setHgap(10);
			    gridPane.setVgap(20);
			    gridPane.setPadding(new Insets(30, 10, 10, 10));

			    TextField newTitle = new TextField();
			    newTitle.setText(listView.getSelectionModel().getSelectedItem().title);
			    TextField newArtist = new TextField();
			    newArtist.setText(listView.getSelectionModel().getSelectedItem().artist);
			    
			    gridPane.add(new Label("New Title"), 0, 1);
			    gridPane.add(newTitle, 1, 1);
			    gridPane.add(new Label("New Artist"), 2, 1);
			    gridPane.add(newArtist, 3, 1);

			    dialog.getDialogPane().setContent(gridPane);

			    // Convert the result to a title-artist pair when the login button is clicked.
			    dialog.setResultConverter(dialogButton -> {
			        if (dialogButton == editbutton) {
			            return new Pair<>(newTitle.getText(), newArtist.getText());
			        }
			        return null;
			    });

			    Optional<Pair<String, String>> result = dialog.showAndWait();
			    
			  //Requests confirmation to edit
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.initOwner(null);
				alert.setTitle("Confirm Edit");
				alert.setHeaderText("Are you sure you want to edit this song?");
				alert.showAndWait();
				
				//Check for confirmation result
				if(alert.getResult() == ButtonType.OK) {
					
					//Send pair information to the main display and update song object.
				    result.ifPresent(pair -> {
				    	Song newSong = new Song(pair.getKey(), pair.getValue());
				    	if(obsList.contains(newSong)){
				    		Alert duplicate = new Alert(AlertType.ERROR);
				    		duplicate.initOwner(null);
				    		duplicate.setTitle("ERROR: Song Already Exists");
				    		duplicate.setHeaderText("The song you are trying to add already exists in this library!");
				    		duplicate.showAndWait();
				    		return;
				    	}
				        listView.getSelectionModel().getSelectedItem().editTitle(pair.getKey());
				        listView.getSelectionModel().getSelectedItem().editArtist(pair.getValue());
				        obsList.set(listView.getSelectionModel().getSelectedIndex(), 
				        		listView.getSelectionModel().getSelectedItem());
				        obsList.sort(null);
				        this.showItemDetails();
				    });
				}
		}
	}
	
	
	/*
	 * Show the details of each song object in the Text Field
	 */
	public void showItemDetails(){
		if(!this.obsList.isEmpty() && listView.getSelectionModel().getSelectedItem() != null) {
			Title.setText(listView.getSelectionModel().getSelectedItem().title);
			Artist.setText(listView.getSelectionModel().getSelectedItem().artist);
		}
		//this.obsList.sort(null);
	}
}