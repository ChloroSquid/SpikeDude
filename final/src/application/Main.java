package application;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


/**
 * 
 * @author Imani Braxton
 *
 */
public class Main extends Application {
		
	@Override
	/**
	 * Start function that sets and loads the Home Page as well as starts the music
	 */
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle("Spike Dude");
		
		Music music = new Music("sand.mp3");
		music.playMusic();
		
		Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
		Scene scene = new Scene(root,800,800);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Launches the application
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
