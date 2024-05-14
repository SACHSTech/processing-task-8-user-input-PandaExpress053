import java.util.Arrays;

import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {
	
  // Initializing variables
	PImage imgCrosshair;
  int intMaxTargetNum = 0;
  int intTargetNum = 0;
  float fltTargetX[] = new float[intMaxTargetNum];
  float fltTargetY[] = new float[intMaxTargetNum];
  float fltTargetW[] = new float[intMaxTargetNum];
  float fltTargetL[] = new float[intMaxTargetNum];
  boolean isAlive[] = new boolean[intMaxTargetNum];

  int intRColour;
  int intGColour;
  int intBColour;
  

  boolean isDrag = true;
  boolean isPlaying = false;

  boolean upPressed = false;
  boolean downPressed = false;
  boolean leftPressed = false;
  boolean rightPressed = false;
  String strMessage = "";

  int intUsernameX = 200;
  int intUsernameY = 30;

  int intScore;
  /**
   * Called once at the beginning of execution, put your size all in this method
   */
  public void settings() {
	  // put your size call here
    size(400, 400);
    
    // initializing images
    imgCrosshair = loadImage("crosshair.png");
    imgCrosshair.resize(imgCrosshair.width / 40, imgCrosshair.height / 40);
  }

  public void setup() {
    
  }

  /**
   * Called repeatedly, anything drawn to the screen goes here
   */
  public void draw() {
	  
    // Changes colour of border in game screen by key input
    if (keyPressed && isPlaying){
      // Turn red
      if (key == 'r'){
        intRColour = 255;
        intBColour = 0;
        intGColour = 0;
      }
      // Turn green
      else if (key == 'g'){
        intRColour = 0;
        intBColour = 255;
        intGColour = 0;
      }
      // Turn blue
      else if (key == 'b'){
        intRColour = 0;
        intBColour = 0;
        intGColour = 255;
      }
      // Turn black
      else if (key == 'd'){
        intRColour = 0;
        intBColour = 0;
        intGColour = 0;
      }
    // When outside of game screen, set border colour to white
    }
    else if (!isPlaying){
      intRColour = 255;
      intBColour = 255;
      intGColour = 255;
    }  

    // Drawing border
    background(intRColour, intBColour, intGColour);
    fill(255);
    rect(10, 10, 380, 380);
    
    // Handles information for each target generated
    for (int i = 0; i < intMaxTargetNum; i++){

      // Checks if user is clicking on the target and if target has decayed yet
      if (mousePressed && !isDrag && Math.abs(fltTargetX[i] - mouseX) < fltTargetW[i] / 2 && Math.abs(fltTargetY[i] - mouseY) < fltTargetL[i] / 2 && fltTargetW[i] >= 30){
        isAlive[i] = false;
        intScore++;
      }

      // Target Generation
      if (!isAlive[i] && isPlaying){
        fltTargetX[i] = random(50, 350);
        fltTargetY[i] = random(50, 350);
        fltTargetL[i] = 80;
        fltTargetW[i] = 80;
        isAlive[i] = true;
      }

      // Target decay over time
      if (fltTargetW[i] > 60){
        fill (0, 255, 0);
        fltTargetL[i] -= 0.75f;
        fltTargetW[i] -= 0.75f;
      }
      else if (fltTargetW[i] > 40){
        fill (255, 255, 0);
        fltTargetL[i] -= 0.45f;
        fltTargetW[i] -= 0.45f;
      }
      else if (fltTargetW[i] > 30){
        fill (255, 0, 0);
        fltTargetL[i] -= 0.35f;
        fltTargetW[i] -= 0.35f;
      }
      else if (fltTargetW[i] > 20){
        fill (0, 0, 0);
        fltTargetL[i] -= 0.1f;
        fltTargetW[i] -= 0.1f;
      }

      // Delete Target after decay
      else if (fltTargetW[i] <= 20){
        isAlive[i] = false;
      }
      
      // Draws targets
      if(isPlaying){
        ellipse(fltTargetX[i], fltTargetY[i], fltTargetW[i], fltTargetL[i]);
      }
    }
    
    if (isPlaying){

      // moving username in game screen in four directions (handles multiple inputs)
      if (upPressed && intUsernameY > 15) {
        intUsernameY--;
      }
      if (downPressed && intUsernameY < 385) {
        intUsernameY++;
      }
      if (leftPressed && intUsernameX > 10) {
        intUsernameX--;
      }
      if (rightPressed && intUsernameX < 390) {
        intUsernameX++;
      }

      // UI in game screen
      // Crosshair
      image(imgCrosshair, mouseX - 23, mouseY - 23);
      
      fill(0);
      textSize(15);
      textAlign(CENTER);

      // Username display
      text(strMessage, intUsernameX, intUsernameY);

      // Display score and number of targets
      textAlign(RIGHT);
      text("Score: " + intScore, 370, 30);
      text("Targets: " + intMaxTargetNum, 370, 50);

    }

    // Home Screen
    if (!isPlaying){
      // Resetting variables for game to be played
      intScore = 0;
      intMaxTargetNum = 0;
      isAlive = new boolean[0];
      fltTargetX = new float[0];
      fltTargetY = new float[0];
      fltTargetL = new float[0];
      fltTargetW = new float[0];

      // Printing Username input UI
      fill(0);
      textSize(30);
      textAlign(CENTER);
      text("Username: ", 200, 150);
      text("=> " + strMessage, 200, 200);

      // Output warning if username is at the character limit
      if (strMessage.length() == 8){
        text("Character limit is 8", 200, 250);     
      }
    }
  }

  public void mouseDragged(){
    // Checks if mouse is being dragged
    isDrag = true;
  }
  public void mouseReleased(){
    // Checks if mouse is not being dragged
    isDrag = false;
  }
  public void keyReleased() {
    // Pressing the spacebar will toggle the game on and off, changing between the home screen and game screen
    if (keyCode == 32 && !isPlaying) {
      isPlaying = true;
    }
    else if (keyCode == 32 && isPlaying) {
      isPlaying = false;
    }
    
    // Increases the number of targets the user can interact with by one
    if (keyCode == 33 && isPlaying && intMaxTargetNum < 10) {
      intMaxTargetNum++;
      // adds empty array data
      isAlive = Arrays.copyOf(isAlive, isAlive.length + 1);
      fltTargetX = Arrays.copyOf(fltTargetX, fltTargetX.length + 1);
      fltTargetY = Arrays.copyOf(fltTargetY, fltTargetY.length + 1);
      fltTargetL = Arrays.copyOf(fltTargetL, fltTargetL.length + 1);
      fltTargetW = Arrays.copyOf(fltTargetW, fltTargetW.length + 1);
    }
    // Decreases the number of targets the user can interact with by one
    else if (keyCode == 34 && isPlaying && intMaxTargetNum > 0) {
      intMaxTargetNum--;
      // deleting array information
      isAlive = Arrays.copyOf(isAlive, isAlive.length - 1);
      fltTargetX = Arrays.copyOf(fltTargetX, fltTargetX.length - 1);
      fltTargetY = Arrays.copyOf(fltTargetY, fltTargetY.length - 1);
      fltTargetL = Arrays.copyOf(fltTargetL, fltTargetL.length - 1);
      fltTargetW = Arrays.copyOf(fltTargetW, fltTargetW.length - 1);
    }

    // Registers when key is not being pressed anymore, toggles boolean that restricts username movement across screen
    if (keyCode == UP) {
      upPressed = false;
    }
    else if (keyCode == DOWN) {
      downPressed = false;
    }
    else if (keyCode == LEFT) {
      leftPressed = false;
    }
    else if (keyCode == RIGHT) {
      rightPressed = false;
    }
  }

  public void keyPressed(){
    // Allows user to use backspace to delete characters in username on home screen
    if (keyCode == 8 && !isPlaying && strMessage.length() > 0){
      strMessage = strMessage.substring(0, strMessage.length() - 1);
    }
    // Makes sure that backspace or spacebar won't be registed as a key to add to the username
    else if (keyCode == 8 && strMessage.length() == 0 || keyCode == 32){}
    
    // Allows user to add character to username as long as length of username doesn't exceed 8 characters
    else if (strMessage.length() < 8 && !isPlaying){
      strMessage += key;
    }

    // Registers when key is being pressed/held down, toggles boolean that moves username across screen
    if (keyCode == UP) {
      upPressed = true;
    }
    else if (keyCode == DOWN) {
      downPressed = true;
    }
    else if (keyCode == LEFT) {
      leftPressed = true;
    }
    else if (keyCode == RIGHT) {
      rightPressed = true;
    }
  }
}