package view;

import java.util.ArrayList;
import java.util.List;

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
	ImageView spike;
	List<ImageView> spikeList; //variable number of spikes depending on resolution
	boolean keyPressed; //boolean in order to prevent held input from firing off multiple times
	
	public GameViewManager() {
		initializeStage();
		createKeyListeners();
	}
	
	private void initializeStage() {
		gamePane = new AnchorPane();
		gameScene = new Scene(gamePane, GAME_WIDTH, GAME_HEIGHT);
		gameStage = new Stage();
		gameStage.setScene(gameScene);
		keyPressed = false;	
	}
	
	private void createKeyListeners() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				//todo finish handling
				if(keyPressed) {
					return;
				}
				
				switch (event.getCode()){
					case D:
					case RIGHT:
						//TODO: replace printlns with things that move the ball
						System.out.println("RIGHT PRESSED");
						keyPressed = true;
						event.consume();
						break;
					case A:
					case LEFT:
						System.out.println("LEFT PRESSED");
						keyPressed = true;
						event.consume();
						break;
					case W:
					case UP:
						System.out.println("UP PRESSED");
						keyPressed = true;
						event.consume();
						break;
					default:
						event.consume();
						keyPressed = true;
						break;
				}
			}
			
		});
		
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				switch (event.getCode()){
				case D:
				case RIGHT:
					//TODO: remove the printlns after debugging
					System.out.println("RIGHT RELEASED");
					keyPressed = false;
					event.consume();
					break;
				case A:
				case LEFT:
					System.out.println("LEFT RELEASED");
					keyPressed = false;
					event.consume();
					break;
				case W:
				case UP:
					System.out.println("UP RELEASED");
					keyPressed = false;
					event.consume();
					break;
				default:
					keyPressed = false;
					event.consume();			
					break;
			}
			}
			
		});
	}
	
	public void createNewGame(Stage menuStage) {
		this.menuStage = menuStage;
		//TODO add logic for ball
		this.menuStage.hide();
		createBall();
		createSpikes();
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
	
	private void createSpikes() {
		//spike aspect ratio is 0.8 so resizing should be a value along that ratio
		final int SPIKE_WIDTH = 80; //number of spikes on screen = spike width / game width
		Image image = new Image("/UpwardSpike.png",SPIKE_WIDTH,100,true,true);
		
		spikeList = new ArrayList<ImageView>();
		for(int i = 0; i < GAME_WIDTH; i+=SPIKE_WIDTH) {
			spike = new ImageView(image);
			spikeList.add(spike);
		}
		for(int i = 0; i<spikeList.size(); ++i) {
			spikeList.get(i).setLayoutX(i*SPIKE_WIDTH);
			spikeList.get(i).setLayoutY(GAME_HEIGHT-100);
			gamePane.getChildren().add(spikeList.get(i));
		}
		
	}
}
