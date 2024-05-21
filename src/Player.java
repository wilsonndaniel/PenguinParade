
public class Player {
	private int colour;
	private String playerName;
	private boolean isJumping;
	private int facingRight;
	
	private int playerX;
	private int playerY;
	
	public Player(int colour, String playerName, boolean isJumping, int facingRight, int playerX, int playerY) {
		this.colour = colour;
		this.playerName = playerName;
		this.isJumping = isJumping;
		this.facingRight = facingRight;
		this.playerX = playerX;
		this.playerY = playerY;
	}
	public void setName(String name) {
		playerName = name;
	} 	
	public String getName() {
		return playerName;
	} 
	public void setColour(int num) {
		colour = num;
	}
	//Returns index of Colour in list
	public int getColour() {
		return colour;
	} 
	//Blue = 1
	//Red = 2
	//Green = 3
	//Yellow = 4
	public boolean getIsJumping() {
		return isJumping;
	}
	public void setIsJumping(boolean jumping) {
		isJumping = jumping;
	}
	public int getFacingRight() {
		return facingRight;
	}
	public void setFacingRight(int num) {
		facingRight = num;
	}
	
	public int getY() {
		return playerY;
	}
	public void setY(int y) {
		playerY = y;
	}
	public int getX() {
		return playerX;
	}
	public void setX(int x) {
		playerX = x;
	}

}
