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

public class MainController {
	
	@FXML
	void handleSettings(ActionEvent actionevent) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
		Parent root = (Parent) loader.load();
		Stage stage= new Stage();
		stage.setTitle("Spike Dude");
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	@FXML
	void handleExit(ActionEvent actionevent) {
		System.exit(0);
	}
	
	@FXML
	void handlePlay(ActionEvent actionevent) {
		Stage stage = new Stage();
		GameViewManager actualGame = new GameViewManager();
		actualGame.createNewGame(stage);
	}
}

	
