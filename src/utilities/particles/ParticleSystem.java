package utilities.particles;

import java.util.*;
import java.util.List;

import javax.swing.*;

import pacman.Main;
import pacman.PacMan;

import java.awt.*;

@SuppressWarnings("serial")
public class ParticleSystem extends JPanel {
	
	List<Particle> currentParticles;
	static int delay = PacMan.FRAMETIME;
	
	public ParticleSystem() {
		currentParticles = new ArrayList<>();
		setBackground(Color.RED);
		setOpaque(false);
		System.out.println("System: " + this);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		System.out.println("[ParticleSystem]: num of particles: " + currentParticles.size());
		if (currentParticles.size() == 0) {
			Main.removeFromLayeredPane(this);
		}
		
		Graphics2D g2D = (Graphics2D) g;
		for (int c = 0; c < currentParticles.size(); c++) {
			currentParticles.get(c).paint(g2D);
		}
	}
	
	public void addParticle(Particle particle) {
		currentParticles.add(particle);
		if (currentParticles.size() == 1) {
			Main.addToLayeredPane(this, JLayeredPane.DRAG_LAYER);
		}
	}
	
	
}
