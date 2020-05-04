package view;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
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
	
	private final int Y_GRAVITY = 200;
	private final int X_PUSH = 200;
	private int xVelocity;
	private int yVelocity;
	private boolean xIsNeg;
	
	
	private Stage menuStage;
	
	private ImageView ball;
	private ImageView spike;
	private List<ImageView> spikeList; //variable number of spikes depending on resolution
	private boolean keyPressed; //boolean in order to prevent held input from firing off multiple times
	
	
	private int moveCode; //1 for up, 2 for left, 3 for right
	private AnimationTimer gameTimer;
	private int frameNum;
	private int frameDecay;
	
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
		moveCode = 0;
		frameNum = 0;
		frameDecay = 0;
		xVelocity = 0;
		yVelocity = 0;
		xIsNeg = false;
	}
	
	private void createKeyListeners() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				//todo finish handling
				if(keyPressed) {
					return;
				}
				frameDecay = 0;
				switch (event.getCode()){
					case D:
					case RIGHT:
						//TODO: replace printlns with things that move the ball
						System.out.println("RIGHT PRESSED");
						keyPressed = true;
						moveCode = 3;
						event.consume();
						break;
					case A:
					case LEFT:
						System.out.println("LEFT PRESSED");
						keyPressed = true;
						moveCode = 2;
						event.consume();
						break;
					case W:
					case UP:
						System.out.println("UP PRESSED");
						keyPressed = true;
						moveCode = 1;
						event.consume();
						break;
					default:
						moveCode = 0;
						keyPressed = true;
						event.consume();
						break;
				}
			}
			
		});
		
		gameScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				moveCode = 0;
				keyPressed = false;
				switch (event.getCode()){
				case D:
				case RIGHT:
					//TODO: remove the printlns after debugging
					System.out.println("RIGHT RELEASED");
					event.consume();
					break;
				case A:
				case LEFT:
					System.out.println("LEFT RELEASED");
					event.consume();
					break;
				case W:
				case UP:
					System.out.println("UP RELEASED");
					event.consume();
					break;
				case R:
					ball.setLayoutX(GAME_WIDTH/2-50);
					ball.setLayoutY(GAME_HEIGHT/2-50);
					xVelocity = 0;
					yVelocity = 0;
				default:
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
		createGameLoop();
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
	
	
	private void createGameLoop() {
		gameTimer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				if(frameNum % 60 == 0) { 
					moveY(0); //set y acceleration to -gravity
					frameNum = 0;
				}
				frameNum++;
				if (frameDecay<60) {
					frameDecay++;
				}
				switch (moveCode) {
				
					case 1:
						moveY(1); 
						break;
					case 2:
						moveX(2);
						break;
					case 3:
						moveX(3);
						break;
					default:
						break;
				}
				moveCode = 0;
				//moveBlah()
				// 	SETS THE VELOCITY OF EACH BALL
				calculateVelocity();
				//	calcs the current velocity of the ball based on the amount of time a move command is given
				calculatePosition();
				//calculate the position of the ball every 1/60 of a second (frame)
				//	high school physics equations
				//	position = lastpos+(velocity*time)
				//	velocityf = velocityi + acceleration*(time)
				//	and set X velocity (I.E where it ends up next frame) to currX-currX(1-frameDecay/60) after calculations
				//	and set Y velocity to currY+currAccelY(1/60)
				//	X velocity decays to zero after a second
				//  Y velocity is continuously decreasing by gravity constant
			}
		};
		gameTimer.start();
	}
	
	
	//MOVE FUNCTIONS ADJUST VELOCITY!!! NOT ACCELERATION, NOT POSITION!!!!!!
	private void moveY(int moveCode) {
		//TODO: implement logic for moving upwards 
		if(this.moveCode == 1) {
			System.out.println("Move up");
			yVelocity = yVelocity+250;
		}else {
			System.out.println("Move down");
		}
		
	}
	
	private void moveX(int moveCode) {
		frameDecay = 0;
		if(this.moveCode == 2) {
			System.out.println("Move left");
			xVelocity = -X_PUSH;
		}else {
			System.out.println("Move right");
			xVelocity = X_PUSH;
		}
	}
	
	private void calculateVelocity() {
		xIsNeg = (xVelocity<0)? true:false;
		xVelocity = Math.abs(xVelocity - frameDecay/60*xVelocity); //decays to 0 over a second unless pushed in the other direction
		if(xIsNeg) {
			xVelocity = -xVelocity; //maintains if its going left or right
		}
		
		yVelocity = yVelocity - Y_GRAVITY/60; //flatly decreases speed by the gravity constant
	}
	
	private void calculatePosition() {
		ball.setLayoutX(ball.getLayoutX()+(xVelocity/60));
		ball.setLayoutY(ball.getLayoutY()-(yVelocity/60));
	}
}
