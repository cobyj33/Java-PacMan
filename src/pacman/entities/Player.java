package pacman.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import pacman.PacMan;
import pacman.entities.ghosts.Ghost;
import utilities.Resources;

public class Player extends Entity implements KeyListener {
	
	public int lives;
	private int score;
	int mouthAngle = 90;
	int incrementAngle;
	
	
	public int pelletsEaten = 0;
	public int ghostsEaten = 0;
	
	public Player(PacMan game, int row, int col) {
		super(game, row, col);
		incrementAngle = -2;
		color = Color.yellow;
		lives = 3;
		score = 0;
	}
	
	@Override
	public void performChecks() {
		int onTopOf = game.getValueAt(getRow(), getCol());
		if (onTopOf == PacMan.DOT || onTopOf == PacMan.PDOT) {
			game.setPos(PacMan.EMPTY, getRow(), getCol());
			pelletsEaten++;
			Resources.playSound(Resources.CHOMPSOUND);
			addToScore(1);
			if (onTopOf == PacMan.PDOT) {
				game.setEnemyState(Ghost.FRIGHTENED);
				addToScore(9);
			}
		}
	}
	
	public void tick() {
		super.move();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		
		switch (keyCode) { 
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP: switchDirection(Direction.UP); break;
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT: switchDirection(Direction.LEFT); break;
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN: switchDirection(Direction.DOWN); break;
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT: switchDirection(Direction.RIGHT); break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void render(Graphics2D g2D) {
		java.awt.Paint originalColor = g2D.getPaint();
		Rectangle bounds = getScreenBounds();
		
		mouthAngle += incrementAngle;
		if (mouthAngle >= 90 || mouthAngle <= 0) {
			incrementAngle = -incrementAngle;
		}
		
		int verticalDirection = direction.getYComponent();
		int horizontalDirection = direction.getXComponent();
		
		int startAngle = (int) Math.toDegrees(Math.atan2((double)-verticalDirection, (double)horizontalDirection));
		g2D.setPaint(this.color);
		g2D.fillOval(bounds.x, bounds.y, bounds.width, bounds.height);
		
		g2D.setPaint(game.colors.get(PacMan.EMPTY));
		g2D.fillArc(bounds.x, bounds.y, bounds.width, bounds.height, startAngle - mouthAngle / 2, mouthAngle);
		
		g2D.setPaint(originalColor);
	}
	
	public void addToScore(int score) {
		this.score += score;
		game.toNextLife -= score;
	}
	
	public int getScore() { return score; }
	
}
