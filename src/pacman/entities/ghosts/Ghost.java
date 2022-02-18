package pacman.entities.ghosts;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import pacman.Main;
import pacman.PacMan;
import pacman.entities.Direction;
import pacman.entities.Entity;
import utilities.Resources;
import utilities.particles.StringParticle;

public abstract class Ghost extends Entity {
	private int state;
	public static final int SCATTER = 0, CHASE = 1, FRIGHTENED = 2, EATEN = 3;
	public static int eatMultiplier;
	
	Color originalColor;
	protected int targetRow;
	protected int targetCol;
	
	static private int defaultSpeed = 4; //time to travel 1 tile in Frames
	static private int frightenedSpeed = 7; //time to travel 1 tile in Frames
	static private int eatenSpeed = 2;
	
	
	Ghost(PacMan game, int row, int col) {
		super(game, row, col);
		// TODO Auto-generated constructor stub
		state = SCATTER;
		direction = Direction.UP;
	}
	
	public abstract void scatter();
	public abstract void chase();
	
	
	public void frightened() {
		color = Color.blue;
		setMoveFrames(frightenedSpeed);
		Direction randomDirection = Direction.getRandom();
		targetRow = this.getRow() + randomDirection.getYComponent();
		targetCol = this.getCol() + randomDirection.getXComponent();
		
		move();
	}
	
	public void eaten() {
		//x and y are row and col in this specific case
		setMoveFrames(eatenSpeed);
		Resources.playIfFinished(Resources.RETREATING);
		targetRow = game.enemySpawn.y + game.enemySpawn.height / 2;
		targetCol = game.enemySpawn.x + game.enemySpawn.width / 2;
		
		move();
		
		if (this.getRow() == targetRow && this.getCol() == targetCol) {
			this.setState(SCATTER);
		}
		
	}
	
	
	
	public void tick() {
		
		performChecks();
		
		switch (state) {
		case SCATTER: scatter(); break;
		case CHASE: chase(); break;
		case FRIGHTENED: frightened(); break;
		case EATEN: eaten(); break;
		}
	}
	
	public void performChecks() {
		
		if (game.getPlayer().getHitBox().intersects(getHitBox())) {
			if (this.state == FRIGHTENED) {
				setState(EATEN);
				int addedScore = 200 * ((int) Math.pow(2, eatMultiplier));
				game.getPlayer().addToScore(addedScore);
				Resources.playSound(Resources.EATGHOST);
				Main.particleSystem.addParticle(new StringParticle(Main.particleSystem, (int) (this.getX() * PacMan.SQUARESIZE) + game.padding.left, (int) (this.getY() * PacMan.SQUARESIZE) + game.padding.top, 2000, "" + addedScore));
				
				eatMultiplier++;
				game.getPlayer().ghostsEaten++;
			} else if (this.state != EATEN && game.gameTimer.isRunning()) {
				game.respawn();
			}
		}
		
		if (state == EATEN && game.enemySpawn.contains(getX(), getY())) {
			setState(game.enemyState);
		}
	}
	
	public void move() {
//		System.out.println("ROW: " + getRow() + " : " + getX());
		if (state != EATEN) {
			if (game.enemySpawn.contains(getCol(), getRow())) {
				targetRow = game.enemySpawn.y - 1;
				targetCol = game.enemySpawn.x + game.enemySpawn.width / 2;
			}
		}
		
		if (!moving) {
			Direction[] possibleDirections = Direction.generateView(direction);
		double bestDistance = Double.MAX_VALUE;
		
		for (int d = 0; d < possibleDirections.length; d++) {
			Direction testDirection = possibleDirections[d];
			int nextRow = this.getRow() + testDirection.getYComponent();
			int nextCol = this.getCol() + testDirection.getXComponent();
			if (game.getValueAt(nextRow, nextCol) == PacMan.WALL || (game.enemySpawn.contains(nextCol, nextRow) && !game.enemySpawn.contains(this.getCol(), this.getRow()) && state != EATEN) ) continue;
			
			int currentDistance = (int) (Main.getDistance((double)targetCol, (double)targetRow, (double)nextCol, (double)nextRow));
			if (currentDistance < bestDistance) {
				bestDistance = currentDistance;
				switchDirection(testDirection);
				} else if (currentDistance == bestDistance) {
					if (testDirection.getPriority() < this.direction.getPriority()) {
						switchDirection(testDirection);
					}
				}
			}
		}
		
		super.move();
		}
	
	public void setState(int state) {
		
		if (state == SCATTER || state == CHASE)
			setMoveFrames(defaultSpeed);
		
		this.state = state;
		color = originalColor;
		
		switch (state) {
		case SCATTER: scatter(); break;
		case CHASE: 
			direction = direction.opposite();
			scatter();
			break;
		case FRIGHTENED: direction = direction.opposite(); 
			frightened();
			break;
		case EATEN: eaten(); break;
		}
	}
	
	public int getState() {
		return state;
	}
	
	public void render(Graphics2D g2D) {
		java.awt.Paint originalPaint = g2D.getPaint();
		Rectangle bounds = getScreenBounds();
		
		if (state != EATEN) {
			g2D.setPaint(this.color);
			
			
			if (state == FRIGHTENED) { //Blinking when frightened
				int framesFrightened = game.getFramesSinceStateSwitch();
				if (framesFrightened >= game.getScatterTime() * 3 / 4 && framesFrightened % 5 > 2) {
					g2D.setPaint(Color.WHITE);
				} else if (framesFrightened >= game.getScatterTime() * 1 / 2 && framesFrightened % 15 > 7) {
					g2D.setPaint(Color.WHITE);
				}
			}
			
			g2D.fill(bounds);
		}
		
		
		
		int eyeSize = bounds.width / 4;
		int verticalDirection = direction.getYComponent();
		int horizontalDirection = direction.getXComponent();
		int pupilSize = eyeSize * 2 / 3;
		
		g2D.setPaint(Color.WHITE);
		Rectangle leftEye = new Rectangle(bounds.x + eyeSize / 2, bounds.y + eyeSize, eyeSize, eyeSize);
		Rectangle rightEye = new Rectangle( bounds.x + bounds.width - eyeSize * 3 / 2, bounds.y + eyeSize, eyeSize, eyeSize);
		g2D.fill(leftEye);
		g2D.fill(rightEye);
		
		g2D.setPaint(Color.BLACK);
		Rectangle leftPupil = new Rectangle(leftEye.x + (leftEye.width / 2) - pupilSize / 2, leftEye.y + (leftEye.height / 2) - pupilSize / 2, pupilSize, pupilSize);
		Rectangle rightPupil = new Rectangle(rightEye.x + (rightEye.width / 2) - pupilSize / 2, rightEye.y + (rightEye.height / 2) - pupilSize / 2, pupilSize, pupilSize);
		int lookOffset = leftPupil.x - leftEye.x;
		
		
		leftPupil.x += lookOffset * horizontalDirection;
		leftPupil.y += lookOffset * verticalDirection;
		
		rightPupil.x += lookOffset * horizontalDirection;
		rightPupil.y += lookOffset * verticalDirection;
		
		g2D.fill(leftPupil);
		g2D.fill(rightPupil);
		
		//see target
//		int targetSize = PacMan.SQUARESIZE / 2;
//		g2D.setPaint(this.color);
//		g2D.fillRect(targetCol * PacMan.SQUARESIZE + ( (PacMan.SQUARESIZE - targetSize) / 2), targetRow * PacMan.SQUARESIZE + ( (PacMan.SQUARESIZE - targetSize) / 2), targetSize, targetSize);
		
		g2D.setPaint(originalPaint);
	}

}
