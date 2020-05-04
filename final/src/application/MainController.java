package application;

import java.io.IOException;
import application.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.GameViewManager;

/**
 * 
 * @author Imani Braxton
 *
 */
public class MainController {
	
	@FXML
	/**
	 * Opens Settings.fxml when the Settings button is pressed
	 * @param actionevent
	 * @throws IOException
	 */
	void handleSettings(ActionEvent actionevent) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
		Parent root = (Parent) loader.load();
		Stage stage= new Stage();
		stage.setTitle("Spike Dude");
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	@FXML
	/**
	 * Closes the game if the exit button is clicked
	 * @param actionevent
	 */
	void handleExit(ActionEvent actionevent) {
		System.exit(0);
	}
	
	@FXML
	/**
	 * Opens the game when the play button is clicked
	 * @param actionevent
	 */
	void handlePlay(ActionEvent actionevent) {
		Stage stage = new Stage();
		GameViewManager actualGame = new GameViewManager();
		actualGame.createNewGame(stage);
	}
	
}

	
