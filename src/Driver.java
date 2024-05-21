import java.io.*;
import java.util.Scanner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Image;
//Mouse
import java.awt.*;
import javax.swing.*;
//Mouse and Key Listener Functionality is turned off even when imported
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//Daniel Wilson
//June 18 2023
//Multiplayer Mario with 3 Levels

public class Driver extends JPanel implements MouseListener, KeyListener, Runnable{
	
	public static int gameState = 0;
	//Gamestate values:
	//0 is Main Menu
	//1 Level Select
	//2 Edit Name
	//8 Edit Colour
	
	//3 Add Player Colour
	//4 Add Player Name
	//5 Gameplay
	//6 Game Lost Screen
	//7 End Game Screen	Top To Bottom
	//9 End Game Screen	Bottom to Top
	//10 Instructions
	
	public static Map<Integer, BufferedImage> backgroundMap = new HashMap<>();
	
	public static ArrayList<Player> allPlayers = new ArrayList<Player>();
	public static ArrayList<Obstacles> allObstacles = new ArrayList<Obstacles>();
	public static ArrayList<Enemy> allEnemies = new ArrayList<Enemy>();
	public static ArrayList<Highscore> highscores = new ArrayList<>();
	public static Image [][]images;
	public static Image starImage;
	public static Image timeImage;
	public static Image endScreen7;
	public static Image endScreen9;
	public static Image instructions;
	public static Image gameLostImage;
	
	public static BufferedImage background;
	
	public static boolean keyW = false;
	public static boolean keyA = false;
	public static boolean keyD = false;
	
	public static boolean keyT = false;
	public static boolean keyF = false;
	public static boolean keyH = false;
	
	public static boolean keyI = false;
	public static boolean keyJ = false;
	public static boolean keyL = false;
	
	public static boolean keyUp = false;
	public static boolean keyLeft = false;
	public static boolean keyRight = false;

	
	//Set Up
	public static int playerCount = 0;
	
	public static int hitboxY = 50;
	public static int hitboxX = 50;

	public static int scrollX = 0;
	
	public static long time = 0;
	public static long startingTime = 0;
	
	public static int levelNum;
	
	
	//Star
	public static Star star = new Star(-250,-250);
	
	//Constructor
	//Reads in images
	public Driver(){
		setPreferredSize(new Dimension(1020,560));
		setBackground(new Color(0,0,0));
		
		addKeyListener(this);
		addMouseListener(this);
		this.setFocusable(true);
		Thread thread = new Thread(this);
		thread.start();
		images = new Image[5][2];
		try {
			//[x][y] 
			//x = colour 
			//y = direction (0 = right, 1 = left)
			images[0][0] = ImageIO.read(new File("blueRight.png"));
			images[0][1] = ImageIO.read(new File("blueLeft.png"));
			images[1][0] = ImageIO.read(new File("redRight.png"));
			images[1][1] = ImageIO.read(new File("redLeft.png"));
			images[2][0] = ImageIO.read(new File("greenRight.png"));
			images[2][1] = ImageIO.read(new File("greenLeft.png"));
			images[3][0] = ImageIO.read(new File("yellowRight.png"));
			images[3][1] = ImageIO.read(new File("yellowLeft.png"));
			images[4][0] = ImageIO.read(new File("pinkRight.png"));
			images[4][1] = ImageIO.read(new File("pinkLeft.png"));
			
			timeImage = ImageIO.read(new File("timeImage.png"));
			starImage = ImageIO.read(new File("StarImage.png"));
			
			background = ImageIO.read(new File("background.png"));
			gameLostImage = ImageIO.read(new File("gameLostImage.png"));
			
			BufferedImage mainMenu = ImageIO.read(new File("mainMenu.png"));
			BufferedImage levelSelect = ImageIO.read(new File("levelSelect.png"));
			BufferedImage nameSelect = ImageIO.read(new File("nameSelect.png"));
			BufferedImage colourSelect = ImageIO.read(new File("colourSelect.png"));
			BufferedImage endScreen7 = ImageIO.read(new File("endScreen1-5.png"));
			BufferedImage endScreen9 = ImageIO.read(new File("endScreen5-1.png"));
			BufferedImage instructions = ImageIO.read(new File("instructions.png"));
			
											//Game State Values
			backgroundMap.put(0, mainMenu); //0 is Main Menu
			backgroundMap.put(1, levelSelect); //1 Level Select
			backgroundMap.put(2, nameSelect); //2 Edit Name
			backgroundMap.put(8, colourSelect);//8 Edit Colour
			backgroundMap.put(3, colourSelect);//3 Add Player Colour
			backgroundMap.put(4, nameSelect);//4 Add Player Name
			backgroundMap.put(7, endScreen7);//7 End Game Screen	Top To Bottom
			backgroundMap.put(9, endScreen9);//9 End Game Screen	Bottom to Top
			backgroundMap.put(10, instructions);//10 Instructions	
			
			
		}
		catch(Exception e) {
			System.out.println("Image not Found");
		}	
	}
	
	//Graphics, everything on the screen goes through here, then is painted on the screen
	public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.blue);
			g.drawString(gameState + "", 50, 50);
			g.drawImage(backgroundMap.get(gameState), 0, 0, null);
			if(gameState == 5 || gameState == 6) {
				g.drawImage(background, scrollX, 0, null);
				
				//Print Based On Level Num
				for(int i = 0; i < allObstacles.size()-1; i++) {
					g.setColor(Color.black);
					g.fillRect(allObstacles.get(i).getX() + scrollX, allObstacles.get(i).getY(), allObstacles.get(i).getWidth(), allObstacles.get(i).getHeight());
				}
				g.fillRect(allObstacles.get(allObstacles.size()-1).getX() + scrollX, allObstacles.get(allObstacles.size()-1).getY(), allObstacles.get(allObstacles.size()-1).getWidth(), allObstacles.get(allObstacles.size()-1).getHeight());
				
				int z = 0;
				for(int i = 0; i <= allEnemies.size()-1; i++) {
					//Right
					if(allEnemies.get(i).getDirection()) {
						z = 0;
					}
					else {
						z = 1;
					}
					g.drawImage(images[1][z], allEnemies.get(i).getX() + scrollX, allEnemies.get(i).getY(), null);
				}
				g.setColor(Color.black);
				if((time/1000) >= 100) {
					g.setFont(new Font("Arial", Font.BOLD, 32));
				}
				else {
					g.setFont(new Font("Arial", Font.BOLD, 52));
				}
				String drawTime = time/100+"";
				String msTime = drawTime.charAt(drawTime.length()-1) + "";
				g.drawImage(timeImage, 50, 20, null);
				g.drawString(time/1000 + "." + msTime, 80, 100);
				
				
			}
			if(gameState <= 6 || gameState == 8) {
				
				g.setColor(Color.white);
				g.setFont(new Font("Dialog", Font.PLAIN, 12));
				
				g.drawImage(images[allPlayers.get(0).getColour()] [allPlayers.get(0).getFacingRight()], allPlayers.get(0).getX(), allPlayers.get(0).getY(), null);
				g.drawString(allPlayers.get(0).getName(), allPlayers.get(0).getX()+5, allPlayers.get(0).getY()-7);
				if(playerCount >= 1) {
					g.drawImage(images[allPlayers.get(1).getColour()] [allPlayers.get(1).getFacingRight()], allPlayers.get(1).getX(), allPlayers.get(1).getY(), null);
					g.drawString(allPlayers.get(1).getName(), allPlayers.get(1).getX()+5, allPlayers.get(1).getY()-7);
					if(playerCount >= 2) {
						g.drawImage(images[allPlayers.get(2).getColour()] [allPlayers.get(2).getFacingRight()], allPlayers.get(2).getX(), allPlayers.get(2).getY(), null);
						g.drawString(allPlayers.get(2).getName(), allPlayers.get(2).getX()+5, allPlayers.get(2).getY()-7);
						if(playerCount >= 3) {
							g.drawImage(images[allPlayers.get(3).getColour()] [allPlayers.get(3).getFacingRight()], allPlayers.get(3).getX(), allPlayers.get(3).getY(), null);
							g.drawString(allPlayers.get(3).getName(), allPlayers.get(3).getX()+5, allPlayers.get(3).getY()-7);
						}
					}
				}
				g.drawImage(starImage, star.getX() + scrollX, star.getY(), null);
				if(gameState == 6) {
					g.drawImage(gameLostImage, 230, 0, null);
				}
			}
			else if(gameState == 7) {
				g.setColor(Color.black);
				g.setFont(new Font("Arial", Font.BOLD, 82));
				String drawTime = time/100+"";
				String msTime = drawTime.charAt(drawTime.length()-1) + "";
				g.drawImage(endScreen7,0, 0, null);
				g.drawString(time/1000 + "." + msTime, 100, 380);
				int y = 100;
				g.setFont(new Font("Dialog", Font.PLAIN, 16));
				for(int i = 0; i < 5; i++) {
					y = y + 75;
					g.drawString(highscores.get(i) + "", 675, y);	
				}
			}
			else if(gameState == 9) {
				g.drawImage(endScreen9,0, 0, null);
				g.setColor(Color.black);
				g.setFont(new Font("Arial", Font.BOLD, 38));
				int y = 190;
				for(int i = 0; i < 5; i++) {
					g.drawString(highscores.size()-i + ":", 570, y);
					y = y + 75;
				}
				g.setFont(new Font("Arial", Font.BOLD, 82));
				String drawTime = time/100+"";
				String msTime = drawTime.charAt(drawTime.length()-1) + "";
				g.drawString(time/1000 + "." + msTime, 100, 380);
				y = 100;
				g.setFont(new Font("Dialog", Font.PLAIN, 16));
				for(int i = 0; i < 5; i++) {
					y = y + 75;
					g.drawString(highscores.get(i) + "", 675, y);	
				}
		        
			}
	}
	
	//Creates the player objects, frame, panel
	public static void main(String[]args) {
		JFrame frame = new JFrame("Penguin Parade");
		Driver panel = new Driver();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//SetUp
		allPlayers.add(new Player(0, "P1", false, 0, 75, 425));
		allPlayers.add(new Player(1, "P2", false, 0, 200, 425));
		allPlayers.add(new Player(2, "P3", false, 0, 325, 425));
		allPlayers.add(new Player(3, "P4", false, 0, 450, 425));
	}
	//Loops through this every 25ms, when gameState = gameplay it will updateEnemies and any other parts of the
	//game that happen regardless of user input
	public void run() {
		int g1 = 0;
		int g2 = 0;
		int g3 = 0;
		int g4 = 0;
		try {
			Thread.sleep(1000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		while(true) {
			repaint();
			if(gameState == 5) {
				time =  System.currentTimeMillis() - startingTime;
				//Going Down
				if(star.getDirection()) {
					//If lower then 25 pixels 
					if(star.getY() >= star.getPresetY() + 40) {
						star.setDirection(false);
					}
					else {
						star.setY(star.getY() + 1);
					}
				}
				
				//Going Up
				else {
					//If same as preset 
					if(star.getY() <= star.getPresetY()) {
						star.setDirection(true);
					}
					else {
						star.setY(star.getY() - 1);
					}
				}
				if(updateEnemies() == true) {
					gameState = 6;
				}
				g1 = updatePlayer(0, g1);
				if(playerCount >= 1) {
					g2 = updatePlayer(1, g2);
					if(playerCount >= 2) {
						g3 = updatePlayer(2, g3);
						if(playerCount >= 3) {
							g4 = updatePlayer(3, g4);
						}
					}
				}
			}
			else {
				g1 = 0;
				g2 = 0;
				g3 = 0;
				g4 = 0;
			}
			
			
			//(1000 Milliseconds = 1 second)
			try {
				Thread.sleep(25);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	//Based on the key that is pressed and the current gameStats different actions will occur,
	//this is where all the keys being pressed originally go through
	public void keyPressed(KeyEvent e) {
		if(gameState == 0) {
			resetGame();
			//2 Edit Name
			//8 Edit Colour
			
			//3 Add Player Colour
			//4 Add Player Name
			//5 Gameplay
			//6 Paused
			//7 End Game Screen
			if(e.getKeyChar() == '1') {
				gameState = 1;
			}
			else if(e.getKeyChar() == '2') {
				gameState = 2;
			}
			else if(e.getKeyChar() == '3') {
				if(playerCount < 3) {
					gameState = 3;
					playerCount++;
				}
			}
			//Remove Player
			else if(e.getKeyChar() == '4') {
				if(playerCount > 0) {
					playerCount = playerCount -1;
				}
			}
			//Remove Player
			else if(e.getKeyChar() == '8') {
				gameState = 8;
			}
			//Instructions
			else if(e.getKeyChar() == 't') {
				gameState = 10;
			}
		}
		else if(gameState == 1) {
			levelCheck(e);
		}
		else if(gameState == 2) {
			nameCheck(e, 0);		
			
		}
		else if(gameState == 8) {
			colourCheck(e, 0);
		}
		else if(gameState == 3) {
			colourCheck(e, playerCount);
		}
		else if(gameState == 4) {
			nameCheck(e, playerCount);
		}
		else if(gameState == 5) {
			setKeys(e);
		}
		else if(gameState == 6) {
			if(e.getKeyChar() == '1') {
				resetGame();
				gameState = 0;
			}
		}
		else if(gameState == 7) {
			if(e.getKeyChar() == '1') {
				resetGame();
				gameState = 0;
			}
			else if(e.getKeyChar() == '2') {
				Collections.sort(highscores);
				gameState = 9;
			}
		}
		else if(gameState == 9) {
			if(e.getKeyChar() == '1') {
				resetGame();
				gameState = 0;
			}
			else if(e.getKeyChar() == '2') {
		        Collections.sort(highscores, new HighscoreScoreComparator());
				gameState = 7;
			}
		}
		else if(gameState == 10) {
			if(e.getKeyChar() == ' ') {
				gameState = 0;
			}
		}
	}
	//For multiple players on one keyboard, needs to have boolean variables
	//for if the key is true/false so multiple keys can be held at once
	//This sets all the keys to either false/true based on input
	public void setKeys(KeyEvent e) {
		if(e.getKeyChar() == 'w') {
			keyW = true;
		}
		else if(e.getKeyChar() == 'd') {
			keyD = true;
		}	
		else if(e.getKeyChar() == 'a') {
			keyA = true;
		}
		
		if(e.getKeyChar() == 't') {
			keyT = true;
		}
		else if(e.getKeyChar() == 'f') {
			keyF = true;
		}	
		else if(e.getKeyChar() == 'h') {
			keyH = true;
		}
		
		if(e.getKeyChar() == 'i') {
			keyI = true;
		}
		else if(e.getKeyChar() == 'j') {
			keyJ = true;
		}	
		else if(e.getKeyChar() == 'l') {
			keyL = true;
		}
		
		//Use getKeyCode works not getKeyChar
		if(e.getKeyCode() == 38) {
			keyUp = true;
		}
		else if(e.getKeyCode() == 39) {
			keyRight = true;
		}	
		else if(e.getKeyCode() == 37) {
			keyLeft = true;
		}
	}
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyChar() == 'w') {
			keyW = false;
		}
		else if(e.getKeyChar() == 'd') {
			keyD = false;
		}	
		else if(e.getKeyChar() == 'a') {
			keyA = false;
		}
		
		if(e.getKeyChar() == 't') {
			keyT = false;
		}
		else if(e.getKeyChar() == 'f') {
			keyF = false;
		}	
		else if(e.getKeyChar() == 'h') {
			keyH = false;
		}
		
		if(e.getKeyChar() == 'i') {
			keyI = false;
		}
		else if(e.getKeyChar() == 'j') {
			keyJ = false;
		}	
		else if(e.getKeyChar() == 'l') {
			keyL = false;
		}
		
		if(e.getKeyCode() == 37) {
			keyLeft = false;
		}
		else if(e.getKeyCode() == 39) {
			keyRight = false;
		}	
		else if(e.getKeyCode() == 38) {
			keyUp = false;
		}
	}
	
	
	
	//User selects Level then calls the level# method
	public void levelCheck(KeyEvent e) {
		if(e.getKeyChar() == ' '){
			gameState = 0;
		}
		else if(e.getKeyChar() == '1') {
			levelNum = 1;
			level1();
		}
		else if(e.getKeyChar() == '2') {
			levelNum = 2;
			level2();
		}
		else if(e.getKeyChar() == '3') {
			levelNum = 3;
			level3();
		}
	}
	
	//User picks a Colour from the list, then the colour in that players object is updated to 
	//the correct number, so in the image array the correct index will give the correct colour
	public void colourCheck(KeyEvent e, int playerNum) {
		//Blue
		if(e.getKeyChar() == '1'){
			allPlayers.get(playerNum).setColour(0);
		}
		//Red
		else if(e.getKeyChar() == '2'){
			allPlayers.get(playerNum).setColour(1);
		}
		//Green
		else if(e.getKeyChar() == '3'){
			allPlayers.get(playerNum).setColour(2);
		}
		//Yellow
		else if(e.getKeyChar() == '4'){
			allPlayers.get(playerNum).setColour(3);
		}
		//Pink
		else if(e.getKeyChar() == '5'){
			allPlayers.get(playerNum).setColour(4);
		}
		if(playerNum != 0) {
			gameState = 4;
		}
		else {
			gameState = 0;
		}
			
	}
	//Parameters: The key that was pressed, and the player number
	//Updates the name string based on what key was pressed (backspace deletes the last letter)
	//then sets the name in the player object
	public void nameCheck(KeyEvent e, int playerNum) {
		String name = allPlayers.get(playerNum).getName();
		if(e.getKeyChar() == ' '){
			if(name == "") {
				name = "PLAYER";
				allPlayers.get(playerNum).setName(name);
			}	
				gameState = 0;
		}	
		//Backspace / Delete
		else if(e.getKeyCode() == 8) {
			if(name.length() >= 1) {
				name = name.substring(0,name.length()-1);
				allPlayers.get(playerNum).setName(name.toUpperCase());
			}
		}
		else if(name.length() <= 5){
			name = name + e.getKeyChar();
			allPlayers.get(playerNum).setName(name.toUpperCase());
		}
	}

	//Updates player based on if any of their keys are set to true (being pressed), then calls hitboxCheck, 
	//and obstaclesCheck before updating the players position
	//Parameters: the playerNumber of the one moving, and their current gravity value
	//Returns updated gravity value of player
	public int updatePlayer(int playerNum, int gravity) {
		Player p = allPlayers.get(playerNum);
		if(playerNum == 0) {
			if(keyD == true) {
				if(p.getFacingRight() == 1) {
					p.setFacingRight(0);
				}
				if(obstaclesCheck(playerNum, 1, gravity)) {
					if(hitboxCheck(playerNum,2,gravity)) {
						if(p.getX() < 820 - hitboxX) {
							p.setX(p.getX() + 5);
						}
						else {
							if(noneFarLeft()) {
								p.setX(820 - hitboxX);
								scrollX = scrollX - 5;
								for(int i = playerCount; i >= 1; i--) {
									allPlayers.get(i).setX(allPlayers.get(i).getX() - 5);
								}
							}
						}
					}
				}
			}
			else if(keyA == true) {
				if(p.getFacingRight() == 0) {
					p.setFacingRight(1);
				}
				if(obstaclesCheck(playerNum, 2, gravity)) {
					if(hitboxCheck(playerNum,3,gravity)) {
						if(p.getX() > hitboxX) {
							p.setX(p.getX() - 5);
						}
						else {
							if(noneFarRight()) {
								p.setX(hitboxX);
								scrollX = scrollX + 5;
								for(int i = playerCount; i >= 1; i--) {
									allPlayers.get(i).setX(allPlayers.get(i).getX() + 5);
								}
							}
						}
					}
				}
			}	
			if(keyW == true) {
				if(p.getIsJumping() == false) {
					p.setIsJumping(true);
				}
			}
		}
		else if(playerNum == 1) {
			if(keyL == true) {
				if(p.getFacingRight() == 1) {
					p.setFacingRight(0);
				}
				if(obstaclesCheck(playerNum, 1, gravity)) {
					if(hitboxCheck(playerNum,2,gravity)) {
						if(p.getX() < 820 - hitboxX) {
							p.setX(p.getX() + 5);
						}
						else {
							if(noneFarLeft()) {
								p.setX(820 - hitboxX);
								scrollX = scrollX - 5;
								
								allPlayers.get(0).setX(allPlayers.get(0).getX() - 5);
								for(int i = playerCount; i >= 2; i--) {
									allPlayers.get(i).setX(allPlayers.get(i).getX() - 5);
								}
							}
						}
					}
				}
			}
			else if(keyJ == true) {
				if(p.getFacingRight() == 0) {
					p.setFacingRight(1);
				}
				if(obstaclesCheck(playerNum, 2, gravity)) {
					if(hitboxCheck(playerNum,3,gravity)) {
						if(p.getX() > hitboxX) {
							p.setX(p.getX() - 5);
						}
						else {
							if(noneFarRight()) {
								p.setX(hitboxX);
								scrollX = scrollX + 5;
								
								allPlayers.get(0).setX(allPlayers.get(0).getX() + 5);
								for(int i = playerCount; i >= 2; i--) {
									allPlayers.get(i).setX(allPlayers.get(i).getX() + 5);
								}
							}
						}
					}
				}
			}	
			if(keyI == true) {
				if(p.getIsJumping() == false) {
					p.setIsJumping(true); 
				}
			}
		}
		else if(playerNum == 3) {
			if(keyH == true) {
				if(p.getFacingRight() == 1) {
					p.setFacingRight(0);
				}
				if(obstaclesCheck(playerNum, 1, gravity)) {
					if(hitboxCheck(playerNum,2,gravity)) {
						if(p.getX() < 820 - hitboxX) {
							p.setX(p.getX() + 5);
						}
						else {
							if(noneFarLeft()) {
								p.setX(820 - hitboxX);
								scrollX = scrollX - 5;
								
								allPlayers.get(0).setX(allPlayers.get(0).getX() - 5);
								allPlayers.get(1).setX(allPlayers.get(1).getX() - 5);
								allPlayers.get(2).setX(allPlayers.get(2).getX() - 5);
							}
						}
					}
				}
			}
			else if(keyF == true) {
				if(p.getFacingRight() == 0) {
					p.setFacingRight(1);
				}
				if(obstaclesCheck(playerNum, 2, gravity)) {
					if(hitboxCheck(playerNum,3,gravity)) {
						if(p.getX() > hitboxX) {
							p.setX(p.getX() - 5);
						}
						else {
							if(noneFarRight()) {
								p.setX(hitboxX);
								scrollX = scrollX + 5;
								
								allPlayers.get(0).setX(allPlayers.get(0).getX() + 5);
								allPlayers.get(1).setX(allPlayers.get(1).getX() + 5);
								allPlayers.get(2).setX(allPlayers.get(2).getX() - 5);
							}
						}
					}
				}
			}	
			if(keyT == true) {
				if(p.getIsJumping() == false) {
					p.setIsJumping(true);
				}
			}
		}
		if(playerNum == 2) {
			if(keyRight == true) {
				if(p.getFacingRight() == 1) {
					p.setFacingRight(0);
				}
				if(obstaclesCheck(playerNum, 1, gravity)) {
					if(hitboxCheck(playerNum,2,gravity)) {
						if(p.getX() < 820 - hitboxX) {
							p.setX(p.getX() + 5);
						}
						else {
							if(noneFarLeft()) {
								p.setX(820 - hitboxX);
								scrollX = scrollX - 5;
								
								allPlayers.get(0).setX(allPlayers.get(0).getX() - 5);
								allPlayers.get(1).setX(allPlayers.get(1).getX() - 5);
								allPlayers.get(3).setX(allPlayers.get(3).getX() - 5);
							}
						}
					}
				}
			}
			else if(keyLeft == true) {
				if(p.getFacingRight() == 0) {
					p.setFacingRight(1);
				}
				if(obstaclesCheck(playerNum, 2, gravity)) {
					if(hitboxCheck(playerNum,3,gravity)) {
						if(p.getX() > hitboxX) {
							p.setX(p.getX() - 5);
						}
						else {
							if(noneFarRight()) {
								p.setX(hitboxX);
								scrollX = scrollX + 5;
								
								allPlayers.get(0).setX(allPlayers.get(0).getX() + 5);
								allPlayers.get(1).setX(allPlayers.get(1).getX() + 5);
								allPlayers.get(3).setX(allPlayers.get(3).getX() + 5);
							}
						}
					}
				}
			}	
			if(keyUp == true) {
				if(p.getIsJumping() == false) {
					p.setIsJumping(true);
				}
			}
		}
		//Checks to see where the star is to see if they should end the level
		if(scrollX < -1000) {
			starCheck(p.getX(), p.getY());
		}
		//SHOULD JUMP
		if(p.getIsJumping()) {
			if(hitboxCheck(playerNum,4,gravity)) {
				if(hitboxCheck(playerNum,1,gravity) && obstaclesCheck(playerNum, 3, gravity)) {
					p.setY(p.getY() - 15);
				}
			}
		}
				
		//Checks if Should Fall
		//SHOULDNT FALL
		if(!hitboxCheck(playerNum,4,gravity) || p.getY() >= 560 - hitboxY || (!obstaclesCheck(playerNum, 0, gravity))) {
			//On the ground
			if(p.getY() >= 560 - hitboxY) {
				p.setY(560 - hitboxY);
				gravity = 0;
				p.setIsJumping(false);
			}
			//If other box has jump will continue 
			if(!hitboxCheck(playerNum,5,gravity)){
				gravity = 0;
				p.setIsJumping(false);
			}
			//True = should fall
			else if(!obstaclesCheck(playerNum, 0, gravity)) {
				gravity = 0;
				p.setIsJumping(false);
			}
		}
		//SHOULD FALL
		else {
			p.setY(p.getY() + gravity);
			gravity += 1;
			if(p.getY() >= 560 - hitboxY) {
				p.setIsJumping(false);
				p.setY(560 - hitboxY);
				gravity = 0;
			}
		}
		
		return gravity;
	}
	//Checks if a player will hit an obstacle
	//Parameters: the playerNumber of the one moving, the direction they are moving, and their current gravity value
	//False if it will hit an obstacle, means the player shouldn't move
	//True if it is okay to move
	public static boolean obstaclesCheck(int playerNum, int direction, int gravity) {
		Player p = allPlayers.get(playerNum);
		int x = p.getX();
		int y = p.getY();
		
		//Falling Down = 0
		//Jumping = 3
		if(direction == 0 || direction == 3) {
			for(int i = 0; i < allObstacles.size(); i++) {
				//RightSide >= ObstacleLeft && RightSide <= ObstacleRight
				//|| LeftSide >= ObstacleLeft && LeftSide <= ObstacleRight
				//|| Middle >= ObstacleLeft && Middle <= ObstacleRight
				if((x + hitboxX >= allObstacles.get(i).getX() + scrollX && x + hitboxX <= allObstacles.get(i).getX() + scrollX + allObstacles.get(i).getWidth()) || (x >= allObstacles.get(i).getX() + scrollX && x <= allObstacles.get(i).getX() + scrollX + allObstacles.get(i).getWidth()) || (x + (hitboxX / 2) >= allObstacles.get(i).getX() + scrollX && x + (hitboxX / 2) <= allObstacles.get(i).getX() + scrollX + allObstacles.get(i).getWidth()) ) {
					if(direction == 0) {
						if((y + hitboxY + gravity > allObstacles.get(i).getY()) && (y + hitboxY + gravity < allObstacles.get(i).getY() + allObstacles.get(i).getHeight())){
							//SHOULD NOT FALL	
							p.setY(p.getY() + (gravity/3));			
							if(p.getY() + hitboxY >= allObstacles.get(i).getY() || p.getY() + 2 + hitboxY >= allObstacles.get(i).getY()) {
								p.setY(allObstacles.get(i).getY() - hitboxY);
							}
							return false;
						}
					}
					if(direction == 3) {
						if((y + gravity - 15 <= allObstacles.get(i).getY() + allObstacles.get(i).getHeight()) && (y + gravity - 15 >= allObstacles.get(i).getY())){
							return false;
						}
					}
				}
			}
		}
		//Going Right
		else if(direction == 1) {
			for(int i = 0; i < allObstacles.size(); i++) {
				if(x + hitboxX + 5 == allObstacles.get(i).getX() + scrollX) {
					if(y + hitboxY > allObstacles.get(i).getY() && y < allObstacles.get(i).getY() + allObstacles.get(i).getHeight()) {
						return false;
					}
				}
			}
		}
		//Going Left
		else if(direction == 2) {
			for(int i = 0; i < allObstacles.size(); i++) {
				if(x - 5 == allObstacles.get(i).getX() + allObstacles.get(i).getWidth() + scrollX ) {
					if(y + hitboxY > allObstacles.get(i).getY() && y < allObstacles.get(i).getY() + allObstacles.get(i).getHeight()) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	//Checks if two players will hit each other
	//Parameters: the playerNumber of the one moving, the direction they are moving, and their current gravity value
	//False if it will hit them, means the player shouldn't move
	//True if it is okay to move
	public static boolean hitboxCheck(int playerNum, int direction, int gravity) {
		//1 = Jump
		//2 = Right
		//3 = Left
		//4 = Down
		Player p = allPlayers.get(playerNum);
		int x = p.getX();
		int y = p.getY();
		
		if(direction == 1) {
			int gravValue = -15 + gravity;
			for(int i = 0; i <= playerCount; i++) {
				if(i != playerNum) {
					//Hitting Bottom of Other Box
					if((y + gravValue <= allPlayers.get(i).getY() + hitboxY) && (y + gravValue >= allPlayers.get(i).getY())){
						if((x + hitboxX > allPlayers.get(i).getX() && x + hitboxX < allPlayers.get(i).getX() + hitboxX) || (x > allPlayers.get(i).getX() && x < allPlayers.get(i).getX() + hitboxX) || x == allPlayers.get(i).getX()) {
							return false;
						}
					}
				}
			}
		}
		else if(direction == 2) {
			for(int i = 0; i <= playerCount; i++) {
				if(i != playerNum) {
					if(x + hitboxX == allPlayers.get(i).getX()) {
						if((y > allPlayers.get(i).getY() && y < allPlayers.get(i).getY() + hitboxY) || (y + hitboxY > allPlayers.get(i).getY() && y + hitboxY < allPlayers.get(i).getY() + hitboxY) || (y == allPlayers.get(i).getY())) {
							return false;
						}
					}
				}
			}
		}
		else if(direction == 3) {
			for(int i = 0; i <= playerCount; i++) {
				if(i != playerNum) {
					if(x == allPlayers.get(i).getX() + hitboxX) {
						if((y > allPlayers.get(i).getY() && y < allPlayers.get(i).getY() + hitboxY) || (y + hitboxY > allPlayers.get(i).getY() && y + hitboxY < allPlayers.get(i).getY() + hitboxY) || (y == allPlayers.get(i).getY())) {
							return false;
						}
					}
				}
			}
		}
		//Checks If Should Fall
		else if(direction == 4) {
			int gravValue = gravity;
			
			for(int i = 0; i <= playerCount; i++) {
				if(i != playerNum) {
					//Hitting Top of Other Box
					if((y + hitboxY + gravValue > allPlayers.get(i).getY()) && (y + hitboxY + gravValue < allPlayers.get(i).getY() + hitboxY)){
						if((x + hitboxX > allPlayers.get(i).getX() && x + hitboxX < allPlayers.get(i).getX() + hitboxX) || (x > allPlayers.get(i).getX() && x < allPlayers.get(i).getX() + hitboxX) || x == allPlayers.get(i).getX()) {
							//SHOULD NOT FALL	
							p.setY(p.getY() + (gravity/3));			
							if(p.getY() + hitboxY >= allPlayers.get(i).getY() || p.getY() + 2 + hitboxY >= allPlayers.get(i).getY()) {
								p.setY(allPlayers.get(i).getY() - hitboxY);
							}
							return false;
						}
					}
				}
			}
		}
		//FOR IF SHOULDNT FALL AND THE OTHER BOX IS ON THE GROUND 
		//(RESET JUMP)
		else if(direction == 5) {
			for(int i = 0; i <= playerCount; i++) {
				if(i != playerNum) {
					//Hitting Top of Other Box
					if(y + hitboxY == allPlayers.get(i).getY()){
						if((x + hitboxX > allPlayers.get(i).getX() && x + hitboxX < allPlayers.get(i).getX() + hitboxX) || (x > allPlayers.get(i).getX() && x < allPlayers.get(i).getX() + hitboxX) || x == allPlayers.get(i).getX()) {		
							//Other box is not in the air
							if(allPlayers.get(i).getIsJumping() == false) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	//Ensures no one is on the far left of the map
	//Returns true if none on the far left of the map
	//Returns false if a player is on the far left of the map, this is so the screen does not scroll
	//and leave a player off screen
	public static boolean noneFarLeft() {
		for(int i = playerCount; i >= 0; i--) {
			if(allPlayers.get(i).getX() <= hitboxX) {
				return false;
			}
		}
		return true;
	}
	//Ensures no one is on the far right of the map
	//Returns true if none on the far right of the map
	//Returns false if a player is on the far right of the map, this is so the screen does not scroll
	//and leave a player off screen
	public static boolean noneFarRight() {
		for(int i = playerCount; i >= 0; i--) {
			if(allPlayers.get(i).getX() >= 820 - hitboxX) {
				return false;
			}
		}
		return true;
	}
	//Returns true if touching a player - level ends and players lose
	//Updates location of enemy based on what direction they are facing
	//Will change direction if enemy reaches one side of the space they are moving back and forth between
	public static boolean updateEnemies() {
		for(int i = 0; i <= allEnemies.size() - 1; i++) {
			//true = right
			if(allEnemies.get(i).getDirection() == false ) {
				allEnemies.get(i).setX(allEnemies.get(i).getX() - 5);
				if(allEnemies.get(i).getX() - 5 <= allEnemies.get(i).getStartX()) {
					allEnemies.get(i).setDirection(true);
				}
			}
			else {
				allEnemies.get(i).setX(allEnemies.get(i).getX() + 5);
				if(allEnemies.get(i).getX() + 5 >= allEnemies.get(i).getEndX()) {
					allEnemies.get(i).setDirection(false);
				}
			}
			//Collision
			int x = allEnemies.get(i).getX() + scrollX;
			int y = allEnemies.get(i).getY();
			for(int j = 0; j <= allPlayers.size() - 1; j++) {
				if((x >= allPlayers.get(j).getX() && x <= allPlayers.get(j).getX() + hitboxX) || (x + hitboxX <= allPlayers.get(j).getX() + hitboxX && x + hitboxX >= allPlayers.get(j).getX())){
					if((y > allPlayers.get(j).getY() && y < allPlayers.get(j).getY() + hitboxY) || (y + hitboxY > allPlayers.get(j).getY() && y + hitboxY < allPlayers.get(j).getY() + hitboxY) || (y == allPlayers.get(j).getY())) {
						//Collision
						//System.out.println(allPlayers.get(j).getName() + " Died to Enemy #" + i + " Player #" + (j +1));
						return true;
					}
				}
			}
		}
		//No collision
		return false;
	}
	
	//Star Check (End Level)
	//Checks players x and y coords to see if they are touching the star, if yes level will end
	public void starCheck(int x, int y) {
		int starX = star.getX() + scrollX;
		int starY = star.getY();
		//75 x 70 dimensions for star
		if((x + hitboxX >= starX && x + hitboxX <= starX + 75) || (x >= starX && x <= starX + 75)) {
			if((y + hitboxY >= starY && y + hitboxY <= starY + 70) || (y >= starY && y <= starY + 70)) {
				saveTime(); 
				try {
					getHighscores();
				}
				catch(Exception g) {
				}
		        Collections.sort(highscores, new HighscoreScoreComparator());
		        gameState = 7;
				scrollX = 0;
			}
		}
	}
	public void saveTime() {
		String textFile = "";
		if(levelNum == 1) {
			textFile = "level1Scores.txt";
		}
		else if(levelNum == 2) {
			textFile = "level2Scores.txt";
		}
		else if(levelNum == 3) {
			textFile = "level3Scores.txt";
		}
		try {
			String str;
			BufferedReader in = new BufferedReader (new FileReader(textFile));
			ArrayList<Highscore> highscoreHolder = new ArrayList<>();
			double score;
			while((str= in.readLine()) != null) {
				score = Double.parseDouble(str.substring(str.indexOf(' ')));
				str = str.substring(0, str.indexOf(' '));
				highscoreHolder.add(new Highscore(str, score));
			}
			try {
				String line = allPlayers.get(0).getName();
				if(playerCount > 0) {
					for(int i = 1; i <= playerCount; i++) {
						line = line + "," + allPlayers.get(i).getName();
					}
				}
				double fullTime = Math.round((double)time/100);
				fullTime = (double)fullTime /10;
				
				PrintWriter out = new PrintWriter (new FileWriter (textFile));
				for(int i = 0; i < highscoreHolder.size(); i++) {
					out.println(highscoreHolder.get(i).inputToFile());
				}
				out.println (line + " " + fullTime);
				out.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			in.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	//Loads in highscores from text file
	public void getHighscores() throws IOException{
		String textFile = "";
		if(levelNum == 1) {
			textFile = "level1Scores.txt";
		}
		else if(levelNum == 2) {
			textFile = "level2Scores.txt";
		}
		else if(levelNum == 3) {
			textFile = "level3Scores.txt";
		} 
		String str;
		double score;
		highscores.clear();
		try {
			BufferedReader in = new BufferedReader (new FileReader(textFile));
			while((str= in.readLine()) != null) {
				score = Double.parseDouble(str.substring(str.indexOf(' ')));
				str = str.substring(0, str.indexOf(' '));
				highscores.add(new Highscore(str, score));
			}
			in.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	//Reset Game
	//Sets all variables back to what they should be at the start of the game
	public void resetGame() {
		scrollX = 0;
		allPlayers.get(0).setX(75);
		allPlayers.get(0).setY(425);
		allPlayers.get(0).setIsJumping(false);
		
		allPlayers.get(1).setX(200);
		allPlayers.get(1).setY(425);
		allPlayers.get(1).setIsJumping(false);
		
		allPlayers.get(2).setX(325);
		allPlayers.get(2).setY(425);
		allPlayers.get(2).setIsJumping(false);
		
		allPlayers.get(3).setX(450);
		allPlayers.get(3).setY(425);
		allPlayers.get(3).setIsJumping(false);
	}
	
	//Levels
	//Adds any obstacles and enemies to the level
	//Sets star location for the level
	public void level1() {
		gameState = 5;
		startingTime = System.currentTimeMillis();
		allObstacles.clear();
		allEnemies.clear();
		//X, Width, Y, Height
		//Y max = 560
		//Front Barrier
		allObstacles.add(new Obstacles(-50, 50, 0, 560));
		
		//Easy
		allObstacles.add(new Obstacles(500, 50, 500, 60));
		allObstacles.add(new Obstacles(700, 50, 500, 60));
		allEnemies.add(new Enemy(750, 1000, 510, true));
		allObstacles.add(new Obstacles(1050, 50, 500, 60));
		allEnemies.add(new Enemy(1100, 1650, 510, true));
		allObstacles.add(new Obstacles(1700, 50, 500, 60));
		
		//Floor 1
		allObstacles.add(new Obstacles(1850, 500, 400, 50));
		//Jumps
		allObstacles.add(new Obstacles(1950, 50, 300, 50));
		allObstacles.add(new Obstacles(2060, 50, 190, 25));
		allObstacles.add(new Obstacles(1950, 50, 130, 25));
		allObstacles.add(new Obstacles(2070, 55, 80, 25));
		
		allObstacles.add(new Obstacles(2300, 50, 100, 50));

		//Back Barrier
		allObstacles.add(new Obstacles(2700, 100, 0, 560));
		star.setX(2580);
		star.setPresetY(50);
	}
	public void level2() {
		
		//scrollX = -2500;
		
		
		gameState = 5;
		startingTime = System.currentTimeMillis();
		allObstacles.clear();
		allEnemies.clear();
		//X, Width, Y, Height
		//Y max = 560
		//Front Barrier
		allObstacles.add(new Obstacles(-50, 50, 0, 560));
		//Maze
		allObstacles.add(new Obstacles(500, 500, 500, 60));
		allObstacles.add(new Obstacles(1000, 50, 200, 360));
		allObstacles.add(new Obstacles(600, 340, 400, 50));
		allObstacles.add(new Obstacles(600, 30, 100, 300));
		allObstacles.add(new Obstacles(700, 300, 290, 35));	
		//Easy Parkour
		allObstacles.add(new Obstacles(1200, 50, 450, 50));
		allObstacles.add(new Obstacles(1325, 60, 375, 50));
		allObstacles.add(new Obstacles(1450, 50, 300, 60));
		allObstacles.add(new Obstacles(1560, 50, 250, 310));
		
		//Hard Parkour
		allObstacles.add(new Obstacles(1700, 50, 500, 60));
		//Floor 1
		allObstacles.add(new Obstacles(1850, 500, 400, 50));
		allObstacles.add(new Obstacles(1950, 50, 300, 50));
		allObstacles.add(new Obstacles(2070, 75, 180, 25));
		allObstacles.add(new Obstacles(2300, 50, 100, 460));
		
		//Star (Level End) (Updates based on player count)
		star.setX(2725);
		star.setPresetY(450);
		//Have to stack
		if(playerCount >= 1) {
			allObstacles.add(new Obstacles(2700, 25, 400, 100));
			star.setX(2725);
			star.setPresetY(350);
			if(playerCount >= 2) {
				allObstacles.add(new Obstacles(2900, 25, 300, 100));
				star.setX(2925);
				star.setPresetY(250);
				if(playerCount >= 3) {
					allObstacles.add(new Obstacles(3125, 25, 200, 100));
					star.setX(3150);
					star.setPresetY(150);
				}
			}
		}	
		//Enemies 
		allEnemies.add(new Enemy(700, 950, 240, true));
		allEnemies.add(new Enemy(1850, 2250, 350, true));
		//End Barrier
		allObstacles.add(new Obstacles(3300, 50, 0, 560));
	}
	public void level3() {
		gameState = 5;
		startingTime = System.currentTimeMillis();
		allObstacles.clear();
		allEnemies.clear();
		//X, Width, Y, Height
		//Y max = 560
		//Front Barrier
		allObstacles.add(new Obstacles(-50, 50, 0, 560));
		
		//Can't fail
		allEnemies.add(new Enemy(550, 1850, 510, true));
		allObstacles.add(new Obstacles(500, 50, 450, 110));
		allObstacles.add(new Obstacles(550, 50, 340, 110));
		allObstacles.add(new Obstacles(750, 50, 340, 50));
		allObstacles.add(new Obstacles(950, 50, 340, 50));
		allObstacles.add(new Obstacles(1175, 50, 340, 50));
		allObstacles.add(new Obstacles(1400, 50, 340, 50));
		allObstacles.add(new Obstacles(1645, 50, 340, 50));
		allObstacles.add(new Obstacles(1890, 50, 340, 220));
		
		if(playerCount == 0) {
			allObstacles.add(new Obstacles(2300, 50, 450, 110));
			allObstacles.add(new Obstacles(2350, 50, 340, 110));
			allEnemies.add(new Enemy(2400, 2500, 510, true));
			star.setX(2620);
			star.setPresetY(260);
		}
		else if(playerCount == 1) {
			allObstacles.add(new Obstacles(2300, 50, 400, 160));
			allObstacles.add(new Obstacles(2350, 50, 340, 60));
			allEnemies.add(new Enemy(2400, 2500, 510, true));
			star.setX(2620);
			star.setPresetY(260);
		}
		else if(playerCount == 2) {
			//3 players
			//1 get through
			allObstacles.add(new Obstacles(2300, 50, 400, 160));
			allObstacles.add(new Obstacles(2350, 50, 240, 160));
			allEnemies.add(new Enemy(2400, 2500, 510, true));
			star.setX(2620);
			star.setPresetY(160);
		}
		else if(playerCount == 3) {
			//4 players
			//1 get through
			//560
			allObstacles.add(new Obstacles(2550, 50, 510, 50));
			allObstacles.add(new Obstacles(2300, 300, 460, 50));
			allObstacles.add(new Obstacles(2250, 50, 400, 110));
			allObstacles.add(new Obstacles(2300, 50, 240, 160));
			allObstacles.add(new Obstacles(2350, 50, 80, 160));
			allEnemies.add(new Enemy(2700, 2900, 510, true));
			star.setX(2620);
			star.setPresetY(0);
		}
		
		//End Barrier
		allObstacles.add(new Obstacles(3300, 50, 0, 560));
	}


	
	//Useless

	//Very fast press and release 
	//Use Either Click OR MousePressed/Released 
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	//Right away even if you leave finger on button it will say pressed once
	public void mousePressed(MouseEvent e) {
//		// TODO Auto-generated method stub
//		paintComponent(this.getGraphics());
		System.out.println("X: " + (e.getX() - scrollX));
		System.out.println("Y: " + e.getY());
	}
	//Once you let go of button says released
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	//Mouse is on the panel (doesnt need clicks)
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	//Mouse leaves the panel((doesnt need clicks)
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
}

