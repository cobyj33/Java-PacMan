package pacman;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import utilities.particles.ParticleSystem;
import screens.ScreenManager;
import utilities.Scheduler;

public class Main {
	public static ScheduledExecutorService scheduler;
	public static ParticleSystem particleSystem;
	public static JLayeredPane layeredPane;
	
	public static void main(String[] args) {
		Scheduler.init();
		scheduler = Scheduler.getThreadPool();
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int frameSize = Math.min(screenSize.width * 9 / 10, screenSize.height * 9 / 10);
		
		frame.setSize(frameSize, frameSize);
		frame.setLayout(new FlowLayout(FlowLayout.CENTER));
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(new Color(10, 10, 10));
		
		JPanel gameHolder = new JPanel();
		gameHolder.setBackground(Color.BLACK);
		gameHolder.setPreferredSize(new Dimension(frame.getWidth() * 9 / 10, frame.getHeight() * 9 / 10));
		gameHolder.setLayout(new FlowLayout(FlowLayout.CENTER));
		frame.add(gameHolder);
//		pacman.start();
		frame.setVisible(true);
		
		particleSystem = new ParticleSystem();
		particleSystem.setBounds(gameHolder.getBounds());
		layeredPane = frame.getLayeredPane();
//		frame.getLayeredPane().add(particleSystem, Integer.valueOf(100));
		
		ScreenManager manager = new ScreenManager(gameHolder);
		
		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				int size = Math.min(frame.getWidth() * 9 / 10, frame.getHeight() * 9 / 10);
				gameHolder.setPreferredSize(new Dimension(size, size));
				frame.revalidate();
				frame.repaint();
				particleSystem.setBounds(gameHolder.getBounds());
				frame.revalidate();
				frame.repaint();
			}
		});
	}
	
	public static double getDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt( Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2)  );
	}
	
	public static Font resizeFont(Font font, Rectangle bounds, String string, JComponent parent) {
		FontMetrics metrics = parent.getFontMetrics(font);
		while (metrics.stringWidth(string) < bounds.width && metrics.getHeight() < bounds.height) {
			font = font.deriveFont(font.getSize() + 1f);
			metrics = parent.getFontMetrics(font);
		}
		font = font.deriveFont(font.getSize() - 1f);
		return font;
	}
	
	public static void addToLayeredPane(JComponent component, Integer zIndex) {
		boolean inPane = Arrays.stream(layeredPane.getComponents()).anyMatch(c -> c.equals(component));
		if (!inPane) {
		layeredPane.add(component, zIndex);
		layeredPane.revalidate(); layeredPane.repaint();
		} else {
			System.out.println("already in pane");
		}
		
	}
	
	public static void removeFromLayeredPane(JComponent component) {
		layeredPane.remove(component);
		layeredPane.revalidate(); layeredPane.repaint();
	}

}
