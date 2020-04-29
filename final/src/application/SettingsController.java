package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

public class SettingsController {
	
	@FXML
	Label label1;
	
	@FXML
	ToggleButton but1;
	
	@FXML
	Button quit;
	
	@FXML
	void handleQuit(ActionEvent actionevent) {
		Stage stage = (Stage) quit.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	void handleSound(ActionEvent event) {
		label1.setText("Sound is Off");

	}

}
