
public class Enemy {
private int startX;
private int endX;
private boolean facingRight;
private int y;

private int x;
//facingRight false = going from endX to startX
//facingRight true = going from startX to endX
	public Enemy(int startX, int endX, int y, boolean facingRight) {
		this.startX = startX;
		this.endX = endX;
		this.y = y;
		x = startX;
	}
	public int getY() {
		return y;
	}
	public int getStartX() {
		return startX;
	}
	public int getEndX() {
		return endX;
	}
	public int getX() {
		return x;
	}
	public void setX(int newX) {
		x = newX;
	}
	public boolean getDirection() {
		return facingRight;
	}
	public void setDirection(boolean dir) {
		facingRight = dir;
	}

}
