package screens;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.time.Duration;
import java.time.Instant;
import java.awt.Color;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import utilities.Scheduler;


public class Transition extends JPanel {
	
	private int frames;
	private int currentFrame;
	private int transitionTime;
	private int mouthAngle; //angle of mouth opening
	private int maxMouthAngle;
	private int incrementAngle;
	private int sizeIncrement;
	private int frameTime;
	
	private int screenSwitchTime;
	Instant lastFrame;
	
	
	private int startAngle; //angle that mouth points in
	Rectangle pacman;
	
	
	Transition() {
		maxMouthAngle = 90;
		mouthAngle = 0;
		incrementAngle = 20;
		transitionTime = 3000;
		screenSwitchTime = transitionTime / 2;
		frames = 60;
		setOpaque(false);
		pacman = new Rectangle(getWidth() / 2, getHeight() / 2, 1, 1);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2D = (Graphics2D) g;
		
		if (lastFrame == null) return;
		if (Duration.between(lastFrame, Instant.now()).toMillisPart() >= frameTime) {
			update();
			lastFrame = Instant.now();
		}
		
		g2D.setPaint(Color.YELLOW);
		g2D.fillOval(pacman.x, pacman.y, pacman.width, pacman.height);
		
		g2D.setPaint(Color.BLACK);
		g2D.fillArc(pacman.x, pacman.y, pacman.width, pacman.height, startAngle - mouthAngle / 2, mouthAngle);
	}
	
	private void update() {
		currentFrame++;
		System.out.println("Current frame: " + currentFrame);
		
		if (currentFrame == frames / 2) sizeIncrement = -sizeIncrement;
		
		mouthAngle += incrementAngle;
		System.out.println(mouthAngle);
		if (mouthAngle >= maxMouthAngle || mouthAngle <= 0) {
			incrementAngle = -incrementAngle;
		}
		
		pacman.width += sizeIncrement;
		pacman.height += sizeIncrement;
		pacman.x -= sizeIncrement / 2;
		pacman.y -= sizeIncrement / 2;
		startAngle = startAngle > 360 ? 0 : startAngle + 20;
	}
	
	public void start() {
		currentFrame = 0;
		frameTime = transitionTime / frames;
		sizeIncrement = Math.max(getWidth(), getHeight()) / (frames / 4);
		pacman = new Rectangle(getWidth() / 2, getHeight() / 2, 1, 1);
		System.out.println(getSize());
		lastFrame = Instant.now();
		Runnable repaint = () -> SwingUtilities.invokeLater(() -> repaint());
		Scheduler.scheduleAtFixedRate(repaint, 0, frameTime, transitionTime, TimeUnit.MILLISECONDS);
	}
	
	public int getTransitionTime() {
		return transitionTime;
	}
	
	public int getScreenSwitchTime() {
		return screenSwitchTime;
	}
}