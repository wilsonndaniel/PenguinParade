
public class Star {
	private int x;
	private int y;
	private int currentY;
	private boolean goingDown;
	public Star(int x, int y) {
		this.x = x;
		this.y = y;
		this.currentY = y;
		goingDown = true;
	}
	public int getX() {
		return x;
	}
	public void setX(int newX) {
		x = newX;
	}
	public void setY(int newY) {
		currentY = newY;
	}
	public void setCurrentY(int y) {
		currentY = y;
	}
	public int getY() {
		return currentY;
	}
	public boolean getDirection() {
		return goingDown;
	}
	public void setDirection(boolean b) {
		goingDown = b;
	}
	public int getPresetY() {
		return y;
	}
	public void setPresetY(int newY) {
		currentY = newY;
		y = newY;
	}
}

