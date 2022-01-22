package pacman;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import pacman.entities.Entity;
import pacman.entities.Player;
import pacman.entities.ghosts.Blinky;
import pacman.entities.ghosts.Clyde;
import pacman.entities.ghosts.Ghost;
import pacman.entities.ghosts.Inky;
import pacman.entities.ghosts.Pinky;
import screens.ScreenManager;
import utilities.Scheduler;

public class PacMan extends JPanel implements KeyListener { //idea, make a program that lets me PAINT THE MAP and returns a board for me
	public static final int EMPTY = 0, PACMAN = 1, DOT = 2, WALL = 3, RED = 4, PINK = 5, ORANGE = 6, CYAN = 7, PDOT = 8;
	public static int SQUARESIZE = 10;
	
	boolean gameOver;
	ScreenManager manager;
	
	Entity[] entities;
	
	private int[][] board;
	public Rectangle enemySpawn = new Rectangle(11, 12, 6, 4);
	
	//default board
	public static final int[][] originalBoard = {
			{3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,}, 
			 {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3,}, 
			 {3, 2, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 2, 3,}, 
			 {3, 8, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 8, 3,}, 
			 {3, 2, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 2, 3,}, 
			 {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3,}, 
			 {3, 2, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 2, 3,}, 
			 {3, 2, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 2, 3,}, 
			 {3, 2, 2, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 2, 2, 3,}, 
			 {3, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 0, 3, 3, 0, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 3,}, 
			 {0, 0, 0, 0, 0, 3, 2, 3, 3, 3, 3, 3, 0, 3, 3, 0, 3, 3, 3, 3, 3, 2, 3, 0, 0, 0, 0, 0,}, 
			 {0, 0, 0, 0, 0, 3, 2, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 2, 3, 0, 0, 0, 0, 0,}, 
			 {0, 0, 0, 0, 0, 3, 2, 3, 3, 0, 3, 3, 3, 0, 0, 3, 3, 3, 0, 3, 3, 2, 3, 0, 0, 0, 0, 0,}, 
			 {3, 3, 3, 3, 3, 3, 2, 3, 3, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0, 3, 3, 2, 3, 3, 3, 3, 3, 3,}, 
			 {0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 3, 0, 4, 5, 6, 7, 0, 3, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0,}, 
			 {3, 3, 3, 3, 3, 3, 2, 3, 3, 0, 3, 0, 0, 0, 0, 0, 0, 3, 0, 3, 3, 2, 3, 3, 3, 3, 3, 3,}, 
			 {0, 0, 0, 0, 0, 3, 2, 3, 3, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 3, 3, 2, 3, 0, 0, 0, 0, 0,}, 
			 {0, 0, 0, 0, 0, 3, 2, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 2, 3, 0, 0, 0, 0, 0,}, 
			 {0, 0, 0, 0, 0, 3, 2, 3, 3, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 3, 3, 2, 3, 0, 0, 0, 0, 0,}, 
			 {3, 3, 3, 3, 3, 3, 2, 3, 3, 0, 3, 3, 3, 3, 3, 3, 3, 3, 0, 3, 3, 2, 3, 3, 3, 3, 3, 3,}, 
			 {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3,}, 
			 {3, 8, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 8, 3,}, 
			 {3, 2, 3, 3, 3, 3, 2, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 2, 3, 3, 3, 3, 2, 3,}, 
			 {3, 2, 2, 2, 3, 3, 2, 2, 2, 2, 2, 2, 2, 1, 0, 2, 2, 2, 2, 2, 2, 2, 3, 3, 2, 2, 2, 3,}, 
			 {3, 3, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3,}, 
			 {3, 3, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 2, 3, 3, 3,}, 
			 {3, 2, 2, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 3, 3, 2, 2, 2, 2, 2, 2, 3,}, 
			 {3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3,}, 
			 {3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 3,}, 
			 {3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3,}, 
			 {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3,}
	};

	public final int middleRow = originalBoard.length / 2;
	public final int middleCol = originalBoard[middleRow].length / 2;
	public Insets insets = new Insets(2, 2, 2, 2);
	public Insets padding;
			

	public static Player pacman;
	
	public Entity blinky;
	public int pelletsInBoard;
	Entity pinky;
	Entity inky;
	Entity clyde;
	
	public HashMap<Integer, Color> colors;
	public javax.swing.Timer gameTimer;
	public static final int FRAMETIME = 50;
	public int level;
	
	private int framesSinceStateSwitch = 0;
	public int enemyState;
	
	private final int defaultStateTime = 150;
	private int scatterLength = defaultStateTime;
	private int chaseLength = defaultStateTime * 2;
	
	
	public PacMan(ScreenManager manager) {
		this.manager = manager;
		setFocusable(true);
		requestFocus();
		setBackground(Color.BLACK);
		constructBoard();
		colors = new HashMap<>();
		constructColors();
		padding = new Insets(insets.top * SQUARESIZE, insets.left * SQUARESIZE, insets.bottom * SQUARESIZE, insets.right * SQUARESIZE);
		gameTimer = new javax.swing.Timer(FRAMETIME, l -> iterateGame());
		
		addAncestorListener(new utilities.AncestorAdapter() {

			@Override
			public void ancestorAdded(AncestorEvent event) {
				if (gameOver) {
					gameOver = false;
					start();
				}
				if (!gameTimer.isRunning()) {
					resume();
				}
				requestFocus();
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				if (gameTimer.isRunning()) {
					pause();
				}
			}		
		});
		
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				int bounds = Math.min(getWidth(), getHeight());
				int size = Math.max(board.length, board[middleCol].length) + Math.max( (insets.left + insets.right) , (insets.top + insets.bottom) );
				SQUARESIZE = bounds / size;
			}
		});
		
		start();
	}
	
	public void start() {
		repaint();
		constructBoard();
		pelletsInBoard = 0;
		
//		List<Entity> loadedEntities = new ArrayList<>();
		
		resetEntityPositions();
		pelletsInBoard = (int) Arrays.stream(originalBoard).flatMapToInt(Arrays::stream).filter(x -> x == DOT || x == PDOT).count();

		addKeyListener(pacman);
		addKeyListener(this);
		
		enemyState = Ghost.SCATTER;
		
		level = 1;
		entities = new Entity[] {pacman, blinky, pinky, inky, clyde};
		repaint();
		System.out.println("STARTING GAME");
//		gameTimer.start();
		
	}
	
	private void constructBoard() {
		board = new int[originalBoard.length][originalBoard[0].length];
		for (int row = 0; row < originalBoard.length; row++) {
			for (int col = 0; col < originalBoard[row].length; col++) {
				board[row][col] = originalBoard[row][col];
			}
		}
	}
	
	private void constructColors() {
		colors.put(EMPTY, Color.BLACK);
		colors.put(DOT, Color.YELLOW);
		colors.put(PACMAN, Color.WHITE);
		colors.put(WALL, Color.BLUE);
		colors.put(RED, Color.RED);
		colors.put(PINK, Color.PINK);
		colors.put(ORANGE, Color.ORANGE);
		colors.put(CYAN, Color.CYAN);
		colors.put(PDOT, Color.YELLOW);
	}
	
	public void iterateGame() {
		for (int e = 0; e < entities.length; e++) {
			Entity current = entities[e];
			current.tick();
		}
		
		framesSinceStateSwitch++;
		
		switch (enemyState) {
		case Ghost.FRIGHTENED:
			if (framesSinceStateSwitch > scatterLength) {
				setEnemyState(Ghost.SCATTER);
			}
			
		case Ghost.SCATTER:
			if (framesSinceStateSwitch > scatterLength) {
				setEnemyState(Ghost.CHASE);
			}
			
		case Ghost.CHASE:
			if (framesSinceStateSwitch > chaseLength) {
				setEnemyState(Ghost.SCATTER);
			}
			
		}
	
		//win check
		if (pacman.pelletsEaten == pelletsInBoard * level) {
			toNextLevel();
		}
		
		repaint();
	}
	public void pause() {
		gameTimer.stop();
	}
	
	public void resume() {
		gameTimer.restart();
	}
	
	
	
	private void toNextLevel() {
		gameTimer.stop();
		System.out.println("GOING TO NEXT LEVEL");
		colors.clear();
		final int levelScreenPause = 60;
		
		Runnable screenBlink = () -> {
			
			if (colors.size() == 1) {
				constructColors();
			} else {
				colors.clear();
				colors.put(EMPTY, Color.BLACK);
			}
			
			SwingUtilities.invokeLater(() -> repaint());
		};
		
		Runnable setUpNextLevel = () -> {
			SwingUtilities.invokeLater(() -> {
				colors.clear();
				constructColors();
				constructBoard();
				resetEntityPositions();
				
				level++;
				scatterLength = (int) (defaultStateTime / (level * 2. / 3));
				chaseLength = (int) (scatterLength * Math.pow(2, level));
				gameTimer.restart();
			});
		};
		
		Scheduler.scheduleAtFixedRate(screenBlink, setUpNextLevel, 0, levelScreenPause * FRAMETIME / 10, levelScreenPause * FRAMETIME, TimeUnit.MILLISECONDS);
	}
	
	
	public void end() {
		gameTimer.stop();
		gameOver = true;
		colors.put(WALL, Color.RED);
		Runnable shrink = () -> {
			pacman.size -= pacman.size / 10;
			SwingUtilities.invokeLater(() -> repaint());
		};
		
		Runnable showDeath = () -> {
			manager.switchScreen(ScreenManager.DEATHSCREEN);
			colors.put(WALL, Color.BLUE);
		};
		
		Scheduler.scheduleAtFixedRate(shrink, showDeath, 0, FRAMETIME, FRAMETIME * 30, TimeUnit.MILLISECONDS);
	}
	
	public void setEnemyState(int state) {
		framesSinceStateSwitch = 0;
		enemyState = state;
		if (enemyState == Ghost.FRIGHTENED && state != Ghost.FRIGHTENED) {
			Ghost.ghostEaten = 0;
		}
		
		Arrays.stream(entities).forEach(e -> {
			if (e instanceof Ghost) {
				((Ghost) (e)).setState(state);
			}
		});
	}
	
	public void respawn() {
		System.out.println("Respawn");
		gameTimer.stop();
		Arrays.stream(entities).forEach(e -> e.canMove = false);
		pacman.lives--;
		
		if (pacman.lives <= 0) {
			end();
			return;
		}
		
		colors.put(WALL, Color.RED);
		
		Runnable shrink = () -> {
			pacman.size -= pacman.size / 10;
			SwingUtilities.invokeLater(() -> repaint());
		};
		
		Runnable restart = () -> {
			pacman.size = 1;
			resetEntityPositions();
			
			Arrays.stream(entities).forEach(e -> e.canMove = true);
			
			SwingUtilities.invokeLater( () -> {
				colors.put(WALL, Color.BLUE);
				gameTimer.restart();
			});
			
			}; 
			
		Scheduler.scheduleAtFixedRate(shrink, restart, 0, FRAMETIME, FRAMETIME * 30, TimeUnit.MILLISECONDS);
	}
	
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		
		padding.set(insets.top * SQUARESIZE, insets.left * SQUARESIZE, insets.bottom * SQUARESIZE, insets.right * SQUARESIZE);
		g2D.setPaint(Color.WHITE);
		
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				int currentPos = board[row][col];
				
				Color currentColor = Color.white;
				if (colors.containsKey(currentPos)) {
					currentColor = colors.get(currentPos);
				}
				
				g2D.setPaint(currentColor);
				if (currentPos == DOT || currentPos == PDOT) {
					
					if (colors.containsKey(EMPTY)) {
						g2D.setPaint(colors.get(EMPTY));
					} else {
						g2D.setPaint(Color.BLACK);
					}
					
					g2D.fillRect(col * SQUARESIZE + padding.top, row * SQUARESIZE + padding.left, SQUARESIZE, SQUARESIZE);
					g2D.setPaint(currentColor);
					
					if (currentPos == DOT) {
						g2D.fillOval(col * SQUARESIZE + SQUARESIZE / 4 + padding.top, row * SQUARESIZE + SQUARESIZE / 4 + padding.left, SQUARESIZE / 2, SQUARESIZE / 2);
					} else {
						g2D.fillOval(col * SQUARESIZE + SQUARESIZE / 8 + padding.top, row * SQUARESIZE + SQUARESIZE / 8  + padding.left, SQUARESIZE * 3 / 4, SQUARESIZE * 3 / 4);
					}
					
					
				} else {
					g2D.fillRect(col * SQUARESIZE  + padding.top, row * SQUARESIZE  + padding.left, SQUARESIZE, SQUARESIZE);
				};
				
				g2D.setPaint(colors.get(EMPTY));
				g2D.drawRect(col * SQUARESIZE  + padding.top, row * SQUARESIZE  + padding.left, SQUARESIZE, SQUARESIZE);
			}
		}
		
		Arrays.stream(entities).forEach(e -> e.render(g2D));
		
		//GUI
		Rectangle livesGUI = new Rectangle(PacMan.SQUARESIZE * (board[0].length - 5), PacMan.SQUARESIZE * (board.length) + padding.top , PacMan.SQUARESIZE * pacman.lives, PacMan.SQUARESIZE);
		int iconSize = SQUARESIZE * 2 / 3;
		int paddingX = iconSize / 2;
		
		g2D.setColor(Color.YELLOW);
		for (int i = 0; i < pacman.lives; i++) {
			g2D.fillRect(livesGUI.x + paddingX * (i + 1) + iconSize * i, livesGUI.y + (livesGUI.height - iconSize), iconSize, iconSize);
		}
		
		
		Rectangle scoreDisplay = new Rectangle(padding.left, SQUARESIZE / 2, SQUARESIZE * 5, SQUARESIZE);
		g2D.setPaint(Color.WHITE);
		String score = "SCORE: " + pacman.score;
		setFont(Main.resizeFont(getFont(), scoreDisplay, score, this).deriveFont(Font.BOLD));
		FontMetrics metrics = g2D.getFontMetrics();
		g2D.drawString(score, scoreDisplay.x + (scoreDisplay.width / 2 - metrics.stringWidth(score) / 2), scoreDisplay.y + scoreDisplay.height * 2 / 3);
		
		Rectangle levelDisplay = new Rectangle(scoreDisplay.x + scoreDisplay.width + SQUARESIZE, scoreDisplay.y, scoreDisplay.width, scoreDisplay.height);
		String levelString = "LEVEL: " + level;
		setFont(Main.resizeFont(getFont(), scoreDisplay, levelString, this).deriveFont(Font.BOLD));
		metrics = g2D.getFontMetrics();
		g2D.drawString(levelString, levelDisplay.x + (levelDisplay.width / 2 - metrics.stringWidth(levelString) / 2), levelDisplay.y + levelDisplay.height * 2 / 3);
		
//		g2D.fillRect(enemySpawn.x * SQUARESIZE + padding.left, enemySpawn.y * SQUARESIZE + padding.top, enemySpawn.width * SQUARESIZE, enemySpawn.height * SQUARESIZE);
	}
	
	
	
	public int getValueAt(int row, int col) {
		if (row < 0 || col < 0 || row >= board.length || col >= board[0].length) {
			System.out.println("[PacMan: getValueAt()] Invalid value");
			return -1;
		}
		return board[row][col];
	}
	
	public Player getPlayer() { return pacman; }
	public int[][] getBoard() { return board; }
	
	public void setPos(int val, int row, int col) {
		if (row < 0 || col < 0 || row >= board.length || col >= board[0].length) {
			System.out.println("[PacMan: setPos()] Invalid value");
			return;
		}
		board[row][col] = val;
	}
	
	public void resetEntityPositions() {
		for (int row = 0; row < originalBoard.length; row++) {
			for (int col = 0; col < originalBoard[row].length; col++) {
				switch (originalBoard[row][col]) { 
				case PACMAN: 
					if (pacman == null) { 
						pacman = new Player(this, row, col);
						board[row][col] = EMPTY;
						} else { pacman.moveTo(col, row); }
					break;
					
				case RED: 
					if (blinky == null) {
						blinky = new Blinky(this, row, col);
						board[row][col] = EMPTY;
						} else { blinky.moveTo(col, row); }
					break;
					
				case ORANGE: 
					if (clyde == null) {
						clyde = new Clyde(this, row, col);
						board[row][col] = EMPTY;
						}	else { clyde.moveTo(col, row); } 
					break;
					
				case CYAN: 
					if (inky == null) {
						inky = new Inky(this, row, col);
						board[row][col] = EMPTY;
						} else { inky.moveTo(col, row); }
					break;
				case PINK: 
					if (pinky == null) {
						pinky = new Pinky(this, row, col);
						board[row][col] = EMPTY;
						} else { pinky.moveTo(col, row); }
					break;
				}
			}
		}
	}
	
	public int getScatterTime() { return scatterLength; }
	public int getFramesSinceStateSwitch() { return framesSinceStateSwitch; }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_ESCAPE: manager.switchScreen(ScreenManager.PAUSESCREEN);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	

}