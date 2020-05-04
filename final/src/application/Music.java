package application;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

/**
 * 
 * @author Imani Braxton
 *
 */
public class Music {
	
	String file;
	public static MediaPlayer mediaPlayer;
	
	
	/**
	 * Creates a music object that that sets the static media player to the song that it is passed
	 * @param file
	 */
	public Music(String file) {
		this.file = file;
		Media sound = new Media(new File(this.file).toURI().toString());
		mediaPlayer = new MediaPlayer(sound);
	}
	
	/**
	 * Plays the selected song on a continuous loop
	 */
	public void playMusic() {
		mediaPlayer.setOnEndOfMedia(new Runnable() {
			public void run() {
				mediaPlayer.seek(Duration.ZERO);
			}
		});
		mediaPlayer.play();
	}
	
	/**
	 * Pauses the music
	 * @param media
	 */
	public void pause(MediaPlayer media) {
		if(media.getStatus().equals(Status.PLAYING))
			media.pause();
	}
	
	/**
	 * Resumes the song
	 * @param media
	 */
	public void resume(MediaPlayer media) {
		if(media.getStatus().equals(Status.PAUSED))
			media.play();
	}
	
	/**
	 * Returns the file name as a string
	 * @return
	 */
	public String getName() {
		return this.file;
	}
	
	/**
	 * Sets the file to a new file
	 * @param filename
	 */
	public void setName(String filename) {
		this.file = filename;
	}
	

}
