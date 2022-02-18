package utilities;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class FallingStars {
	JComponent parent;
	int numOfStars;
	int starSize;
	int trailLength;
	int timeToNext;
	Instant last;
	public Color starColor;
	ArrayList<Rectangle> stars;
	
	public FallingStars(JComponent parent) {
		this.parent = parent;
		numOfStars = 10;
		starSize = 20;
		trailLength = 15;
		timeToNext = 0;
		last = Instant.now();
		starColor = Color.YELLOW;
		stars = new ArrayList<>();
		javax.swing.Timer timer = new javax.swing.Timer(50, l -> { parent.repaint(); });
		
		parent.addAncestorListener(new AncestorListener() {

			@Override
			public void ancestorAdded(AncestorEvent event) {
				timer.start();
				
			}

			@Override
			public void ancestorRemoved(AncestorEvent event) {
				timer.stop();
				
			}

			@Override
			public void ancestorMoved(AncestorEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void animate(Graphics2D g2D) {
		int redFactor = starColor.getRed() / trailLength;
		int blueFactor = starColor.getBlue() / trailLength;
		int greenFactor = starColor.getGreen() / trailLength;
		
		
		if (stars.size() < numOfStars) {
			if (timeToNext <= 0) {
				int x = (int) (Math.random() * parent.getWidth());
				stars.add(new Rectangle(x, 0, starSize, starSize));
				timeToNext = (int) (Math.random() * 200);
			} else {
				int timeElapsed = Duration.between(last, Instant.now()).toMillisPart();
				timeToNext -= timeElapsed;
			}
		}
		
		for (int s = 0; s < stars.size(); s++) {
			Rectangle star = stars.get(s);
			if (star.y - (trailLength * starSize) > parent.getHeight()) { stars.remove(s); s--; continue; }
			
			star.setLocation(star.x, star.y + starSize);
			Color currentColor = starColor;
			g2D.setColor(currentColor);
			g2D.fill(star);
			
			for (int i = 1; i < trailLength; i++) {
				currentColor = new Color(starColor.getRed() - redFactor * i, starColor.getGreen() - greenFactor * i, starColor.getBlue() - blueFactor * i);
				Rectangle trail = new Rectangle(star.x, star.y - starSize * i, starSize, starSize);
				g2D.setColor(currentColor);
				g2D.fill(trail);
			}
		}
		
	}
}
