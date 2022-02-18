package pacman.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import pacman.PacMan;
import utilities.Scheduler;

public abstract class Entity {
//	public int direction;
	public Direction direction;
	private double x, y;
	protected boolean moving;
	public boolean canMove;
	public double size = 1;
	
	private int moveFrames;
	
	protected Color color;
	protected PacMan game;
	
	
	protected Entity(PacMan game, int row, int col) {
		this.game = game;
		x = (double)col;
		y = (double)row;
		canMove = true;
		moving = false;
		moveFrames = 4;
		direction = Direction.NONE;
	}
	
	public void move() {
		
		if (!moving && canMove && getPosInFront() != PacMan.WALL) {
			moving = true;
			double xOffset = (double)direction.getXComponent() / moveFrames;
			double yOffset = (double)direction.getYComponent() / moveFrames;
			
			Runnable movementTask = () -> {
				if (canMove) { 
					moveTo(x + xOffset, y + yOffset);
					Rectangle bounds = getScreenBounds();
					SwingUtilities.invokeLater(() -> game.repaint(bounds.x, bounds.y, bounds.width, bounds.height) );
				}
			};
			
			Runnable movementEnd = () -> {
				moving = false;
				x = Math.round(x);
				y = Math.round(y);
				performChecks();
				
				if (getPosInFront() == PacMan.WALL) {
					canMove = false;
				} else {
					canMove = true;
					System.out.println(this + " can move");
				}
			};
			
			Scheduler.scheduleAtFixedRate(movementTask, movementEnd, 0, PacMan.FRAMETIME, PacMan.FRAMETIME * moveFrames, TimeUnit.MILLISECONDS);
		}
		
	}
	
	public abstract void tick();
	public abstract void performChecks();
	public abstract void render(Graphics2D g2D);
	
	public void switchDirection(Direction direction) {
		this.direction = direction;
		if (getPosInFront() != PacMan.WALL)
			canMove = true;
	}
	
	public void moveTo(double x, double y) {
		this.x = x;
		this.y = y;
		
		if (x < 0) { this.x = game.getBoard()[0].length - 1; }
		else if (x > game.getBoard()[0].length) { this.x = 0; }
	}
	
	public int getPosInFront() {
		int pos = game.getValueAt(getRow() + direction.getYComponent(), getCol() + direction.getXComponent());
		return pos; 
	}
	
	public Rectangle getScreenBounds() {
		Rectangle bounds = new Rectangle();
		bounds.x = (int) ((x * PacMan.SQUARESIZE + game.padding.top) + ((PacMan.SQUARESIZE - size * PacMan.SQUARESIZE) / 2));
		bounds.y = (int) ((y * PacMan.SQUARESIZE + game.padding.left) + ((PacMan.SQUARESIZE - size * PacMan.SQUARESIZE) / 2));
		bounds.width = (int) (PacMan.SQUARESIZE * size);
		bounds.height = (int) (PacMan.SQUARESIZE * size);
		return bounds;
	}
	
	public int getRow() { return (int)y; }
	public int getCol() { return (int)x; }
	
	public Direction getDirection() { return direction; };
	
	public double getX() { return x; }
	public double getY() { return y; }
	public double getSize() { return size; }
	
	public Rectangle2D.Double getHitBox() {
		return new Rectangle2D.Double(x, y, 1, 1);
	}
	
	public void setMoveFrames(int speed) {
		moveFrames = speed;
	}
	
	public int getMoveFrames() {
		return moveFrames;
	}
	
}
