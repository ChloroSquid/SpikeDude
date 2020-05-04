/**@author Corey Blanke <gtm871>
 * 
 * Class which creates and manages everything in the main game loop.
 */

package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class GameViewManager {
	private AnchorPane gamePane;
	private Scene gameScene;
	private Stage gameStage;
	private boolean gameStarted;
	
	private static final int GAME_WIDTH = 800;
	private static final int GAME_HEIGHT = 800;
	private static final int DS_WIDTH = 20;
	private static final int DS_HEIGHT = 200;
	private static final int Y_GRAVITY = 200;
	private static final int X_PUSH = 200;
	
	private double xVelocity; //velocity in pixels per second
	private double yVelocity;
	private boolean xIsNeg;
	private double newXPos;
	private double newYPos;
	
	private double spikeVelocity;
	private double spikeXPos;
	private double spikeYPos;
	private double popPoint;
	
	
	private Stage menuStage;
	
	private ImageView ball;
	private ImageView spike;
	private ImageView downSpike;
	private List<ImageView> spikeList; //variable number of spikes depending on resolution
	private boolean keyPressed; //boolean in order to prevent held input from firing off multiple times
	private boolean spikeOnScreen;
	
	
	private int moveCode; //1 for up, 2 for left, 3 for right
	private AnimationTimer gameTimer;
	private int frameNum;
	private int frameDecay;
	Random rand;
	
	/**
	 * Constructor for the Game View Manager.
	 * Calls functions to set up the stage and the keyboard listeners.
	 * 
	 */
	public GameViewManager() {
		initializeStage();
		createKeyListeners();
	}
	
	/**
	 * Initializes the elements of the stage to starting values.
	 */
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
		newXPos = 0;
		newYPos = 0;
		xIsNeg = false;
		spikeOnScreen = false;
		rand = new Random();
		gameStarted = false;
	}
	
	/**
	 * Creates a keyboard listener and defines the behavior of each keypress.
	 * Also includes behavior on key release.
	 * Mainly used to control the game.
	 */
	private void createKeyListeners() {
		gameScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			
			@Override
			public void handle(KeyEvent event) {
				if(!gameStarted) {
					gameTimer.start();
					gameStarted = true;
				}
				if(keyPressed) {
					return;
				}
				
				frameDecay = 0;
				switch (event.getCode()){
					case D:
					case RIGHT:
						keyPressed = true;
						moveCode = 3;
						event.consume();
						break;
					case A:
					case LEFT:
						keyPressed = true;
						moveCode = 2;
						event.consume();
						break;
					case W:
					case UP:
						keyPressed = true;
						moveCode = 1;
						event.consume();
						break;
					case R:
						reset();
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
					event.consume();
					break;
				case A:
				case LEFT:
					event.consume();
					break;
				case W:
				case UP:
					event.consume();
					break;
				case R:
					reset();
					event.consume();
					break;
				case ESCAPE:
					event.consume();
					handleExit();
					break;
				default:
					event.consume();			
					break;
			}
			}
			
		});
	}
	
	/**
	 * Function to invoke the game loop from a controller class.
	 * @param menuStage stage of the main menu to be hidden when the game is invoked
	 */
	public void createNewGame(Stage menuStage) {
		this.menuStage = menuStage;
		this.menuStage.hide();
		createBall();
		createSpikes();
		createGameLoop();
		gameStage.show();
		
	}
	
	/**
	 * Creates the ball and adds it onto the game pane.
	 */
	private void createBall() {
		Image image = new Image("/ball.png", 100,100,false,true);
		ball = new ImageView(image);
		ball.setLayoutX(GAME_WIDTH/2-50);
		ball.setLayoutY(GAME_HEIGHT/2-50);
		gamePane.getChildren().add(ball);
	}
	
	/**
	 * Creates the row of spikes at the bottom as a visual indicator of the pop zone.
	 */
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
	
	/**
	 * Creates a long, thin, falling spike that appears in a random position along the top of the pane.
	 */
	private void createFallingSpike() {
		spikeOnScreen = true;
		Image image = new Image("/DownwardSpike.png",DS_WIDTH,DS_HEIGHT,false,true);
		downSpike = new ImageView(image);
		spikeXPos = rand.nextInt(GAME_WIDTH-DS_WIDTH);
		spikeYPos = -DS_HEIGHT;
		downSpike.setLayoutX(spikeXPos);
		downSpike.setLayoutY(-DS_HEIGHT);
		gamePane.getChildren().add(downSpike);
		spikeVelocity = 0;
		popPoint = spikeXPos+DS_WIDTH/2;
		
	}
	
	/**
	 * Initializes and runs the game loop, as well as manages the calculations and positions of each asset every frame.
	 * 
	 * Every frame does the following:
	 * -Maintains two internal timers, one tracking the number of frames passed, one tracking the amount of time since the last key press.
	 * Both are capped at one second.
	 * -If the random falling spike has left the screen, create a new one.
	 * -Updates the velocity of the ball if a key has been pressed that frame
	 * -Invokes collision checking, velocity calculations, and position calculations
	 */
	private void createGameLoop() {
		gameTimer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				if(!spikeOnScreen) {
					createFallingSpike();
				}
				
				if(frameNum % 60 == 0) { 
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
				// 	SETS THE VELOCITY OF EACH BALL
				checkCollision();
				calculateVelocity();
				//	calcs the current velocity of the ball based on the amount of time a move command is given
				calculatePosition();
				//calculate the position of the ball every 1/60 of a second (frame)
			}
		};
		
	}
	
	
	/**
	 * Updates the velocity in the Y direction based on user input.
	 * @param moveCode Integer maintaining the current state of the movement commands from the user.
	 */
	private void moveY(int moveCode) {
		if(this.moveCode == 1) {
			yVelocity = yVelocity+250;
		}
	}
	
	/**
	 * Updates the velocity in the X direction based on user input.
	 * @param moveCode Integer maintaining the current state of the movement commands from the user.
	 */
	private void moveX(int moveCode) {
		frameDecay = 0;
		if(this.moveCode == 2) {
			xVelocity = xVelocity-X_PUSH;
		}else {
			xVelocity = xVelocity+X_PUSH;
		}
	}
	
	/**
	 * Calculates the exact velocity of each asset in one particular frame.
	 * -Calculates the speed based on gravity for the Y direction of the falling spike.
	 * -Calculates the speed based on gravity and user input for the Y direction of the ball.
	 * -Calculates the speed based on user input and a decay factor for the X direction of the ball.
	 */
	private void calculateVelocity() {
		xIsNeg = (xVelocity<0)? true:false;
		xVelocity = Math.abs(xVelocity - frameDecay/90*xVelocity); //decays to some fraction of the push over a second unless pushed in the other direction
		
		if(xIsNeg) {
			xVelocity = -xVelocity; //maintains if its going left or right
		}
		
		yVelocity = yVelocity - Y_GRAVITY/60; //flatly decreases speed by the gravity constant
		spikeVelocity = spikeVelocity - Y_GRAVITY/60;
	}
	
	/**
	 * Calculates and draws the position of the assets in one particular frame.
	 * Also does some collision checking with the spike and the wall (handles the side bouncing behavior)
	 */
	private void calculatePosition() {
		spikeYPos = downSpike.getLayoutY()-spikeVelocity/60;
		newXPos = ball.getLayoutX()+(xVelocity/60);
		newYPos = ball.getLayoutY()-(yVelocity/60);
		
		if(newXPos>GAME_WIDTH-100 || newXPos < 0) {
			xVelocity = -xVelocity*.5; //Bounces off the wall at half speed
		}
		if(newYPos<0) {
			yVelocity = -yVelocity*.5; //Bounces off the ceiling at half speed
		}
		
		if(ball.getLayoutY() + 100 > spikeYPos && ball.getLayoutY() < spikeYPos+DS_HEIGHT) { //Check new position will be inside spike
			if(	
				(popPoint-DS_WIDTH/2 < newXPos+100)
			&&  (popPoint+DS_WIDTH/2 > newXPos))
			{
				xVelocity = -xVelocity*.75; //Bounce off side of spike at three quarters speed
			}
		}
		downSpike.setLayoutY(spikeYPos);
		ball.setLayoutX(ball.getLayoutX()+(xVelocity/60)); //The velocities have to be adjusted by pixels per frame as they are in pixels per second
		ball.setLayoutY(ball.getLayoutY()-(yVelocity/60));
		
	}
	
	/**
	 * Checks if the ball is inside of a pop zone, as well as handles the falling spike when it approaches the pop zone
	 * If ball is inside the pop zone at the bottom, end the game.
	 * If the top of the ball touches the tip of the falling spike, end the game.
	 * If the spike is in the pop zone, remove the spike.
	 */
	private void checkCollision() {
		if( ball.getLayoutY()>GAME_HEIGHT-190) {
			handleExit();
			return;
		}
		
		if(spikeYPos > GAME_HEIGHT - (DS_HEIGHT+100)) {
			gamePane.getChildren().remove(downSpike);
			spikeOnScreen = false;
		}
		
		if( 
			(Math.abs(ball.getLayoutY()-100 - downSpike.getLayoutY()-DS_HEIGHT/4) < 20 ) //if the bottom of the spike is on the same level as the top of the ball 
			&& (popPoint > ball.getLayoutX())
			&& (popPoint < ball.getLayoutX()+100) // and the ball's X coords are within the spike
		){
			handleExit();
		} 
		
	}
	
	/**
	 * "Hidden" debug function accessible by pressing R.
	 * Resets the position and velocity of the ball to the original values.
	 */
	private void reset() {
		ball.setLayoutX(GAME_WIDTH/2-50);
		ball.setLayoutY(GAME_HEIGHT/2-50);
		xVelocity = 0;
		yVelocity = 0;
	}
	
	/**
	 * Closes the game.
	 */
	private void handleExit() {
		gameStage.close();
	}
}
