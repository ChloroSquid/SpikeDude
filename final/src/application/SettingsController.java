package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class SettingsController {
	
	
	@FXML
	Button quit,pause,play;
	
	@FXML
	void handleQuit(ActionEvent actionevent) {
		Stage stage = (Stage) quit.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	void handleSound(ActionEvent event) {
		
		

	}

}
