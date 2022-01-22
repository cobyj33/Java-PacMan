package utilities.particles;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

//import conquest.Main;
import pacman.Main;

//import conquest.Main;

public class StringParticle extends Particle {
	String string;
	Font font;
	float fontSize;
	float bloom;
	int width;
	int height;
	
	public StringParticle(ParticleSystem system, int x, int y, int duration, String string) {
		super(system, x, y, 0, 0, duration);
		this.string = string;
		//font = Main.fonts.defaultFont;
		font = new Font("Times New Roman", Font.PLAIN, 12);
		fontSize = 12f;
		bloom = 0.5f;
		frames = 30;
		 //System.out.println("System: " + system);
		System.out.println("Frame time: " + duration / frames);

		Future<?> future = Main.scheduler.scheduleAtFixedRate(() -> {
			fontSize += bloom;
			font = font.deriveFont(fontSize);
			//System.out.println("calling repaint");
			Graphics g = system.getGraphics();
			g.setFont(font);
			FontMetrics metrics = g.getFontMetrics();
			//metrics.get
			int width = metrics.stringWidth(string);
			int height = metrics.getHeight();
			
			//system.revalidate();
			//system.repaint(x, y, width, metrics.getMaxDescent());
			SwingUtilities.invokeLater( () -> system.repaint(x, y - height, width + 5, height));
			//system.repaint();
		}, 0, duration / frames, TimeUnit.MILLISECONDS);
		
		deleter = new javax.swing.Timer(duration, l -> {
			system.currentParticles.remove(this);
			future.cancel(true);
			system.repaint();
			//System.out.println("Particle deleted!");
		});
		deleter.setRepeats(false);
		
		deleter.start();
	}
	
	public void setBloom(float bloom) {
		this.bloom = bloom;
	}

	@Override
	public void paint(Graphics2D g2D) {
		g2D.setPaint(Color.CYAN);
		g2D.setFont(font.deriveFont(Font.BOLD));
		g2D.drawString(string, x, y);
	}
	
}
