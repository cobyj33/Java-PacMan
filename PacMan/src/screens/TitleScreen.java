package screens;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utilities.FallingStars;




public class TitleScreen extends JPanel {
	ScreenManager manager;
	GUIButton playButton;
	GUIButton quitButton;
	JLabel title;
	FallingStars animation;
	
	
	TitleScreen(ScreenManager manager) {
		this.manager = manager;
		setLayout(new GridBagLayout());
		setOpaque(false);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		animation = new FallingStars(this);
		
		title = new JLabel("PAC   MAN");
		title.setFont(title.getFont().deriveFont(12f));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(JLabel.CENTER);
		constraints.gridwidth = 4;
		add(title, constraints);
		
		constraints.gridy = 1;
		playButton = new GUIButton("PLAY");
		playButton.addActionListener(l -> manager.switchScreen(ScreenManager.GAMESCREEN));
		add(playButton, constraints);
		
		constraints.gridy = 2;
		quitButton = new GUIButton("QUIT GAME");
		quitButton.addActionListener(l -> System.exit(0));
		add(quitButton, constraints);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		animation.animate((Graphics2D) g);
	}
}
