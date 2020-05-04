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
		newXPos = 0;
		newYPos = 0;
		xIsNeg = false;
		spikeOnScreen = false;
		rand = new Random();
		gameStarted = false;
	}
	
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
						//System.out.println("RIGHT PRESSED");
						keyPressed = true;
						moveCode = 3;
						event.consume();
						break;
					case A:
					case LEFT:
						//System.out.println("LEFT PRESSED");
						keyPressed = true;
						moveCode = 2;
						event.consume();
						break;
					case W:
					case UP:
						//System.out.println("UP PRESSED");
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
					//TODO: remove the printlns after debugging
					//System.out.println("RIGHT RELEASED");
					event.consume();
					break;
				case A:
				case LEFT:
					//System.out.println("LEFT RELEASED");
					event.consume();
					break;
				case W:
				case UP:
					//System.out.println("UP RELEASED");
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
		//System.out.println("spike added");
		
	}
	
	
	private void createGameLoop() {
		gameTimer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				if(!spikeOnScreen) {
					createFallingSpike();
				}
				
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
				checkCollision();
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
				
				//System.out.println((double)yVelocity/60);
			}
		};
		
	}
	
	
	//update velocities
	private void moveY(int moveCode) {
		//TODO: implement logic for moving upwards 
		if(this.moveCode == 1) {
			//System.out.println("Move up");
			yVelocity = yVelocity+250;
		}else {
			//System.out.println("Move down");
		}
		
	}
	
	private void moveX(int moveCode) {
		frameDecay = 0;
		if(this.moveCode == 2) {
			//System.out.println("Move left");
			xVelocity = xVelocity-X_PUSH;
		}else {
			//System.out.println("Move right");
			xVelocity = xVelocity+X_PUSH;
		}
	}
	
	private void calculateVelocity() {
		xIsNeg = (xVelocity<0)? true:false;
		xVelocity = Math.abs(xVelocity - frameDecay/90*xVelocity); //decays to some fraction of the push over a second unless pushed in the other direction
		
		if(xIsNeg) {
			xVelocity = -xVelocity; //maintains if its going left or right
		}
		
		yVelocity = yVelocity - Y_GRAVITY/60; //flatly decreases speed by the gravity constant
		spikeVelocity = spikeVelocity - Y_GRAVITY/60;
	}
	
	private void calculatePosition() {
		spikeYPos = downSpike.getLayoutY()-spikeVelocity/60;
		newXPos = ball.getLayoutX()+(xVelocity/60);
		newYPos = ball.getLayoutY()-(yVelocity/60);
		if(newXPos>GAME_WIDTH-100 || newXPos < 0) {
			xVelocity = -xVelocity*.5;
		}
		if(newYPos<0) {
			yVelocity = -yVelocity*.5;
		}
		
		if(ball.getLayoutY() + 100 > spikeYPos && ball.getLayoutY() < spikeYPos+DS_HEIGHT) {
			if(	
				(popPoint-DS_WIDTH/2 < newXPos+100)
			&&  (popPoint+DS_WIDTH/2 > newXPos))
			{
				xVelocity = -xVelocity*.75;
			}
		}
		downSpike.setLayoutY(spikeYPos);
		ball.setLayoutX(ball.getLayoutX()+(xVelocity/60)); //The velocities have to be adjusted by pixels per frame as they are in pixels per second
		ball.setLayoutY(ball.getLayoutY()-(yVelocity/60));
		
	}
	
	private void checkCollision() {
		if( ball.getLayoutY()>GAME_HEIGHT-190) {
			handleExit();
			return;
		}
		
		if(spikeYPos > GAME_HEIGHT - (DS_HEIGHT+100)) {
			gamePane.getChildren().remove(downSpike);
			//System.out.println("spike removed");
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
	private void reset() {
		ball.setLayoutX(GAME_WIDTH/2-50);
		ball.setLayoutY(GAME_HEIGHT/2-50);
		xVelocity = 0;
		yVelocity = 0;
	}
	
	private void handleExit() {
		gameStage.close();
	}
}
