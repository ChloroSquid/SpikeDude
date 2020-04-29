package application;
	
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle("Spike Dude");
		
		String musicFile = "sand.mp3";
		Media sound = new Media(new File(musicFile).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.setVolume(.3);
		mediaPlayer.play();
		
		Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
		Scene scene = new Scene(root,800,800);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
