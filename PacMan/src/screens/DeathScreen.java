package screens;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import pacman.PacMan;
import utilities.FallingStars;

public class DeathScreen extends JPanel {
	ScreenManager manager;
	JLabel title;
	JButton backToGame;
	JButton toTitleScreen;
	FallingStars animation;
	
	JTextPane textPane;
	
	DeathScreen(ScreenManager manager) {
		this.manager = manager;
		
		setLayout(new GridBagLayout());
		setOpaque(false);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		animation = new FallingStars(this);
		animation.starColor = Color.RED;
		
		title = new JLabel("GAME OVER");
		title.setFont(title.getFont().deriveFont(12f));
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(JLabel.CENTER);
		constraints.gridwidth = 4;
		add(title, constraints);
		
		textPane = new JTextPane();
		textPane.setBackground(Color.BLACK);
		textPane.setFont(getFont().deriveFont(12f).deriveFont(Font.BOLD));
		textPane.setForeground(Color.WHITE);
		textPane.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
		textPane.setFocusable(false);
		textPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		textPane.setEditable(false);
		textPane.setOpaque(false);
		
		StyledDocument doc = textPane.getStyledDocument();;
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		textPane.setText("PELLETS EATEN: " + PacMan.pacman.pelletsEaten);
		constraints.gridy = 1;
		add(textPane, constraints);
		
		constraints.gridy = 2;
		backToGame = new GUIButton("PLAY AGAIN");
		backToGame.addActionListener(l -> { 
			
			manager.switchScreen(ScreenManager.GAMESCREEN);
		});
		add(backToGame, constraints);
		
		constraints.gridy = 3;
		toTitleScreen = new GUIButton("TO TITLE SCREEN");
		toTitleScreen.addActionListener(l -> manager.switchScreen(ScreenManager.TITLESCREEN));
		add(toTitleScreen, constraints);
		
		addAncestorListener(new utilities.AncestorAdapter() {
			@Override
			public void ancestorAdded(AncestorEvent event) {
				textPane.setText("PELLETS EATEN: " + PacMan.pacman.pelletsEaten);
			}
		});
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		animation.animate((Graphics2D) g);
	}
}
