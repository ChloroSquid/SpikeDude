package application;


import java.io.File;

import application.Music;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;

public class SettingsController {
	
	MediaPlayer mediaPlayer;


	
	
	@FXML
	Button quit,pause,play;
	
	@FXML
	void handleQuit(ActionEvent actionevent) {
		Stage stage = (Stage) quit.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	
	void soundOn(ActionEvent event) {
		if(Music.mediaPlayer.getStatus().equals(Status.PAUSED))
			Music.mediaPlayer.play();	
	}
	
	@FXML
	void soundOff(ActionEvent event) {
		if(Music.mediaPlayer.getStatus().equals(Status.PLAYING))
			Music.mediaPlayer.pause();	
	}
	
	
	//boolean playing = mediaPlayer.getStatus().equals(Status.PLAYING);

}
