package utilities.particles;

import java.awt.Graphics2D;

public abstract class Particle {
	ParticleSystem system;
	int x;
	int y;
	int width;
	int height;
	
	int lifeSpan;
	int lifeTime;
	
	int frames;
	int frame;
	javax.swing.Timer deleter;
	
	
	protected Particle(ParticleSystem system, int x, int y, int width, int height, int duration) {
		this.system = system;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.lifeSpan = duration;
		this.frames = 10;
		this.frame = 0;
		this.lifeTime = 0;
		
//		javax.swing.Timer deleter = new javax.swing.Timer(duration, l -> {
//			system.currentParticles.remove(this);
//			system.repaint();
//			timer.stop();
//			System.out.println("particle deleted!!!!");
//		});
//		deleter.setRepeats(false);
//		deleter.start();
	}
	
	public abstract void paint(Graphics2D g2D);
}
