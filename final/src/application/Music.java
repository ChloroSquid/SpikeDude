package application;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;



public class Music {
	
	String file;
	public static MediaPlayer mediaPlayer;
	
	public Music(String file) {
		this.file = file;
		Media sound = new Media(new File(this.file).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
	}
	
	
	
	public void playMusic(String filename) {

		mediaPlayer.play();
	}
	
	public void pause(MediaPlayer media) {
		if(media.getStatus().equals(Status.PLAYING))
			media.pause();
	}
	
	public void resume(MediaPlayer media) {
		if(media.getStatus().equals(Status.PAUSED))
			media.play();
	}
	
	public String getName() {
		return this.file;
	}
	

}
