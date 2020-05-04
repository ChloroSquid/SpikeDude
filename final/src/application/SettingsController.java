package application;



import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.stage.Stage;

/**
 * 
 * @author Imani Braxton
 *
 */
public class SettingsController {
	
	MediaPlayer mediaPlayer;
	
	@FXML
	Button quit,pause,play;
	
	@FXML
	/**
	 * Closes the setting page if exit button is pressed
	 * @param actionevent
	 */
	void handleQuit(ActionEvent actionevent) {
		Stage stage = (Stage) quit.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	/**
	 * Plays the song if it is currently paused
	 * @param event
	 */
	void soundOn(ActionEvent event) {
		if(Music.mediaPlayer.getStatus().equals(Status.PAUSED))
			Music.mediaPlayer.play();	
	}
	
	@FXML
	/**
	 * Pauses the song if it is currently playing
	 * @param event
	 */
	void soundOff(ActionEvent event) {
		if(Music.mediaPlayer.getStatus().equals(Status.PLAYING))
			Music.mediaPlayer.pause();	
	}
	

}
