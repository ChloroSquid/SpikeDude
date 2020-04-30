package view;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.BALL;

public class GameViewManager {
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	
	private static final int GAME_WIDTH = 800;
	private static final int GAME_HEIGHT = 800;
	
	private Stage menuStage;
	
	BALL ballEnum;
	ImageView ball;
	
	public GameViewManager() {
		initializeStage();
		createKeyListeners();
	}
	
	private void initializeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage();
		gameStage.setScene(gameScene);
		
	}
	
	private void createKeyListeners() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				//todo finish handling
				switch (event.getCode()){
					case RIGHT:
						break;
					case LEFT:
						break;
					case UP:
						break;
					default:
						break;
				}
			}
			
		});
		
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				
			}
			
		});
	}
	
	public void createNewGame(Stage menuStage) {
		this.menuStage = menuStage;
		//TODO add logic for ball
		this.menuStage.hide();
		createBall();
		gameStage.show();
		
	}
	
	private void createBall() {
		//TODO: make the ball do stuff
		Image image = new Image("/ball.png", 100,100,false,true);
		ball = new ImageView(image);
		ball.setLayoutX(GAME_WIDTH/2-50);
		ball.setLayoutY(GAME_HEIGHT/2-50);
		gamePane.getChildren().add(ball);
	}
}
