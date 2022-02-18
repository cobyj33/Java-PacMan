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

public class PauseScreen extends JPanel {
	ScreenManager manager;
	JLabel title;
	JButton backToGame;
	JButton toTitleScreen;
	FallingStars animation;
	
	PauseScreen(ScreenManager manager) {
		this.manager = manager;
		setLayout(new GridBagLayout());
		setOpaque(false);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		animation = new FallingStars(this);
		
		title = new JLabel("PAUSED");
		title.setFont(title.getFont().deriveFont(12f));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(JLabel.CENTER);
		constraints.gridwidth = 4;
		add(title, constraints);
		
		constraints.gridy = 1;
		backToGame = new GUIButton("PLAY");
		backToGame.addActionListener(l -> manager.switchScreen(ScreenManager.GAMESCREEN));
		add(backToGame, constraints);
		
		constraints.gridy = 2;
		toTitleScreen = new GUIButton("TO TITLE SCREEN");
		toTitleScreen.addActionListener(l -> manager.switchScreen(ScreenManager.TITLESCREEN));
		add(toTitleScreen, constraints);
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		animation.animate((Graphics2D) g);
	}
}
